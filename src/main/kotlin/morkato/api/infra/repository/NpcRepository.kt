package morkato.api.infra.repository

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.and

import morkato.api.exception.model.NpcNotFoundError
import morkato.api.infra.tables.npcs
import morkato.api.model.npc.NpcType

object NpcRepository {
  public data class NpcPayload(
    val guildId: String,
    val id: Long,
    val name: String,
    val type: NpcType,
    val familyId: Long,
    val surname: String,
    val energy: Int,
    val prodigy: Boolean,
    val mark: Boolean,
    val maxLife: Long,
    val maxBreath: Long,
    val maxBlood: Long,
    val currentLife: Long,
    val currentBreath: Long,
    val currentBlood: Long,
    val icon: String?
  ) {
    public constructor(row: ResultRow) : this(
      row[npcs.guild_id],
      row[npcs.id],
      row[npcs.name],
      row[npcs.type],
      row[npcs.family_id],
      row[npcs.surname],
      row[npcs.energy],
      row[npcs.prodigy],
      row[npcs.mark],
      row[npcs.max_life],
      row[npcs.max_breath],
      row[npcs.max_blood],
      row[npcs.current_life],
      row[npcs.current_breath],
      row[npcs.current_blood],
      row[npcs.icon]
    );
  }
  private object DefaultValue {
    const val energy: Int = 100
    const val mark: Boolean = false
    const val prodigy: Boolean = false
    const val attr: Long = 0
  }
  fun findById(guildId: String, id: Long) : NpcPayload {
    return try {
      NpcPayload(
        npcs
          .selectAll()
          .where({
            (npcs.guild_id eq guildId)
              .and(npcs.id eq id)
          })
          .limit(1)
          .single()
      )
    } catch (exc: NoSuchElementException) {
      val extra: MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guildId
      extra["id"] = id.toString()
      throw NpcNotFoundError(extra)
    }
  }
  fun findBySurname(guildId: String, surname: String) : NpcPayload {
    return try {
      NpcPayload(
        npcs
          .selectAll()
          .where({
            (npcs.guild_id eq guildId)
              .and(npcs.surname eq surname)
          })
          .limit(1)
          .single()
      )
    } catch (exc: NoSuchElementException) {
      val extra: MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guildId
      extra["id"] = surname
      throw NpcNotFoundError(extra)
    }
  }
  fun findByPlayerId(guildId: String, id: String): NpcPayload {
    return try {
      NpcPayload(
        npcs
          .selectAll()
          .where({
            (npcs.guild_id eq guildId)
              .and(npcs.player_id eq id)
          })
          .limit(1)
          .single()
      )
    } catch (exc: NoSuchElementException) {
      val extra: MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guildId
      extra["player_id"] = id
      throw NpcNotFoundError(extra)
    }
  }
  fun createNpc(
    playerId: String? = null,
    guildId: String,
    name: String,
    type: NpcType,
    familyId: Long,
    surname: String,
    energy: Int?,
    prodigy: Boolean?,
    mark: Boolean?,
    life: Long?,
    breath: Long?,
    blood: Long?,
    icon: String?
  ) : NpcPayload {
    val id = npcs.insert {
      it[this.guild_id] = guildId
      it[this.name] = name
      it[this.type] = type
      it[this.family_id] = familyId
      it[this.surname] = surname
      it[this.icon] = icon
      if (playerId != null) {
        it[this.player_id] = playerId
      }
      if (energy != null) {
        it[this.energy] = energy
      }
      if (prodigy != null) {
        it[this.prodigy] = prodigy
      }
      if (mark != null) {
        it[this.mark] = mark
      }
      if (life != null) {
        it[this.max_life] = life
        it[this.current_life] = life
      }
      if (breath != null) {
        it[this.max_breath] = breath
        it[this.current_breath] = breath
      }
      if (blood != null) {
        it[this.max_blood] = blood
        it[this.current_blood] = blood
      }
    } get npcs.id
    return NpcPayload(
      guildId = guildId,
      id = id,
      name = name,
      type = type,
      familyId = familyId,
      surname = surname,
      energy = energy ?: DefaultValue.energy,
      prodigy = prodigy ?: DefaultValue.prodigy,
      mark = mark ?: DefaultValue.mark,
      maxLife = life ?: DefaultValue.attr,
      maxBreath = breath ?: DefaultValue.attr,
      maxBlood = blood ?: DefaultValue.attr,
      currentLife = life ?: DefaultValue.attr,
      currentBreath = breath ?: DefaultValue.attr,
      currentBlood = blood ?: DefaultValue.attr,
      icon = icon
    )
  }
  fun updateNpc(
    guildId: String,
    id: Long,
    name: String?,
    type: NpcType?,
    surname: String?,
    energy: Int?,
    prodigy: Boolean?,
    mark: Boolean?,
    maxLife: Long?,
    maxBreath: Long?,
    maxBlood: Long?,
    currentLife: Long?,
    currentBreath: Long?,
    currentBlood: Long?,
    icon: String?
  ) : Unit {
    npcs.update({
      (npcs.guild_id eq guildId)
        .and(npcs.id eq id)
    }) {
      if (name != null) {
        it[this.name] = name
      }
      if (type != null) {
        it[this.type] = type
      }
      if (surname != null) {
        it[this.surname] = surname
      }
      if (energy != null) {
        it[this.energy] = energy
      }
      if (prodigy != null) {
        it[this.prodigy] = prodigy
      }
      if (mark != null) {
        it[this.mark] = mark
      }
      if (maxLife != null) {
        it[this.max_life] = maxLife
      }
      if (maxBreath != null) {
        it[this.max_breath] = maxBreath
      }
      if (maxBlood != null) {
        it[this.max_blood] = maxBlood
      }
      if (currentLife != null) {
        it[this.current_life] = currentLife
      }
      if (currentBreath != null) {
        it[this.current_breath] = currentBreath
      }
      if (currentBlood != null) {
        it[this.current_blood] = currentBlood
      }
      if (icon != null) {
        it[this.icon] = icon
      }
    }
  }
  fun deleteNpc(guildId: String, id: Long) : Unit {
    npcs.deleteWhere {
      (this.guild_id eq guildId)
        .and(this.id eq id)
    }
  }
}