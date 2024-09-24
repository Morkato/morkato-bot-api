package morkato.api.database.family

import jakarta.validation.constraints.NotNull
import morkato.api.validation.NameSchema
import morkato.api.validation.DescriptionSchema
import morkato.api.validation.BannerSchema

data class FamilyCreateData(
  @NameSchema @NotNull val name: String,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
) {
}