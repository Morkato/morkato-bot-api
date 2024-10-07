package morkato.api.models.ability

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import morkato.api.database.tables.abilities
import morkato.api.database.guild.Guild
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.*

class Ability(
  val guild: Guild,
  val payload: AbilityPayload
) {
  companion object {
    fun getPayload(row: ResultRow) : AbilityPayload {
      return AbilityPayload(
        row[abilities.guild_id],
        row[abilities.id],
        row[abilities.name],
        row[abilities.type],
        row[abilities.percent],
        row[abilities.npc_kind],
        row[abilities.immutable],
        row[abilities.description],
        row[abilities.banner]
      )
    }
  }

  fun update(data: AbilityUpdateData) : Ability {
    abilities.update({
      (abilities.guild_id eq this@Ability.guild.id)
        .and(abilities.id eq this@Ability.payload.id)
    }) {
      if (data.name != null) {
        it[abilities.name] = data.name
      }
      if (data.type != null) {
        it[abilities.type] = data.type
      }
      if (data.percent != null) {
        it[abilities.percent] = data.percent
      }
      if (data.npc_kind != null) {
        it[abilities.npc_kind] = data.npc_kind
      }
      if (data.description != null) {
        it[abilities.description] = data.description
      }
      if (data.banner != null) {
        it[abilities.banner] = data.banner
      }
    }
    return Ability(this.guild, payload.extend(data))
  }

  fun delete() : Ability {
    abilities.deleteWhere {
      (abilities.guild_id eq this@Ability.guild.id)
        .and(abilities.id eq this@Ability.payload.id)
    }
    return this
  }
}