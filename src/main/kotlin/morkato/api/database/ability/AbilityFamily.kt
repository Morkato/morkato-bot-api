package morkato.api.database.ability

import morkato.api.database.ability.Ability.Companion.abilities
import morkato.api.database.ability.AbilityFamily.Companion.families.family_id
import morkato.api.database.ability.AbilityFamily.Companion.families.guild_id
import morkato.api.database.family.Family
import morkato.api.database.guild.Guild
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class AbilityFamily(
  guildId: String,
  id: Long,
  val family_id: Long,
  name: String,
  type: AbilityType,
  percent: Int,
  npcKind: Int,
  immutable: Boolean,
  description: String?,
  banner: String?
) : Ability(guildId, id, name, type, percent, npcKind, immutable, description, banner) {
  companion object {
    private fun from(row: ResultRow) : AbilityFamily {
      return AbilityFamily(
        row[abilities.guild_id],
        row[abilities.id],
        row[families.family_id],
        row[abilities.name],
        row[abilities.type],
        row[abilities.percent],
        row[abilities.npc_kind],
        row[abilities.immutable],
        row[abilities.description],
        row[abilities.banner]
      )
    }

    private fun from(family: Family, ability: Ability) : AbilityFamily {
      if (family.guildId != ability.guildId) {
        throw Exception()
      }
      return AbilityFamily(
        family.guildId,
        ability.id,
        family.id,
        ability.name,
        ability.type,
        ability.percent,
        ability.npcKind,
        ability.immutable,
        ability.description,
        ability.banner
      )
    }

    object families : Table("abilities_families") {
      val guild_id = reference("guild_id", Guild.Companion.guilds.id)
      val ability_id = reference("ability_id", abilities.id)
      val family_id = reference("family_id", Family.Companion.families.id)

      override val primaryKey = PrimaryKey(guild_id, ability_id, family_id)
    }
    fun findAllByGuildIdAndFamilyId(guild_id: String, family_id: Long) : List<Ability> {
      return families
        .join(abilities, JoinType.INNER, additionalConstraint = {
          (families.guild_id eq abilities.guild_id)
            .and({ families.ability_id eq abilities.id })
        })
        .selectAll()
        .where({ (families.guild_id eq guild_id) and (families.family_id eq family_id) })
        .map(::from)
        .toList()
    }
    fun findAllByGuildId(guild_id: String) : List<AbilityFamily> {
      return abilities
        .join(families, JoinType.INNER, additionalConstraint = {
          (abilities.guild_id eq families.guild_id)
            .and({ abilities.id eq families.ability_id })
        })
        .selectAll()
        .where({ abilities.guild_id eq guild_id })
        .map(::from)
        .toList()
    }
    fun getReference(family: Family, ability: Ability) : AbilityFamily {
      return from(
        families
          .join(abilities, JoinType.INNER, additionalConstraint = {
            (families.guild_id eq abilities.guild_id)
              .and({ families.ability_id eq abilities.id })
          })
          .selectAll()
          .where({
            (families.guild_id eq family.guildId)
              .and(families.family_id eq family.id)
              .and(families.ability_id eq ability.id)
          })
          .single()
      )
    }
    fun create(family: Family, ability: Ability) : AbilityFamily {
      if (family.guildId != ability.guildId) {
        throw Exception()
      }
      families.insert {
        it[families.guild_id] = family.guildId
        it[families.family_id] = family.id
        it[families.ability_id] = ability.id
      }
      return from(family, ability)
    }
  }

  fun drop() : AbilityFamily {
    families.deleteWhere {
      (families.guild_id eq guild_id)
        .and(families.family_id eq family_id)
        .and(families.ability_id eq id)
    }
    return this
  }
}