package morkato.api.model.art;

import morkato.api.dto.validation.NameSchema;
import morkato.api.dto.validation.DescriptionSchema;
import morkato.api.dto.validation.BannerSchema;

data class ArtUpdateData(
  @NameSchema val name: String?,
  val type: ArtType?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
);
