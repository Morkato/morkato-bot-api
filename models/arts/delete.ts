import type { MorkatoAPP } from 'morkato/app'
import type { ArtDatabase } from '.'
import type { Client } from 'pg'

import { ArtNotFoundError, InternalServerError } from 'errors'
import { artDelQueryBuilder } from 'models/queries/arts'
import { formatArt } from './formatters'

const locationCode = "models/arts"

export function delArt(app: MorkatoAPP, pg: Client): ArtDatabase['delArt'] {
  const logger = app.getLoggerContext(locationCode)
  return async ({ guild_id, id }) => {
    const values: unknown[] = []
    const where = {
      "art.guild_id": guild_id,
      "art.id": id
    }

    const query = artDelQueryBuilder.sql(where, values)
    logger.debug("SQL QUERY: %s with values: 5s", query, values)
    const {rows, rowCount} = await pg.query(query, values)
    logger.debug("RESULT DELETE QUERY: %s where: %s", rows, where)

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

    const art = formatArt(rows[0])
    app.notify("art.delete", art)
    return art;
  }
}