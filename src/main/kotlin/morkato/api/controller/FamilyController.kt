package morkato.api.controller

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import jakarta.validation.Valid

import morkato.api.infra.repository.AbilityFamilyRepository
import morkato.api.infra.repository.GuildRepository
import morkato.api.exception.model.FamilyNotFoundError
import morkato.api.exception.model.GuildNotFoundError
import morkato.api.dto.family.FamilyAbilityResponseData
import morkato.api.dto.family.FamilyResponseData
import morkato.api.dto.family.FamilyCreateData
import morkato.api.dto.family.FamilyUpdateData
import morkato.api.dto.validation.IdSchema
import morkato.api.model.guild.Guild

@RestController
@RequestMapping("/families/{guild_id}")
class FamilyController {
  @GetMapping
  @Transactional
  fun getAllByGuildId(
    @PathVariable("guild_id") @IdSchema guild_id: String
  ) : List<FamilyResponseData> {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val abilities = guild.getAllAbilitiesFamilies()
        .toMutableList()
      guild.getAllFamilies()
        .map { family ->
          val (valid, invalid) = abilities.partition { it.familyId == family.id }
          abilities.clear()
          abilities.addAll(invalid)
          FamilyResponseData(
            family = family,
            abilities = valid
              .asSequence()
              .map(AbilityFamilyRepository.AbilityFamilyPayload::abilityId)
              .map(Long::toString)
              .toList()
          )
        }.toList()
    } catch (exc: GuildNotFoundError) {
      listOf()
    }
  }
  @PostMapping
  @Transactional
  fun createByGuildId(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @RequestBody @Valid data: FamilyCreateData
  ) : FamilyResponseData {
    val guild = Guild(GuildRepository.findByIdOrCreate(guild_id))
    val family = guild.createFamily(
      name = data.name,
      npcKind = data.npc_kind,
      percent = data.percent,
      description = data.description,
      banner = data.banner
    )
    return FamilyResponseData(family, listOf())
  }
  @GetMapping("/{id}")
  @Transactional
  fun getReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : FamilyResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val family = guild.getFamily(id.toLong())
      val abilities = family.getAllAbilities()
        .map(AbilityFamilyRepository.AbilityFamilyPayload::abilityId)
        .map(Long::toString)
        .toList()
      FamilyResponseData(family, abilities)
    } catch (exc: GuildNotFoundError) {
      val extra : MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guild_id
      extra["id"] = id
      throw FamilyNotFoundError(extra)
    }
  }
  @PutMapping("/{id}")
  @Transactional
  fun updateFamilyByRef(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: FamilyUpdateData
  ) : FamilyResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val before = guild.getFamily(id.toLong())
      val family = before.update(
        name = data.name,
        npcKind = null,
        percent = data.percent,
        description = data.description,
        banner = data.banner
      )
      val abilities = family.getAllAbilities()
        .map(AbilityFamilyRepository.AbilityFamilyPayload::abilityId)
        .map(Long::toString)
        .toList()
      FamilyResponseData(family, abilities)
    } catch (exc: GuildNotFoundError) {
      val extra : MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guild_id
      extra["id"] = id
      throw FamilyNotFoundError(extra)
    }
  }
  @DeleteMapping("/{id}")
  @Transactional
  fun deleteFamilyByRef(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : FamilyResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val family = guild.getFamily(id.toLong())
      val abilities = family.getAllAbilities()
        .map(AbilityFamilyRepository.AbilityFamilyPayload::abilityId)
        .map(Long::toString)
        .toList()
      family.delete()
      FamilyResponseData(family, abilities)
    } catch (exc: GuildNotFoundError) {
      val extra : MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guild_id
      extra["id"] = id
      throw FamilyNotFoundError(extra)
    }
  }
  @PostMapping("/{id}/abilities/{ability_id}")
  @Transactional
  fun syncAbilityInFamily(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @PathVariable("ability_id") @IdSchema ability_id: String
  ) : FamilyAbilityResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val family = guild.getFamily(id.toLong())
      val result = family.addAbility(ability_id.toLong())
      FamilyAbilityResponseData(result)
    } catch (exc: GuildNotFoundError) {
      val extra : MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guild_id
      extra["id"] = id
      throw FamilyNotFoundError(extra)
    }
  }
  @DeleteMapping("/{id}/abilities/{ability_id}")
  @Transactional
  fun unsyncAbilityInFamily(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @PathVariable("ability_id") @IdSchema ability_id: String
  ) : FamilyAbilityResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val family = guild.getFamily(id.toLong())
      val result = family.dropAbility(ability_id.toLong())
      FamilyAbilityResponseData(result)
    } catch (exc: GuildNotFoundError) {
      val extra : MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guild_id
      extra["id"] = id
      throw FamilyNotFoundError(extra)
    }
  }
}