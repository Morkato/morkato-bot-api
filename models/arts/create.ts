import type { ConnectionContext } from 'models/database'
import type { ArtDatabase } from '.'

import { ArtAlreadyExistsError, InternalServerError } from 'errors'
import { artInsertBuilder } from 'models/queries/arts'
import { formatArt } from './formatters'
import { DatabaseError } from 'pg'
import { stripAll } from 'utils'

export function createArt({logger, models, locationCode, pg, dispatch}: ConnectionContext): ArtDatabase['createArt'] {
  return async ({ name, guild_id, type, description, banner }) => {
    const values: unknown[] = []
    const payload = {
      name: name,
      key: name === undefined ? undefined : stripAll(name),
      guild_id: guild_id,
      type: type,
      description: description,
      banner: banner
    }
    const query = artInsertBuilder.sql({}, payload, values)
    logger.debug("SQL QUERY: %s with values: %s", query, values)

    async function execute(guildCreated: boolean = false) {
      try {
        const result = await pg.query(query, values)
        const art = formatArt(result.rows[0])
        logger.debug("RESULT CREATE QUERY: %s with payload: %s", art, payload)
        dispatch("art.create", art)
        return art;
      } catch (err) {
        if (err instanceof DatabaseError) {
          if (err.constraint === 'art.guild' && !guildCreated) {
            await models.createGuild({ id: guild_id })
            return await execute(true);
          } else if (err.constraint === 'art.key') {
            throw new ArtAlreadyExistsError({
              errorLocationCode: locationCode,
              guild_id: guild_id,
              art_name: name
            });
          }
          throw new InternalServerError({
            errorLocationCode: locationCode
          });
        }
        throw err;
      }
    }
    return await execute();
  }
}