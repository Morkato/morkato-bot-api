import type { PayloadType } from 'morkato/controller'
import type { Controller } from 'morkato/controller'
import type { LoggerContext } from 'morkato/app'
import type { Request, Response } from 'express'
import type { Guild } from 'models/guilds'

async function getGuild(req: Request, res: Response<Guild>, { models, params: { guild_id } }: PayloadType<any>) {
  const guild = await models.getGuild({ id: guild_id })
  res.status(200).json(guild)
}

async function delGuild(req: Request, res: Response<Guild>, { models, params: { guild_id } }: PayloadType<any>) {
  const guild = await models.delGuild({ id: guild_id })
  res.status(200).json(guild)
}

export function setup(controller: Controller) {
  controller.get("/guilds/:guild_id", getGuild)
  controller.delete("/guilds/:guild_id", delGuild)
}