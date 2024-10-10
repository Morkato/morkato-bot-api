package morkato.api.infra.tables

import org.jetbrains.exposed.sql.Table
import morkato.api.model.art.ArtType

object arts : Table("arts") {
  val guild_id = reference("guild_id", guilds.id)
  val id = long("id").autoIncrement()

  val name = varchar("name", length = 32)
  val type = enumerationByName<ArtType>("type", length = 16, klass = ArtType::class)
  val description = varchar("description", length = 2048).nullable()
  val banner = text("banner").nullable()
  val energy = integer("energy")
  val life = long("life")
  val breath = long("breath")
  val blood = long("blood")

  override val primaryKey = PrimaryKey(guild_id, id)
}