package morkato.api.dto.guild

data class GuildUpdateData(
  val human_initial_life: Long?,
  val oni_initial_life: Long?,
  val hybrid_initial_life: Long?,
  val breath_initial: Long?,
  val blood_initial: Long?,
  val family_roll: Int?,
  val ability_roll: Int?,
  val roll_category_id: String?,
  val off_category_id: String?
)
