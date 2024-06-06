import type { Handler, Request } from 'express'
import { InternalServerError, ValidationError } from 'errors'
import { ErrorType } from 'errors/bases/base'

import unidecode from 'remove-accents'
import errorHandler from 'error'
import Joi from 'joi'

type WhereOP = '=' | '<>' | 'IS NOT' | 'IS'
type WhereParams = {
  op: WhereOP
  val: unknown
}
type FinderParams = {
  table_name: string
  select: string | string[]
  include?: Record<string, FinderParams>
  map?: string
  orderBy?: [string, "ASC" | "DESC"]
  where?: Record<string, WhereParams>
}
type JsonAGGType = {
  orderBy?: [string, "ASC" | "DESC"],
  query: string
}

const locationCode = "utils"
export const schemas = {
  id: Joi.string().regex(/^[0-9]{15,}$/),
  art_type: Joi.string().valid('RESPIRATION', 'KEKKIJUTSU', 'FIGHTING_STYLE'),
  name: Joi.string().regex(/^\s*[^:0-9][^:]{1,31}\s*$/),
  surname: Joi.string().regex(/^[^0-9][a-z0-9\-_.]{1,31}$/)
}

export function json_agg({ orderBy, query }: JsonAGGType): string {
  let jsonQuery = `json_agg(${query}`
  if (orderBy) {
    const [col, by] = orderBy
    jsonQuery += ` ORDER BY ${col} ${by}`
  }
  jsonQuery += ')'
  return jsonQuery;
}

export function jsonb_build_object(json: Record<string, string>): string {
  const keys = Object.entries(json).map(([key, val]) => `'${key}',${val}`)
  return `jsonb_build_object(${keys.join(',')})`
}

export function finderQueryBuilder({ table_name, select, include, map, orderBy, where }: FinderParams): string {
  let query = "SELECT"
  query += ` ${typeof select === 'string' ? select : select.join(',')}`
  if (include) {
    for (const [key, val] of Object.entries(include)) {
      query += `,(${finderQueryBuilder(val)}) AS "${key}"`
    }
  }
  query += ` FROM "${table_name}"`
  if (map) {
    query += ` "${map}"`
  }
  if (where) {
    query += " WHERE "
    query += Object.entries(where).map(([key, val]) => `${key}${val.op}${val.val}`).join(" AND ")
  }
  if (orderBy) {
    const [col, by] = orderBy
    query += ` ORDER BY ${col} ${by}`
  }

  return query;
}

export function thenHandler(handler: Handler): Handler {
  return async (req, res, next) => {
    try {
      const result = handler(req, res, next)

      if ((result as any) instanceof Promise) {
        await result
      }
    } catch (err) {
      errorHandler(err, req, res)
    }
  }
}

export function extractParamRequest(req: Request, name: string): string {
  const id = req.params[name]
  
  if (typeof id === 'string') {
    return id;
  }

  throw new InternalServerError({
    message: "Erro interno",
    errorLocationCode: locationCode
  })
}

export function extractHeaderRequest(req: Request, name: string): string {
  const id = req.headers[name.toLocaleLowerCase()]
  
  if (typeof id === 'string') {
    return id;
  }

  throw new ValidationError({
    message: "Header is Required",
    errorLocationCode: locationCode
  })
}

export const extractorParamRequest = (name: string) => (req: Request) => extractParamRequest(req, name)
export const extractorHeaderRequest = (name: string) => (req: Request) => extractHeaderRequest(req, name)

const formatter = /((\\)?\$(?<key>\$|[^ \n\t.]+))/g

type StringStripParams = {
  ignore_accents?: boolean
  ignore_empty?: boolean
  case_insensitive?: boolean
  trim?: boolean
}


export function format(text: string, params: Record<string, string>) {
  return text.replace(formatter, sub => {
    if (sub[0] == '\\') {
      return sub.slice(1, sub.length);
    }

    sub = sub.slice(1, sub.length)
    
    return params[sub] ?? sub;
  })
}

export function isEmpty(text: string): boolean {
  return !!text.match(/^\s*$/)
}

export function strip(text: string, {
  ignore_accents = false,
  ignore_empty = false,
  case_insensitive = false,
  trim = false
}: StringStripParams) {
  if (trim) text = text.trim();
  if (ignore_accents) text = unidecode(text);
  if (case_insensitive) text = text.toLocaleLowerCase();
  if (ignore_empty) text = text.replace(/\s+/g, '-');

  return text;
}

export function stripAll(text: string): string {
  return strip(text, {
    ignore_accents: true,
    ignore_empty: true,
    case_insensitive: true,
    trim: true
  })
}

export function assert<T>(
  schema: Joi.AnySchema<T>,
  obj: T
) {
  try {
    obj = JSON.parse(JSON.stringify(obj))
  } catch {
    throw new ValidationError({ message: "O body tem que ser um Json." });
  }

  const { value, error } = schema.validate(obj)

  if (error) {
    throw new ValidationError({
      message: error.details[0].message,
      errorLocationCode: 'MODEL:VALIDATOR:SCHEMA',
      type: ErrorType.GENERIC
    });
  }

  return value;
}