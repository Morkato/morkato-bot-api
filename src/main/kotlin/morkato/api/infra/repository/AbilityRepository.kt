package morkato.api.infra.repository

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.and

import morkato.api.exception.model.AbilityNotFoundError
import morkato.api.model.ability.AbilityType
import morkato.api.infra.tables.abilities

object AbilityRepository {
  public data class AbilityPayload(
    val guildId: String,
    val id: Long,
    val name: String,
    val type: AbilityType,
    val percent: Int,
    val npcKind: Int,
    val immutable: Boolean,
    val description: String?,
    val banner: String?
  ) {
    public constructor(row: ResultRow) : this(
      row[abilities.guild_id],
      row[abilities.id],
      row[abilities.name],
      row[abilities.type],
      row[abilities.percent],
      row[abilities.npc_kind],
      row[abilities.immutable],
      row[abilities.description],
      row[abilities.banner]
    ) {}
  }
  private object DefaultValue {
    const val percent = 50
    const val immutable = false
  }
  fun findAllByGuildId(id: String) : Sequence<AbilityPayload> {
    return abilities
      .selectAll()
      .where({
        (abilities.guild_id eq id)
      })
      .asSequence()
      .map(::AbilityPayload)
  }
  fun findById(guildId: String, id: Long) : AbilityPayload {
    return try {
      AbilityPayload(
        abilities
          .selectAll()
          .where({
            (abilities.guild_id eq guildId)
              .and(abilities.id eq id)
          })
          .limit(1)
          .single()
      )
    } catch (exc: NoSuchElementException) {
      val extra: MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guildId
      extra["id"] = id.toString()
      throw AbilityNotFoundError(extra)
    }
  }
  fun createAbility(
    guildId: String,
    name: String,
    type: AbilityType,
    percent: Int?,
    npcKind: Int,
    immutable: Boolean?,
    description: String?,
    banner: String?
  ) : AbilityPayload {
    val id = abilities.insert {
      it[this.guild_id] = guildId
      it[this.name] = name
      it[this.type] = type
      it[this.npc_kind] = npcKind
      if (percent != null) {
        it[this.percent] = percent
      }
      if (immutable != null) {
        it[this.immutable] = immutable
      }
      if (description != null) {
        it[this.description] = description
      }
      if (banner != null) {
        it[this.banner] = banner
      }
    } get abilities.id
    return AbilityPayload(
      guildId = guildId,
      id = id,
      name = name,
      type = type,
      percent = percent ?: DefaultValue.percent,
      npcKind = npcKind,
      immutable = immutable ?: DefaultValue.immutable,
      description = description,
      banner = banner
    )
  }
  fun updateAbility(
    guildId: String,
    id: Long,
    name: String?,
    type: AbilityType?,
    percent: Int?,
    npcKind: Int?,
    description: String?,
    banner: String?
  ) : Unit {
    abilities.update({
      (abilities.guild_id eq guildId)
        .and(abilities.id eq id)
    }) {
      if (name != null) {
        it[this.name] = name
      }
      if (type != null) {
        it[this.type] = type
      }
      if (percent != null) {
        it[this.percent] = percent
      }
      if (npcKind != null) {
        it[this.npc_kind] = npcKind
      }
      if (description != null) {
        it[this.description] = description
      }
      if (banner != null) {
        it[this.banner] = banner
      }
    }
  }
  fun deleteAbility(guildId: String, id: Long) : Unit {
    abilities.deleteWhere {
      (this.guild_id eq guildId)
        .and(this.id eq id)
    }
  }
}