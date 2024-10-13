package morkato.api.model.guild

import morkato.api.exception.NotFoundError
import morkato.api.exception.ModelType
import morkato.api.infra.repository.*

import morkato.api.model.ability.AbilityType
import morkato.api.model.ability.Ability
import morkato.api.model.family.Family
import morkato.api.model.attack.Attack
import morkato.api.model.player.Player
import morkato.api.model.npc.NpcType
import morkato.api.model.npc.Npc
import morkato.api.model.art.ArtType
import morkato.api.model.art.Art

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
    banner: String?,
    energy: Int?,
    life: Long?,
    breath: Long?,
    blood: Long?
  ) : Art {
    val payload = ArtRepository.createArt(
      guildId = this.id,
      name = name,
      type = type,
      description = description,
      banner = banner,
      energy = energy,
      life = life,
      breath = breath,
      blood = blood
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
    return Ability(this, payload)
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
    val payload = NpcRepository.findById(this.id, id)
    return Npc(this, payload)
  }
  fun getNpcBySurname(surname: String) : Npc {
    val payload = NpcRepository.findBySurname(this.id, surname)
    return Npc(this, payload)
  }
  fun createNpc(
    playerId: String? = null,
    name: String,
    type: NpcType,
    familyId: Long,
    surname: String,
    energy: Int?,
    flags: Int?,
    icon: String?
  ) : Npc {
    val life = when (type) {
      NpcType.HUMAN -> this.humanInitialLife
      NpcType.ONI -> this.oniInitialLife
      NpcType.HYBRID -> this.hybridInitialLife
    }
    val breath = this.breathInitial
    val blood = this.bloodInitial
    val payload = NpcRepository.createNpc(
      playerId = playerId,
      guildId = this.id,
      name = name,
      type = type,
      familyId = familyId,
      surname = surname,
      energy = energy,
      flags = flags,
      life = life,
      breath = breath,
      blood = blood,
      icon = icon
    )
    return Npc(this, payload)
  }

  fun getPlayer(id: String) : Player {
    val payload = PlayerRepository.findById(this.id, id)
    return Player(this, payload)
  }
  fun createPlayer(
    id: String,
    expectedNpcType: NpcType,
    abilityRoll: Int?,
    familyRoll: Int?,
    prodigyRoll: Int?,
    markRoll: Int?,
    berserkRoll: Int?,
    flags: Int?,
    expectedFamilyId: Long?
  ) : Player {
    val thisAbilityRoll = abilityRoll ?: this@Guild.abilityRoll
    val thisFamilyRoll = familyRoll ?: this@Guild.familyRoll
    val payload = PlayerRepository.createPlayer(
      this.id, id,
      abilityRoll = thisAbilityRoll,
      familyRoll = thisFamilyRoll,
      prodigyRoll = prodigyRoll,
      markRoll = markRoll,
      berserkRoll = berserkRoll,
      flags = flags,
      expectedFamilyId = expectedFamilyId,
      expectedNpcType = expectedNpcType
    )
    return Player(this, payload)
  }
}