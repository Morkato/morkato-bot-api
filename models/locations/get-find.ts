import type { ConnectionContext } from 'models/database'
import type { LocationDatabase } from '.'

import { localsQueryBuilder, localQueryBuilder } from 'models/queries/locations'
import { InternalServerError, LocationNotFoundError } from 'errors'
import { formatLocation } from './formatters'

export function findLocation({logger, pg}: ConnectionContext): LocationDatabase["findLocation"] {
  return async ({ guild_id, location_id }) => {
    const values: unknown[] = []
    const where = {
      "local.guild_id": guild_id,
      "local.location_id": location_id
    }

    const sql = localsQueryBuilder.sql(where, values)
    logger.debug("SQL QUERY: %s with values: %s", sql, values)
    const {rows} = await pg.query(sql, values)
    logger.debug("RESULT FIND QUERY: %s where: %s", rows, where)
    return rows.map(formatLocation);
  }
}

export function getLocation({logger, locationCode, pg}: ConnectionContext): LocationDatabase["getLocation"] {
  return async ({ guild_id, id }) => {
    const values: unknown[] = []
    const where = {
      "location.guild_id": guild_id,
      "location.id": id
    }

    const sql = localQueryBuilder.sql(where, values)
    logger.debug("SQL QUERY: %s with values: %s", sql, values)
    const {rows, rowCount} = await pg.query(sql, values)
    logger.debug("RESULT GET QUERY: %s where: %s", rows, where)

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

    return formatLocation(rows[0]);
  }
}