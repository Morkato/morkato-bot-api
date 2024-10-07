package morkato.api.response.data

import morkato.api.database.art.ArtType

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
    fun from(art: morkato.api.model.art.Art, attacks: List<AttackArtResponseData>) : ArtResponseData {
      val payload = art.payload
      return ArtResponseData(
        art.guild.id, payload.id.toString(),
        payload.name,
        payload.type,
        payload.description,
        payload.banner,
        attacks
      )
    }
  }
}