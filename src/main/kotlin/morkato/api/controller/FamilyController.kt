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
import morkato.api.response.data.AbilityResponseData
import morkato.api.database.ability.AbilityFamily
import morkato.api.database.ability.Ability
import morkato.api.database.family.Family
import morkato.api.database.guild.Guild
import morkato.api.validation.IdSchema

@RestController
@RequestMapping("/families/{guild_id}")
class FamilyController  {
  @GetMapping
  @Transactional
  fun getAllByGuildId(
    @PathVariable("guild_id") @IdSchema guild_id: String
  ) : List<FamilyResponseData> {
    val families = Family.findAllByGuildId(guild_id)
    val abilities = AbilityFamily.findAllByGuildId(guild_id)
    return families
      .map { family ->
        val familyAbilities = abilities
          .asSequence()
          .filter { ability -> ability.family_id == family.id }
          .map(AbilityResponseData::from)
          .toList()
        FamilyResponseData.from(family, familyAbilities)
      }
  }
  @PostMapping
  @Transactional
  fun createByGuildId(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @RequestBody @Valid data: FamilyCreateData
  ) : FamilyResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val family = Family.create(data, guild)
    return FamilyResponseData.from(family, listOf<AbilityResponseData>())
  }
  @GetMapping("/{id}")
  @Transactional
  fun getReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : FamilyResponseData {
    val family = Family.getReference(guild_id, id.toLong())
    val abilities = AbilityFamily.findAllByGuildIdAndFamilyId(guild_id, id.toLong())
    return FamilyResponseData.from(family, abilities
      .map(AbilityResponseData::from)
    )
  }
  @PutMapping("/{id}")
  @Transactional
  fun updateFamilyByRef(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: FamilyUpdateData
  ) : FamilyResponseData {
    val before = Family.getReference(guild_id, id.toLong())
    val abilities = AbilityFamily.findAllByGuildIdAndFamilyId(guild_id, before.id)
    val family = before.update(data)
    return FamilyResponseData.from(family, abilities
      .map(AbilityResponseData::from)
    )
  }
  @DeleteMapping("/{id}")
  @Transactional
  fun deleteFamilyByRef(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : FamilyResponseData {
    val family = Family.getReference(guild_id, id.toLong())
    val abilities = AbilityFamily.findAllByGuildIdAndFamilyId(guild_id, family.id)
    return FamilyResponseData.from(family.delete(), abilities
      .map(AbilityResponseData::from)
    )
  }
  @PostMapping("/{id}/abilities/{ability_id}")
  @Transactional
  fun syncAbilityInFamily(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @PathVariable("ability_id") @IdSchema ability_id: String
  ) : FamilyAbilityResponseData {
    val family = Family.getReference(guild_id, id.toLong())
    val ability = Ability.getReference(guild_id, ability_id.toLong())
    AbilityFamily.create(family, ability)
    return FamilyAbilityResponseData.from(family, ability)
  }
  @DeleteMapping("/{id}/abilities/{ability_id}")
  @Transactional
  fun unsyncAbilityInFamily(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @PathVariable("ability_id") @IdSchema ability_id: String
  ) : FamilyAbilityResponseData {
    val family = Family.getReference(guild_id, id.toLong())
    val ability = Ability.getReference(guild_id, ability_id.toLong())
    val ref = AbilityFamily.getReference(family, ability)
    ref.drop()
    return FamilyAbilityResponseData.from(family, ability)
  }
}