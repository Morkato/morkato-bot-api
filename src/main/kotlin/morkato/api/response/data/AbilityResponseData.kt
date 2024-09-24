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
      return AbilityResponseData(
        ability.guildId, ability.id.toString(),
        ability.name,
        ability.type,
        ability.percent,
        ability.npcKind,
        ability.immutable,
        ability.description,
        ability.banner
      )
    }
  }
}
