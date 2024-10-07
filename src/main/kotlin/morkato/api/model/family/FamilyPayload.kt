package morkato.api.model.family

import morkato.api.database.npc.NpcType

data class FamilyPayload(
  val guildId: String,
  val id: Long,
  val percent: Int,
  val npcKind: NpcType,
  val name: String,
  val description: String?,
  val banner: String?
) {
  fun extend(data: FamilyUpdateData) : FamilyPayload {
    return extend(
      name = data.name,
      percent = data.percent,
      description = data.description,
      banner = data.banner
    )
  }
  fun extend(name: String?, percent: Int?, description: String?, banner: String?) : FamilyPayload {
    return FamilyPayload(
      guildId, id,
      name = name ?: this.name,
      percent = percent ?: this.percent,
      npcKind = this.npcKind,
      description = description ?: this.description,
      banner = banner ?: this.banner
    )
  }
}
