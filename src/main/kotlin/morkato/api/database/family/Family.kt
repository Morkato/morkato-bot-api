package morkato.api.database.family

import morkato.api.database.guild.Guild
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.*

class Family(
  val guildId: String,
  val id: Long,
  val name: String,
  val description: String?,
  val banner: String?
) {
  companion object {
    private fun from(data: FamilyCreateData, guild: Guild, id: Long) : Family {
      return Family(
        guild.id,
        id,
        data.name,
        data.description,
        data.banner
      )
    }
    fun from(row: ResultRow) : Family {
      return Family(
        row[families.guild_id],
        row[families.id],
        row[families.name],
        row[families.description],
        row[families.banner]
      )
    }
    object families : Table("families") {
      val guild_id = reference("guild_id", Guild.Companion.guilds.id)
      val id = long("id").autoIncrement()

      val name = varchar("name", length = 32)
      val description = varchar("description", length = 2048).nullable()
      val banner = text("banner").nullable()

      override val primaryKey = PrimaryKey(guild_id, id)
    }

    fun findAllByGuildId(guild_id: String) : List<Family> {
      return families
        .selectAll()
        .where({ families.guild_id eq guild_id })
        .map(::from)
        .toList()
    }

    fun getReference(guild_id: String, id: Long) : Family {
      return from(
        families
          .selectAll()
          .where({ (families.guild_id eq guild_id) and (families.id eq id) })
          .single()
      )
    }

    fun create(data: FamilyCreateData, guild: Guild) : Family {
      val id = families.insert {
        it[families.guild_id] = guild.id
        it[families.name] = data.name
        it[families.description] = data.description
        it[families.banner] = data.banner
      } get families.id
      return from(data, guild, id)
    }
  }
  private fun copy(data: FamilyUpdateData) : Family {
    return Family(
      guildId, id,
      data.name ?: name,
      data.description ?: description,
      data.banner ?: banner
    )
  }

  fun update(data: FamilyUpdateData) : Family {
    families.update({ (families.guild_id eq guildId) and (families.id eq id) }) {
      if (data.name != null) {
        it[families.name] = data.name
      }
      if (data.description != null) {
        it[families.description] = data.description
      }
      if (data.banner != null) {
        it[families.banner] = data.banner
      }
    }
    return copy(data)
  }

  fun delete() : Family {
    families.deleteWhere { (families.guild_id eq guild_id) and (families.id eq id) }
    return this
  }
}