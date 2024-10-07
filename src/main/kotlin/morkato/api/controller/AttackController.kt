package morkato.api.controller

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.GetMapping

import morkato.api.exception.model.AttackNotFoundError
import morkato.api.exception.model.GuildNotFoundError
import morkato.api.exception.model.ArtNotFoundError
import morkato.api.infra.repository.GuildRepository
import morkato.api.model.guild.Guild
import morkato.api.dto.attack.AttackResponseData
import morkato.api.dto.attack.AttackCreateData
import morkato.api.dto.attack.AttackUpdateData
import morkato.api.dto.validation.IdSchema
import jakarta.validation.Valid

@RestController
@RequestMapping("/attacks/{guild_id}")
class AttackController {
  @GetMapping
  @Transactional
  fun getAllByGuildId(
    @PathVariable("guild_id") @IdSchema guild_id: String
  ) : List<AttackResponseData> {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      guild.getAllAttacks()
        .map(::AttackResponseData)
        .toList()
    } catch (exc: GuildNotFoundError) {
      return listOf()
    }
  }
  @GetMapping("/{id}")
  @Transactional
  fun getReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : AttackResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val attack = guild.getAttack(id.toLong())
      AttackResponseData(attack)
    } catch (exc: GuildNotFoundError) {
      val extra: MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guild_id
      extra["id"] = id
      throw AttackNotFoundError(extra)
    }
  }
  @PostMapping("/{art_id}")
  @Transactional
  fun createAttackByArt(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("art_id") @IdSchema art_id: String,
    @RequestBody @Valid data: AttackCreateData
  ) : AttackResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val art = guild.getArt(art_id.toLong())
      val attack = art.createAttack(
        name = data.name,
        namePrefixArt = data.name_prefix_art,
        description = data.description,
        banner = data.banner,
        damage = data.damage,
        breath = data.breath,
        blood = data.blood,
        intents = data.intents
      )
      AttackResponseData(attack)
    } catch (exc: GuildNotFoundError) {
      val extra: MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guild_id
      extra["id"] = art_id
      throw ArtNotFoundError(extra)
    }
  }
  @PutMapping("/{id}")
  @Transactional
  fun updateAttackById(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: AttackUpdateData
  ) : AttackResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val before = guild.getAttack(id.toLong())
      val attack = before.update(
        name = data.name,
        namePrefixArt = data.name_prefix_art,
        description = data.description,
        banner = data.banner,
        damage = data.damage,
        breath = data.breath,
        blood = data.blood,
        intents = data.intents
      )
      AttackResponseData(attack)
    } catch (exc: GuildNotFoundError) {
      val extra: MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guild_id
      extra["id"] = id
      throw AttackNotFoundError(extra)
    }
  }
  @DeleteMapping("/{id}")
  @Transactional
  fun deleteByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : AttackResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val attack = guild.getAttack(id.toLong())
      attack.delete()
      AttackResponseData(attack)
    } catch (exc: GuildNotFoundError) {
      val extra: MutableMap<String, Any?> = mutableMapOf()
      extra["guild_id"] = guild_id
      extra["id"] = id
      throw AttackNotFoundError(extra)
    }
  }
}
