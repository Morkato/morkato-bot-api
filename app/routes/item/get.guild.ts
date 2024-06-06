import type { MorkatoAPP } from 'morkato'
import type { Handler } from 'express'

import { extractorParamRequest } from 'utils'

const extractGuildID = extractorParamRequest("guild_id")

export default function prepare(app: MorkatoAPP): Handler {
  return async (req, res) => {
    const guild_id = extractGuildID(req)
    const items = await app.database.findItem({ guild_id })
    return res.status(200).json(items);
  }
}