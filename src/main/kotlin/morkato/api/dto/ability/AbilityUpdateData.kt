package morkato.api.dto.ability

import morkato.api.dto.validation.DescriptionSchema
import morkato.api.dto.validation.BannerSchema
import morkato.api.dto.validation.NameSchema
import morkato.api.model.ability.AbilityType

data class AbilityUpdateData(
  @NameSchema val name: String?,
  val type: AbilityType?,
  val percent: Int?,
  val npc_kind: Int?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
)
