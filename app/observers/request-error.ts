import type { Request, Response } from 'express'
import type { MorkatoAPP } from 'morkato/app'

import { ErrorType } from 'errors/bases/base'
import { BaseError } from 'errors'

const locationCode = "request-error"

export async function onRequestError(app: MorkatoAPP, req: Request, res: Response, error: Error, reset: () => Promise<void>) {
  app.logger.error(locationCode, "An error ocurred (%s): %s", error, error.name, error.message)
  if (app.dev) {
    console.log(error.stack)
  }
  if (error instanceof BaseError) {
    const payload: Record<string, unknown> = {
      message: error.message,
      locationCode: error.errorLocationCode,
      type: error.type
    }

    if (app.dev) {
      payload.isDevelopmentAmbient = true
    }

    res.status(error.statusCode ?? 500).json(payload)
    return;
  }

  const payload: Record<string, unknown> = {
    message: "Um erro interno não mapeado ocorreu.",
    locationCode: "unbound",
    type: ErrorType.GENERIC
  }
  
  if (app.dev) {
    payload.isDevelopmentAmbient = true
  }

  res.status(500).json(payload)
}