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

import morkato.api.database.npc.NpcUpdateData
import morkato.api.database.npc.NpcCreateData
import morkato.api.database.guild.Guild
import morkato.api.database.npc.Npc

import org.jetbrains.exposed.sql.transactions.transaction
import morkato.api.response.data.NpcResponseData
import morkato.api.validation.IdSchema
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
    if ("^[0-9]{15,30}$".toRegex().containsMatchIn(id)) {
      val npc = Npc.getReference(guild_id, id.toLong())
      return NpcResponseData.from(npc)
    }
    val npc = Npc.getReferenceBySurname(guild_id, id)
    return NpcResponseData.from(npc)
  }
  @PostMapping
  @Transactional
  fun createNpcByGuild(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @RequestBody @Valid data: NpcCreateData
  ) : NpcResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val npc = Npc.create(data, guild)
    return NpcResponseData.from(npc)
  }
  @PutMapping("/{id}")
  @Transactional
  fun updateNpcByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: NpcUpdateData
  ) : NpcResponseData {
    val npc = Npc.getReference(guild_id, id.toLong())
    return NpcResponseData.from(npc.update(data))
  }
  @DeleteMapping("/{id}")
  @Transactional
  fun deleteNpcByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
  ) : NpcResponseData {
    val npc = Npc.getReference(guild_id, id.toLong())
    return NpcResponseData.from(npc.delete())
  }
}