import type { MorkatoAPP } from 'morkato'
import type { Handler } from 'express'

import { extractorParamRequest } from 'utils'

const getGuildID = extractorParamRequest("guild_id")
const getLocationID = extractorParamRequest("location_id")

export default function prepare(app: MorkatoAPP): Handler {
  return async (req, res) => {
    const guild_id = getGuildID(req)
    const location_id = getLocationID(req)
    const location = await app.database.delLocation({ guild_id, id: location_id })
    res.status(200).json(location)
  }
}