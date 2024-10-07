package morkato.api.infra.repository

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.and

import morkato.api.exception.model.AttackNotFoundError
import morkato.api.infra.tables.attacks

object AttackRepository {
  public data class AttackPayload(
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
    public constructor(row: ResultRow) : this(
      row[attacks.guild_id],
      row[attacks.id],
      row[attacks.name],
      row[attacks.art_id],
      row[attacks.name_prefix_art],
      row[attacks.description],
      row[attacks.banner],
      row[attacks.damage],
      row[attacks.breath],
      row[attacks.blood],
      row[attacks.intents]
    ) {}
  }
  private object DefaultValue {
    const val damage: Long = 1
    const val breath: Long = 1
    const val blood: Long = 1
    const val intents: Int = 0
  }
  fun findAllByGuildId(id: String) : Sequence<AttackPayload> {
    return attacks
      .selectAll()
      .where({
        (attacks.guild_id eq id)
      })
      .asSequence()
      .map(::AttackPayload)
  }
  fun findAllByGuildIdAndArtId(guildId: String, artId: Long) : Sequence<AttackPayload> {
    return attacks
      .selectAll()
      .where({
        (attacks.guild_id eq guildId)
          .and(attacks.art_id eq artId)
      })
      .asSequence()
      .map(::AttackPayload)
  }
  fun findById(guildId: String, id: Long) : AttackPayload {
    return try {
      AttackPayload(
        attacks
          .selectAll()
          .where({
            (attacks.guild_id eq guildId)
              .and(attacks.id eq id)
          })
          .limit(1)
          .single()
      )
    } catch (exc: NoSuchElementException) {
      val extra: MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guildId
      extra["id"] = id.toString()
      throw AttackNotFoundError(extra)
    }
  }
  fun createAttack(
    guildId: String,
    artId: Long,
    name: String,
    namePrefixArt: String?,
    description: String?,
    banner: String?,
    damage: Long?,
    breath: Long?,
    blood: Long?,
    intents: Int?
  ) : AttackPayload {
    val id = attacks.insert {
      it[this.guild_id] = guildId
      it[this.art_id] = artId
      it[this.name] = name
      it[this.name_prefix_art] = namePrefixArt
      it[this.description] = description
      it[this.banner] = banner
      if (damage != null) {
        it[this.damage] = damage
      }
      if (breath != null) {
        it[this.breath] = breath
      }
      if (blood != null) {
        it[this.blood] = blood
      }
      if (intents != null) {
        it[this.intents] = intents
      }
    } get attacks.id
    return AttackPayload(
      guildId = guildId,
      id = id,
      name = name,
      artId = artId,
      namePrefixArt = namePrefixArt,
      description = description,
      banner = banner,
      damage = damage ?: DefaultValue.damage,
      breath = breath ?: DefaultValue.breath,
      blood = blood ?: DefaultValue.blood,
      intents = intents ?: DefaultValue.intents
    )
  }
  fun updateAttack(
    guildId: String,
    id: Long,
    name: String? = null,
    namePrefixArt: String? = null,
    description: String? = null,
    banner: String? = null,
    damage: Long? = null,
    breath: Long? = null,
    blood: Long? = null,
    intents: Int? = null
  ) : Unit {
    attacks.update({
      (attacks.guild_id eq guildId)
        .and(attacks.id eq id)
    }) {
      if (name != null) {
        it[this.name] = name
      }
      if (namePrefixArt != null) {
        it[this.name_prefix_art] = namePrefixArt
      }
      if (description != null) {
        it[this.description] = description
      }
      if (banner != null) {
        it[this.banner] = banner
      }
      if (damage != null) {
        it[this.damage] = damage
      }
      if (breath != null) {
        it[this.breath] = breath
      }
      if (blood != null) {
        it[this.blood] = blood
      }
      if(intents != null) {
        it[this.intents] = intents
      }
    }
  }
  fun deleteAttack(guildId: String, id: Long) : Unit {
    attacks.deleteWhere {
      (this.guild_id eq guildId)
        .and(this.id eq id)
    }
  }
}