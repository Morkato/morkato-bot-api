import type { MorkatoAPP } from 'type:morkato'
import type { Handler } from 'express'

import { extractorParamRequest } from 'utils'

const extractGuildID = extractorParamRequest("guild_id")
const extractPlayerID = extractorParamRequest("player_id")

export default function prepare(app: MorkatoAPP): Handler {
  return async (req, res) => {
    const player_id = extractGuildID(req)
    const guild_id  = extractPlayerID(req)

    const playerAttacks = await app.database.findPlayerAttack({
      player_id: player_id,
      guild_id:  guild_id
    })

    res
      .status(200)
      .json(playerAttacks)
  }
}