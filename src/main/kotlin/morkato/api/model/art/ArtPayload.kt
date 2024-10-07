package morkato.api.model.art

data class ArtPayload(
  val guildId: String,
  val id: Long,
  val name: String,
  val type: ArtType,
  val description: String?,
  val banner: String?
) {
  fun extend(data: ArtUpdateData) : ArtPayload {
    return this.extend(
      name = data.name,
      type = data.type,
      description = data.description,
      banner = data.banner
    )
  }
  fun extend(name: String?, type: ArtType?, description: String?, banner: String?) : ArtPayload {
    return ArtPayload(
      guildId, id,
      name = name ?: this.name,
      type = type ?: this.type,
      description = description ?: this.description,
      banner = banner ?: this.banner
    )
  }
}
