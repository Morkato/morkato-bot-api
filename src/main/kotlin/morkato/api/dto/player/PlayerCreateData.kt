package morkato.api.dto.player

import morkato.api.model.npc.NpcType

data class PlayerCreateData(
  val ability_roll: Int?,
  val family_roll: Int?,
  val is_prodigy: Boolean?,
  val has_mark: Boolean?,
  val expected_npc_kind: NpcType
);
