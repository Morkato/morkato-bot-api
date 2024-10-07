package morkato.api.response.data;

import morkato.api.database.attack.Attack

data class AttackArtResponseData(
  val guild_id: String,
  val id: String,
  val name: String,
  val name_prefix_art: String?,
  val description: String?,
  val banner: String?,
  val damage: Long,
  val breath: Long,
  val blood: Long,
  val intents: Int
) {
  companion object {
    fun from(attack: Attack) : AttackArtResponseData {
      val payload = attack.payload
      return AttackArtResponseData(
        attack.guild.id, payload.id.toString(),
        payload.name,
        payload.namePrefixArt,
        payload.description,
        payload.banner,
        payload.damage,
        payload.breath,
        payload.blood,
        payload.intents
      );
    }
  }
}
