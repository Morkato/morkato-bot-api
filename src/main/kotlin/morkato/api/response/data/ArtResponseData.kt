package morkato.api.response.data

import morkato.api.database.art.ArtType
import morkato.api.database.art.Art

data class ArtResponseData(
  val guild_id: String,
  val id: String,
  val name: String,
  val type: ArtType,
  val description: String?,
  val banner: String?,
  val attacks: List<AttackArtResponseData>
) {
  companion object {
    fun from(art: Art, attacks: List<AttackArtResponseData>) : ArtResponseData {
      return ArtResponseData(
        art.guildId,
        art.id.toString(),
        art.name,
        art.type,
        art.description,
        art.banner,
        attacks
      )
    }
  }
}