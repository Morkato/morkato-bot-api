import type { MorkatoAPP } from 'morkato'
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
    try {
      const result = await pg.query(query, values)
      return formatGuild(result.rows[0]);
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