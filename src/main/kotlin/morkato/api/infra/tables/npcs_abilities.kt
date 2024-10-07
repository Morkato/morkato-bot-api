package morkato.api.infra.tables

import org.jetbrains.exposed.sql.Table

object npcs_abilities : Table("npcs_abilities") {
  val guild_id = reference("guild_id", guilds.id)
  val npc_id = reference("npc_id", npcs.id)
  val ability_id = reference("ability_id", abilities.id)

  override val primaryKey = PrimaryKey(guild_id, npc_id, ability_id)
}