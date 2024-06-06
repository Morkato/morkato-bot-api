import type { MorkatoAPP } from 'morkato'

import { UnauthorizedError } from 'errors'
import { thenHandler } from 'utils'
import { Router } from 'express'

import routePostGuild from './post.guild'
import routeGetGuild from './get.guild'

import routeDeleteGuildArt from './delete.guild-art'
import routePutGuildArt from './put.guild-art'
import routeGetGuildArt from './get.guild-art'

export default (app: MorkatoAPP) => {
  const route = Router()

  route.use(app.inject(((req, res, next) => {
    if (!req.usr.roles.includes('MANAGE:ARTS')) {
      throw new UnauthorizedError({ type: 'generic.unknown' })
    }

    next()
  })))

  route.get('/:guild_id', app.inject(routeGetGuild(app)))
  route.post('/:guild_id', app.inject(routePostGuild(app)))

  route.get('/:guild_id/:art_id', app.inject(routeGetGuildArt(app)))
  route.put('/:guild_id/:art_id', app.inject(routePutGuildArt(app)))
  route.delete('/:guild_id/:art_id', app.inject(routeDeleteGuildArt(app)))

  return route;
}