import type { ConnectionContext } from 'models/database'
import type { Art } from '../arts'

import { getGuild } from "./get-find"
import { createGuild, createAnonymousGuild } from "./create"
import { delGuild } from "./delete"

export type Guild = {
  id: string
  default_player_life: number
  default_player_breath: number
  default_player_blood: number
  default_player_resistance: number
  default_player_force: number
  default_player_velocity: number
  arts: Array<Omit<Art, 'guild_id'>>
}

export type GuildDatabase = {
  getGuild({ id }: Pick<Guild, 'id'>): Promise<Guild>
  createGuild({ id, ...data }: Omit<Partial<Guild>, 'id' | 'arts'> & Pick<Guild, 'id'>): Promise<Guild>
  createAnonymousGuild({ id }: Pick<Guild, 'id'>): Promise<Guild>
  delGuild({ id }: Pick<Guild, 'id'>): Promise<Guild>
}

export function prepareGuild(ctx: ConnectionContext): GuildDatabase {
  return {
    getGuild: getGuild(ctx),
    createGuild: createGuild(ctx),
    createAnonymousGuild: createAnonymousGuild(ctx),
    delGuild: delGuild(ctx)
  }
}

export default prepareGuild;