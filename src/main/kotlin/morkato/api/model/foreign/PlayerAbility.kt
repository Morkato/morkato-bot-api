package morkato.api.model.foreign

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.and

import morkato.api.database.tables.players_abilities
import morkato.api.database.guild.Guild

class PlayerAbility(
  val guild: Guild,
  val playerId: String,
  val abilityId: Long
) {
  fun delete() : PlayerAbility{
    players_abilities.deleteWhere {
      (this.guild_id eq this@PlayerAbility.guild.id)
        .and(this.player_id eq this@PlayerAbility.playerId)
        .and(this.ability_id eq this@PlayerAbility.abilityId)
    }
    return this
  }
}