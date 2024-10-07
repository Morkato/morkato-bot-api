package morkato.api.model.ability

import morkato.api.dto.validation.BannerSchema
import morkato.api.dto.validation.DescriptionSchema
import morkato.api.dto.validation.NameSchema

data class AbilityUpdateData(
  @NameSchema val name: String?,
  val type: AbilityType?,
  val percent: Int?,
  val npc_kind: Int?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
) {
}
