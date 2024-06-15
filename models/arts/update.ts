import type { ConnectionContext } from 'models/database'
import type { ArtDatabase } from '.'

import { ArtNotFoundError, InternalServerError } from 'errors'
import { artUpdateQueryBuilder } from 'models/queries/arts'
import { literal } from 'models/sql-builder'
import { formatArt } from './formatters'
import { stripAll } from 'utils'

export function updateArt({logger, models, locationCode, pg, dispatch}: ConnectionContext): ArtDatabase['updateArt'] {
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

    const before = await models.getArt({ guild_id, id })
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
    dispatch("art.update", before, after)
    return after;
  };
}