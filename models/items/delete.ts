import type { MorkatoAPP } from 'morkato/app'
import type { ItemDatabase } from "."
import type { Client } from "pg"

import { ItemNotFoundError, InternalServerError } from "errors"
import { itemDeleteQueryBuilder } from "models/queries/items"
import { formatItem } from "./formatter"

const locationCode = "models/items"

export function delItem(app: MorkatoAPP, pg: Client): ItemDatabase['delItem'] {
  return async ({ guild_id, id }) => {
    const values: unknown[] = []
    const where = {
      "item.guild_id": guild_id,
      "item.id": id
    }

    const sql = itemDeleteQueryBuilder.sql(where, values)
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
    app.notify("item.delete", item)
    return item;
  }
}