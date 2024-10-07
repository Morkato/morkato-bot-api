package morkato.api.model.foreign

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.and

import morkato.api.database.tables.players_families
import morkato.api.database.guild.Guild

class PlayerFamily(
  val guild: Guild,
  val playerId: String,
  val familyId: Long
) {
  fun delete() {
    players_families.deleteWhere {
      (this.guild_id eq this@PlayerFamily.guild.id)
        .and(this.player_id eq this@PlayerFamily.playerId)
        .and(this.family_id eq this@PlayerFamily.familyId)
    }
  }
}