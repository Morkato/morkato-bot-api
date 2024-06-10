import type { PayloadType } from 'morkato/controller'
import type { Controller } from 'morkato/controller'
import type { LoggerContext } from 'morkato/app'
import type { Request, Response } from 'express'

async function postAttack(req: Request, res: Response, { models, params: { guild_id } }: PayloadType<any>) {
  const attack = await models.createAttack({ guild_id, ...req.body })
  res.status(201).json(attack)
}

async function putAttack(req: Request, res: Response, { models, params: { guild_id, attack_id } }: PayloadType<any>) {
  const attack = await models.updateAttack({ guild_id, id: attack_id, ...req.body })
  res.status(200).json(attack)
}

async function delAttack(req: Request, res: Response, { models, params: { guild_id, attack_id } }: PayloadType<any>) {
  const attack = await models.delAttack({ guild_id, id: attack_id, ...req.body })
  res.status(200).json(attack)
}

export function setup(controller: Controller, logger: LoggerContext) {
  controller.post("/attacks/:guild_id", postAttack)
  controller.put("/attacks/:guild_id/:attack.id", putAttack)
  controller.delete("/attacks/:guild_id/:attack.id", delAttack)
}