package morkato.api.database.attack;

import jakarta.validation.constraints.NotNull;
import morkato.api.validation.NameSchema;
import morkato.api.validation.NamePrefixArtSchema;
import morkato.api.validation.DescriptionSchema;
import morkato.api.validation.BannerSchema;
import morkato.api.validation.AttrSchema;

data class AttackCreateData(
  @NotNull @NameSchema val name: String,
  @NamePrefixArtSchema val name_prefix_art: String?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?,
  @AttrSchema val damage: Long?,
  @AttrSchema val breath: Long?,
  @AttrSchema val blood: Long?,
  val intents: Int?
);
