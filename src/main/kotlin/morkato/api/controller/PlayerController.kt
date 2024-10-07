package morkato.api.controller

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.GetMapping
import jakarta.validation.Valid

import morkato.api.response.data.PlayerResponseData
import morkato.api.database.player.PlayerCreateData
import morkato.api.database.player.PlayerNpcCreateData
import morkato.api.database.player.PlayerUpdateData
import morkato.api.database.guild.Guild
import morkato.api.response.data.PlayerAbilityResponseData
import morkato.api.response.data.PlayerFamilyResponseData
import morkato.api.dto.validation.IdSchema

@RestController
@RequestMapping("/players/{guild_id}/{id}")
class PlayerController {
  @GetMapping
  @Transactional
  fun getPlayerByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : PlayerResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val player = guild.getPlayer(id)
    return PlayerResponseData.from(player)
  }
  @PostMapping
  @Transactional
  fun createPlayerByGuild(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: PlayerCreateData
  ) : PlayerResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val player = guild.createPlayer(data, id)
    return PlayerResponseData.from(player)
  }
  @PostMapping("/npc")
  @Transactional
  fun createPlayerNpcByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: PlayerNpcCreateData
  ) : PlayerResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val player = guild.getPlayer(id)
    val npc = player.createNpc(data)
    return PlayerResponseData.from(player, npc)
  }
  @PutMapping
  @Transactional
  fun updatePlayerByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: PlayerUpdateData
  ) : PlayerResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val before = guild.getPlayer(id)
    val player = before.update(data)
    return PlayerResponseData.from(player)
  }
  @DeleteMapping
  @Transactional
  fun deletePlayerByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : PlayerResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val player = guild.getPlayer(id)
    val response = PlayerResponseData.from(player)
    player.delete()
    return response
  }
  @PostMapping("/abilities/{ability_id}")
  @Transactional
  fun syncPlayerAbility(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @PathVariable("ability_id") @IdSchema ability_id: String
  ) : PlayerAbilityResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val player = guild.getPlayer(id)
    return PlayerAbilityResponseData.from(player.addAbility(ability_id.toLong()))
  }
  @PostMapping("/families/{family_id}")
  @Transactional
  fun syncPlayerFamily(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @PathVariable("family_id") @IdSchema family_id: String
  ) : PlayerFamilyResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val player = guild.getPlayer(id)
    return PlayerFamilyResponseData.from(player.addFamily(family_id.toLong()))
  }
}
