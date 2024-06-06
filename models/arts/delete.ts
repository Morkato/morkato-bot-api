import type { MorkatoAPP } from 'morkato'
import type { ArtDatabase } from '.'
import type { Client } from 'pg'

import { ArtNotFoundError, InternalServerError } from 'errors'
import { artDelQueryBuilder } from 'models/queries/arts'

const locationCode = "models/arts"

export function delArt(app: MorkatoAPP, pg: Client): ArtDatabase['delArt'] {
  return async ({ guild_id, id }) => {
    const values: unknown[] = []
    const where = {
      "art.guild_id": guild_id,
      "art.id": id
    }

    const query = artDelQueryBuilder.sql(where, values)
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

    return rows[0];
  }
}