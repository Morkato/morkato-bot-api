package morkato.api.dto.guild

import morkato.api.model.guild.Guild

data class GuildResponseData(
  val id: String,
  val human_initial_life: Long,
  val oni_initial_life: Long,
  val hybrid_initial_life: Long,
  val breath_initial: Long,
  val blood_initial: Long,
  val family_roll: Int,
  val ability_roll: Int,
  val roll_category_id: String?,
  val off_category_id: String?
) {
  public constructor(guild: Guild) : this(
    guild.id,
    guild.humanInitialLife,
    guild.oniInitialLife,
    guild.hybridInitialLife,
    guild.breathInitial,
    guild.bloodInitial,
    guild.familyRoll,
    guild.abilityRoll,
    guild.rollCategoryId,
    guild.offCategoryId
  ) {}
}