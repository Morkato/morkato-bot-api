package morkato.api.models.family

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import morkato.api.database.foreign.AbilityFamily
import morkato.api.database.tables.abilities_families
import morkato.api.database.tables.families
import morkato.api.database.guild.Guild
import org.jetbrains.exposed.sql.*

class Family(
  val guild: Guild,
  val payload: FamilyPayload
) {
  companion object {
    fun getPayload(row: ResultRow) : FamilyPayload {
      return FamilyPayload(
        row[families.guild_id],
        row[families.id],
        row[families.percent],
        row[families.npc_kind],
        row[families.name],
        row[families.description],
        row[families.banner]
      )
    }
  }
  fun getAllAbilities() : List<AbilityFamily> {
    return abilities_families
      .selectAll()
      .where({
        (abilities_families.guild_id eq this@Family.guild.id)
          .and(abilities_families.family_id eq this@Family.payload.id)
      })
      .asSequence()
      .map { AbilityFamily(this@Family.guild, it[abilities_families.ability_id], it[abilities_families.family_id]) }
      .toList()
  }
  fun update(data: FamilyUpdateData) : Family {
    families.update({
      (families.guild_id eq this@Family.guild.id)
        .and(families.id eq this@Family.payload.id)
    }) {
      if (data.name != null) {
        it[this.name] = data.name
      }
      if (data.description != null) {
        it[this.description] = data.description
      }
      if (data.banner != null) {
        it[this.banner] = data.banner
      }
    }
    val payload = payload.extend(data)
    return Family(this.guild, payload)
  }

  fun addAbility(id: Long) : AbilityFamily {
    abilities_families.insert {
      it[this.guild_id] = this@Family.guild.id
      it[this.ability_id] = id
      it[this.family_id] = this@Family.payload.id
    }
    return AbilityFamily(this.guild, id, this.payload.id)
  }
  fun dropAbility(id: Long) : AbilityFamily {
    val ability = AbilityFamily(this.guild, id, this.payload.id)
    return ability.delete()
  }

  fun delete() : Family {
    families.deleteWhere {
      (families.guild_id eq this@Family.guild.id)
        .and(families.id eq this@Family.payload.id)
    }
    return this
  }
}