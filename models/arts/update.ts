import type { MorkatoAPP } from 'morkato'
import type { ArtDatabase } from '.'
import type { Client } from 'pg'

import { ArtNotFoundError, InternalServerError } from 'errors'
import { artUpdateQueryBuilder } from 'models/queries/arts'
import { literal } from 'models/sql-builder'
import { formatArt } from './formatters'
import { stripAll } from 'utils'

const locationCode = "models/arts"

export function updateArt(app: MorkatoAPP, pg: Client): ArtDatabase['updateArt'] {
  return async ({ guild_id, id, name, type, description, banner }) => {
    const values: unknown[] = []
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

    const query = artUpdateQueryBuilder.sql({
      "art.guild_id": guild_id,
      "art.id": id
    }, payload, values)
    const {rows, rowCount} = await pg.query(query, values)

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

    return formatArt(rows[0]);
  };
}