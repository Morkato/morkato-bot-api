import type { MorkatoAPP } from 'morkato/app'
// import type { PlayerAttack } from 'type:models/playerAttack'
// import type { PlayerItem } from 'type:models/playerItem'
// import type { PlayerArt } from 'type:models/playerArt'
// import type { Player } from "type:models/player"
import type { Guild } from 'models/guilds'
import type { Attack } from "models/attacks"
import type { Item } from 'models/items'
import type { Art } from 'models/arts'

type Player = any
type PlayerItem = any
type PlayerArt = any
type PlayerAttack = any

export const guild = {
  async create(app: MorkatoAPP, guild: Guild) { },
  async update(app: MorkatoAPP, guild: Guild) { },
  async delete(app: MorkatoAPP, guild: Guild) { }
}
export const player = {
  async create(app: MorkatoAPP, player: Player) {},
  async update(app: MorkatoAPP, before: Player, player: Player) {},
  async delete(app: MorkatoAPP, player: Player) {}
}

export const item = {
  async create(app: MorkatoAPP, item: Item) {},
  async update(app: MorkatoAPP, before: Item, item: Item) {},
  async delete(app: MorkatoAPP, item: Item) {}
}

export const plItem = {
  async create(app: MorkatoAPP, plItem: PlayerItem) {},
  async update(app: MorkatoAPP, before: Item, plItem: PlayerItem) {},
  async delete(app: MorkatoAPP, plItem: PlayerItem) {}
}

export const plArt = {
  async create(app: MorkatoAPP, plArt: PlayerArt) {},
  async delete(app: MorkatoAPP, plArt: PlayerArt) {}
}

export const plAttack = {
  async create(app: MorkatoAPP, plAttack: PlayerAttack) {},
  async delete(app: MorkatoAPP, plAttack: PlayerAttack) {}
}

export const attack = {
  async create(app: MorkatoAPP, player: Attack) {},
  async update(app: MorkatoAPP, before: Item, attack: Attack) {},
  async delete(app: MorkatoAPP, player: Attack) {}
}

export const art = {
  async create(app: MorkatoAPP, player: Art) {},
  async update(app: MorkatoAPP, before: Item, art: Art) {},
  async delete(app: MorkatoAPP, player: Art) {}
}
