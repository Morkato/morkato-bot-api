import type { MorkatoAPP } from 'morkato'

import { UnauthorizedError } from 'errors'
import { thenHandler } from 'utils'
import { Router } from 'express'

import routePostGuild from './post.guild'
import routeGetGuild from './get.guild'

import routeDeleteGuildItem from './delete.guild-item'
import routePutGuildItem from './put.guild-item'
import routeGetGuildItem from './get.guild-item'

export default (app: MorkatoAPP) => {
  const route = Router()

  route.use(thenHandler((req, res, next) => {
    if (!req.usr.roles.includes('MANAGE:ITEMS')) {
      throw new UnauthorizedError({ type: 'generic.unknown' })
    }

    next()
  }))

  route.get('/:guild_id', app.inject(routeGetGuild(app)))
  route.post('/:guild_id', app.inject(routePostGuild(app)))

  route.get('/:guild_id/:item_id', app.inject(routeGetGuildItem(app)))
  route.put('/:guild_id/:item_id', app.inject(routePutGuildItem(app)))
  route.delete('/:guild_id/:item_id', app.inject(routeDeleteGuildItem(app)))

  return route;
}