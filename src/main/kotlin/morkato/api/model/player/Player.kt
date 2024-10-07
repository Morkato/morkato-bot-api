package morkato.api.model.player

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.*

import morkato.api.database.tables.players_abilities
import morkato.api.database.tables.players_families
import morkato.api.database.foreign.PlayerAbility
import morkato.api.database.foreign.PlayerFamily
import morkato.api.database.tables.players
import morkato.api.database.tables.npcs
import morkato.api.database.guild.Guild
import morkato.api.database.npc.NpcPayload
import morkato.api.database.npc.Npc
import morkato.api.database.npc.NpcType

class Player(
  val guild: Guild,
  val payload: PlayerPayload
) {
  companion object {
    fun getPayload(row: ResultRow) : PlayerPayload {
      return PlayerPayload(
        row[players.guild_id],
        row[players.id],
        row[players.ability_roll],
        row[players.family_roll],
        row[players.is_prodigy],
        row[players.has_mark],
        row[players.expected_family_id],
        row[players.expected_npc_kind]
      )
    }
  }

  fun getReferredNpc() : Npc? {
    try {
      val row = npcs
        .selectAll()
        .where({
          (npcs.guild_id eq this@Player.guild.id)
            .and(npcs.player_id eq this@Player.payload.id)
        })
        .limit(1)
        .single()
      val payload = Npc.getPayload(row)
      return Npc(this.guild, payload)
    } catch (exc: NoSuchElementException) {
      return null
    }
  }

  fun createNpc(data: PlayerNpcCreateData) : Npc {
    if (payload.expectedFamilyId == null) {
      throw Exception()
    }
    val maxLife = when (this.payload.expectedNpcType) {
      NpcType.HUMAN -> this.guild.human_initial_life
      NpcType.ONI -> this.guild.oni_initial_life
      NpcType.HYBRID -> this.guild.hybrid_initial_life
    }
    val maxBreath = this.guild.breath_initial
    val maxBlood = this.guild.blood_initial
    val id = npcs.insert {
      it[this.guild_id] = this@Player.guild.id
      it[this.name] = data.name
      it[this.surname] = data.surname
      it[this.player_id] = this@Player.payload.id
      it[this.type] = this@Player.payload.expectedNpcType
      it[this.family_id] = this@Player.payload.expectedFamilyId
      it[this.icon] = data.icon
    } get npcs.id
    val payload = NpcPayload(
      guildId = this.guild.id,
      id = id,
      name = data.name,
      type = this.payload.expectedNpcType,
      familyId = this.payload.expectedFamilyId.toLong(),
      surname = data.surname,
      energy = 100,
      maxLife = maxLife,
      maxBreath = maxBreath,
      maxBlood = maxBlood,
      currentLife = maxLife,
      currentBreath = maxBreath,
      currentBlood = maxBlood,
      icon = data.icon
    )
    return Npc(this.guild, payload)
  }

  fun getAllAbilities() : List<PlayerAbility> {
    return players_abilities
      .selectAll()
      .where({
        (players_abilities.guild_id eq this@Player.guild.id)
          .and(players_abilities.player_id eq this@Player.payload.id)
      })
      .map {
        return@map PlayerAbility(this@Player.guild, this.payload.id, it[players_abilities.ability_id])
      }
  }

  fun getAllFamilies() : List<PlayerFamily> {
    return players_families
      .selectAll()
      .where({
        (players_families.guild_id eq this@Player.guild.id)
          .and(players_families.player_id eq this@Player.payload.id)
      })
      .map {
        return@map PlayerFamily(this@Player.guild, this.payload.id, it[players_families.family_id])
      }
  }

  fun addAbility(id: Long) : PlayerAbility {
    players_abilities.insert {
      it[this.guild_id] = this@Player.guild.id
      it[this.player_id] = this@Player.payload.id
      it[this.ability_id] = id
    }
    players.update({
      (players.guild_id eq this@Player.guild.id)
        .and(players.id eq this@Player.payload.id)
    }) {
      it[this.ability_roll] = this@Player.payload.abilityRoll - 1
    }
    return PlayerAbility(this.guild, this.payload.id, id)
  }
  fun addFamily(id: Long) : PlayerFamily {
    players_families.insert {
      it[this.guild_id] = this@Player.guild.id
      it[this.player_id] = this@Player.payload.id
      it[this.family_id] = id
    }
    players.update({
      (players.guild_id eq this@Player.guild.id)
        .and(players.id eq this@Player.payload.id)
    }) {
      it[this.family_roll] = this@Player.payload.familyRoll - 1
    }
    return PlayerFamily(this.guild, this.payload.id, id)
  }

  fun update(data: PlayerUpdateData) : Player {
    players.update({
      (players.guild_id eq this@Player.guild.id)
        .and(players.id eq this@Player.payload.id)
    }) {
      if (data.ability_roll != null) {
        it[this.ability_roll] = data.ability_roll
      }
      if (data.family_roll != null) {
        it[this.family_roll] = data.family_roll
      }
      if (data.is_prodigy != null) {
        it[this.is_prodigy] = data.is_prodigy
      }
      if (data.has_mark != null) {
        it[this.has_mark] = data.has_mark
      }
      if (data.family_id != null && this@Player.payload.expectedFamilyId == null) {
        it[this.expected_family_id] = data.family_id.toLong()
      }
    }
    return Player(this.guild, payload.extend(data))
  }

  fun delete() {
    players.deleteWhere {
      (this.guild_id eq this@Player.guild.id)
        .and(this.id eq this@Player.payload.id)
    }
  }
}
