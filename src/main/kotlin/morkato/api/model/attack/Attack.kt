package morkato.api.model.attack

import morkato.api.infra.repository.AttackRepository
import morkato.api.model.guild.Guild

import org.jetbrains.exposed.sql.ResultRow

class Attack(
  val guild: Guild,
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
  public constructor(guild: Guild, row: ResultRow) : this(guild, AttackRepository.AttackPayload(row)) {}
  public  constructor(guild: Guild, payload: AttackRepository.AttackPayload) : this(
    guild,
    payload.id,
    payload.name,
    payload.artId,
    payload.namePrefixArt,
    payload.description,
    payload.banner,
    payload.damage,
    payload.breath,
    payload.blood,
    payload.intents
  ) {}
  fun update(
    name: String? = null,
    namePrefixArt: String? = null,
    description: String? = null,
    banner: String? = null,
    damage: Long? = null,
    breath: Long? = null,
    blood: Long? = null,
    intents: Int? = null
  ) : Attack {
    val payload = AttackRepository.AttackPayload(
      guildId = this.guild.id,
      id = this.id,
      name = name ?: this.name,
      artId = this.artId,
      namePrefixArt = namePrefixArt ?: this.namePrefixArt,
      description = description ?: this.description,
      banner = banner ?: this.banner,
      damage = damage ?: this.damage,
      breath = breath ?: this.breath,
      blood = blood ?: this.blood,
      intents = intents ?: this.intents
    )
    AttackRepository.updateAttack(
      guildId = this.guild.id,
      id = this.id,
      name = name,
      namePrefixArt = namePrefixArt,
      description = description,
      banner = banner,
      damage = damage,
      breath = breath,
      blood = blood,
      intents = intents
    )
    return Attack(this.guild, payload)
  }
  fun delete() : Attack {
    AttackRepository.deleteAttack(this.guild.id, this.id)
    return this
  }
}
