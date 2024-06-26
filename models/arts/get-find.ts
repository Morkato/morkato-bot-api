import type { ConnectionContext } from 'models/database'
import type { ArtDatabase } from '.'

import { artsQueryBuilder, artQueryBuilder } from 'models/queries/arts'
import { ArtNotFoundError, InternalServerError } from 'errors'
import { formatArt } from './formatters'

export function findArt({logger, pg}: ConnectionContext): ArtDatabase['findArt'] {
  return async ({ guild_id }) => {
    const values: unknown[] = []
    const where = {
      "art.guild_id": guild_id
    }

    const query = artsQueryBuilder.sql(where, values)
    logger.debug("SQL QUERY: %s with values: %s", query, values)
    const {rows} = await pg.query(query, values)
    logger.debug("RESULT FIND QUERY: %s with values: %s", rows, values)
    return rows.map(formatArt);
  }
}

export function getArt({logger, locationCode, pg}: ConnectionContext): ArtDatabase['getArt'] {
  return async ({ guild_id, id }) => {
    const values: unknown[] = []
    const where = {
      "art.guild_id": guild_id,
      "art.id": id
    }

    const query = artQueryBuilder.sql(where, values)
    logger.debug("SQL QUERY: %s with values: %s", query, values)
    const {rows, rowCount} = await pg.query(query, values)
    logger.debug("RESULT GET QUERY: %s where %s", rows, where)

    if (!rowCount || rowCount === 0) {
      throw new ArtNotFoundError({
        errorLocationCode: locationCode,
        guild_id: guild_id,
        art_id: id
      });
    } else if (rowCount > 1) {
      throw new InternalServerError({
        errorLocationCode: locationCode
      });
    }

    return formatArt(rows[0])
  }
}