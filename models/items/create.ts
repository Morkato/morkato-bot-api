import type { ConnectionContext } from 'models/database'
import type { ItemDatabase, Item } from '.'

import { ItemAlreadyExistsError, ArtNotFoundError, InternalServerError } from 'errors'
import { itemInsertQueryBuilder } from 'models/queries/items'
import { formatItem } from './formatter'
import { DatabaseError } from 'pg'
import { stripAll } from 'utils'

export function createItem({logger, models, locationCode, pg, dispatch}: ConnectionContext): ItemDatabase['createItem'] {
  return async ({ guild_id, name, ...data }) => {
    const values: unknown[] = []
    const payload = Object.assign({}, data, {
      name: name,
      key: name === undefined ? undefined : stripAll(name),
      guild_id: guild_id
    })

    const sql = itemInsertQueryBuilder.sql({}, payload, values)
    logger.debug("SQL QUERY: %s with values: %s", sql, values)
    async function execute(guildHasCreated: boolean = false): Promise<Item> {
      try {
        const {rows} = await pg.query(sql, values)
        logger.debug("RESULT CREATE QUERY: %s with payload: %s", rows, payload)
        const item = formatItem(rows[0])
        return item;
      } catch (err) {
        if (err instanceof DatabaseError) {
          if (err.constraint === 'item.guild' && !guildHasCreated) {
            await models.createGuild({ id: guild_id })
            return execute(true);
          } else if (err.constraint === 'item.key') {
            throw new ItemAlreadyExistsError({
              errorLocationCode: locationCode,
              guild_id: guild_id,
              item_name: name
            });
          } else if (err.constraint === 'item.art' && data.art_id) {
            throw new ArtNotFoundError({
              errorLocationCode: locationCode,
              guild_id: guild_id,
              art_id: data.art_id
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