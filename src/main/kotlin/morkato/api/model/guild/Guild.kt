package morkato.api.model.guild

import morkato.api.infra.repository.AbilityFamilyRepository
import morkato.api.infra.repository.AbilityRepository
import morkato.api.infra.repository.FamilyRepository
import morkato.api.infra.repository.AttackRepository
import morkato.api.infra.repository.GuildRepository
import morkato.api.infra.repository.ArtRepository
import morkato.api.exception.NotFoundError
import morkato.api.exception.ModelType

import morkato.api.model.ability.AbilityType
import morkato.api.model.ability.Ability
import morkato.api.model.family.Family
import morkato.api.model.attack.Attack
import morkato.api.model.art.ArtType
import morkato.api.model.art.Art
import morkato.api.model.npc.NpcType

import org.jetbrains.exposed.sql.ResultRow

//import morkato.api.database.ability.AbilityCreateData
//import morkato.api.database.ability.AbilityPayload
//import morkato.api.database.ability.Ability
//
//import morkato.api.database.tables.abilities_families
//import morkato.api.database.tables.abilities
//import morkato.api.database.tables.families
//import morkato.api.database.tables.players
//import morkato.api.database.tables.attacks
//import morkato.api.database.tables.arts
//import morkato.api.database.tables.npcs
//
//import morkato.api.database.art.ArtCreateData
//import morkato.api.database.art.ArtPayload
//
//import morkato.api.database.foreign.AbilityFamily
//import morkato.api.database.attack.Attack
//
//import morkato.api.database.family.FamilyCreateData
//import morkato.api.database.family.FamilyPayload
//import morkato.api.database.family.Family
//
//import morkato.api.database.npc.NpcCreateData
//import morkato.api.database.npc.NpcPayload
//import morkato.api.database.npc.NpcType
//import morkato.api.database.npc.Npc
//
//import morkato.api.database.player.PlayerCreateData
//import morkato.api.database.player.PlayerPayload
//import morkato.api.database.player.Player

class Guild(
  val id: String,
  val humanInitialLife: Long,
  val oniInitialLife: Long,
  val hybridInitialLife: Long,
  val breathInitial: Long,
  val bloodInitial: Long,
  val familyRoll: Int,
  val abilityRoll: Int,
  val rollCategoryId: String?,
  val offCategoryId: String?
) {
  public constructor(row: ResultRow) : this(GuildRepository.GuildPayload(row)) {}
  public constructor(payload: GuildRepository.GuildPayload) : this(
    payload.id,
    payload.humanInitialLife,
    payload.oniInitialLife,
    payload.hybridInitialLife,
    payload.breathInitial,
    payload.bloodInitial,
    payload.familyRoll,
    payload.abilityRoll,
    payload.rollCategoryId,
    payload.offCategoryId
  ) {}
  fun update(
    humanInitialLife: Long?,
    oniInitialLife: Long?,
    hybridInitialLife: Long?,
    breathInitial: Long?,
    bloodInitial: Long?,
    familyRoll: Int?,
    abilityRoll: Int?,
    rollCategoryId: String?,
    offCategoryId: String?
  ) : Guild {
    val payload = GuildRepository.GuildPayload(
      id = this.id,
      humanInitialLife = humanInitialLife ?: this.humanInitialLife,
      oniInitialLife = oniInitialLife ?: this.oniInitialLife,
      hybridInitialLife = hybridInitialLife ?: this.hybridInitialLife,
      breathInitial = breathInitial ?: this.breathInitial,
      bloodInitial = bloodInitial ?: this.bloodInitial,
      familyRoll = familyRoll ?: this.familyRoll,
      abilityRoll = abilityRoll ?: this.abilityRoll,
      rollCategoryId = rollCategoryId ?: this.rollCategoryId,
      offCategoryId = offCategoryId ?: this.offCategoryId
    )
    GuildRepository.updateGuild(
      id = this.id,
      humanInitialLife = humanInitialLife,
      oniInitialLife = oniInitialLife,
      hybridInitialLife = hybridInitialLife,
      breathInitial = breathInitial,
      bloodInitial = bloodInitial,
      familyRoll = familyRoll,
      abilityRoll = abilityRoll,
      rollCategoryId = rollCategoryId,
      offCategoryId = offCategoryId
    )
    return Guild(payload)
  }
  fun getAllArts() : Sequence<Art> {
    return ArtRepository.findAllByGuildId(this.id)
      .map { Art(this@Guild, it) }
  }
  fun getArt(id: Long) : Art {
    val payload = ArtRepository.findById(this.id, id)
    return Art(this, payload)
  }
  fun createArt(
    name: String,
    type: ArtType,
    description: String?,
    banner: String?
  ) : Art {
    val payload = ArtRepository.createArt(
      guildId = this.id,
      name = name,
      type = type,
      description = description,
      banner = banner
    )
    return Art(this, payload)
  }

  fun getAllAttacks() : Sequence<Attack> {
    return AttackRepository.findAllByGuildId(this.id)
      .map { Attack(this@Guild, it) }
  }
  fun getAttack(id: Long) : Attack {
    val payload = AttackRepository.findById(this.id, id)
    return Attack(this, payload)
  }

  fun getAllAbilities() : Sequence<Ability> {
    return AbilityRepository.findAllByGuildId(this.id)
      .map { Ability(this, it) }
  }
  fun getAbility(id: Long) : Ability {
    val payload = AbilityRepository.findById(this.id, id)
    Ability(this, payload)
  }
  fun createAbility(
    name: String,
    type: AbilityType,
    percent: Int?,
    npcKind: Int,
    immutable: Boolean?,
    description: String?,
    banner: String?
  ) : Ability {
    val payload = AbilityRepository.createAbility(
      guildId = this.id,
      name = name,
      type = type,
      percent = percent,
      npcKind = npcKind,
      immutable = immutable,
      description = description,
      banner = banner
    )
    return Ability(this, payload)
  }

  fun getAllFamilies() : Sequence<Family> {
    return FamilyRepository.findAllByGuildId(this.id)
      .map { Family(this@Guild, it) }
  }
  fun getFamily(id: Long) : Family {
    val payload = FamilyRepository.findById(this.id, id)
    return Family(this, payload)
  }
  fun createFamily(
    name: String,
    npcKind: NpcType,
    percent: Int?,
    description: String?,
    banner: String?
  ) : Family {
    val payload = FamilyRepository.createFamily(
      guildId = this.id,
      name = name,
      npcKind = npcKind,
      percent = percent,
      description = description,
      banner = banner
    )
    return Family(this, payload)
  }

  fun getAllAbilitiesFamilies() : Sequence<AbilityFamilyRepository.AbilityFamilyPayload> {
    return AbilityFamilyRepository.findAllByGuildId(this.id)
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