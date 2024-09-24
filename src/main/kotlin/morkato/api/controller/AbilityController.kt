package morkato.api.controller

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import jakarta.validation.Valid
import org.jetbrains.exposed.sql.transactions.transaction
import morkato.api.response.data.AbilityResponseData
import morkato.api.database.guild.Guild
import morkato.api.database.ability.Ability
import morkato.api.database.ability.AbilityUpdateData
import morkato.api.database.ability.AbilityCreateData
import morkato.api.validation.IdSchema
import org.springframework.web.bind.annotation.DeleteMapping

@RestController
@RequestMapping("/abilities/{guild_id}")
class AbilityController {
  @GetMapping
  @Transactional
  fun getAllByGuildId(
    @PathVariable("guild_id") @IdSchema guild_id: String
  ) : List<AbilityResponseData>{
    val abilities = Ability.findAllByGuildId(guild_id)
    return abilities
      .map(AbilityResponseData::from)
  }
  @PostMapping
  @Transactional
  fun createAbilityByGuildId(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @RequestBody @Valid data: AbilityCreateData
  ) : AbilityResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val ability = Ability.create(data, guild)
    return AbilityResponseData
      .from(ability)
  }
  @GetMapping("/{id}")
  @Transactional
  fun getReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : AbilityResponseData {
    val ability = Ability.getReference(guild_id, id.toLong())
    return AbilityResponseData
      .from(ability)
  }
  @PutMapping("/{id}")
  @Transactional
  fun updateAbilityByRef(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: AbilityUpdateData
  ) : AbilityResponseData {
    val before = Ability.getReference(guild_id, id.toLong())
    val ability = before.update(data)
    return AbilityResponseData
      .from(ability)
  }
  @DeleteMapping("/{id}")
  @Transactional
  fun deleteAbilityByRef(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : AbilityResponseData {
    val ability = Ability.getReference(guild_id, id.toLong())
    ability.delete()
    return AbilityResponseData
      .from(ability)
  }
}