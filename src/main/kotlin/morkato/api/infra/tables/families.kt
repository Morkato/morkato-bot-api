package morkato.api.infra.tables

import org.jetbrains.exposed.sql.Table

object families : Table("families") {
  val guild_id = reference("guild_id", guilds.id)
  val id = long("id").autoIncrement()

  val name = varchar("name", length = 32)
  val percent = integer("percent")
  val npc_kind = reference("npc_kind", npcs.type)
  val description = varchar("description", length = 2048).nullable()
  val banner = text("banner").nullable()

  override val primaryKey = PrimaryKey(guild_id, id)
}