package morkato.api.controller

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.GetMapping
import jakarta.validation.Valid

import morkato.api.exception.model.GuildNotFoundError
import morkato.api.dto.guild.GuildResponseData
import morkato.api.dto.guild.GuildUpdateData
import morkato.api.model.guild.Guild

import morkato.api.infra.repository.GuildRepository
import morkato.api.dto.validation.IdSchema

@RestController
@RequestMapping("/guilds/{id}")
class GuildController {
  @GetMapping
  @Transactional
  fun getGuildByReference(
    @PathVariable("id") @IdSchema id: String
  ) : GuildResponseData {
    return try {
      val payload = GuildRepository.findById(id)
      val guild = Guild(payload)
      GuildResponseData(guild)
    } catch (exc: GuildNotFoundError) {
      val payload = GuildRepository.getPayloadWithDefaults(id)
      val guild = Guild(payload)
      GuildResponseData(guild)
    }
  }
  @PutMapping
  @Transactional
  fun updateGuildByReference(
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: GuildUpdateData
  ) : GuildResponseData {
    return try {
      this.updateGuildWithReference(id, data)
    } catch (exc: GuildNotFoundError) {
      this.updateGuildWithUnknownReference(id, data)
    }
  }

  fun updateGuildWithReference(id: String, data: GuildUpdateData) : GuildResponseData {
    val payload = GuildRepository.findById(id)
    val beforeGuild = Guild(payload)
    val guild = beforeGuild.update(
      humanInitialLife = data.human_initial_life,
      oniInitialLife = data.oni_initial_life,
      hybridInitialLife = data.hybrid_initial_life,
      breathInitial = data.breath_initial,
      bloodInitial = data.blood_initial,
      familyRoll = data.family_roll,
      abilityRoll = data.ability_roll,
      rollCategoryId = data.roll_category_id,
      offCategoryId = data.off_category_id
    )
    return GuildResponseData(guild)
  }

  fun updateGuildWithUnknownReference(id: String, data: GuildUpdateData) : GuildResponseData {
    val payload = GuildRepository.createGuild(
      id = id,
      humanInitialLife = data.human_initial_life,
      oniInitialLife = data.oni_initial_life,
      hybridInitialLife = data.hybrid_initial_life,
      breathInitial = data.breath_initial,
      bloodInitial = data.blood_initial,
      familyRoll = data.family_roll,
      abilityRoll = data.ability_roll,
      rollCategoryId = data.roll_category_id,
      offCategoryId = data.off_category_id
    )
    val guild = Guild(payload)
    return GuildResponseData(guild)
  }
}