package morkato.api.dto.ability

import morkato.api.model.ability.Ability
import morkato.api.model.ability.AbilityType

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
  public constructor(ability: Ability) : this(
    ability.guild.id,
    ability.id.toString(),
    ability.name,
    ability.type,
    ability.percent,
    ability.npcKind,
    ability.immutable,
    ability.description,
    ability.banner
  ) {}
}
