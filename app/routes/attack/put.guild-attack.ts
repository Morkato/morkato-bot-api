import type { MorkatoAPP } from 'morkato'
import type { Handler } from 'express'

import { extractorParamRequest } from 'utils'

const extractGuildID = extractorParamRequest("guild_id")
const extractAttackID = extractorParamRequest("attack_id")

export default function prepare(app: MorkatoAPP): Handler {
  return async (req, res) => {
    const guild_id = extractGuildID(req)
    const attack_id = extractAttackID(req)
    
    const attack = await app.database.updateAttack({ guild_id, id: attack_id, ...req.body })

    return res.status(200).json(attack);
  }
}