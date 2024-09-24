package morkato.api.database.npc;

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq;
import morkato.api.database.guild.Guild.Companion.guilds;
import morkato.api.database.guild.Guild;
import org.jetbrains.exposed.sql.*;

class Npc(
  val guild_id: String,
  val id: Long,
  val name: String,
  val type: NpcType,
  val family_id: Long?,
  val surname: String,
  val energy: Int,
  val max_life: Long,
  val max_breath: Long,
  val max_blood: Long,
  val current_life: Long,
  val current_breath: Long,
  val current_blood: Long,
  val icon: String?
) {
  companion object {
    object npcs : Table("npcs") {
      val guild_id = reference("guild_id", guilds.id);
      val id = long("id").autoIncrement();

      val name = varchar("name", length = 32);
      val type = enumerationByName<NpcType>("type", length = 16, klass = NpcType::class);
      val family_id = long("family_id").nullable();
      val surname = varchar("surname", length = 32);
      val energy = integer("energy");
      val max_life = long("max_life");
      val max_breath = long("max_breath");
      val max_blood = long("max_blood");
      val current_life = long("current_life");
      val current_breath = long("current_breath");
      val current_blood = long("current_blood");
      val icon = text("icon").nullable();

      override val primaryKey = PrimaryKey(guild_id, id);
    }

    private fun toNpc(row: ResultRow): Npc {
      return Npc(
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
      );
    }

    private fun toNpc(data: NpcCreateData, guild: Guild, id: Long) : Npc {
      val life = when (data.type) {
        NpcType.HUMAN -> guild.human_initial_life;
        NpcType.ONI -> guild.oni_initial_life;
        NpcType.HYBRID -> guild.hybrid_initial_life;
      };
      return Npc(
        guild.id,
        id,
        data.name,
        data.type,
        null,
        data.surname,
        data.energy ?: 100,
        data.max_life ?: life,
        data.max_breath ?: guild.breath_initial,
        data.max_blood ?: guild.blood_initial,
        data.max_life ?: life,
        data.max_breath ?: guild.breath_initial,
        data.max_blood ?: guild.blood_initial,
        data.icon
      );
    }

    private fun toNpc(npc: Npc, data: NpcUpdateData) : Npc {
      return Npc(
        npc.guild_id,
        npc.id,
        data.name ?: npc.name,
        data.type ?: npc.type,
        data.family_id?.toLong() ?: npc.family_id,
        data.surname ?: npc.surname,
        data.energy ?: npc.energy,
        data.max_life ?: npc.max_life,
        data.max_breath ?: npc.max_breath,
        data.max_blood ?: npc.max_blood,
        data.current_life ?: npc.current_life,
        data.current_breath ?: npc.current_breath,
        data.current_blood ?: npc.current_blood,
        data.icon
      );
    }

    fun getReference(guild_id: String, id: Long) : Npc {
      val row = npcs.selectAll()
        .where({ (npcs.guild_id eq guild_id) and (npcs.id eq id) })
        .limit(1)
        .single()
      return toNpc(row);
    }

    fun getReferenceBySurname(guild_id: String, surname: String) : Npc {
      val row = npcs.selectAll()
        .where({ (npcs.guild_id eq guild_id) and (npcs.surname eq surname)})
        .limit(1)
        .single()
      return toNpc(row);
    }

    fun create(data: NpcCreateData, guild: Guild) : Npc {
      val id = npcs.insert {
        it[npcs.guild_id] = guild.id;
        it[npcs.name] = data.name;
        it[npcs.type] = data.type;
        it[npcs.surname] = data.surname;
        it[npcs.icon] = data.icon;
        if (data.energy != null) {
          it[npcs.energy] = data.energy;
        }
        if (data.max_life != null) {
          it[npcs.max_life] = data.max_life;
        }
        if (data.max_breath != null) {
          it[npcs.max_breath] = data.max_breath;
        }
        if (data.max_blood != null) {
          it[npcs.max_blood] = data.max_blood;
        }
        if (data.current_life != null) {
          it[npcs.current_life] = data.current_life;
        }
        if (data.current_breath != null) {
          it[npcs.current_breath] = data.current_breath;
        }
        if (data.current_blood != null) {
          it[npcs.current_blood] = data.current_blood;
        }
      } get npcs.id;
      return toNpc(data, guild, id);
    }
  }

  fun update(data: NpcUpdateData) : Npc {
    npcs.update({(npcs.guild_id eq npcs.guild_id) and (npcs.id eq npcs.id)}) {
      if (data.name != null) {
        it[npcs.name] = data.name;
      }
      if (data.type != null) {
        it[npcs.type] = data.type;
      }
      if (data.surname != null) {
        it[npcs.surname] = data.surname;
      }
      if (data.energy != null) {
        it[npcs.energy] = data.energy;
      }
      if (data.max_life != null) {
        it[npcs.max_life] = data.max_life;
      }
      if (data.max_breath != null) {
        it[npcs.max_breath] = data.max_breath;
      }
      if (data.max_blood != null) {
        it[npcs.max_blood] = data.max_blood;
      }
      if (data.current_life != null) {
        it[npcs.current_life] = data.current_life;
      }
      if (data.current_breath != null) {
        it[npcs.current_breath] = data.current_breath;
      }
      if (data.current_blood != null) {
        it[npcs.current_blood] = data.current_blood;
      }
      if (data.icon != null) {
        it[npcs.icon] = data.icon;
      }
    };
    return toNpc(this, data);
  }
  fun delete() : Npc {
    npcs.deleteWhere { (npcs.guild_id eq guild_id) and (npcs.id eq id) };
    return this;
  }
}