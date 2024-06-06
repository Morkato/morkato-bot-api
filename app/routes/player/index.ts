import type { MorkatoAPP } from 'type:morkato'

import { UnauthorizedError } from 'errors'
import { thenHandler } from 'utils'
import { Router } from 'express'

import routePostGuild from './post.guild'
import routeGetGuild from './get.guild'

import routeDeleteGuildPlayer from './delete.guild-player'
import routePutGuildPlayer from './put.guild-player'
import routeGetGuildPlayer from './get.guild-player'

import routeGetGuildPlayerInventory from './get.guild-player-inventory'

import routeDeleteGuildPlayerAttack from './delete.guild-player-attack'
import routePostGuildPlayerAttack from './post.guild-player-attack'
import routeGetGuildPlayerAttacks from './get.guild-player-attacks'

import routeDeleteGuildPlayerArt from './delete.guild-player-art'
import routePostGuildPlayerArt from './post.guild-player-art'
import routeGeGuildPlayerArts from './get.guild-player-arts'

export default (app: MorkatoAPP) => {
  const route = Router()

  route.use(thenHandler((req, res, next) => {
    if (!req.usr.roles.includes('MANAGE:PLAYERS')) {
      throw new UnauthorizedError({ type: 'generic.unknown' })
    }

    next()
  }))

  route.get('/:guild_id', thenHandler(routeGetGuild(app)))
  route.post('/:guild_id', thenHandler(routePostGuild(app)))

  route.get('/:guild_id/:player_id', thenHandler(routeGetGuildPlayer(app)))
  route.put('/:guild_id/:player_id', thenHandler(routePutGuildPlayer(app)))
  route.delete('/:guild_id/:player_id', thenHandler(routeDeleteGuildPlayer(app)))
  
  route.get('/:guild_id/:player_id/arts', thenHandler(routeGeGuildPlayerArts(app)))
  route.post('/:guild_id/:player_id/arts/:art_id', thenHandler(routePostGuildPlayerArt(app)))
  route.delete('/:guild_id/:player_id/arts/:art_id', thenHandler(routeDeleteGuildPlayerArt(app)))

  route.get('/:guild_id/:player_id/attacks', thenHandler(routeGetGuildPlayerAttacks(app)))
  route.post('/:guild_id/:player_id/attacks/:attack_id', thenHandler(routePostGuildPlayerAttack(app)))
  route.delete('/:guild_id/:player_id/attacks/:attack_id', thenHandler(routeDeleteGuildPlayerAttack(app)))

  route.get('/:guild_id/:player_id/inventory', thenHandler(routeGetGuildPlayerInventory(app)))

  return route;
}