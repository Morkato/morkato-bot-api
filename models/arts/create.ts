import type { MorkatoAPP } from 'morkato'
import type { ArtDatabase } from '.'
import type { Client } from 'pg'

import { ArtAlreadyExistsError, InternalServerError } from 'errors'
import { artInsertBuilder } from 'models/queries/arts'
import { formatArt } from './formatters'
import { DatabaseError } from 'pg'
import { stripAll } from 'utils'

const locationCode = "models/arts"

export function createArt(app: MorkatoAPP, pg: Client): ArtDatabase['createArt'] {
  return async ({ name, guild_id, type, description, banner }) => {
    const values: unknown[] = []
    const query = artInsertBuilder.sql({}, {
      name: name,
      key: name === undefined ? undefined : stripAll(name),
      guild_id: guild_id,
      type: type,
      description: description,
      banner: banner
    }, values)

    async function execute(guildCreated: boolean = false) {
      try {
        const result = await pg.query(query, values)
        return formatArt(result.rows[0]);
      } catch (err) {
        if (err instanceof DatabaseError) {
          if (err.constraint === 'art.guild' && !guildCreated) {
            await app.database.createGuild({ id: guild_id })
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