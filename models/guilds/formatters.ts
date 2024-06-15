import type { Guild } from 'models/guilds'
import type { Art } from 'models/arts'

export function formatGuildArt(payload: any): Omit<Art, 'guild_id'> {
  return {
    name: payload.name,
    id: payload.id,
    type: payload.type,
    description: payload.description,
    banner: payload.banner,
    updated_at: payload.updated_at === null ? null : new Date(payload.updated_at).getTime(),
    attacks: !payload.attacks ? [] : payload.attacks
  }
}

export function formatGuild(payload: any): Guild {
  return {
    id: payload.id,
    default_player_life: payload.default_player_life,
    default_player_breath: payload.default_player_breath,
    default_player_blood: payload.default_player_blood,
    default_player_force: payload.default_player_force,
    default_player_resistance: payload.default_player_resistance,
    default_player_velocity: payload.default_player_velocity,
    arts: !payload.arts ? [] : payload.arts.map(formatGuildArt)
  }
}