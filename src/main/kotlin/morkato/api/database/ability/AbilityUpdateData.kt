package morkato.api.database.ability

import morkato.api.validation.BannerSchema
import morkato.api.validation.DescriptionSchema
import morkato.api.validation.NameSchema

data class AbilityUpdateData(
  @NameSchema val name: String?,
  val type: AbilityType?,
  val percent: Int?,
  val npc_kind: Int?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
) {
  fun empty() : Boolean {
    return name == null && type == null && percent == null && npc_kind == null && description == null && banner == null
  }
}
