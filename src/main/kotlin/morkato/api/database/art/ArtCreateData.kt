package morkato.api.database.art

import jakarta.validation.constraints.NotNull;
import morkato.api.validation.NameSchema;
import morkato.api.validation.DescriptionSchema;
import morkato.api.validation.BannerSchema;

data class ArtCreateData(
  @NotNull val name: String,
  @NotNull val type: ArtType,
  val description: String?,
  val banner: String?
);
