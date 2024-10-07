package morkato.api.model.player

import jakarta.validation.constraints.NotNull
import morkato.api.dto.validation.BannerSchema
import morkato.api.dto.validation.KeySchema
import morkato.api.dto.validation.NameSchema

data class PlayerNpcCreateData(
  @NotNull @NameSchema val name: String,
  @NotNull @KeySchema val surname: String,
  @BannerSchema val icon: String?
)
