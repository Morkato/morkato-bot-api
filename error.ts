import type { Request, Response } from "express"
import { BaseError } from "errors"

export default (err: any, req: Request, res: Response) => {
  if (err instanceof BaseError) {
    res.status(err.statusCode ?? 500).json(err.getJSONResponse(req))
    
    return;
  }  

  res.status(500).json({
    message: "Erro interno",
    type: "generic.unknown"
  })
}