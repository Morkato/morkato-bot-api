import { ValidationError } from 'errors'
import type Joi from 'joi'

export type WhereData = Partial<Record<string, unknown>>
export type WhereOperator = '=' | '<' | '>' | '<=' | '>=' | '!=' | 'LIKE' | 'IS' | 'IS NOT' | '~'
export type WhereOptions<T = any> = {
  op?: WhereOperator
  validator: Joi.AnySchema<T>
  required?: boolean | 'partial'
  converter?: string
}
export type ColumnOptions<T = any> = {
  required?: boolean
  validator?: Joi.AnySchema<T>
}
export type PGLiteral = {}

class LiteralValue implements PGLiteral {
  constructor(private value: unknown) {}
  getValue() { return this.value; }
  setValue(newValue: unknown) { this.value = newValue; }
}

export function literal(value: unknown) {
  return new LiteralValue(value);
}

export class QueryBuilder {
  protected readonly whereQuery: Record<string, WhereOptions> = {}
  protected readonly selectQuery: [string, string?][] = []
  protected readonly includeQuery: Record<string, { sql: SelectQueryBuilder; where?: WhereData }> = {}
  protected readonly columnQuery: Record<string, ColumnOptions> = {}
  protected orderByQuery: string | null = null
  protected orderByConverter: string | null = null
  protected limitQuery: number | null = null
  protected skipQuery: number | null = null
  protected isJsonAgg: boolean = false
  protected hasRequiredWhere: boolean = false
  protected hasRequiredColumn: boolean = false
  protected minPartialRequired: number = 0

  public constructor(protected readonly tableName: string, protected readonly mapTableName: string | null = null) {}
  public partial(newPartialValue: number): this {
    this.minPartialRequired = newPartialValue
    return this;
  }
  public select(col: string, converter?: string): this {
    this.selectQuery.push([col, converter])
    return this;
  }
  public column(colname: string, { validator, required }: ColumnOptions): this {
    this.columnQuery[colname] = {validator, required}
    if (required) {
      this.hasRequiredColumn = true
    }
    return this;
  }
  public where<T>(key: string, { validator, required, converter, op }: WhereOptions<T>): this {
    this.whereQuery[key] = { validator, required, converter, op }
    if (required) {
      this.hasRequiredWhere = true
    }
    return this
  }
  public include(key: string, sql: SelectQueryBuilder, where?: WhereData): this {
    this.includeQuery[key] = { sql, where }
    return this;
  }
  public orderBy(key: string, converter?: string): this {
    this.orderByQuery = key
    if (converter) {
      this.orderByConverter = converter
    }
    return this;
  }

  public agg(): this {
    this.isJsonAgg = true
    return this;
  }

  public limit(idx: number): this {
    this.limitQuery = idx
    return this;
  }

  public skip(idx: number): this {
    this.skipQuery = idx
    return this;
  }
  
  protected validate<T>({ key, value, required, validator }: { key: string, value: unknown, required?: boolean | 'partial', validator?: Joi.AnySchema<T> }): unknown | undefined {
    if (required === true && (value === undefined || !validator)) {
      throw new ValidationError({
        message: `Key: ${key} is required to construct SQL QUERY.`,
        errorLocationCode: "models/sql-builder"
      });
    }
    
    if (value === undefined || !validator) {
      return value;
    }

    const { error, value: validValue } = validator.validate(value, { stripUnknown: true, abortEarly: false })
    if (!error) {
      return validValue;
    }

    throw new ValidationError({
      message: error.details[0].message.replace("\"value\"", `"${key}"`),
      errorLocationCode: "models/sql-builder",
      type: error.details[0].type
    });
  }

