package morkato.api.infra.tables

import morkato.api.model.ability.AbilityType
import org.jetbrains.exposed.sql.Table

object abilities : Table("abilities") {
  val guild_id = reference("guild_id", guilds.id)
  val id = long("id").autoIncrement()

  val name = varchar("name", 32)
  val type = enumerationByName<AbilityType>("type", 30, klass = AbilityType::class)
  val percent = integer("percent")
  val npc_kind = integer("npc_kind")
  val immutable = bool("immutable")
  val description = varchar("description", length = 2048).nullable()
  val banner = text("banner").nullable()

  override val primaryKey = PrimaryKey(guild_id, id)
}