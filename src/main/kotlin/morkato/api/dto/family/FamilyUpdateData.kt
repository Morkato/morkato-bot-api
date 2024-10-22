package morkato.api.dto.family

import jakarta.validation.constraints.Digits
import morkato.api.dto.validation.DescriptionSchema
import morkato.api.dto.validation.BannerSchema
import morkato.api.dto.validation.AttrSchema
import morkato.api.dto.validation.NameSchema
import java.math.BigDecimal

data class FamilyUpdateData(
  @NameSchema val name: String?,
  @Digits(integer = 3, fraction = 0)
  val percent: BigDecimal?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
);