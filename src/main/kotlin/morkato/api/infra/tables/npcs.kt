package morkato.api.infra.tables

import org.jetbrains.exposed.sql.Table
import morkato.api.model.npc.NpcType

object npcs : Table("npcs") {
  val guild_id = reference("guild_id", guilds.id)
  val id = long("id").autoIncrement()

  val name = varchar("name", length = 32)
  val type = enumerationByName<NpcType>("type", length = 16, klass = NpcType::class)
  val family_id = long("family_id")
  val player_id = reference("player_id", players.id).nullable()
  val surname = varchar("surname", length = 32)
  val energy = integer("energy")
  val prodigy = bool("prodigy")
  val mark = bool("mark")
  val max_life = long("max_life")
  val max_breath = long("max_breath")
  val max_blood = long("max_blood")
  val current_life = long("current_life")
  val current_breath = long("current_breath")
  val current_blood = long("current_blood")
  val icon = text("icon").nullable()

  override val primaryKey = PrimaryKey(guild_id, id)
}