import type { Location } from '.'

export function formatLocation(payload: Record<string, any>): Location {
  return {
    guild_id: payload.guild_id,
    id: payload.id,
    name: payload.name,
    location_id: payload.location_id,
    description: payload.description,
    resume_description: payload.resume_description,
    access: payload.access,
    banner: payload.banner,
    updated_at: payload.updated_at === null ? null : new Date(payload.updated_at).getTime()
  };
}