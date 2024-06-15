import type { ConnectionContext } from 'models/database'
import type { LocationDatabase } from '.'

import { LocationNotFoundError, InternalServerError, LocationAlreadyExistsError } from 'errors'
import { localUpdateQueryBuilder } from 'models/queries/locations'
import { formatLocation } from './formatters'
import { DatabaseError } from 'pg'
import { stripAll } from 'utils'

export function updateLocation({logger, models, locationCode, pg, dispatch}: ConnectionContext): LocationDatabase["updateLocation"] {
  return async ({ guild_id, id, name, ...data }) => {
    const values: unknown[] = []
    const payload = Object.assign({}, data, {
      key: name !== undefined ? stripAll(name) : undefined,
      name: name
    })
    const where = {
      "location.guild_id": guild_id,
      "location.id": id
    }

    const before = await models.getLocation({ guild_id, id })
    const sql = localUpdateQueryBuilder.sql(where, payload, values)
    logger.debug("SQL QUERY: %s with values: %s", sql, values)
    try {
      const {rows, rowCount} = await pg.query(sql, values)
      logger.debug("RESULT UPDATE QUERY: %s with payload: %s where: %s", rows, payload, where)

      if (!rowCount || rowCount === 0) {
        throw new LocationNotFoundError({
          errorLocationCode: locationCode,
          guild_id: guild_id,
          location_id: id
        });
      } else if (rowCount > 1) {
        throw new InternalServerError({
          errorLocationCode: locationCode
        });
      }

      const local = formatLocation(rows[0])
      dispatch("location.update", before, local)
      return local;
    } catch (err) {
      if (err instanceof DatabaseError && err.constraint !== undefined) {
        if (err.constraint === 'local.key' && name) {
          throw new LocationAlreadyExistsError({
            errorLocationCode: locationCode,
            guild_id: guild_id,
            location_id: name
          });
        } else if (err.constraint === 'local.pkey') {
          throw new LocationAlreadyExistsError({
            errorLocationCode: locationCode,
            guild_id: guild_id,
            location_id: id
          });
        } else if (err.constraint === 'local.parent' && data.location_id) {
          throw new LocationNotFoundError({
            errorLocationCode: locationCode,
            guild_id: guild_id,
            location_id: data.location_id
          })
        }
      }
    }
    throw new InternalServerError({
      errorLocationCode: locationCode
    });
  }
}