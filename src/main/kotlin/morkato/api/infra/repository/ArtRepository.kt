package morkato.api.infra.repository

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.and

import morkato.api.exception.model.ArtNotFoundError
import morkato.api.infra.tables.arts
import morkato.api.model.art.ArtType

object ArtRepository {
  public data class ArtPayload(
    val guildId: String,
    val id: Long,
    val name: String,
    val type: ArtType,
    val description: String?,
    val banner: String?
  ) {
    public constructor(row: ResultRow) : this(
      row[arts.guild_id],
      row[arts.id],
      row[arts.name],
      row[arts.type],
      row[arts.description],
      row[arts.banner]
    ) {}
  }
  fun findAllByGuildId(id: String) : Sequence<ArtPayload> {
    return arts
      .selectAll()
      .where({
        (arts.guild_id eq id)
      })
      .asSequence()
      .map(::ArtPayload)
  }
  fun findById(guildId: String, id: Long) : ArtPayload {
    return try {
      ArtPayload(
        arts
          .selectAll()
          .where({
            (arts.guild_id eq guildId)
              .and(arts.id eq id)
          })
          .limit(1)
          .single()
      )
    } catch (exc: NoSuchElementException) {
      val extra: MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guildId
      extra["id"] = id.toString()
      throw ArtNotFoundError(extra)
    }
  }
  fun createArt(
    guildId: String,
    name: String,
    type: ArtType,
    description: String?,
    banner: String?
  ) : ArtPayload {
    val id = arts.insert {
      it[this.guild_id] = guildId
      it[this.name] = name
      it[this.type] = type
      it[this.description] = description
      it[this.banner] = banner
    } get arts.id
    return ArtPayload(
      guildId = guildId,
      id = id,
      name = name,
      type = type,
      description = description,
      banner = banner
    )
  }
  fun updateArt(
    guildId: String,
    id: Long,
    name: String?,
    type: ArtType?,
    description: String?,
    banner: String?
  ) : Unit {
    arts.update({
      (arts.guild_id eq guildId)
        .and(arts.id eq id)
    }) {
      if (name != null) {
        it[this.name] = name
      }
      if (type != null) {
        it[this.type] = type
      }
      if (description != null) {
        it[this.description] = description
      }
      if (banner != null) {
        it[this.banner] = banner
      }
    }
  }
  fun deleteArt(guildId: String, id: Long) {
    arts.deleteWhere {
      (this.guild_id eq guildId)
        .and(this.id eq id)
    }
  }
}