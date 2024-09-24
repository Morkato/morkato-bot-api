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
      return AttackArtResponseData(
        attack.guildId,
        attack.id.toString(),
        attack.name,
        attack.namePrefixArt,
        attack.description,
        attack.banner,
        attack.damage,
        attack.breath,
        attack.blood,
        attack.intents
      );
    }
  }
}
