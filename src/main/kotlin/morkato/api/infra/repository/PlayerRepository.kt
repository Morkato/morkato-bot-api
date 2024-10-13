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
    val prodigyRoll: Int,
    val markRoll: Int,
    val berserkRoll: Int,
    val flags: Int,
    val expectedFamilyId: Long?,
    val expectedNpcType: NpcType
  ) {
    public constructor(row: ResultRow) : this(
      row[players.guild_id],
      row[players.id],
      row[players.ability_roll],
      row[players.family_roll],
      row[players.prodigy_roll],
      row[players.mark_roll],
      row[players.berserk_roll],
      row[players.flags],
      row[players.expected_family_id],
      row[players.expected_npc_kind]
    );
  }
  private object DefaultValue {
    const val familyRoll: Int = 3
    const val abilityRoll: Int = 3
    const val roll: Int = 1
    const val flags: Int = 0
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
    prodigyRoll: Int?,
    markRoll: Int?,
    berserkRoll: Int?,
    flags: Int?,
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
      if (prodigyRoll != null) {
        it[this.prodigy_roll] = prodigyRoll
      }
      if (markRoll != null) {
        it[this.mark_roll] = markRoll
      }
      if (berserkRoll != null) {
        it[this.berserk_roll] = berserkRoll
      }
      if (flags != null) {
        it[this.flags] = flags
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
      prodigyRoll = prodigyRoll ?: DefaultValue.roll,
      markRoll = markRoll ?: DefaultValue.roll,
      berserkRoll = berserkRoll ?: DefaultValue.roll,
      flags = flags ?: DefaultValue.flags
    )
  }
  fun updatePlayer(
    guildId: String,
    id: String,
    expectedFamilyId: Long?,
    abilityRoll: Int?,
    familyRoll: Int?,
    prodigyRoll: Int?,
    markRoll: Int?,
    berserkRoll: Int?,
    flags: Int?,
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
      if (prodigyRoll != null) {
        it[this.prodigy_roll] = prodigyRoll
      }
      if (markRoll != null) {
        it[this.mark_roll] = markRoll
      }
      if (berserkRoll != null) {
        it[this.berserk_roll] = berserkRoll
      }
      if (flags != null) {
        it[this.flags] = flags
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
  }
}