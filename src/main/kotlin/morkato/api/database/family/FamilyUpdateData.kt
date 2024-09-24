package morkato.api.database.family

import morkato.api.validation.BannerSchema
import morkato.api.validation.DescriptionSchema
import morkato.api.validation.NameSchema

data class FamilyUpdateData(
  @NameSchema val name: String?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
)
