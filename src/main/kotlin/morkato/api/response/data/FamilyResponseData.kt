package morkato.api.response.data

import morkato.api.database.family.Family
import morkato.api.database.npc.NpcType

data class FamilyResponseData(
  val guild_id: String,
  val id: String,
  val name: String,
  val percent: Int,
  val npc_kind: NpcType,
  val description: String?,
  val banner: String?,
  val abilities: List<String>
) {
  companion object {
    fun from(family: Family, abilities: List<String>) : FamilyResponseData {
      val payload = family.payload
      return FamilyResponseData(
        family.guild.id, payload.id.toString(),
        payload.name,
        payload.percent,
        payload.npcKind,
        payload.description,
        payload.banner,
        abilities
      )
    }
  }
}
