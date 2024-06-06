import type { Attack } from '.'

export function formatAttack(payload: Record<string, any>): Attack {
  return {
    name_prefix_art: payload.name_prefix_art,
    name: payload.name,
    id: payload.id,
    art_id: payload.art_id,
    guild_id: payload.guild_id,
    title: payload.title,
    resume_description: payload.resume_description,
    description: payload.description,
    banner: payload.banner,
    damage: payload.damage,
    breath: payload.breath,
    blood: payload.blood,
    intents: payload.intents,
    updated_at: payload.updated_at === null ? null : payload.updated_at.getTime()
  }
}