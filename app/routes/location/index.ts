import type { MorkatoAPP } from 'type:morkato'

import { thenHandler } from 'utils'
import { Router } from 'express'

import routeDeleteGuildLocation from './delete.guild-location'
import routePutGuildLocation from './put.guild-location'
import routeGetGuildLocation from './get.guild-location'
import routePostGuild from './post.guild'
import routeGetGuild from './get.guild'

export default (app: MorkatoAPP) => {
  const route = Router()

  route.get("/:guild_id", thenHandler(routeGetGuild(app)))
  route.post("/:guild_id", thenHandler(routePostGuild(app)))
  route.get("/:guild_id/:location_id", thenHandler(routeGetGuildLocation(app)))
  route.put("/:guild_id/:location_id", thenHandler(routePutGuildLocation(app)))
  route.delete("/:guild_id/:location_id", thenHandler(routeDeleteGuildLocation(app)))
  
  return route;
}