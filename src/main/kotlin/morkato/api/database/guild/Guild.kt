package morkato.api.database.guild;

import org.jetbrains.exposed.sql.transactions.transaction;
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq;
import org.jetbrains.exposed.sql.*;

class Guild(
  val id: String,
  val human_initial_life: Long,
  val oni_initial_life: Long,
  val hybrid_initial_life: Long,
  val breath_initial: Long,
  val blood_initial: Long
) {
  companion object {
    fun from(row: ResultRow) : Guild {
      return Guild(
        row[guilds.id],
        row[guilds.human_initial_life],
        row[guilds.oni_initial_life],
        row[guilds.hybrid_initial_life],
        row[guilds.breath_initial],
        row[guilds.blood_initial]
      )
    }

    object guilds : Table("guilds") {
      val id = varchar("id", 30);

      val human_initial_life = long("human_initial_life");
      val oni_initial_life = long("oni_initial_life");
      val hybrid_initial_life = long("hybrid_initial_life");
      val breath_initial = long("breath_initial");
      val blood_initial = long("blood_initial");

      override val primaryKey = PrimaryKey(id);
    }

    fun getRefOrCreate(id: String) : Guild {
      try {
        return getReference(id);
      } catch(exc: Exception) {
        return create(id);
      }
    }

    fun getReference(id: String) : Guild {
      return from(
        guilds
          .selectAll()
          .where(guilds.id eq id)
          .single()
      );
    }

    fun create(id: String) : Guild {
      guilds.insert {
        it[guilds.id] = id
      }
      return Guild(id, 1000, 500, 1500, 500, 1000);
    }
  }
}