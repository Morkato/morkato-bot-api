package morkato.api.model.attack

data class AttackPayload(
  val guildId: String,
  val id: Long,
  val name: String,
  val artId: Long,
  val namePrefixArt: String?,
  val description: String?,
  val banner: String?,
  val damage: Long,
  val breath: Long,
  val blood: Long,
  val intents: Int
) {
  fun extend(data: AttackUpdateData) : AttackPayload {
    return extend(
      name = data.name,
      namePrefixArt = data.name_prefix_art,
      description = data.description,
      banner = data.banner,
      damage = data.damage,
      breath = data.breath,
      blood = data.blood,
      intents = data.intents
    )
  }
  fun extend(
    name: String?,
    namePrefixArt: String?,
    description: String?,
    banner: String?,
    damage: Long?,
    breath: Long?,
    blood: Long?,
    intents: Int?
  ) : AttackPayload {
    return AttackPayload(
      guildId, id,
      name = name ?: this.name,
      namePrefixArt = namePrefixArt ?: this.namePrefixArt,
      artId = artId,
      description = description ?: this.description,
      banner = banner ?: this.banner,
      damage = damage ?: this.damage,
      breath = breath ?: this.breath,
      blood = blood ?: this.blood,
      intents = intents ?: this.intents
    )
  }
}
