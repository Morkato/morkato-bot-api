package morkato.api.response.data

import morkato.api.database.family.Family

data class FamilyResponseData(
  val guild_id: String,
  val id: String,
  val name: String,
  val description: String?,
  val banner: String?,
  val abilities: List<AbilityResponseData>
) {
  companion object {
    fun from(family: Family, abilities: List<AbilityResponseData>) : FamilyResponseData {
      return FamilyResponseData(
        family.guildId, family.id.toString(),
        family.name,
        family.description,
        family.banner,
        abilities
      )
    }
  }
}
