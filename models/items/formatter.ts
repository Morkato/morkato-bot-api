import type { Item } from '.'

export function formatItem(payload: Record<string, any>): Item {
  return {
    guild_id: payload.guild_id,
    id: payload.id,
    art_id: payload.art_id,
    name: payload.name,
    description: payload.description,
    banner: payload.banner,
    stack: payload.stack,
    usable: payload.usable,
    updated_at: !payload.updated_at ? null : payload.updated_at.getTime()
  }
}