package morkato.api.database.art

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import morkato.api.database.guild.Guild
import org.jetbrains.exposed.sql.*

class Art(
  val guildId: String,
  val id: Long,
  val name: String,
  val type: ArtType,
  val description: String?,
  val banner: String?
) {
  companion object {
    fun from(row: ResultRow) : Art {
      return Art(
        row[arts.guild_id],
        row[arts.id],
        row[arts.name],
        row[arts.type],
        row[arts.description],
        row[arts.banner]
      )
    }
    object arts : Table("arts") {
      val guild_id = varchar("guild_id", length = 30)
      val id = long("id").autoIncrement()

      val name = varchar("name", length = 32)
      val type = enumerationByName<ArtType>("type", length = 16, klass = ArtType::class)
      val description = varchar("description", length = 2048).nullable()
      val banner = text("banner").nullable()

      override val primaryKey = PrimaryKey(guild_id, id)
    }

    fun findAllByGuildId(guildId: String) : List<Art> {
      return arts
        .selectAll()
        .where(arts.guild_id eq guildId)
        .map { from(it) }
        .toList()
    }

    fun getReference(guildId: String, id: Long) : Art {
      return from(
        arts
          .selectAll()
          .where(arts.guild_id eq guildId)
          .andWhere { arts.id eq id }
          .single()
      )
    }

    fun create(data: ArtCreateData, guild: Guild) : Art {
      val id = arts.insert {
        it[arts.guild_id] = guild.id;
        it[arts.name] = data.name;
        it[arts.type] = data.type;
        it[arts.description] = data.description;
        it[arts.banner] = data.banner;
      } get arts.id
      return Art(guild.id, id, data.name, data.type, data.description, data.banner)
    }
  }

  fun copy(data: ArtUpdateData) : Art {
    return Art(
      guildId, id,
      data.name ?: this.name,
      data.type ?: this.type,
      data.description ?: this.description,
      data.banner ?: this.banner
    )
  }

  fun update(data: ArtUpdateData) : Art {
    arts.update({ (arts.guild_id eq guildId) and (arts.id eq id) }) {
      if (data.name != null) {
        it[arts.name] = data.name
      }
      if (data.type != null) {
        it[arts.type] = data.type
      }
      if (data.description != null) {
        it[arts.description] = data.description
      }
      if (data.banner != null) {
        it[arts.banner] = data.banner
      }
    };
    return copy(data)
  }
  fun delete() : Art {
    arts.deleteWhere { (arts.guild_id eq guildId) and (arts.id eq id) };
    return this;
  }
}