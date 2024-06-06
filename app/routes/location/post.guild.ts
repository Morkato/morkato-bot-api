import type { MorkatoAPP } from 'type:morkato'
import type { Handler } from 'express'

import { extractorParamRequest } from 'utils'

const getGuildID = extractorParamRequest("guild_id")

export default function prepare(app: MorkatoAPP): Handler {
  return async (req, res) => {
    const guild_id = getGuildID(req)
    const location = await app.database.createLocation({ guild_id, data: req.body })
    res.status(201).json(location)
  }
}