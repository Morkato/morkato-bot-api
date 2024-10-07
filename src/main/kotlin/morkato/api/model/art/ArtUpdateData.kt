package morkato.api.models.art;

import morkato.api.infra.validation.NameSchema;
import morkato.api.infra.validation.DescriptionSchema;
import morkato.api.infra.validation.BannerSchema;

data class ArtUpdateData(
  @NameSchema val name: String?,
  val type: ArtType?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
);
