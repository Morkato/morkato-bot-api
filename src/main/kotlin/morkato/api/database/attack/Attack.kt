package morkato.api.database.attack

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import morkato.api.database.guild.Guild.Companion.guilds
import morkato.api.database.art.Art
import org.jetbrains.exposed.sql.*

class Attack(
  val guildId: String,
  val id: Long,

  val name: String,
  val artId: Long,
  val namePrefixArt: String?,
  val description: String?,
  val banner: String?,
  val damage: Long,
  val breath: Long,
  val blood: Long,
  val intents: Int
) {
  companion object {
    fun from(row: ResultRow): Attack {
      return Attack(
        row[attacks.guild_id],
        row[attacks.id],
        row[attacks.name],
        row[attacks.art_id],
        row[attacks.name_prefix_art],
        row[attacks.description],
        row[attacks.banner],
        row[attacks.damage],
        row[attacks.breath],
        row[attacks.blood],
        row[attacks.intents]
      )
    }

    fun from(data: AttackCreateData, art: Art, id: Long): Attack {
      return Attack(
        art.guildId,
        id,
        data.name,
        art.id,
        data.name_prefix_art,
        data.description,
        data.banner,
        data.damage ?: 0,
        data.breath ?: 0,
        data.blood ?: 0,
        data.intents ?: 0
      )
    }

    object attacks : Table("attacks") {
      val guild_id = reference("guild_id", guilds.id)
      val id = long("id").autoIncrement()

      val name = varchar("name", length = 30)
      val art_id = long("art_id")
      val name_prefix_art = varchar("name_prefix_art", length = 32).nullable()
      val description = varchar("description", length = 2048).nullable()
      val banner = text("banner").nullable()
      val damage = long("damage")
      val breath = long("breath")
      val blood = long("blood")
      val intents = integer("intents")

      override val primaryKey = PrimaryKey(guild_id, id)
    }

    fun findAllByGuildId(guild_id: String) : List<Attack> {
      return attacks
        .selectAll()
        .where(attacks.guild_id eq guild_id)
        .map { from(it) }
        .toList()
    }

    fun findAllByGuildIdAndArtId(guild_id: String, art_id: Long) : List<Attack> {
      return attacks
        .selectAll()
        .where((attacks.guild_id eq guild_id) and (attacks.art_id eq art_id))
        .map { from(it) }
        .toList()
    }

    fun getReference(guild_id: String, id: Long) : Attack {
      val row = attacks
        .selectAll()
        .where((attacks.guild_id eq guild_id) and (attacks.id eq id))
        .limit(1)
        .single();
      return from(row)
    }

    fun create(data: AttackCreateData, art: Art) : Attack {
      val id = attacks.insert {
        it[attacks.guild_id] = art.guildId
        it[attacks.art_id] = art.id
        it[attacks.name] = data.name
        it[attacks.name_prefix_art] = data.name_prefix_art
        it[attacks.description] = data.description
        it[attacks.banner] = data.banner
        if (data.damage != null) {
          it[attacks.damage] = data.damage
        }
        if (data.breath != null) {
          it[attacks.breath] = data.breath
        }
        if (data.blood != null) {
          it[attacks.blood] = data.blood
        }
        if (data.intents != null) {
          it[attacks.intents] = data.intents
        }
      } get attacks.id;
      return from(data, art, id)
    }
  }

  private fun copy(data: AttackUpdateData) : Attack {
    return Attack(
      guildId, id,
      data.name ?: name,
      artId,
      data.name_prefix_art ?: namePrefixArt,
      data.description ?: description,
      data.banner ?: banner,
      data.damage ?: damage,
      data.breath ?: breath,
      data.blood ?: blood,
      data.intents ?: intents
    )
  }

  fun update(data: AttackUpdateData) : Attack {
    attacks.update({ (attacks.guild_id eq guildId) and (attacks.id eq id) }) {
      if (data.name != null) {
        it[attacks.name] = data.name
      }
      if (data.name_prefix_art != null) {
        it[attacks.name_prefix_art] = data.name_prefix_art
      }
      if (data.description != null) {
        it[attacks.description] = data.description
      }
      if (data.banner != null) {
        it[attacks.banner] = data.banner
      }
      if (data.damage != null) {
        it[attacks.damage] = data.damage
      }
      if (data.breath != null) {
        it[attacks.breath] = data.breath
      }
      if (data.blood != null) {
        it[attacks.blood] = data.blood
      }
      if (data.intents != null) {
        it[attacks.intents] = data.intents
      }
    };
    return copy(data)
  }
  fun delete() : Attack {
    attacks.deleteWhere { (attacks.guild_id eq guild_id) and (attacks.id eq id) }
    return this
  }
}
