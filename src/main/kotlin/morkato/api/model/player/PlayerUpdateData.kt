package morkato.api.model.player

data class PlayerUpdateData(
  val ability_roll: Int?,
  val family_roll: Int?,
  val is_prodigy: Boolean?,
  val has_mark: Boolean?,
  val family_id: String?
)
