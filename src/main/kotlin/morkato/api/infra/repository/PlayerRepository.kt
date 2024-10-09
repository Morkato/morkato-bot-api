package morkato.api.infra.repository

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.and

import morkato.api.exception.model.PlayerNotFoundError
import morkato.api.infra.tables.players
import morkato.api.model.npc.NpcType

object PlayerRepository {
  public data class PlayerPayload(
    val guildId: String,
    val id: String,
    val abilityRoll: Int,
    val familyRoll: Int,
    val isProdigy: Boolean,
    val hasMark: Boolean,
    val expectedFamilyId: Long?,
    val expectedNpcType: NpcType
  ) {
    public constructor(row: ResultRow) : this(
      row[players.guild_id],
      row[players.id],
      row[players.ability_roll],
      row[players.family_roll],
      row[players.is_prodigy],
      row[players.has_mark],
      row[players.expected_family_id],
      row[players.expected_npc_kind]
    );
  }
  private object DefaultValue {
    const val familyRoll: Int = 3
    const val abilityRoll: Int = 3
    const val isPridigy: Boolean = false
    const val hasMark: Boolean = false
  }
  fun findById(guildId: String, id: String) : PlayerPayload {
    return try {
      PlayerPayload(
        players
          .selectAll()
          .where({
            (players.guild_id eq guildId)
              .and(players.id eq id)
          })
          .limit(1)
          .single()
      )
    } catch (exc: NoSuchElementException) {
      val extra: MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guildId
      extra["id"] = id
      throw PlayerNotFoundError(extra)
    }
  }
  fun createPlayer(
    guildId: String,
    id: String,
    expectedNpcType: NpcType,
    abilityRoll: Int?,
    familyRoll: Int?,
    isProdigy: Boolean?,
    hasMark: Boolean?,
    expectedFamilyId: Long?
  ) : PlayerPayload {
    players.insert {
      it[this.guild_id] = guildId
      it[this.id] = id
      it[this.expected_npc_kind] = expectedNpcType
      if (abilityRoll != null) {
        it[this.ability_roll] = abilityRoll
      }
      if (familyRoll != null) {
        it[this.family_roll] = familyRoll
      }
      if (isProdigy != null) {
        it[this.is_prodigy] = isProdigy
      }
      if (hasMark != null) {
        it[this.has_mark] = hasMark
      }
      if (expectedFamilyId != null) {
        it[this.expected_family_id] = expectedFamilyId
      }
    }
    return PlayerPayload(
      guildId = guildId,
      id = id,
      expectedNpcType = expectedNpcType,
      expectedFamilyId = expectedFamilyId,
      abilityRoll = abilityRoll ?: DefaultValue.abilityRoll,
      familyRoll = familyRoll ?: DefaultValue.familyRoll,
      isProdigy = isProdigy ?: DefaultValue.isPridigy,
      hasMark = hasMark ?: DefaultValue.hasMark
    )
  }
  fun updatePlayer(
    guildId: String,
    id: String,
    expectedFamilyId: Long?,
    abilityRoll: Int?,
    familyRoll: Int?,
    isProdigy: Boolean?,
    hasMark: Boolean?
  ) : Unit {
    players.update({
      (players.guild_id eq guildId)
        .and(players.id eq id)
    }) {
      if (abilityRoll != null) {
        it[this.ability_roll] = abilityRoll
      }
      if (familyRoll != null) {
        it[this.family_roll] = familyRoll
      }
      if (isProdigy != null) {
        it[this.is_prodigy] = isProdigy
      }
      if (hasMark != null) {
        it[this.has_mark] = hasMark
      }
      if (expectedFamilyId != null) {
        it[this.expected_family_id] = expectedFamilyId
      }
    }
  }
  fun deletePlayer(guildId: String, id: String) : Unit {
    players.deleteWhere {
      (this.guild_id eq guildId)
        .and(this.id eq id)
    }
    Unit
  }
}