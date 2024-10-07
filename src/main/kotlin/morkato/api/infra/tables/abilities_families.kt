package morkato.api.infra.tables

import org.jetbrains.exposed.sql.Table

object abilities_families : Table("abilities_families") {
  val guild_id = reference("guild_id", guilds.id)
  val ability_id = reference("ability_id", abilities.id)
  val family_id = reference("family_id", families.id)

  override val primaryKey = PrimaryKey(guild_id, ability_id, family_id)
}