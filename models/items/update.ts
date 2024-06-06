import type { MorkatoAPP } from 'morkato'
import type { ItemDatabase } from "."
import type { Client } from "pg"

import { ItemNotFoundError, ArtNotFoundError, InternalServerError } from "errors"
import { itemUpdateQueryBuilder } from "models/queries/items"
import { formatItem } from "./formatter"
import { DatabaseError } from "pg"
import { stripAll } from "utils"

const locationCode = "models/items"

export function updateItem(app: MorkatoAPP, pg: Client): ItemDatabase['updateItem'] {
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

    const sql = itemUpdateQueryBuilder.sql(where, payload, values)
    app.logger.debug(locationCode, "SQL QUERY: %s with values: %s", sql, values)
    try {
      const {rows, rowCount} = await pg.query(sql, values)

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
      
      return formatItem(rows[0])
    } catch (err) {
      if (err instanceof DatabaseError) {
        if (err.constraint === 'item.art') {
          throw new ArtNotFoundError({
            errorLocationCode: locationCode,
            guild_id: guild_id,
            art_id: payload.art_id as string
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