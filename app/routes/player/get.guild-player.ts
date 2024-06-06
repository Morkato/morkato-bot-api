import type { MorkatoAPP } from 'type:morkato'
import type { Handler } from 'express'

import { extractorParamRequest } from 'utils'

const extractGuildID = extractorParamRequest("guild_id")
const extractPlayerID = extractorParamRequest("player_id")

export default function prepare(app: MorkatoAPP): Handler {
  return async (req, res) => {
    const guild_id = extractGuildID(req)
    const player_id = extractPlayerID(req)
    
    const player = await app.database.getPlayer({ guild_id, id: player_id })

    return res.status(200).json(player);
  }
}