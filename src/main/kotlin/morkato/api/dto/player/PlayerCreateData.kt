package morkato.api.dto.player

import morkato.api.model.npc.NpcType

data class PlayerCreateData(
  val ability_roll: Int?,
  val family_roll: Int?,
  val prodigy_roll: Int?,
  val mark_roll: Int?,
  val berserk_roll: Int?,
  val flags: Int?,
  val expected_npc_kind: NpcType
);
