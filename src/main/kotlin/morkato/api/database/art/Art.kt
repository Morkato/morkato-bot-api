package morkato.api.database.art

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import morkato.api.database.tables.arts
import morkato.api.database.guild.Guild
import org.jetbrains.exposed.sql.*

class Art(
  val guild: Guild,
  val payload: ArtPayload
) {
  companion object {
    fun getPayload(row: ResultRow) : ArtPayload {
      return ArtPayload(
        row[arts.guild_id],
        row[arts.id],
        row[arts.name],
        row[arts.type],
        row[arts.description],
        row[arts.banner]
      )
    }
  }

  fun update(data: ArtUpdateData) : Art {
    arts.update({ (arts.guild_id eq guild.id) and (arts.id eq payload.id) }) {
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
    }
    return Art(guild, payload.extend(data))
  }
  fun delete() : Art {
    arts.deleteWhere {
      (arts.guild_id eq guild.id)
        .and(arts.id eq payload.id)
    }
    return this;
  }
}