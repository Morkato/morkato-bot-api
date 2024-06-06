import type { Request, Response, NextFunction } from 'express'
import type { MorkatoAPP } from 'morkato'
import botUser from 'bot.user.json'
import anonymous from 'anonymous'

export type Role =
  | 'MANAGE:GUILDS'
  | "MANAGE:PLAYERS"
  | "MANAGE:PLAYER:ATTACKS"
  | "MANAGE:PLAYER:ITEMS"
  | "MANAGE:PLAYER:ARTS"
  | "MANAGE:ITEMS"
  | "MANAGE:ATTACKS"
  | "MANAGE:ARTS"

export type User = {
  name: string
  authorization: string
  roles: Role[]
}

declare global {
  export namespace Express {
    export interface Request {
      usr: User
      isBot: boolean
    }
  }
}

export default (app: MorkatoAPP) => {
  return async (req: Request, res: Response, next: NextFunction) => {
    if (req.headers.authorization === botUser.authorization) {
      req.usr = botUser as User
      req.isBot = true

      return next();
    }

    req.isBot = false
    req.usr = anonymous

    next();
  };
}