package morkato.api.models.npc

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import morkato.api.database.foreign.AbilityNpc
import morkato.api.database.tables.npcs_abilities
import morkato.api.database.tables.npcs
import morkato.api.database.guild.Guild
import org.jetbrains.exposed.sql.*

class Npc(
  val guild: Guild,
  val payload: NpcPayload
) {
  companion object {
    fun getPayload(row: ResultRow): NpcPayload {
      return NpcPayload(
        row[npcs.guild_id],
        row[npcs.id],
        row[npcs.name],
        row[npcs.type],
        row[npcs.family_id],
        row[npcs.surname],
        row[npcs.energy],
        row[npcs.max_life],
        row[npcs.max_breath],
        row[npcs.max_blood],
        row[npcs.current_life],
        row[npcs.current_breath],
        row[npcs.current_blood],
        row[npcs.icon]
      )
    }
  }

  fun getAllAbilities() : List<AbilityNpc> {
    return npcs_abilities
      .selectAll()
      .where({
        (npcs_abilities.guild_id eq this@Npc.guild.id)
          .and(npcs_abilities.npc_id eq this@Npc.payload.id)
      })
      .map {
        return@map AbilityNpc(this@Npc.guild, this.payload.id, it[npcs_abilities.ability_id])
      }
  }
  fun addAbility(id: Long) : AbilityNpc {
    npcs_abilities.insert {
      it[this.guild_id] = this@Npc.guild.id
      it[this.npc_id] = this@Npc.payload.id
      it[this.ability_id] = id
    }
    return AbilityNpc(this.guild, this.payload.id, id)
  }

  fun update(data: NpcUpdateData) : Npc {
    npcs.update({
      (npcs.guild_id eq this@Npc.guild.id)
        .and(npcs.id eq this@Npc.payload.id)
    }) {
      if (data.name != null) {
        it[npcs.name] = data.name
      }
      if (data.type != null) {
        it[npcs.type] = data.type
      }
      if (data.surname != null) {
        it[npcs.surname] = data.surname
      }
      if (data.energy != null) {
        it[npcs.energy] = data.energy
      }
      if (data.max_life != null) {
        it[npcs.max_life] = data.max_life
      }
      if (data.max_breath != null) {
        it[npcs.max_breath] = data.max_breath
      }
      if (data.max_blood != null) {
        it[npcs.max_blood] = data.max_blood
      }
      if (data.current_life != null) {
        it[npcs.current_life] = data.current_life
      }
      if (data.current_breath != null) {
        it[npcs.current_breath] = data.current_breath
      }
      if (data.current_blood != null) {
        it[npcs.current_blood] = data.current_blood
      }
      if (data.icon != null) {
        it[npcs.icon] = data.icon
      }
    };
    return Npc(this.guild, payload.extend(data))
  }
  fun delete() : Npc {
    npcs.deleteWhere {
      (this.guild_id eq this@Npc.guild.id)
        .and(this.id eq this@Npc.payload.id)
    }
    return this
  }
}