import type { MorkatoAPP } from 'type:morkato'
import type { Handler } from 'express'

import { extractorParamRequest, extractorHeaderRequest } from 'utils'
import { ValidationError } from 'errors'

const extractGuildID = extractorParamRequest("guild_id")
const extractHeaderContentTake = extractorHeaderRequest("Content-Take")
const extractHeaderContentSkip = extractorHeaderRequest("Content-Skip")

export default function prepare(app: MorkatoAPP): Handler {
  return async (req, res) => {
    const guild_id = extractGuildID(req)
    const take = Number.parseInt(extractHeaderContentTake(req))
    const skip = Number.parseInt(extractHeaderContentSkip(req))

    if (typeof take !== 'number' || typeof skip !== 'number') {
      throw new ValidationError({});
    }
    
    const playerItems = await app.database.findPlayerItem({ guild_id, take, skip })

    return res.status(200).json(playerItems);
  }
}