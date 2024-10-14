package morkato.api.infra.tables

import org.jetbrains.exposed.sql.Table

object npcs_arts : Table("npcs_arts") {
  val guild_id = reference("guild_id", guilds.id)
  val npc_id = reference("npc_id", npcs.id)
  val art_id = reference("art_id", arts.id)
  val exp = long("exp")

  override val primaryKey = PrimaryKey(guild_id, npc_id, art_id)
}