package morkato.api.dto.npc

import morkato.api.model.npc.Npc
import morkato.api.model.npc.NpcType

data class NpcResponseData(
  val guild_id: String,
  val id: String,
  val name: String,
  val type: NpcType,
  val family_id: String,
  val surname: String,
  val energy: Int,
  val flags: Int,
  val max_life: Long,
  val max_breath: Long,
  val max_blood: Long,
  val current_life: Long,
  val current_breath: Long,
  val current_blood: Long,
  val icon: String?,
  val abilities: List<String>,
  val arts: Map<String, Long>
) {
  public constructor(npc: Npc, abilities: List<String>, arts: Map<String, Long>) : this(
    npc.guild.id,
    npc.id.toString(),
    npc.name,
    npc.type,
    npc.familyId.toString(),
    npc.surname,
    npc.energy,
    npc.flags,
    npc.maxLife,
    npc.maxBreath,
    npc.maxBlood,
    npc.currentLife,
    npc.currentBreath,
    npc.currentBlood,
    npc.icon,
    abilities,
    arts
  );
}
