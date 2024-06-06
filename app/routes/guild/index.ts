import type { MorkatoAPP } from 'morkato'

import { UnauthorizedError } from 'errors'
import { thenHandler } from 'utils'
import { Router } from 'express'

import routeGetGuild from './get.guild'

export default (app: MorkatoAPP) => {
  const route = Router()
  
  route.use(app.inject((req, res, next) => {
    if (!req.usr.roles.includes('MANAGE:GUILDS')) {
      throw new UnauthorizedError({ type: 'generic.unknown' })
    }

    next()
  }))
  
  route.get('/:guild_id', app.inject(routeGetGuild(app)))

  return route;
}