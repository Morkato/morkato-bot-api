import type { Request, Response } from 'express'
import type { MorkatoAPP } from 'morkato'

import { ErrorType } from 'errors/bases/base'
import { BaseError } from 'errors'

export async function onRequestError(app: MorkatoAPP, req: Request, res: Response, error: Error) {
  if (error instanceof BaseError) {
    const payload = {
      message: error.message,
      locationCode: error.errorLocationCode,
      type: error.type
    }

    res.status(error.statusCode ?? 500).json(payload)
    return;
  }

  const payload = {
    message: "Um erro interno não mapeado ocorreu.",
    locationCode: "unbound",
    type: ErrorType.GENERIC
  }
}