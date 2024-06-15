import type { ConnectionContext } from 'models/database'
import type { ItemDatabase } from '.'

import { itemQueryBuilder, itemsQueryBuilder } from 'models/queries/items'
import { ItemNotFoundError, InternalServerError } from 'errors'
import { formatItem } from './formatter'

export function findItem({logger, pg}: ConnectionContext): ItemDatabase['findItem'] {
  return async ({ guild_id }) => {
    const values: unknown[] = []
    const where = {
      "item.guild_id": guild_id
    }

    const sql = itemsQueryBuilder.sql(where, values)
    logger.debug("SQL QUERY: %s with values: %s", sql, values)
    const {rows} = await pg.query(sql)
    logger.debug("RESULT FIND QUERY: %s where: 5s", rows, where)
    return rows.map(formatItem);
  }
}

export function getItem({logger, locationCode, pg}: ConnectionContext): ItemDatabase['getItem'] {
  return async ({ guild_id, id }) => {
    const values: unknown[] = []
    const where = {
      "item.guild_id": guild_id,
      "item.id": id
    }

    const sql = itemQueryBuilder.sql(where, values)
    logger.debug("SQL QUERY: %s with values: %s", sql, values)
    const {rows, rowCount} = await pg.query(sql, values)
    logger.debug("RESULT GET QUERY: %s where: %s", rows, where)
    
    if (!rowCount || rowCount === 0) {
      throw new ItemNotFoundError({
        errorLocationCode: locationCode,
        guild_id: guild_id,
        item_id: id
      });
    } else if (rowCount > 1) {
      throw new InternalServerError({
        errorLocationCode: locationCode
      });
    }

    return formatItem(rows[0]);
  }
}