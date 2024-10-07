package morkato.api.model.npc;

import morkato.api.dto.validation.AttrSchema;
import morkato.api.dto.validation.BannerSchema;
import morkato.api.dto.validation.KeySchema;
import morkato.api.dto.validation.NameSchema;

data class NpcUpdateData(
  @NameSchema val name: String?,
  @KeySchema val surname: String?,
  val type: NpcType?,
  val energy: Int?,
  @AttrSchema val max_life: Long?,
  @AttrSchema val max_breath: Long?,
  @AttrSchema val max_blood: Long?,
  @AttrSchema val current_life: Long?,
  @AttrSchema val current_breath: Long?,
  @AttrSchema val current_blood: Long?,
  @BannerSchema val icon: String?
);
