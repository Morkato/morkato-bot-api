package morkato.api.response.data

import morkato.api.database.foreign.PlayerAbility

data class PlayerAbilityResponseData(
  val guild_id: String,
  val player_id: String,
  val ability_id: String
) {
  companion object {
    fun from(playerAbility: PlayerAbility) : PlayerAbilityResponseData {
      return PlayerAbilityResponseData(
        playerAbility.guild.id,
        playerAbility.playerId,
        playerAbility.abilityId.toString()
      )
    }
  }
}
