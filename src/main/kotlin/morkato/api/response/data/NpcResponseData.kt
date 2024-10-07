package morkato.api.response.data

import morkato.api.database.npc.NpcType
import morkato.api.database.npc.Npc

data class NpcResponseData(
  val guild_id: String,
  val id: String,
  val name: String,
  val type: NpcType,
  val family_id: String?,
  val surname: String,
  val energy: Int,
  val max_life: Long,
  val max_breath: Long,
  val max_blood: Long,
  val current_life: Long,
  val current_breath: Long,
  val current_blood: Long,
  val icon: String?,
  val abilities: List<String>
) {
  companion object {
    fun from(npc: Npc, abilities: List<String>) : NpcResponseData {
      val payload = npc.payload
      return NpcResponseData(
        npc.guild.id, payload.id.toString(),
        payload.name,
        payload.type,
        payload.familyId.toString(),
        payload.surname,
        payload.energy,
        payload.maxLife, payload.maxBreath, payload.maxBlood,
        payload.currentLife, payload.currentBreath, payload.currentBlood,
        payload.icon,
        abilities
      )
    }
  }
}
