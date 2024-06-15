import type { ConnectionContext } from 'models/database'
import type { GuildDatabase } from '.'

import { guildQueryBuilder } from 'models/queries/guilds'
import { InternalServerError } from 'errors'
import { formatGuild } from './formatters'

export function getGuild({logger, models, locationCode, pg}: ConnectionContext): GuildDatabase['getGuild'] {
  return async ({ id }) => {
    const values: unknown[] = []
    const where = {
      "guild.id": id
    }

    const query = guildQueryBuilder.sql(where, values)
    logger.debug("SQL QUERY: %s with values: %s", query, values)
    const {rows, rowCount} = await pg.query(query, values)
    logger.debug("RESULT GET QUERY: %s where: %s", rows, where)

    if (!rowCount || rowCount === 0) {
      return await models.createAnonymousGuild({ id });
    } else if (rowCount > 1) {
      throw new InternalServerError({
        errorLocationCode: locationCode
      });
    }

    return formatGuild(rows[0]);
  }
}