  protected getSQLIdentifier(): string {
    let query = `"${this.tableName}"`
    if (this.mapTableName) {
      query += ` "${this.mapTableName}"`
    }
    return query;
  }
  protected buildSQLIncludePaths(whereContext: WhereData, values: unknown[]): string {
    let query = ''
    for (const [key, {sql, where}] of Object.entries(this.includeQuery)) {
      query += (
        this.isJsonAgg
        ? `'${key}',${sql.subquery(Object.assign({}, whereContext[key]??{}, where??{}), values)},`
        : `${sql.subquery(Object.assign({}, whereContext[key]??{}, where??{}), values)} AS "${key}",`
      )
    }
    return query.slice(0, -1);
  }
  protected buildSQLJsonAGGSelectQuery(where: WhereData, values: unknown[]): string {
    if (this.selectQuery.length == 0) {
      throw new Error('Select fields must be provided for JSON aggregation');
    }

    let query = 'json_agg(jsonb_build_object('
    for (const [key, converter] of this.selectQuery) {
      query += `'${key}',`
      if (this.mapTableName) {
        query += `"${this.mapTableName}".`
      }
      query += `"${key}"`
      if (converter) {
        query += `::${converter}`
      }
      query += ',';
    }

    query = query.slice(0, -1)
    if (Object.keys(this.includeQuery).length > 0) {
      query += `,${this.buildSQLIncludePaths(where, values)}`
    }

    query += ')'
    if (this.orderByQuery) {
      query += " ORDER BY "
      if (this.mapTableName) {
        query += `"${this.mapTableName}".`
      }
      query += `"${this.orderByQuery}"`
      if (this.orderByConverter) {
        query += `"${this.orderByConverter}"`
      }
      query += ' ASC'
    }
    query += ')'
    return query;
  }

  protected buildSQLSelectQuery(where: WhereData, values: unknown[]): string {
    if (this.isJsonAgg) {
      return this.buildSQLJsonAGGSelectQuery(where, values);
    }

    let query = ''
    if (this.selectQuery.length > 0) {
      for (const [key, converter] of this.selectQuery) {
        if (this.mapTableName) {
          query += `"${this.mapTableName}".`
        }
        query += `"${key}"`
        if (converter) {
          query += `::${converter}`
        }
        query += ','
      }
      query = query.slice(0, -1)
    } else {
      query += '*'
    }

    if (Object.keys(this.includeQuery).length > 0) {
      query += `,${this.buildSQLIncludePaths(where, values)}`
    }

    return query;
  }

  protected buildSQLWhereQuery(where: WhereData, values: unknown[]): string {
    let getPartialValues = 0
    let query = ''
    for (const [key, {validator, required, converter, op}] of Object.entries(this.whereQuery)) {
      const value = where[key]
      let clause = key
      if (converter) {
        clause += `::${converter}`
      }
      clause += op??'='
      if (value instanceof LiteralValue) {
        const validValue = value.getValue()
        clause += validValue
      } else {
        const validValue = this.validate({ key, value, validator, required })
        if (validValue === undefined) {
          continue;
        }
        clause += `$${values.length+1}`
        values.push(validValue)
      }
      query += `${clause} AND `
      if (required === 'partial') {
        ++getPartialValues;
      }
    }

    if (getPartialValues !== this.minPartialRequired) {
      throw new ValidationError({
        message: `Values has violete partial required values: ${this.minPartialRequired} (${getPartialValues}).`,
        errorLocationCode: "models/sql-builder"
      });
    }
    return query.slice(0, -5);
  }

  public query(where: WhereData, columns: Record<string, any>, values: unknown[]): string {
    throw new Error("This is virtual method.");
  }
  public sql(where: WhereData, columns: Record<string, any>, values: unknown[]): string {
    throw new Error("This is virtual method.");
  }
}

