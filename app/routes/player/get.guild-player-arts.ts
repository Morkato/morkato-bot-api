import type { MorkatoAPP } from 'type:morkato'
import type { Handler } from 'express'

import { extractorParamRequest } from 'utils'

const extractGuildID = extractorParamRequest("guild_id")
const extractPlayerID = extractorParamRequest("player_id")

export default function prepare(app: MorkatoAPP): Handler {
  return async (req, res) => {
    const player_id = extractPlayerID(req)
    const guild_id  = extractGuildID(req)

    const arts = await app.database.findPlayerArt({
      guild_id: guild_id,
      player_id: player_id
    })

    res
      .status(200)
      .json(arts)
  }
}