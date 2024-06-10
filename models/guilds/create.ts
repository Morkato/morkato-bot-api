import type { MorkatoAPP } from 'morkato/app'
import type { GuildDatabase } from '.'
import type { Client } from 'pg'

import { GuildAlreadyExistsError, InternalServerError } from 'errors'
import { guildInsertQueryBuilder } from 'models/queries/guilds'
import { formatGuild } from './formatters'
import { DatabaseError } from 'pg'

const locationCode = "models/guilds"

export function assertGuildCreateError(err: DatabaseError, guild_id: string) {
  if (err.constraint === 'guilds_pkey') {
    throw new GuildAlreadyExistsError({
      errorLocationCode: locationCode,
      guild_id: guild_id
    });
  }
}

export function createGuild(app: MorkatoAPP, pg: Client): GuildDatabase['createGuild'] {
  const logger = app.getLoggerContext(locationCode)
  return async ({ id, default_player_life, default_player_breath, default_player_blood, default_player_force, default_player_resistance, default_player_velocity }) => {
    const values: unknown[] = []
    const payload = {
      id: id,
      default_player_life: default_player_life,
      default_player_breath: default_player_breath,
      default_player_blood: default_player_blood,
      default_player_force: default_player_force,
      default_player_resistance: default_player_resistance,
      default_player_velocity: default_player_velocity
    }

    const query = guildInsertQueryBuilder.sql({}, payload, values)
    logger.debug("SQL QUERY: %s with values: %s", query, values)
    try {
      const {rows} = await pg.query(query, values)
      logger.debug("RESULT CREATE QUERY: %s with payload: %s", rows, payload)
      const guild = formatGuild(rows[0])
      app.notify("guild.create", guild)
      return guild;
    } catch (err) {
      if (err instanceof DatabaseError) {
        assertGuildCreateError(err, id)
      }
      
      throw new InternalServerError({
        errorLocationCode: locationCode
      });
    }
  }
}