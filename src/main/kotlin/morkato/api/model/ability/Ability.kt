package morkato.api.model.ability

import morkato.api.infra.repository.AbilityRepository
import morkato.api.model.guild.Guild

import org.jetbrains.exposed.sql.ResultRow

class Ability(
  val guild: Guild,
  val id: Long,
  val name: String,
  val type: AbilityType,
  val percent: Int,
  val npcKind: Int,
  val immutable: Boolean,
  val description: String?,
  val banner: String?
) {
  public constructor(guild: Guild, row: ResultRow) : this(guild, AbilityRepository.AbilityPayload(row)) {}
  public constructor(guild: Guild, payload: AbilityRepository.AbilityPayload) : this(
    guild,
    payload.id,
    payload.name,
    payload.type,
    payload.percent,
    payload.npcKind,
    payload.immutable,
    payload.description,
    payload.banner
  ) {}

  fun update(
    name: String?,
    type: AbilityType?,
    percent: Int?,
    npcKind: Int?,
    description: String?,
    banner: String?
  ) : Ability {
    val payload = AbilityRepository.AbilityPayload(
      guildId = this.guild.id,
      id = this.id,
      name = name ?: this.name,
      type = type ?: this.type,
      percent = percent ?: this.percent,
      npcKind = npcKind ?: this.npcKind,
      description = description ?: this.description,
      immutable = this.immutable,
      banner = banner ?: this.banner
    )
    AbilityRepository.updateAbility(
      guildId = this.guild.id,
      id = this.id,
      name = name,
      type = type,
      percent = percent,
      npcKind = npcKind,
      description = description,
      banner = banner
    )
    return Ability(this.guild, payload)
  }

  fun delete() : Ability {
    AbilityRepository.deleteAbility(this.guild.id, this.id)
    return this
  }
}