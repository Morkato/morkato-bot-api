package morkato.api.dto.art

import morkato.api.dto.validation.DescriptionSchema
import morkato.api.dto.validation.BannerSchema
import morkato.api.dto.validation.NameSchema
import morkato.api.dto.validation.AttrSchema
import morkato.api.model.art.ArtType

data class ArtUpdateData(
  @NameSchema val name: String?,
  val type: ArtType?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?,
  val energy: Int?,
  @AttrSchema val life: Long?,
  @AttrSchema val breath: Long?,
  @AttrSchema val blood: Long?
) {}
