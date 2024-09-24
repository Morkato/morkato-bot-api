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

import morkato.api.database.attack.AttackUpdateData
import morkato.api.database.attack.AttackCreateData
import morkato.api.database.attack.Attack
import morkato.api.database.guild.Guild
import morkato.api.database.art.Art

import org.jetbrains.exposed.sql.transactions.transaction
import morkato.api.response.data.AttackResponseData
import morkato.api.validation.IdSchema
import jakarta.validation.Valid

@RestController
@RequestMapping("/attacks/{guild_id}")
class AttackController {
  @GetMapping
  @Transactional
  fun getAllByGuildId(
    @PathVariable("guild_id") @IdSchema guild_id: String
  ) : List<AttackResponseData> {
    return Attack.findAllByGuildId(guild_id)
      .map(AttackResponseData::from)
  }
  @GetMapping("/{id}")
  @Transactional
  fun getReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : AttackResponseData {
    val attack = Attack.getReference(guild_id, id.toLong())
    return AttackResponseData.from(attack)
  }
  @PostMapping("/{art_id}")
  @Transactional
  fun createAttackByArt(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("art_id") @IdSchema art_id: String,
    @RequestBody @Valid data: AttackCreateData
  ) : AttackResponseData {
    val guild = Guild.getReference(guild_id)
    val art = Art.getReference(guild.id, art_id.toLong())
    val attack = Attack.create(data, art)
    return AttackResponseData.from(attack)
  }
  @PutMapping("/{id}")
  @Transactional
  fun updateAttackById(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: AttackUpdateData
  ) : AttackResponseData {
    val attack = Attack.getReference(guild_id, id.toLong())
    return AttackResponseData.from(attack.update(data))
  }
  @DeleteMapping("/{id}")
  @Transactional
  fun deleteByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : AttackResponseData {
    val attack = Attack.getReference(guild_id, id.toLong())
    return AttackResponseData.from(attack.delete())
  }
}
