import type { ConnectionContext } from 'models/database'
import type { LocationDatabase } from '.'

import { InternalServerError, LocationNotFoundError } from 'errors'
import { localDeleteQueryBuilder } from 'models/queries/locations'
import { formatLocation } from './formatters'

export function delLocation({logger, locationCode, pg, dispatch}: ConnectionContext): LocationDatabase["delLocation"] {
  return async ({ guild_id, id }) => {
    const values: unknown[] = []
    const where = {
      "location.guild_id": guild_id,
      "location.id": id
    }

    const sql = localDeleteQueryBuilder.sql(where, values)
    logger.debug("SQL QUERY: %s with values: %s", sql, values)
    const {rows, rowCount} = await pg.query(sql, values)
    logger.debug("RESULT DELETE QUERY: %s where: %s", rows, where)

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
    dispatch("local.delete", local)
    return local;
  }
}