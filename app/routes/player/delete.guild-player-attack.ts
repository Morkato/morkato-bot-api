import type { MorkatoAPP } from 'type:morkato'
import type { Handler } from 'express'

import { extractorParamRequest } from 'utils'

const extractGuildID = extractorParamRequest("guild_id")
const extractPlayerID = extractorParamRequest("player_id")
const extractAttackID = extractorParamRequest("attack_id")

export default function prepare(app: MorkatoAPP): Handler {
  return async (req, res) => {
    const player_id = extractPlayerID(req)
    const attack_id = extractAttackID(req)
    const guild_id  = extractGuildID(req)

    const playerAttack = await app.database.delPlayerAttack({
      player_id: player_id,
      attack_id: attack_id,
      guild_id:  guild_id
    })

    res
      .status(200)
      .json(playerAttack)
  }
}