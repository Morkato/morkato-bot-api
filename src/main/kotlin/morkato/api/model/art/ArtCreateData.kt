package morkato.api.models.art

import jakarta.validation.constraints.NotNull;

data class ArtCreateData(
  @NotNull val name: String,
  @NotNull val type: ArtType,
  val description: String?,
  val banner: String?
);
