package morkato.api.database.ability

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import morkato.api.database.family.Family
import morkato.api.database.guild.Guild
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.*

open class Ability(
  val guildId: String,
  val id: Long,
  val name: String,
  val type: AbilityType,
  val percent: Int,
  val npcKind: Int,
  val immutable: Boolean,
  val description: String?,
  val banner: String?
) {
  companion object {
    private fun from(data: AbilityCreateData, guild: Guild, id: Long) : Ability {
      return Ability(
        guild.id,
        id,
        data.name,
        data.type,
        data.percent ?: 50,
        data.npc_kind,
        data.immutable ?: false,
        data.description,
        data.banner
      )
    }
    fun from(row: ResultRow) : Ability {
      return Ability(
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
    object abilities : Table("abilities") {
      val guild_id = varchar("guild_id", length = 30)
      val id = long("id").autoIncrement()

      val name = varchar("name", 32)
      val type = enumerationByName<AbilityType>("type", 30, klass = AbilityType::class)
      val percent = integer("percent")
      val npc_kind = integer("npc_kind")
      val immutable = bool("immutable")
      val description = varchar("description", length = 2048).nullable()
      val banner = text("banner").nullable()

      override val primaryKey = PrimaryKey(guild_id, id)
    }

    fun findAllByGuildId(guild_id: String) : List<Ability> {
      return abilities
        .selectAll()
        .where({ abilities.guild_id eq guild_id })
        .map(::from)
        .toList()
    }
    fun getReference(guild_id: String, id: Long) : Ability {
      return from(
        abilities
          .selectAll()
          .where({ (abilities.guild_id eq guild_id) and (abilities.id eq id) })
          .single()
      )
    }

    fun create(data: AbilityCreateData, guild: Guild) : Ability {
      val id = abilities.insert {
        it[abilities.guild_id] = guild.id
        it[abilities.name] = data.name
        it[abilities.type] = data.type
        it[abilities.npc_kind] = data.npc_kind
        it[abilities.description] = data.description
        it[abilities.banner] = data.banner
        if (data.percent != null) {
          it[abilities.percent] = data.percent
        }
        if (data.immutable != null) {
          it[abilities.immutable] = data.immutable
        }
      } get abilities.id
      return from(data, guild, id)
    }
  }

  private fun copy(data: AbilityUpdateData) : Ability {
    return Ability(
      guildId, id,
      data.name ?: name,
      data.type ?: type,
      data.percent ?: percent,
      data.npc_kind ?: npcKind,
      immutable,
      data.description ?: description,
      data.banner ?: banner
    )
  }

  fun update(data: AbilityUpdateData) : Ability {
    if (data.empty()) {
      return this;
    }
    abilities.update({ (abilities.guild_id eq guildId) and (abilities.id eq id) }) {
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

    return copy(data)
  }

  fun delete() : Ability {
    abilities.deleteWhere { (abilities.guild_id eq guildId) and (abilities.id eq id) }
    return this
  }
}