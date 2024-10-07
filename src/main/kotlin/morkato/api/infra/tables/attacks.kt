package morkato.api.infra.tables

import org.jetbrains.exposed.sql.Table

object attacks : Table("attacks") {
  val guild_id = reference("guild_id", guilds.id)
  val id = long("id").autoIncrement()

  val name = varchar("name", length = 30)
  val art_id = reference("art_id", arts.id)
  val name_prefix_art = varchar("name_prefix_art", length = 32).nullable()
  val description = varchar("description", length = 2048).nullable()
  val banner = text("banner").nullable()
  val damage = long("damage")
  val breath = long("breath")
  val blood = long("blood")
  val intents = integer("intents")

  override val primaryKey = PrimaryKey(guild_id, id)
}