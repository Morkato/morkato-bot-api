import type { MorkatoAPP } from 'morkato'
import type { Handler } from 'express'

import { extractorParamRequest } from 'utils'

const extractGuildID = extractorParamRequest("guild_id")
const extractItemID = extractorParamRequest("item_id")

export default function prepare(app: MorkatoAPP): Handler {
  return async (req, res) => {
    const guild_id = extractGuildID(req)
    const item_id = extractItemID(req)
    const item = await app.database.updateItem({ guild_id, id: item_id, ...req.body })

    return res.status(200).json(item);
  }
}