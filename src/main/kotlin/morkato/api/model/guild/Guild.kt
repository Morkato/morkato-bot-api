package morkato.api.models.guild

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.*

import morkato.api.infra.repository.GuildRepository

import morkato.api.database.ability.AbilityCreateData
import morkato.api.database.ability.AbilityPayload
import morkato.api.database.ability.Ability

import morkato.api.database.tables.abilities_families
import morkato.api.database.tables.abilities
import morkato.api.database.tables.families
import morkato.api.database.tables.players
import morkato.api.database.tables.attacks
import morkato.api.database.tables.guilds
import morkato.api.database.tables.arts
import morkato.api.database.tables.npcs

import morkato.api.database.art.ArtCreateData
import morkato.api.database.art.ArtPayload
import morkato.api.models.art.Art

import morkato.api.database.foreign.AbilityFamily
import morkato.api.database.attack.Attack

import morkato.api.database.family.FamilyCreateData
import morkato.api.database.family.FamilyPayload
import morkato.api.database.family.Family

import morkato.api.database.npc.NpcCreateData
import morkato.api.database.npc.NpcPayload
import morkato.api.database.npc.NpcType
import morkato.api.database.npc.Npc

import morkato.api.database.player.PlayerCreateData
import morkato.api.database.player.PlayerPayload
import morkato.api.database.player.Player
import morkato.api.exception.NotFoundError
import morkato.api.exception.ModelType;

