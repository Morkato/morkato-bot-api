package morkato.api.model.ability

data class AbilityPayload(
  val guildId: String,
  val id: Long,
  val name: String,
  val type: AbilityType,
  val percent: Int,
  val npcKind: Int,
  val immutable: Boolean,
  val description: String?,
  val banner: String?
) {
  fun extend(data: AbilityUpdateData) : AbilityPayload {
    return extend(
      name = data.name,
      type = data.type,
      percent = data.percent,
      npcKind = data.npc_kind,
      description = data.description,
      banner = data.banner
    )
  }
  fun extend(name: String?, type: AbilityType?, percent: Int?, npcKind: Int?, description: String?, banner: String?) : AbilityPayload {
    return AbilityPayload(
      guildId, id,
      name = name ?: this.name,
      type = type ?: this.type,
      percent = percent ?: this.percent,
      npcKind = npcKind ?: this.npcKind,
      immutable = this.immutable,
      description = description ?: this.description,
      banner = banner ?: this.banner
    )
  }
}