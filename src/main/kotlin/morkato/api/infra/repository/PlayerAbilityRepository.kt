package morkato.api.infra.repository

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.and

import morkato.api.infra.tables.players_abilities

object PlayerAbilityRepository {
  public data class PlayerAbilityPayload(
    val guildId: String,
    val playerId: String,
    val abilityId: Long
  ) {
    public constructor(row: ResultRow) : this(
      row[players_abilities.guild_id],
      row[players_abilities.player_id],
      row[players_abilities.ability_id]
    )
  }
  fun findAllByGuildIdAndPlayerId(guildId: String, id: String) : Sequence<PlayerAbilityPayload> {
    return players_abilities
      .selectAll()
      .where({
        (players_abilities.guild_id eq guildId)
          .and(players_abilities.player_id eq id)
      })
      .asSequence()
      .map(::PlayerAbilityPayload)
  }
  fun createPLayerAbility(guildId: String, playerId: String, abilityId: Long) : PlayerAbilityPayload {
    players_abilities.insert {
      it[this.guild_id] = guildId
      it[this.player_id] = playerId
      it[this.ability_id] = abilityId
    }
    return PlayerAbilityPayload(
      guildId = guildId,
      playerId = playerId,
      abilityId = abilityId
    )
  }
  fun deletePlayerAbility(guildId: String, playerId: String, abilityId: Long) : PlayerAbilityPayload {
    players_abilities.deleteWhere {
      (this.guild_id eq guildId)
        .and(this.player_id eq playerId)
        .and(this.ability_id eq abilityId)
    }
    return PlayerAbilityPayload(
      guildId = guildId,
      playerId = playerId,
      abilityId = abilityId
    )
  }
}