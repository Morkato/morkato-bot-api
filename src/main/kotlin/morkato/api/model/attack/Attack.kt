package morkato.api.models.attack

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import morkato.api.database.tables.attacks
import morkato.api.database.guild.Guild
import org.jetbrains.exposed.sql.*

class Attack(
  val guild: Guild,
  val payload: AttackPayload
) {
  companion object {
    fun getPayload(row: ResultRow): AttackPayload {
      return AttackPayload(
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
      )
    }
  }

  fun update(data: AttackUpdateData) : Attack {
    attacks.update({
      (attacks.guild_id eq guild.id)
        .and (attacks.id eq payload.id)
    }) {
      if (data.name != null) {
        it[attacks.name] = data.name
      }
      if (data.name_prefix_art != null) {
        it[attacks.name_prefix_art] = data.name_prefix_art
      }
      if (data.description != null) {
        it[attacks.description] = data.description
      }
      if (data.banner != null) {
        it[attacks.banner] = data.banner
      }
      if (data.damage != null) {
        it[attacks.damage] = data.damage
      }
      if (data.breath != null) {
        it[attacks.breath] = data.breath
      }
      if (data.blood != null) {
        it[attacks.blood] = data.blood
      }
      if (data.intents != null) {
        it[attacks.intents] = data.intents
      }
    }
    return Attack(guild, payload.extend(data))
  }
  fun delete() : Attack {
    attacks.deleteWhere {
      (attacks.guild_id eq guild.id)
        .and(attacks.id eq payload.id)
    }
    return this
  }
}
