package morkato.api.database

import morkato.api.database.art.Art.Companion.arts.autoIncrement
import morkato.api.database.art.Art.Companion.arts.nullable
import morkato.api.database.art.ArtType
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
}