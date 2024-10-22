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
import java.math.BigDecimal
import java.math.RoundingMode

object AttackRepository {
  public data class AttackPayload(
    val guildId: String,
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
      row[attacks.flags]
    ) {}
  }
  private object DefaultValue {
    const val flags: Int = 0
    val attr = BigDecimal(0).setScale(12, RoundingMode.UP)
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
    damage: BigDecimal?,
    breath: BigDecimal?,
    blood: BigDecimal?,
    flags: Int?
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
      if (flags != null) {
        it[this.flags] = flags
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
      damage = damage ?: DefaultValue.attr,
      breath = breath ?: DefaultValue.attr,
      blood = blood ?: DefaultValue.attr,
      flags = flags ?: DefaultValue.flags
    )
  }
  fun updateAttack(
    guildId: String,
    id: Long,
    name: String? = null,
    namePrefixArt: String? = null,
    description: String? = null,
    banner: String? = null,
    damage: BigDecimal? = null,
    breath: BigDecimal? = null,
    blood: BigDecimal? = null,
    flags: Int? = null
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
      if(flags != null) {
        it[this.flags] = flags
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