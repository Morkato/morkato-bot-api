package morkato.api.database.art;

import morkato.api.validation.NameSchema;
import morkato.api.validation.DescriptionSchema;
import morkato.api.validation.BannerSchema;

data class ArtUpdateData(
  @NameSchema val name: String?,
  val type: ArtType?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
);
