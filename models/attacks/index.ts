import type { MorkatoAPP } from 'morkato/app'
import type { Guild } from '../guilds'
import type { Art } from '../arts'
import type { Client } from "pg"

import { findAttack, getAttack } from "./get-find"
import { createAttack } from "./create"
import { updateAttack } from "./update"
import { delAttack } from "./delete"

export type Attack = {
  name: string
  id: string
  title: string | null
  name_prefix_art: string | null
  resume_description: string | null
  description: string | null
  banner: string | null
  damage: number
  breath: number
  blood: number
  intents: number
  guild_id: Guild['id']
  art_id: Art['id']
  updated_at: number | null
}

export type AttackDatabase = {
  findAttack({ guild_id, art_id }: Partial<Pick<Attack, 'art_id'>> & Pick<Attack, 'guild_id'>): Promise<Attack[]>
  getAttack({ guild_id, id }: Pick<Attack, 'guild_id' | 'id'>): Promise<Attack>
  createAttack({ guild_id, art_id, name, ...data }: Omit<Partial<Attack>, 'updated_at' | 'id' | 'guild_id'> & Pick<Attack, 'guild_id' | 'art_id' | 'name'>): Promise<Attack>
  updateAttack({ guild_id, id, ...data }: Omit<Partial<Attack>, 'updated_at' | 'art_id' | 'updated_at'> & Pick<Attack, 'id' | 'guild_id'>): Promise<Attack>
  delAttack({ guild_id, id }: Pick<Attack, 'guild_id' | 'id'>): Promise<Attack>
}

export function prepareAttack(app: MorkatoAPP, pg: Client): AttackDatabase {
  return {
    findAttack: findAttack(app, pg),
    getAttack: getAttack(app, pg),
    createAttack: createAttack(app, pg),
    updateAttack: updateAttack(app, pg),
    delAttack: delAttack(app, pg)
  };
}

export default prepareAttack;