import type { PayloadType } from 'morkato/controller'
import type { Controller } from 'morkato/controller'
import type { LoggerContext } from 'morkato/app'
import type { Request, Response } from 'express'

async function getGuildArts(req: Request, res: Response, { models, params: { guild_id } }: PayloadType<any>) {
  const arts = await models.findArt({ guild_id })
  res.status(200).json(arts)
}

async function getGuildArt(req: Request, res: Response, { models, params: { guild_id, art_id } }: PayloadType<any>) {
  const art = await models.getArt({ guild_id, id: art_id })
  res.status(200).json(art)
}

async function createGuildArt(req: Request, res: Response, { models, params: { guild_id } }: PayloadType<any>) {
  const art = await models.createArt({ guild_id, ...req.body })
  res.status(201).json(art)
}

async function updateGuildArt(req: Request, res: Response, { models, params: { guild_id, art_id } }: PayloadType<any>) {
  const art = await models.updateArt({ guild_id, id: art_id, ...req.body })
  res.status(200).json(art)
}

export async function delGuildArt(req: Request, res: Response, { models, params: { guild_id, art_id } }: PayloadType<any>) {
  const art = await models.delArt({ guild_id, id: art_id })
  res.status(200).json(art)
}

export function setup(controller: Controller, logger: LoggerContext) {
  controller.get("/arts/:guild_id", getGuildArts)
  controller.get("/arts/:guild_id/:art_id", getGuildArt)
  controller.post("/arts/:guild_id", createGuildArt)
  controller.put("/arts/:guild_id/:art_id", updateGuildArt)
  controller.delete("/arts/:guild_id/:art_id", delGuildArt)
}