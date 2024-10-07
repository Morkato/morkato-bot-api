package morkato.api.dto.attack

import morkato.api.model.attack.Attack

data class AttackResponseData(
  val guild_id: String,
  val id: String,
  val name: String,
  val art_id: String,
  val name_prefix_art: String?,
  val description: String?,
  val banner: String?,
  val damage: Long,
  val breath: Long,
  val blood: Long,
  val intents: Int
) {
  public constructor(attack: Attack) : this(
    attack.guild.id,
    attack.id.toString(),
    attack.name,
    attack.artId.toString(),
    attack.namePrefixArt,
    attack.description,
    attack.banner,
    attack.damage,
    attack.breath,
    attack.blood,
    attack.intents
  ) {}
}