package morkato.api.response.data

import morkato.api.database.foreign.PlayerFamily

data class PlayerFamilyResponseData(
  val guild_id: String,
  val player_id: String,
  val family_id: String
) {
  companion object {
    fun from(playerFamily: PlayerFamily) : PlayerFamilyResponseData {
      return PlayerFamilyResponseData(
        playerFamily.guild.id,
        playerFamily.playerId,
        playerFamily.familyId.toString()
      )
    }
  }
}
