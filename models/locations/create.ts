import type { ConnectionContext } from 'models/database'
import type { LocationDatabase } from '.'

import { InternalServerError, LocationNotFoundError } from 'errors'
import { localInsertQueryBuilder } from 'models/queries/locations'
import { formatLocation } from './formatters'
import { DatabaseError } from 'pg'
import { stripAll } from 'utils'

export function createLocation({logger, models, locationCode, pg, dispatch}: ConnectionContext): LocationDatabase['createLocation'] {
  return async ({ guild_id, id, name, ...data }) => {
    const values: unknown[] = []
    const payload = Object.assign(data, {
      name: name,
      key: name ? stripAll(name) : undefined,
      guild_id: guild_id,
      id: id
    })

    const sql = localInsertQueryBuilder.sql({}, payload, values)
    logger.debug("SQL QUERY: %s with values: 5s", sql, values)
    async function execute(guildHasCreated: boolean = false) {
      try {
        const {rows} = await pg.query(sql, values)
        logger.debug("RESULT CREATE QUERY: %s with payload: %s", rows, payload)
        const local = formatLocation(rows[0])
        dispatch("location.create", local)
        return local;
      } catch (err) {
        if (err instanceof DatabaseError) {
          if (err.constraint === 'local.guild' && !guildHasCreated) {
            await models.createGuild({ id: guild_id })
            return await execute(true);
          } else if (err.constraint === 'local.parent' && data.location_id) {
            throw new LocationNotFoundError({
              errorLocationCode: locationCode,
              guild_id: guild_id,
              location_id: data.location_id
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