class Guild(
  val id: String,
  val human_initial_life: Long,
  val oni_initial_life: Long,
  val hybrid_initial_life: Long,
  val breath_initial: Long,
  val blood_initial: Long,
  val family_roll: Int,
  val ability_roll: Int,
  val roll_category_id: String?,
  val off_category_id: String?
) {
  public constructor(row: ResultRow) : this(GuildRepository.GuildPayload(row)) {}
  public constructor(payload: GuildRepository.GuildPayload) : this(
    payload.id,
    payload.human_initial_life,
    payload.oni_initial_life,
    payload.hybrid_initial_life,
    payload.breath_initial,
    payload.blood_initial,
    payload.family_roll,
    payload.ability_roll,
    payload.roll_category_id,
    payload.off_category_id
  ) {}
  companion object {
    fun from(row: ResultRow) : Guild {
      return Guild(
        row[guilds.id],
        row[guilds.human_initial_life],
        row[guilds.oni_initial_life],
        row[guilds.hybrid_initial_life],
        row[guilds.breath_initial],
        row[guilds.blood_initial],
        row[guilds.family_roll],
        row[guilds.ability_roll],
        row[guilds.roll_category_id],
        row[guilds.off_category_id]
      )
    }

    fun getRefOrCreate(id: String) : Guild {
      try {
        return getReference(id);
      } catch(exc: Exception) {
        return create(id);
      }
    }

    fun getReference(id: String) : Guild {

    }

    fun create(id: String) : Guild {
      guilds.insert {
        it[guilds.id] = id
      }
      return Guild(
        id = id,
        human_initial_life = 1000,
        oni_initial_life = 500,
        hybrid_initial_life = 1500,
        breath_initial = 500,
        blood_initial = 1000,
        family_roll = 3,
        ability_roll = 3,
        roll_category_id = null,
        off_category_id = null
      )
    }
  }
  fun update(data: GuildUpdateData) : Guild {
    guilds.update({
      (guilds.id eq this@Guild.id)
    }) {
      if (data.human_initial_life != null) {
        it[this.human_initial_life] = data.human_initial_life
      }
      if (data.oni_initial_life != null) {
        it[this.oni_initial_life] = data.oni_initial_life
      }
      if (data.hybrid_initial_life != null) {
        it[this.hybrid_initial_life] = data.hybrid_initial_life
      }
      if (data.breath_initial != null) {
        it[this.breath_initial] = data.breath_initial
      }
      if (data.blood_initial != null) {
        it[this.blood_initial] = data.blood_initial
      }
      if (data.family_roll != null) {
        it[this.family_roll] = data.family_roll
      }
      if (data.ability_roll != null) {
        it[this.ability_roll] = data.ability_roll
      }
      if (data.roll_category_id != null) {
        it[this.roll_category_id] = data.roll_category_id
      }
      if (data.off_category_id != null) {
        it[this.off_category_id] = data.off_category_id
      }
    }
    return Guild(
      id = id,
      human_initial_life = data.human_initial_life ?: 1000,
      oni_initial_life = data.oni_initial_life ?: 500,
      hybrid_initial_life = data.hybrid_initial_life ?: 1500,
      breath_initial = data.breath_initial ?: 500,
      blood_initial = data.blood_initial ?: 1000,
      family_roll = data.family_roll ?: 3,
      ability_roll = data.ability_roll ?: 3,
      roll_category_id = data.roll_category_id,
      off_category_id = data.off_category_id
    )
  }
  fun getAllArts() : List<morkato.api.models.art.Art> {
    return arts
      .selectAll()
      .where(arts.guild_id eq id)
      .asSequence()
      .map(morkato.api.models.art.Art::getPayload)
      .map { morkato.api.models.art.Art(this, it) }
      .toList()
  }
  fun getArt(id: Long) : morkato.api.models.art.Art {
    try {
      val row = arts
        .selectAll()
        .where(
          (arts.guild_id eq this.id)
            and (arts.id eq id)
        )
        .single()
      val payload = morkato.api.models.art.Art.getPayload(row)
      return morkato.api.models.art.Art(this, payload)
    } catch (exc: NoSuchElementException) {
      val extra: Map<String, Any?> = mapOf(
        "guild_id" to this.id,
        "id" to id.toString()
      )
      throw NotFoundError(ModelType.ART, extra)
    }
  }
  fun createArt(data: ArtCreateData) : morkato.api.models.art.Art {
    val id = arts.insert {
      it[this.guild_id] = this@Guild.id
      it[this.name] = data.name
      it[this.type] = data.type
      it[this.description] = data.description
      it[this.banner] = data.banner
    } get arts.id
    val payload = ArtPayload(
      this.id, id,
      name = data.name,
      type = data.type,
      description = data.description,
      banner = data.banner
    )
    return morkato.api.models.art.Art(this, payload)
  }

  fun getAllAttacks() : List<Attack> {
    return attacks
      .selectAll()
      .where(attacks.guild_id eq id)
      .asSequence()
      .map(Attack::getPayload)
      .map { Attack(this, it) }
      .toList()
  }
  fun getAttack(id: Long) : Attack {
    try {
      val row = attacks
        .selectAll()
        .where((attacks.guild_id eq this.id)
          .and (attacks.id eq id))
        .limit(1)
        .single()
      val payload = Attack.getPayload(row)
      return Attack(this, payload)
    } catch (exc: NoSuchElementException) {
      val extra: Map<String, Any?> = mapOf(
        "guild_id" to this.id,
        "id" to id.toString()
      )
      throw NotFoundError(ModelType.ATTACK, extra)
    }
  }

  fun getAllAbilities() : List<Ability> {
    return abilities
      .selectAll()
      .where({ abilities.guild_id eq this@Guild.id })
      .asSequence()
      .map(Ability::getPayload)
      .map { Ability(this@Guild, it) }
      .toList()
  }
  fun getAbility(id: Long) : Ability {
    try {
      val row = abilities
        .selectAll()
        .where({
          (abilities.guild_id eq this@Guild.id)
            .and(abilities.id eq id)
        })
        .single()
      val payload = Ability.getPayload(row)
      return Ability(this, payload)
    } catch (exc: NoSuchElementException) {
      val extra: Map<String, Any?> = mapOf(
        "guild_id" to this.id,
        "id" to id.toString()
      )
      throw NotFoundError(ModelType.ABILITY, extra)
    }
  }
  fun createAbility(data: AbilityCreateData) : Ability {
    val id = abilities.insert {
      it[abilities.guild_id] = this@Guild.id
      it[abilities.name] = data.name
      it[abilities.type] = data.type
      it[abilities.npc_kind] = data.npc_kind
      it[abilities.description] = data.description
      it[abilities.banner] = data.banner
      if (data.percent != null) {
        it[abilities.percent] = data.percent
      }
      if (data.immutable != null) {
        it[abilities.immutable] = data.immutable
      }
    } get abilities.id
    val payload = AbilityPayload(
      this.id, id,
      name = data.name,
      type = data.type,
      percent = data.percent ?: 50,
      npcKind = data.npc_kind,
      immutable = data.immutable ?: false,
      description = data.description,
      banner = data.banner
    )
    return Ability(this, payload)
  }

  fun getAllFamilies() : List<Family> {
    return families
      .selectAll()
      .where({ families.guild_id eq this@Guild.id })
      .asSequence()
      .map(Family::getPayload)
      .map { Family(this@Guild, it) }
      .toList()
  }
  fun getFamily(id: Long) : Family {
    try {
      val row = families
        .selectAll()
        .where({
          (families.guild_id eq this@Guild.id)
            .and(families.id eq id)
        })
        .single()
      val payload = Family.getPayload(row)
      return Family(this, payload)
    } catch (exc: NoSuchElementException) {
      val extra: Map<String, Any?> = mapOf(
        "guild_id" to this.id,
        "id" to id.toString()
      )
      throw NotFoundError(ModelType.FAMILY, extra)
    }
  }
  fun createFamily(data: FamilyCreateData) : Family {
    val id = families.insert {
      it[this.guild_id] = this@Guild.id
      it[this.name] = data.name
      it[this.npc_kind] = data.npc_kind
      it[this.description] = data.description
      it[this.banner] = data.banner
      if (data.percent != null) {
        it[this.percent] = data.percent
      }
    } get families.id
    val payload = FamilyPayload(
      this.id, id,
      name = data.name,
      percent = data.percent ?: 50,
      npcKind = data.npc_kind,
      description = data.description,
      banner = data.banner
    )
    return Family(this, payload)
  }

  fun getAllRelationAbilitiesFamilies() : List<AbilityFamily> {
    return abilities_families
      .selectAll()
      .where({
        (abilities_families.guild_id eq this@Guild.id)
      })
      .asSequence()
      .map { AbilityFamily(this@Guild, it[abilities_families.ability_id], it[abilities_families.family_id]) }
      .toList()
  }

  fun getNpc(id: Long) : Npc {
    try {
      val row = npcs
        .selectAll()
        .where({
          (npcs.guild_id eq this@Guild.id)
            .and(npcs.id eq id)
        })
        .limit(1)
        .single()
      val payload = Npc.getPayload(row)
      return Npc(this, payload)
    } catch (exc: NoSuchElementException) {
      val extra: Map<String, Any?> = mapOf(
        "guild_id" to this.id,
        "id" to id.toString()
      )
      throw NotFoundError(ModelType.FAMILY, extra)
    }
  }
  fun getNpcBySurname(surname: String) : Npc {
    try {
      val row = npcs
        .selectAll()
        .where({
          (npcs.guild_id eq this@Guild.id)
            .and(npcs.surname eq surname)
        })
        .limit(1)
        .single()
      val payload = Npc.getPayload(row)
      return Npc(this, payload)
    } catch (exc: NoSuchElementException) {
      val extra: Map<String, Any?> = mapOf(
        "guild_id" to this.id,
        "id" to id.toString()
      )
      throw NotFoundError(ModelType.FAMILY, extra)
    }
  }
  fun createNpc(data: NpcCreateData) : Npc {
    val maxLife = when (data.type) {
      NpcType.HUMAN -> this.human_initial_life
      NpcType.ONI -> this.oni_initial_life
      NpcType.HYBRID -> this.hybrid_initial_life
    }
    val maxBreath = this.breath_initial
    val maxBlood = this.blood_initial
    val id = npcs.insert {
      it[this.guild_id] = this@Guild.id
      it[this.name] = data.name
      it[this.family_id] = data.family_id.toLong()
      it[this.type] = data.type
      it[this.surname] = data.surname
      it[this.icon] = data.icon
      it[this.max_life] = maxLife
      it[this.max_breath] = maxBreath
      it[this.max_blood] = maxBlood
    } get npcs.id
    val payload = NpcPayload(
      this.id, id,
      name = data.name,
      type = data.type,
      familyId = data.family_id.toLong(),
      surname = data.surname,
      energy = 100,
      maxLife = maxLife,
      maxBreath = maxBreath,
      maxBlood = maxBlood,
      currentLife = maxLife,
      currentBreath = maxBreath,
      currentBlood = maxBlood,
      icon = data.icon
    )
    return Npc(this, payload)
  }

  fun getPlayer(id: String) : Player {
    try {
      val row = players
        .selectAll()
        .where({
          (players.guild_id eq this@Guild.id)
            .and(players.id eq id)
        })
        .limit(1)
        .single()
      val payload = Player.getPayload(row)
      return Player(this, payload)
    } catch (exc: NoSuchElementException) {
      val extra: Map<String, String> = mapOf(
        "guild_id" to this.id,
        "id" to id
      )
      throw NotFoundError(ModelType.PLAYER, extra)
    }
  }
  fun createPlayer(data: PlayerCreateData, id: String) : Player {
    val abilityRoll = data.ability_roll ?: this@Guild.ability_roll
    val familyRoll = data.family_roll ?: this@Guild.family_roll
    players.insert {
      it[this.guild_id] = this@Guild.id
      it[this.id] = id
      it[this.ability_roll] = abilityRoll
      it[this.family_roll] = familyRoll
      it[this.expected_npc_kind] = data.expected_npc_kind
      if (data.is_prodigy != null) {
        it[this.is_prodigy] = data.is_prodigy
      }
      if (data.has_mark != null) {
        it[this.has_mark] = data.has_mark
      }
    }
    val payload = PlayerPayload(
      this.id, id,
      abilityRoll = abilityRoll,
      familyRoll = familyRoll,
      isProdigy = data.is_prodigy ?: false,
      hasMark = data.has_mark ?: false,
      expectedFamilyId = null,
      expectedNpcType = data.expected_npc_kind
    )
    return Player(this, payload)
  }
}