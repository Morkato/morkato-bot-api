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

import morkato.api.response.data.FamilyAbilityResponseData
import morkato.api.response.data.FamilyResponseData
import morkato.api.database.family.FamilyCreateData
import morkato.api.database.family.FamilyUpdateData
import morkato.api.database.foreign.AbilityFamily
import morkato.api.database.family.Family
import morkato.api.database.guild.Guild
import morkato.api.dto.validation.IdSchema

@RestController
@RequestMapping("/families/{guild_id}")
class FamilyController {
  companion object {
    fun filterAbilitiesWithFamily(family: Family, abilities: Sequence<AbilityFamily>) : Sequence<AbilityFamily> {
      return abilities
        .filter {
          return@filter it.familyId == family.payload.id
        }
    }
  }
  @GetMapping
  @Transactional
  fun getAllByGuildId(
    @PathVariable("guild_id") @IdSchema guild_id: String
  ) : List<FamilyResponseData> {
    val guild = Guild.getRefOrCreate(guild_id)
    val families = guild.getAllFamilies()
    val abilities = guild.getAllRelationAbilitiesFamilies()
    return families
      .map {
        val filteredAbilities = filterAbilitiesWithFamily(it, abilities.asSequence())
          .map(AbilityFamily::abilityId)
          .map(Long::toString)
          .toList()
        return@map FamilyResponseData.from(it, filteredAbilities)
      }
  }
  @PostMapping
  @Transactional
  fun createByGuildId(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @RequestBody @Valid data: FamilyCreateData
  ) : FamilyResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val family = guild.createFamily(data)
    return FamilyResponseData.from(family, listOf<String>())
  }
  @GetMapping("/{id}")
  @Transactional
  fun getReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : FamilyResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val family = guild.getFamily(id.toLong())
    val abilities = family.getAllAbilities()
      .asSequence()
      .map(AbilityFamily::abilityId)
      .map(Long::toString)
      .toList()
    return FamilyResponseData.from(family, abilities)
  }
  @PutMapping("/{id}")
  @Transactional
  fun updateFamilyByRef(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: FamilyUpdateData
  ) : FamilyResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val before = guild.getFamily(id.toLong())
    val family = before.update(data)
    val abilities = family.getAllAbilities()
      .asSequence()
      .map(AbilityFamily::abilityId)
      .map(Long::toString)
      .toList()
    return FamilyResponseData.from(family, abilities)
  }
  @DeleteMapping("/{id}")
  @Transactional
  fun deleteFamilyByRef(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : FamilyResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val family = guild.getFamily(id.toLong())
    val abilities = family.getAllAbilities()
      .asSequence()
      .map(AbilityFamily::abilityId)
      .map(Long::toString)
      .toList()
    return FamilyResponseData.from(family.delete(), abilities)
  }
  @PostMapping("/{id}/abilities/{ability_id}")
  @Transactional
  fun syncAbilityInFamily(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @PathVariable("ability_id") @IdSchema ability_id: String
  ) : FamilyAbilityResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val family = guild.getFamily(id.toLong())
    val result = family.addAbility(ability_id.toLong())
    return FamilyAbilityResponseData.from(result)
  }
  @DeleteMapping("/{id}/abilities/{ability_id}")
  @Transactional
  fun unsyncAbilityInFamily(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @PathVariable("ability_id") @IdSchema ability_id: String
  ) : FamilyAbilityResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val family = guild.getFamily(id.toLong())
    val result = family.dropAbility(ability_id.toLong())
    return FamilyAbilityResponseData.from(result)
  }
}