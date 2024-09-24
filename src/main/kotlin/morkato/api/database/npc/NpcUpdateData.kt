package morkato.api.database.npc;

import morkato.api.validation.AttrSchema;
import morkato.api.validation.BannerSchema;
import morkato.api.validation.IdSchema;
import morkato.api.validation.KeySchema;
import morkato.api.validation.NameSchema;

data class NpcUpdateData(
  @NameSchema val name: String?,
  @IdSchema val family_id: String?,
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
