package morkato.api.model.attack

import morkato.api.infra.repository.AttackRepository
import morkato.api.model.guild.Guild

import org.jetbrains.exposed.sql.ResultRow
import java.math.BigDecimal

class Attack(
  val guild: Guild,
  val id: Long,
  val name: String,
  val artId: Long,
  val namePrefixArt: String?,
  val description: String?,
  val banner: String?,
  val damage: BigDecimal,
  val breath: BigDecimal,
  val blood: BigDecimal,
  val flags: Int
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
    payload.flags
  );
  fun update(
    name: String? = null,
    namePrefixArt: String? = null,
    description: String? = null,
    banner: String? = null,
    damage: BigDecimal? = null,
    breath: BigDecimal? = null,
    blood: BigDecimal? = null,
    flags: Int? = null
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
      flags = flags ?: this.flags
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
      flags = flags
    )
    return Attack(this.guild, payload)
  }
  fun delete() : Attack {
    AttackRepository.deleteAttack(this.guild.id, this.id)
    return this
  }
}
