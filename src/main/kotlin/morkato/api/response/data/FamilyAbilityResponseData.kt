package morkato.api.response.data

import morkato.api.database.foreign.AbilityFamily

data class FamilyAbilityResponseData(
  val guild_id: String,
  val family_id: String,
  val ability_id: String
) {
  companion object {
    fun from(familyAbility: AbilityFamily) : FamilyAbilityResponseData {
      return FamilyAbilityResponseData(
        familyAbility.guild.id,
        familyAbility.familyId.toString(),
        familyAbility.abilityId.toString()
      )
    }
  }
}
