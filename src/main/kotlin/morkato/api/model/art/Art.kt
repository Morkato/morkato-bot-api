package morkato.api.model.art

import morkato.api.infra.repository.AttackRepository
import morkato.api.infra.repository.ArtRepository
import morkato.api.model.attack.Attack
import morkato.api.model.guild.Guild

import org.jetbrains.exposed.sql.ResultRow

class Art(
  val guild: Guild,
  val id: Long,
  val name: String,
  val type: ArtType,
  val description: String?,
  val banner: String?
) {
  public constructor(guild: Guild, row: ResultRow) : this(guild, ArtRepository.ArtPayload(row)) {}
  public  constructor(guild: Guild, payload: ArtRepository.ArtPayload) : this(
    guild,
    payload.id,
    payload.name,
    payload.type,
    payload.description,
    payload.banner
  ) {}
  fun getAllAttacks() : Sequence<Attack> {
    return AttackRepository.findAllByGuildIdAndArtId(this.guild.id, this.id)
      .map { Attack(this@Art.guild, it) }
  }
  fun createAttack(
    name: String,
    namePrefixArt: String?,
    description: String?,
    banner: String?,
    damage: Long?,
    breath: Long?,
    blood: Long?,
    intents: Int?
  ) : Attack {
    val payload = AttackRepository.createAttack(
      guildId = this.guild.id,
      artId = this.id,
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
  fun update(
    name: String? = null,
    type: ArtType? = null,
    description: String? = null,
    banner: String? = null
  ) : Art {
    val payload = ArtRepository.ArtPayload(
      guildId = this.guild.id,
      id = this.id,
      name = name ?: this.name,
      type = type ?: this.type,
      description = description ?: this.description,
      banner = banner ?: this.banner
    )
    ArtRepository.updateArt(
      guildId = this.guild.id,
      id = this.id,
      name = name,
      type = type,
      description = description,
      banner = banner
    )
    return Art(this.guild, payload)
  }
  fun delete() : Art {
    ArtRepository.deleteArt(this.guild.id, this.id)
    return this
  }
}