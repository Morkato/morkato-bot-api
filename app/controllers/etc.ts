import type { Controller } from 'morkato/controller'
import type { Request, Response } from 'express'
import type { LoggerContext } from 'morkato/app'

async function sendPing(req: Request, res: Response) {
  res.status(200).json(Date.now())
}

export function setup(controller: Controller, logger: LoggerContext) {
  controller.get("/ping", sendPing)
}