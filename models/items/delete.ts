import type { ConnectionContext } from 'models/database'
import type { ItemDatabase } from '.'

import { ItemNotFoundError, InternalServerError } from 'errors'
import { itemDeleteQueryBuilder } from 'models/queries/items'
import { formatItem } from './formatter'

const locationCode = "models/items"

export function delItem({logger, locationCode, pg, dispatch}: ConnectionContext): ItemDatabase['delItem'] {
  return async ({ guild_id, id }) => {
    const values: unknown[] = []
    const where = {
      "item.guild_id": guild_id,
      "item.id": id
    }

    const sql = itemDeleteQueryBuilder.sql(where, values)
    logger.debug("SQL QUERY: %s with values: %s", sql, values)
    const {rows, rowCount} = await pg.query(sql, values)
    logger.debug("RESULT DELETE QUERY: %s where: %s", rows, where)
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

    const item = formatItem(rows[0])
    dispatch("item.delete", item)
    return item;
  }
}