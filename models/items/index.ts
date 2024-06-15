import type { ConnectionContext } from 'models/database'
import { findItem, getItem } from "./get-find"
import { createItem } from "./create"
import { updateItem } from "./update"
import { delItem } from "./delete"

export type Item = {
  guild_id: string
  id: string
  art_id: string | null
  name: string
  description: string | null
  banner: string | null
  stack: number
  usable: boolean
  updated_at: number | null
}

export type ItemDatabase = {
  findItem({ guild_id }: Pick<Item, 'guild_id'>): Promise<Item[]>
  getItem({ guild_id, id }: Pick<Item, 'guild_id' | 'id'>): Promise<Item>
  createItem({ guild_id, name, ...data }: Omit<Partial<Item>, 'updated_at' | 'id'> & Pick<Item, 'guild_id' | 'name'>): Promise<Item>
  updateItem({ guild_id, id, ...data }: Omit<Partial<Item>, 'updated_at'> & Pick<Item, 'guild_id' | 'id'> ): Promise<Item>
  delItem({ guild_id, id }: Pick<Item, 'guild_id' | 'id'>): Promise<Item>
}

export function prepareItem(ctx: ConnectionContext): ItemDatabase {
  return {
    findItem: findItem(ctx),
    getItem: getItem(ctx),
    createItem: createItem(ctx),
    updateItem: updateItem(ctx),
    delItem: delItem(ctx)
  }
}

export default prepareItem;