import type { MorkatoAPP } from 'morkato'

import { UnauthorizedError } from 'errors'
import { Router } from 'express'

import routePostGuild from './post.guild'
import routeGetGuild from './get.guild'

import routeDeleteGuildAttack from './delete.guild-attack'
import routePutGuildAttack from './put.guild-attack'
import routeGetGuildAttack from './get.guild-attack'

export default (app: MorkatoAPP) => {
  const route = Router()
  
  route.use(app.inject((req, res, next) => {
    if (!req.usr.roles.includes('MANAGE:ATTACKS')) {
      throw new UnauthorizedError({ type: 'generic.unknown' })
    }

    next()
  }))
  
  route.get('/:guild_id', app.inject(routeGetGuild(app)))
  route.post('/:guild_id', app.inject(routePostGuild(app)))

  route.get('/:guild_id/:attack_id', app.inject(routeGetGuildAttack(app)))
  route.put('/:guild_id/:attack_id', app.inject(routePutGuildAttack(app)))
  route.delete('/:guild_id/:attack_id', app.inject(routeDeleteGuildAttack(app)))

  return route;
}