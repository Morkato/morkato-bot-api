import type { MorkatoAPP } from 'type:morkato'
import type { Handler } from 'express'

import { extractorParamRequest } from 'utils'

const extractGuildID = extractorParamRequest("guild_id")
const extractPlayerID = extractorParamRequest("player_id")
const extractArtID = extractorParamRequest("art_id")

export default function prepare(app: MorkatoAPP): Handler {
  return async (req, res) => {
    const player_id = extractPlayerID(req)
    const guild_id  = extractGuildID(req)
    const art_id    = extractArtID(req)

    const playerArt = await app.database.createPlayerArt({
      player_id: player_id,
      guild_id:   guild_id,
      art_id:       art_id
    })

    res
      .status(201)
      .json(playerArt)
  }
}