package morkato.api.database

import morkato.api.database.ability.AbilityType
import morkato.api.database.art.ArtType
import morkato.api.database.npc.NpcType
import org.jetbrains.exposed.sql.Table

object tables {
  object guilds : Table("guilds") {
    val id = varchar("id", length = 30);

    val human_initial_life = long("human_initial_life");
    val oni_initial_life = long("oni_initial_life");
    val hybrid_initial_life = long("hybrid_initial_life");
    val breath_initial = long("breath_initial");
    val blood_initial = long("blood_initial");
    val family_roll = long("family_roll")
    val ability_roll = long("ability_roll")

    override val primaryKey = PrimaryKey(id);
  }

  object arts : Table("arts") {
    val guild_id = reference("guild_id", guilds.id)
    val id = long("id").autoIncrement()

    val name = varchar("name", length = 32)
    val type = enumerationByName<ArtType>("type", length = 16, klass = ArtType::class)
    val description = varchar("description", length = 2048).nullable()
    val banner = text("banner").nullable()

    override val primaryKey = PrimaryKey(guild_id, id)
  }

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

  object families : Table("families") {
    val guild_id = reference("guild_id", guilds.id)
    val id = long("id").autoIncrement()

    val name = varchar("name", length = 32)
    val description = varchar("description", length = 2048).nullable()
    val banner = text("banner").nullable()

    override val primaryKey = PrimaryKey(guild_id, id)
  }

  object abilities_families : Table("abilities_families") {
    val guild_id = reference("guild_id", guilds.id)
    val ability_id = reference("ability_id", abilities.id)
    val family_id = reference("family_id", families.id)

    override val primaryKey = PrimaryKey(guild_id, ability_id, family_id)
  }

  object npcs : Table("npcs") {
    val guild_id = reference("guild_id", guilds.id)
    val id = long("id").autoIncrement()

    val name = varchar("name", length = 32)
    val type = enumerationByName<NpcType>("type", length = 16, klass = NpcType::class)
    val family_id = long("family_id")
    val surname = varchar("surname", length = 32)
    val energy = integer("energy")
    val max_life = long("max_life")
    val max_breath = long("max_breath")
    val max_blood = long("max_blood")
    val current_life = long("current_life")
    val current_breath = long("current_breath")
    val current_blood = long("current_blood")
    val icon = text("icon").nullable()

    override val primaryKey = PrimaryKey(guild_id, id)
  }
}