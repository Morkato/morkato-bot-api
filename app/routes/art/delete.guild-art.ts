import type { MorkatoAPP } from 'morkato'
import type { Handler } from 'express'

import { extractorParamRequest } from 'utils'

const extractGuildID = extractorParamRequest("guild_id")
const extractArtID = extractorParamRequest("art_id")

export default function prepare(app: MorkatoAPP): Handler {
  return async (req, res) => {
    const guild_id = extractGuildID(req)
    const art_id   = extractArtID(req)
    
    const art = await app.database.delArt({ guild_id, id: art_id })
    return res.status(200).json(art);
  }
}