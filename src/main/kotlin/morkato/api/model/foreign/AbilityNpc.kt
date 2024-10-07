package morkato.api.model.foreign

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import morkato.api.database.tables.npcs_abilities
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.and
import morkato.api.database.guild.Guild

class AbilityNpc(
  val guild: Guild,
  val npcId: Long,
  val abilityId: Long
) {
  fun delete() : AbilityNpc{
    npcs_abilities.deleteWhere {
      (this.guild_id eq this@AbilityNpc.guild.id)
        .and(this.npc_id eq this@AbilityNpc.npcId)
        .and(this.ability_id eq this@AbilityNpc.abilityId)
    }
    return this
  }
}