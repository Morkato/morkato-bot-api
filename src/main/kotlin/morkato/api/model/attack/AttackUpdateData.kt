package morkato.api.models.attack;

import morkato.api.infra.validation.NameSchema;
import morkato.api.infra.validation.NamePrefixArtSchema;
import morkato.api.infra.validation.DescriptionSchema;
import morkato.api.infra.validation.BannerSchema;
import morkato.api.infra.validation.AttrSchema;

data class AttackUpdateData(
  @NameSchema val name: String?,
  @NamePrefixArtSchema val name_prefix_art: String?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?,
  @AttrSchema val damage: Long?,
  @AttrSchema val breath: Long?,
  @AttrSchema val blood: Long?,
  val intents: Int?
);
