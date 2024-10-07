package morkato.api.models.ability

import jakarta.validation.constraints.NotNull
import morkato.api.infra.validation.NameSchema
import morkato.api.infra.validation.DescriptionSchema
import morkato.api.infra.validation.BannerSchema

data class AbilityCreateData(
  @NameSchema @NotNull val name: String,
  @NotNull val type: AbilityType,
  val percent: Int?,
  @NotNull val npc_kind: Int,
  val immutable: Boolean?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
)
