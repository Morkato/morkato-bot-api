import type { Art } from '.'

export function formatArt(payload: Record<string, any>): Art {
  return {
    name: payload.name,
    type: payload.type,
    id: payload.id,
    guild_id: payload.guild_id,
    description: payload.description,
    banner: payload.banner,
    updated_at: !payload.updated_at ? null : payload.updated_at.getTime(),
    attacks: !payload.attacks ? [] : payload.attacks
  }
}