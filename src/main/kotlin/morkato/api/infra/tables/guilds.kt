package morkato.api.infra.tables

import org.jetbrains.exposed.sql.Table

object guilds : Table("guilds") {
  val id = varchar("id", length = 30);

  val human_initial_life = long("human_initial_life");
  val oni_initial_life = long("oni_initial_life");
  val hybrid_initial_life = long("hybrid_initial_life");
  val breath_initial = long("breath_initial");
  val blood_initial = long("blood_initial");
  val family_roll = integer("family_roll")
  val ability_roll = integer("ability_roll")
  val roll_category_id = varchar("roll_category_id", length = 30).nullable()
  val off_category_id = varchar("off_category_id", length = 30).nullable()

  override val primaryKey = PrimaryKey(id);
}