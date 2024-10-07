package morkato.api.models.art

import morkato.api.database.attack.Attack
import morkato.api.database.attack.AttackCreateData
import morkato.api.database.attack.AttackPayload
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import morkato.api.database.tables.arts
import morkato.api.database.guild.Guild
import morkato.api.database.tables.attacks
import org.jetbrains.exposed.sql.*

class Art(
  val guild: Guild,
  val payload: morkato.api.models.art.ArtPayload
) {
  companion object {
    fun getPayload(row: ResultRow) : morkato.api.models.art.ArtPayload {
      return morkato.api.models.art.ArtPayload(
        row[arts.guild_id],
        row[arts.id],
        row[arts.name],
        row[arts.type],
        row[arts.description],
        row[arts.banner]
      )
    }
  }

  fun getAllAttacks() : List<Attack> {
    return attacks
      .selectAll()
      .where(
        (attacks.guild_id eq guild.id)
          .and(attacks.art_id eq payload.id))
      .asSequence()
      .map(Attack::getPayload)
      .map { Attack(this.guild, it) }
      .toList()
  }
  fun createAttack(data: AttackCreateData) : Attack {
    val id = attacks.insert {
      it[attacks.guild_id] = guild.id
      it[attacks.art_id] = payload.id
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
    } get attacks.id
    val payload = AttackPayload(
      guild.id, id,
      name = data.name,
      artId = payload.id,
      namePrefixArt = data.name_prefix_art,
      description = data.description,
      banner = data.banner,
      damage = data.damage ?: 1,
      breath = data.breath ?: 1,
      blood = data.blood ?: 1,
      intents = data.intents ?: 0
    )
    return Attack(this.guild, payload)
  }
  fun update(data: morkato.api.models.art.ArtUpdateData) : morkato.api.models.art.Art {
    arts.update({ (arts.guild_id eq guild.id) and (arts.id eq payload.id) }) {
      if (data.name != null) {
        it[arts.name] = data.name
      }
      if (data.type != null) {
        it[arts.type] = data.type
      }
      if (data.description != null) {
        it[arts.description] = data.description
      }
      if (data.banner != null) {
        it[arts.banner] = data.banner
      }
    }
    return morkato.api.models.art.Art(guild, payload.extend(data))
  }
  fun delete() : morkato.api.models.art.Art {
    arts.deleteWhere {
      (arts.guild_id eq guild.id)
        .and(arts.id eq payload.id)
    }
    return this;
  }
}