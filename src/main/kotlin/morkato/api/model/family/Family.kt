package morkato.api.model.family

import morkato.api.infra.repository.AbilityFamilyRepository
import morkato.api.infra.repository.FamilyRepository
import morkato.api.model.guild.Guild
import morkato.api.model.npc.NpcType

import org.jetbrains.exposed.sql.ResultRow

class Family(
  val guild: Guild,
  val id: Long,
  val percent: Int,
  val npcKind: NpcType,
  val name: String,
  val description: String?,
  val banner: String?
) {
  public constructor(guild: Guild, row: ResultRow) : this(guild, FamilyRepository.FamilyPayload(row));
  public constructor(guild: Guild, payload: FamilyRepository.FamilyPayload) : this(
    guild,
    payload.id,
    payload.percent,
    payload.npcKind,
    payload.name,
    payload.description,
    payload.banner
  );
  fun getAllAbilities() : Sequence<AbilityFamilyRepository.AbilityFamilyPayload> {
    return AbilityFamilyRepository.findAllByGuildIdAndFamilyId(this.guild.id, this.id)
  }
  fun update(
    name: String?,
    npcKind: NpcType?,
    percent: Int?,
    description: String?,
    banner: String?
  ) : Family {
    val payload = FamilyRepository.FamilyPayload(
      guildId = this.guild.id,
      id = this.id,
      name = name ?: this.name,
      npcKind = npcKind ?: this.npcKind,
      percent = percent ?: this.percent,
      description = description ?: this.description,
      banner = banner ?: this.banner
    )
    FamilyRepository.updateFamily(
      guildId = this.guild.id,
      id = this.id,
      name = name,
      npcKind = npcKind,
      percent = percent,
      description = description,
      banner = banner
    )
    return Family(this.guild, payload)
  }

  fun addAbility(id: Long) : AbilityFamilyRepository.AbilityFamilyPayload {
    return AbilityFamilyRepository.createAbilityFamily(this.guild.id, id, this.id)
  }
  fun dropAbility(id: Long) : AbilityFamilyRepository.AbilityFamilyPayload {
    AbilityFamilyRepository.deleteAbilityFamily(this.guild.id, id, this.id)
    return AbilityFamilyRepository.AbilityFamilyPayload(this.guild.id, id, this.id)
  }

  fun delete() : Family {
    FamilyRepository.deleteFamily(this.guild.id, this.id)
    return this
  }
}