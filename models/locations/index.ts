import type { ConnectionContext } from "models/database"
import { getLocation, findLocation } from "./get-find"
import { createLocation } from "./create"
import { updateLocation } from "./update"
import { delLocation } from "./delete"

export type Location = {
  guild_id: string
  id: string
  name: string
  location_id: string | null
  description: string | null
  resume_description: string | null
  access: number
  banner: string | null
  updated_at: number | null
}

export type LocationDatabase = {
  findLocation({ guild_id, location_id }: Partial<Pick<Location, 'location_id'>> & Pick<Location, 'guild_id'>): Promise<Location[]>
  getLocation({ guild_id, id }: Pick<Location, "guild_id" | "id">): Promise<Location>
  createLocation({ guild_id, id, ...data }: Omit<Partial<Location>, 'updated_at'> & Pick<Location, 'guild_id' | 'id' | 'name'>): Promise<Location>
  updateLocation({ guild_id, id, name, ...data }: Omit<Partial<Location>, 'updated-at'> & Pick<Location, 'guild_id' | 'id'>): Promise<Location>
  delLocation({ guild_id, id }: Pick<Location, 'guild_id' | 'id'>): Promise<Location>
}

export function prepareLocation(ctx: ConnectionContext): LocationDatabase {
  return {
    findLocation: findLocation(ctx),
    getLocation: getLocation(ctx),
    createLocation: createLocation(ctx),
    updateLocation: updateLocation(ctx),
    delLocation: delLocation(ctx)
  };
}

export default prepareLocation;