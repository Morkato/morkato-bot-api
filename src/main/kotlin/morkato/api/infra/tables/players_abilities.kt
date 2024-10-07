package morkato.api.infra.tables

import org.jetbrains.exposed.sql.Table

object players_abilities : Table("players_abilities") {
  val guild_id = reference("guild_id", guilds.id)
  val player_id = reference("player_id", players.id)
  val ability_id = reference("ability_id", abilities.id)

  override val primaryKey = PrimaryKey(guild_id, player_id, ability_id)
}