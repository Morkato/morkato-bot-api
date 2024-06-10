import type { MorkatoAPP } from 'morkato/app'
import type { ArtDatabase } from '.'
import type { Client } from 'pg'

import { ArtNotFoundError, InternalServerError } from 'errors'
import { artUpdateQueryBuilder } from 'models/queries/arts'
import { literal } from 'models/sql-builder'
import { formatArt } from './formatters'
import { stripAll } from 'utils'

const locationCode = "models/arts"

export function updateArt(app: MorkatoAPP, pg: Client): ArtDatabase['updateArt'] {
  const logger = app.getLoggerContext(locationCode)
  return async ({ guild_id, id, name, type, description, banner }) => {
    const values: unknown[] = []
    const where = {
      "art.guild_id": guild_id,
      "art.id": id
    }
    const payload = {
      name: name,
      type: type,
      description: description,
      banner: banner,
      updated_at: literal("NOW()")
    } as Record<string, unknown>

    if (name) {
      payload.key = stripAll(name)
    }

    const before = await app.database.getArt({ guild_id, id })
    const query = artUpdateQueryBuilder.sql(where, payload, values)
    logger.debug("SQL QUERY: %s with values: %s", query, values)
    const {rows, rowCount} = await pg.query(query, values)
    logger.debug("RESULT UPDATE QUERY: %s with payload: %s where: %s", rows, payload, where)

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
    
    const after = formatArt(rows[0])
    app.notify("art.update", before, after)
    return after;
  };
}