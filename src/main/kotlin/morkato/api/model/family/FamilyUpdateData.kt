package morkato.api.model.family

import morkato.api.dto.validation.AttrSchema
import morkato.api.dto.validation.BannerSchema
import morkato.api.dto.validation.DescriptionSchema
import morkato.api.dto.validation.NameSchema

data class FamilyUpdateData(
  @NameSchema val name: String?,
  @AttrSchema val percent: Int?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
)
