package morkato.api.response.data

import morkato.api.database.ability.Ability
import morkato.api.database.family.Family

data class FamilyAbilityResponseData(
  val guild_id: String,
  val family_id: String,
  val ability_id: String,
  val family_name: String,
  val ability_name: String
) {
  companion object {
    fun from(family: Family, ability: Ability) : FamilyAbilityResponseData {
      return FamilyAbilityResponseData(
        family.guildId,
        family.id.toString(),
        ability.id.toString(),
        family.name,
        ability.name
      )
    }
  }
}
