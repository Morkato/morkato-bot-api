package morkato.api.dto.player

data class PlayerUpdateData(
  val ability_roll: Int?,
  val family_roll: Int?,
  val prodigy_roll: Int?,
  val mark_roll: Int?,
  val berserk_roll: Int?,
  val flags: Int?,
  val family_id: String?
);