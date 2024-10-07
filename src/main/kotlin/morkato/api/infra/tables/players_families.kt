package morkato.api.infra.tables

import org.jetbrains.exposed.sql.Table

object players_families : Table("players_families") {
  val guild_id = reference("guild_id", guilds.id)
  val player_id = reference("player_id", players.id)
  val family_id = reference("family_id", families.id)

  override val primaryKey = PrimaryKey(guild_id, player_id, family_id)
}