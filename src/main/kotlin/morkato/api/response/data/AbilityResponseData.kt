package morkato.api.response.data

import morkato.api.database.ability.Ability
import morkato.api.database.ability.AbilityType

data class AbilityResponseData(
  val guild_id: String,
  val id: String,
  val name: String,
  val type: AbilityType,
  val percent: Int,
  val npc_kind: Int,
  val immutable: Boolean,
  val description: String?,
  val banner: String?
) {
  companion object {
    fun from(ability: Ability) : AbilityResponseData {
      val payload = ability.payload
      return AbilityResponseData(
        ability.guild.id, payload.id.toString(),
        payload.name,
        payload.type,
        payload.percent,
        payload.npcKind,
        payload.immutable,
        payload.description,
        payload.banner
      )
    }
  }
}
