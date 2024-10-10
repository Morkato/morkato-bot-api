package morkato.api.dto.art

import jakarta.validation.constraints.NotNull
import morkato.api.dto.validation.DescriptionSchema
import morkato.api.dto.validation.BannerSchema
import morkato.api.dto.validation.AttrSchema
import morkato.api.model.art.ArtType

data class ArtCreateData(
  @NotNull val name: String,
  @NotNull val type: ArtType,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?,
  val energy: Int?,
  @AttrSchema val life: Long?,
  @AttrSchema val breath: Long?,
  @AttrSchema val blood: Long?
) {}
