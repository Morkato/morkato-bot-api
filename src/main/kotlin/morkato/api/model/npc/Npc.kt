package morkato.api.model.npc

import morkato.api.infra.repository.NpcAbilityRepository
import morkato.api.infra.repository.NpcRepository
import morkato.api.model.guild.Guild

import org.jetbrains.exposed.sql.ResultRow

class Npc(
  val guild: Guild,
  val id: Long,
  val name: String,
  val type: NpcType,
  val familyId: Long,
  val surname: String,
  val energy: Int,
  val flags: Int,
  val maxLife: Long,
  val maxBreath: Long,
  val maxBlood: Long,
  val currentLife: Long,
  val currentBreath: Long,
  val currentBlood: Long,
  val icon: String?
) {
  public constructor(guild: Guild, row: ResultRow) : this(guild, NpcRepository.NpcPayload(row));
  public constructor(guild: Guild, payload: NpcRepository.NpcPayload) : this(
    guild,
    payload.id,
    payload.name,
    payload.type,
    payload.familyId,
    payload.surname,
    payload.energy,
    payload.flags,
    payload.maxLife,
    payload.maxBreath,
    payload.maxBlood,
    payload.currentLife,
    payload.currentBreath,
    payload.currentBlood,
    payload.icon
  );
  fun getAllAbilities() : Sequence<NpcAbilityRepository.NpcAbilityPayload> {
    return NpcAbilityRepository.findAllByGuildIdAndNpcId(this.guild.id, this.id)
  }
  fun addAbility(id: Long) : NpcAbilityRepository.NpcAbilityPayload {
    return NpcAbilityRepository.createNpcAbility(this.guild.id, this.id, id)
  }

  fun update(
    name: String?,
    type: NpcType?,
    surname: String?,
    energy: Int?,
    flags: Int?,
    maxLife: Long?,
    maxBreath: Long?,
    maxBlood: Long?,
    currentLife: Long?,
    currentBreath: Long?,
    currentBlood: Long?,
    icon: String?
  ) : Npc {
    NpcRepository.updateNpc(
      guildId = this.guild.id,
      id = this.id,
      name = name,
      type = type,
      surname = surname,
      energy = energy,
      flags = flags,
      maxLife = maxLife,
      maxBreath = maxBreath,
      maxBlood = maxBlood,
      currentLife = currentLife,
      currentBreath = currentBreath,
      currentBlood = currentBlood,
      icon = icon
    )
    return Npc(
      guild = this.guild,
      id = this.id,
      name = name ?: this.name,
      type = type ?: this.type,
      familyId = this.familyId,
      surname = surname ?: this.surname,
      energy = energy ?: this.energy,
      flags = flags ?: this.flags,
      maxLife = maxLife ?: this.maxLife,
      maxBreath = maxBreath ?: this.maxBreath,
      maxBlood = maxBlood ?: this.maxBlood,
      currentLife = currentLife ?: this.currentLife,
      currentBreath = currentBreath ?: this.currentBreath,
      currentBlood = currentBlood ?: this.currentBlood,
      icon = icon ?: this.icon
    )
  }
  fun delete() : Npc {
    NpcRepository.deleteNpc(guildId = this.guild.id, id = this.id)
    return this
  }
}