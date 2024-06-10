import type { MorkatoAPP } from 'morkato/app'
import type { Client } from 'pg'

import { findArt, getArt } from "./get-find"
import { createArt } from "./create"
import { updateArt } from "./update"
import { delArt } from "./delete"

export type ArtType = "RESPIRATION" | "KEKKIJUTSU" | 'FIGHTING_STYLE'
export type Art = {
  name: string
  type: ArtType
  id: string
  description: string | null
  banner: string | null
  guild_id: string
  updated_at: null | number
  attacks: Array<Omit<any, 'guild_id' | 'art_id'>>
}
export type ArtDatabase = {
  findArt({ guild_id }: Pick<Art, 'guild_id'>): Promise<Art[]>
  getArt({ guild_id, id }: Pick<Art, 'guild_id' | 'id'>): Promise<Art>
  createArt({ guild_id, ...data }: Partial<Omit<Art, 'attacks' | 'updated_at' | 'id'>> & Pick<Art, 'guild_id' | 'name' | 'type'>): Promise<Art>
  updateArt({ guild_id, id, ...data }: Partial<Omit<Art, 'attacks' | 'updated_at'>> & Pick<Art, 'guild_id' | 'id'>): Promise<Art>
  delArt({ guild_id, id }: Pick<Art, 'guild_id' | 'id'>): Promise<Art>
}

export function prepareArt(app: MorkatoAPP, pg: Client): ArtDatabase {
  return {
    findArt: findArt(app, pg),
    getArt: getArt(app, pg),
    createArt: createArt(app, pg),
    updateArt: updateArt(app, pg),
    delArt: delArt(app, pg)
  };
}

export default prepareArt;