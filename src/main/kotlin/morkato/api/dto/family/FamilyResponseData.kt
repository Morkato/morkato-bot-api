package morkato.api.dto.family

import morkato.api.model.family.Family
import morkato.api.model.npc.NpcType

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
  public constructor(family: Family, abilities: List<String>) : this(
    family.guild.id,
    family.id.toString(),
    family.name,
    family.percent,
    family.npcKind,
    family.description,
    family.banner,
    abilities
  );
}
