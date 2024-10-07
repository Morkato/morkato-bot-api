package morkato.api.models.family

import morkato.api.infra.validation.AttrSchema
import morkato.api.infra.validation.BannerSchema
import morkato.api.infra.validation.DescriptionSchema
import morkato.api.infra.validation.NameSchema

data class FamilyUpdateData(
  @NameSchema val name: String?,
  @AttrSchema val percent: Int?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
)
