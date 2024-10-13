package morkato.api.infra.tables

import org.jetbrains.exposed.sql.Table

object players : Table("players") {
  val guild_id = reference("guild_id", guilds.id)
  val id = varchar("id", length = 30)

  val ability_roll = integer("ability_roll")
  val family_roll = integer("family_roll")
  val prodigy_roll = integer("prodigy_roll")
  val mark_roll = integer("mark_roll")
  val berserk_roll = integer("berserk_roll")
  val flags = integer("flags")
  val expected_family_id = reference("expected_family_id", families.id)
  val expected_npc_kind = reference("expected_npc_kind", npcs.type)

  override val primaryKey = PrimaryKey(guild_id, id)
}