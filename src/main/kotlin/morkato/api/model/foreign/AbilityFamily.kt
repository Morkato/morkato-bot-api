package morkato.api.model.foreign

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import morkato.api.database.tables.abilities_families
import morkato.api.database.guild.Guild
import org.jetbrains.exposed.sql.*

class AbilityFamily(
  val guild: Guild,
  val abilityId: Long,
  val familyId: Long
) {
  fun delete() : AbilityFamily {
    abilities_families.deleteWhere {
      (this.guild_id eq this@AbilityFamily.guild.id)
        .and(this.ability_id eq this@AbilityFamily.abilityId)
        .and(this.family_id eq this@AbilityFamily.familyId)
    }
    return this
  }
}