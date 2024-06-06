import type { MorkatoAPP } from 'morkato'
import type { ItemDatabase } from "."
import type { Client } from "pg"

import { itemQueryBuilder, itemsQueryBuilder } from "models/queries/items"
import { ItemNotFoundError, InternalServerError } from "errors"
import { formatItem } from "./formatter"

const locationCode = "models/items"

export function findItem(app: MorkatoAPP, pg: Client): ItemDatabase['findItem'] {
  return async ({ guild_id }) => {
    const values: unknown[] = []
    const where = {
      "item.guild_id": guild_id
    }

    const sql = itemsQueryBuilder.sql(where, values)
    const {rows, rowCount} = await pg.query(sql)
    return rows.map(formatItem);
  }
}

export function getItem(app: MorkatoAPP, pg: Client): ItemDatabase['getItem'] {
  return async ({ guild_id, id }) => {
    const values: unknown[] = []
    const where = {
      "item.guild_id": guild_id,
      "item.id": id
    }

    const sql = itemQueryBuilder.sql(where, values)
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

    return formatItem(rows[0]);
  }
}