export class SelectQueryBuilder extends QueryBuilder {
  public subquery(where: WhereData, values: unknown[]): string {
    return `(${this.query(where, values)})`;
  }
  public override query(where: WhereData, values: unknown[]): string {
    let query = "SELECT "
    query += this.buildSQLSelectQuery(where, values)
    query += ` FROM "${this.tableName}"`
    if (this.mapTableName) {
      query += ` "${this.mapTableName}"`
    }
    if (Object.keys(this.whereQuery).length > 0) {
      const value = this.buildSQLWhereQuery(where, values)
      if (value) {
        query += ` WHERE ${value}`
      }
    }

    if (this.orderByQuery && !this.isJsonAgg) {
      query += " ORDER BY "
      if (this.mapTableName) {
        query += `"${this.mapTableName}".`
      }
      query += `"${this.orderByQuery}"`
      if (this.orderByConverter) {
        query += `"${this.orderByConverter}"`
      }
      query += ' ASC'
    }

    if (this.limitQuery !== null) {
      query += ` LIMIT ${this.limitQuery}`
    }

    if (this.skipQuery !== null) {
      query += ` OFFSET ${this.skipQuery}`
    }

    return query;
  }

  public override sql(where: WhereData, values: unknown[]): string {
    return `${this.query(where, values)};`;
  }
}

export class InsertIntoQueryBuilder extends QueryBuilder {
  public override query(where: WhereData, columns: Record<string, any>, values: unknown[]): string {
    let keys: string = ''
    let colvalues: string = ''
    let query = `INSERT INTO "${this.tableName}"`

    for (const [key, {validator, required}] of Object.entries(this.columnQuery)) {
      const value = columns[key]
      if (value instanceof LiteralValue) {
        const validValue = value.getValue()
        keys += `${key},`
        colvalues += `${validValue},`
        continue;
      }
      const validValue = this.validate({ key, value, validator, required })
      if (validValue === undefined) {
        continue;
      }
      values.push(validValue)
      keys += `"${key}",`
      colvalues += `$${values.length},`
    }

    keys = keys.slice(0, -1)
    colvalues = colvalues.slice(0, -1)

    query += `(${keys}) VALUES (${colvalues}) `
    query += `RETURNING ${this.buildSQLSelectQuery(where, values)}`
    return query;
  }
  public override sql(where: WhereData, columns: Record<string, any>, values: unknown[]) {
    return `${this.query(where, columns, values)};`
  }
}

export class UpdateQueryBuilder extends QueryBuilder {
  protected buildSQLSetQuery(columns: Record<string, any>, values: unknown[]): string {
    let query = ''
    for (const [key, {validator, required}] of Object.entries(this.columnQuery)) {
      const value = columns[key]
      if (value instanceof LiteralValue) {
        const validValue = value.getValue()
        query += `"${key}"=${validValue},`
        continue;
      }
      const validValue = this.validate({ key, value, validator, required })
      if (validValue === undefined) {
        continue;
      }
      values.push(validValue)
      query += `"${key}"=$${values.length},`
    }
    return query.slice(0, -1);
  }

  public override query(where: WhereData, columns: Record<string, any>, values: unknown[]): string {
    if (!this.hasRequiredWhere || !this.hasRequiredColumn) {
      throw new Error();
    } 

    return `UPDATE ${this.getSQLIdentifier()} SET ${this.buildSQLSetQuery(columns, values)} WHERE ${this.buildSQLWhereQuery(where, values)} RETURNING ${this.buildSQLSelectQuery(where, values)}`;
  }
  public override sql(where: WhereData, columns: Record<string, any>, values: unknown[]): string {
    return `${this.query(where, columns, values)};`;
  }
}

export class DeleteFromQueryBuilder extends QueryBuilder {
  public override query(where: WhereData, values: unknown[]): string {
    if (!this.hasRequiredWhere) {
      throw new Error();
    } 

    return `DELETE FROM ${this.getSQLIdentifier()} WHERE ${this.buildSQLWhereQuery(where, values)} RETURNING ${this.buildSQLSelectQuery(where, values)}`;
  }
  public override sql(where: WhereData, values: unknown[]): string {
    return `${this.query(where, values)};`;
  }
}