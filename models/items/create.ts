import type { MorkatoAPP } from 'morkato/app'
import type { ItemDatabase, Item } from "."
import type { Client } from "pg"

import { ItemAlreadyExistsError, ArtNotFoundError, InternalServerError } from "errors"
import { itemInsertQueryBuilder } from "models/queries/items"
import { formatItem } from "./formatter"
import { DatabaseError } from "pg"
import { stripAll } from "utils"

const locationCode = "models/items"

export function createItem(app: MorkatoAPP, pg: Client): ItemDatabase['createItem'] {
  return async ({ guild_id, name, ...data }) => {
    const values: unknown[] = []
    const payload = Object.assign({}, data, {
      name: name,
      key: name === undefined ? undefined : stripAll(name),
      guild_id: guild_id
    })

    const sql = itemInsertQueryBuilder.sql({}, payload, values)
    async function execute(guildHasCreated: boolean = false): Promise<Item> {
      try {
        const {rows, rowCount} = await pg.query(sql, values)
        const item = formatItem(rows[0])
        return item;
      } catch (err) {
        if (err instanceof DatabaseError) {
          if (err.constraint === 'item.guild' && !guildHasCreated) {
            await app.database.createGuild({ id: guild_id })
            return execute(true);
          } else if (err.constraint === 'item.key') {
            throw new ItemAlreadyExistsError({
              errorLocationCode: locationCode,
              guild_id: guild_id,
              item_name: name
            });
          } else if (err.constraint === 'item.art') {
            throw new ArtNotFoundError({
              errorLocationCode: locationCode,
              guild_id: guild_id,
              art_id: data.art_id as string
            });
          }

          throw new InternalServerError({
            errorLocationCode: locationCode
          })
        }
        throw err;
      }
    }
    
    return await execute();
  }
}