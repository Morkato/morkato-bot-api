package morkato.api.dto.attack

import morkato.api.dto.validation.NamePrefixArtSchema
import morkato.api.dto.validation.DescriptionSchema
import morkato.api.dto.validation.NameSchema
import morkato.api.dto.validation.BannerSchema
import morkato.api.dto.validation.AttrSchema

data class AttackUpdateData(
  @NameSchema val name: String?,
  @NamePrefixArtSchema val name_prefix_art: String?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?,
  @AttrSchema val damage: Long?,
  @AttrSchema val breath: Long?,
  @AttrSchema val blood: Long?,
  val intents: Int?
) {}
