import type { ConnectionContext } from 'models/database'
import type { ItemDatabase } from '.'

import { ItemNotFoundError, ArtNotFoundError, InternalServerError } from 'errors'
import { itemUpdateQueryBuilder } from 'models/queries/items'
import { formatItem } from './formatter'
import { DatabaseError } from 'pg'
import { stripAll } from 'utils'

export function updateItem({logger, models, locationCode, pg, dispatch}: ConnectionContext): ItemDatabase['updateItem'] {
  return async ({ guild_id, id, name, ...data }) => {
    const values: unknown[] = []
    const where = {
      "item.guild_id": guild_id,
      "item.id": id
    }
    const payload = Object.assign({}, data, {
      name: name,
      key: name === undefined ? undefined : stripAll(name)
    })

    const before = await models.getItem({ guild_id, id })
    const sql = itemUpdateQueryBuilder.sql(where, payload, values)
    logger.debug("SQL QUERY: %s with values: %s", sql, values)
    try {
      const {rows, rowCount} = await pg.query(sql, values)
      logger.debug("RESULT UPDATE QUERY: %s with payload: %s where: %s", rows, payload, where)

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
      dispatch("item.update", before, item)
      return item;
    } catch (err) {
      if (err instanceof DatabaseError) {
        if (err.constraint === 'item.art' && payload.art_id) {
          throw new ArtNotFoundError({
            errorLocationCode: locationCode,
            guild_id: guild_id,
            art_id: payload.art_id
          });
        }

        throw new InternalServerError({
          errorLocationCode: locationCode
        });
      }
      throw err;
    }
  }
}