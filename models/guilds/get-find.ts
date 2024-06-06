import type { MorkatoAPP } from 'morkato'
import type { GuildDatabase } from '.'
import type { Client } from 'pg'

import { GuildNotFoundError, InternalServerError } from 'errors'
import { guildQueryBuilder } from 'models/queries/guilds'
import { formatGuild } from './formatters'

const locationCode = "models/guilds"

export function getGuild(app: MorkatoAPP, pg: Client): GuildDatabase['getGuild'] {
  return async ({ id }) => {
    const values: unknown[] = []
    const where = {
      "guild.id": id
    }

    const query = guildQueryBuilder.sql(where, values)
    const {rows, rowCount} = await pg.query(query, values)

    if (!rowCount || rowCount === 0) {
      return await app.database.createGuild({ id });
    } else if (rowCount > 1) {
      throw new InternalServerError({
        errorLocationCode: locationCode
      });
    }

    return formatGuild(rows[0]);
  }
}