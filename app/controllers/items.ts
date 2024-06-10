import type { PayloadType } from 'morkato/controller'
import type { Controller } from 'morkato/controller'
import type { Request, Response } from 'express'
import type { Item } from 'models/items'

async function getItem(req: Request, res: Response<Item>, { models, params: { guild_id, item_id } }: PayloadType<any>) {
  const item = await models.getItem({ guild_id, id: item_id })
  res.status(200).json(item)
}

async function postItem(req: Request, res: Response<Item>, { models, params: { guild_id } }: PayloadType<any>) {
  const item = await models.createItem({ guild_id, ...req.body })
  res.status(201).json(item)
}

async function putItem(req: Request, res: Response<Item>, { models, params: { guild_id, item_id } }: PayloadType<any>) {
  const item = await models.updateItem({ guild_id, id: item_id, ...req.body })
  res.status(200).json(item)
}

async function delItem(req: Request, res: Response<Item>, { models, params: { guild_id, item_id } }: PayloadType<any>) {
  const item = await models.delItem({ guild_id, id: item_id })
  res.status(200).json(item)
}

export function setup(controller: Controller) {
  controller.get("/items/:guild_id/:item_id", getItem)
  controller.post("/items/:guild_id", postItem)
  controller.put("/items/:guild_id/:item_id", putItem)
  controller.delete("/items/:guild_id/:item_id", delItem)
}