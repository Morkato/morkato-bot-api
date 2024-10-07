package morkato.api.controller

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.GetMapping

import morkato.api.database.foreign.AbilityNpc
import morkato.api.database.npc.NpcUpdateData
import morkato.api.database.npc.NpcCreateData
import morkato.api.database.guild.Guild

import morkato.api.response.data.NpcResponseData
import morkato.api.dto.validation.IdSchema
import jakarta.validation.Valid

@RestController
@RequestMapping("/npcs/{guild_id}")
class NpcController {
  @GetMapping("/{id}")
  @Transactional
  fun getReferenceByIdOrSurname(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") id: String
  ) : NpcResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val npc = if ("^[0-9]{15,30}$".toRegex().containsMatchIn(id))
      guild.getNpc(id.toLong())
    else guild.getNpcBySurname(id)
    val abilities = npc.getAllAbilities()
      .asSequence()
      .map(AbilityNpc::abilityId)
      .map(Long::toString)
      .toList()
    return NpcResponseData.from(npc, abilities)
  }
  @PostMapping
  @Transactional
  fun createNpcByGuild(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @RequestBody @Valid data: NpcCreateData
  ) : NpcResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val npc = guild.createNpc(data)
    return NpcResponseData.from(npc, listOf<String>())
  }
  @PutMapping("/{id}")
  @Transactional
  fun updateNpcByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: NpcUpdateData
  ) : NpcResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val before = guild.getNpc(id.toLong())
    val npc = before.update(data)
    val abilities = npc.getAllAbilities()
      .asSequence()
      .map(AbilityNpc::abilityId)
      .map(Long::toString)
      .toList()
    return NpcResponseData.from(npc, abilities)
  }
  @DeleteMapping("/{id}")
  @Transactional
  fun deleteNpcByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
  ) : NpcResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val npc = guild.getNpc(id.toLong())
    val abilities = npc.getAllAbilities()
      .asSequence()
      .map(AbilityNpc::abilityId)
      .map(Long::toString)
      .toList()
    npc.delete()
    return NpcResponseData.from(npc, abilities)
  }
}