package morkato.api.database.npc;

import jakarta.validation.constraints.NotNull;
import morkato.api.validation.BannerSchema;
import morkato.api.validation.NameSchema;
import morkato.api.validation.KeySchema;
import morkato.api.validation.AttrSchema;

data class NpcCreateData(
  @NotNull @NameSchema val name: String,
  @NotNull @KeySchema val surname: String,
  @NotNull val type: NpcType,
  val energy: Int?,
  @AttrSchema val max_life: Long?,
  @AttrSchema val max_breath: Long?,
  @AttrSchema val max_blood: Long?,
  @AttrSchema val current_life: Long?,
  @AttrSchema val current_breath: Long?,
  @AttrSchema val current_blood: Long?,
  @BannerSchema val icon: String?
);
