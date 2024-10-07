package morkato.api.model.ability

import jakarta.validation.constraints.NotNull
import morkato.api.dto.validation.NameSchema
import morkato.api.dto.validation.DescriptionSchema
import morkato.api.dto.validation.BannerSchema

data class AbilityCreateData(
  @NameSchema @NotNull val name: String,
  @NotNull val type: AbilityType,
  val percent: Int?,
  @NotNull val npc_kind: Int,
  val immutable: Boolean?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
)
