package morkato.api.models.npc;

import jakarta.validation.constraints.NotNull;
import morkato.api.infra.validation.BannerSchema
import morkato.api.infra.validation.IdSchema
import morkato.api.infra.validation.KeySchema
import morkato.api.infra.validation.NameSchema

data class NpcCreateData(
  @NotNull @NameSchema val name: String,
  @IdSchema @NotNull val family_id: String,
  @NotNull @KeySchema val surname: String,
  @NotNull val type: NpcType,
  @BannerSchema val icon: String?
)