import type { Request, Response, NextFunction } from 'express'
import type { MorkatoAPP } from 'morkato'

const locationCode = "api/logging"

export default (app: MorkatoAPP) => {
  return async (req: Request, res: Response, next: NextFunction) => {
    app.logger.debug(locationCode, "%s %s %s", req.method, req.usr.name, req.originalUrl)
    if (req.usr.name == 'anonymous') {
      res.status(401).json({ message: "This API is for MorkatoBOT only!" })
      return;
    }
    return next();
  };
}