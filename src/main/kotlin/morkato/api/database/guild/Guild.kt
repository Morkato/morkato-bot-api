package morkato.api.database.guild

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import morkato.api.database.tables.guilds
import morkato.api.database.tables.arts
import org.jetbrains.exposed.sql.*
import morkato.api.database.art.Art
import morkato.api.database.art.ArtCreateData
import morkato.api.database.art.ArtPayload

class Guild(
  val id: String,
  val human_initial_life: Long,
  val oni_initial_life: Long,
  val hybrid_initial_life: Long,
  val breath_initial: Long,
  val blood_initial: Long,
  val family_roll: Long,
  val ability_roll: Long
) {
  companion object {
    fun from(row: ResultRow) : Guild {
      return Guild(
        row[guilds.id],
        row[guilds.human_initial_life],
        row[guilds.oni_initial_life],
        row[guilds.hybrid_initial_life],
        row[guilds.breath_initial],
        row[guilds.blood_initial],
        row[guilds.family_roll],
        row[guilds.ability_roll]
      )
    }

    fun getRefOrCreate(id: String) : Guild {
      try {
        return getReference(id);
      } catch(exc: Exception) {
        return create(id);
      }
    }

    fun getReference(id: String) : Guild {
      return from(
        guilds
          .selectAll()
          .where(guilds.id eq id)
          .single()
      );
    }

    fun create(id: String) : Guild {
      guilds.insert {
        it[guilds.id] = id
      }
      return Guild(id, 1000, 500, 1500, 500, 1000, 3, 3);
    }
  }
  fun getAllArts() : List<Art> {
    return arts
      .selectAll()
      .where(arts.guild_id eq id)
      .asSequence()
      .map(Art::getPayload)
      .map { Art(this, it) }
      .toList()
  }
  fun getArt(id: Long) : Art {
    val row = arts
      .selectAll()
      .where(
        (arts.guild_id eq this.id)
          and (arts.id eq id)
      )
      .single()
    val payload = Art.getPayload(row)
    return Art(this, payload)
  }
  fun createArt(data: ArtCreateData) : Art {
    val id = arts.insert {
      it[arts.guild_id] = this@Guild.id
      it[arts.name] = data.name
      it[arts.type] = data.type
      it[arts.description] = data.description
      it[arts.banner] = data.banner
    } get arts.id
    val payload = ArtPayload(
      this.id, id,
      name = data.name,
      type = data.type,
      description = data.description,
      banner = data.banner
    )
    return Art(this, payload)
  }
}