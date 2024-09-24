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
  val icon: String?
) {
  companion object {
    fun from(npc: Npc) : NpcResponseData {
      return NpcResponseData(
        npc.guild_id, npc.id.toString(),
        npc.name,
        npc.type,
        npc.family_id?.toString(),
        npc.surname,
        npc.energy,
        npc.max_life, npc.max_breath, npc.max_blood,
        npc.current_life, npc.current_breath, npc.current_blood,
        npc.icon
      )
    }
  }
}
