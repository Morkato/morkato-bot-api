package morkato.api.response.data

import morkato.api.database.guild.Guild

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
  companion object {
    fun from(guild: Guild) : GuildResponseData {
      return GuildResponseData(
        id = guild.id,
        human_initial_life = guild.human_initial_life,
        oni_initial_life = guild.oni_initial_life,
        hybrid_initial_life = guild.hybrid_initial_life,
        breath_initial = guild.breath_initial,
        blood_initial = guild.blood_initial,
        family_roll = guild.family_roll,
        ability_roll = guild.ability_roll,
        roll_category_id = guild.roll_category_id,
        off_category_id = guild.off_category_id
      )
    }
  }
}
