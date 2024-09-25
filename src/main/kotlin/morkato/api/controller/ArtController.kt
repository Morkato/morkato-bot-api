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

import morkato.api.response.data.AttackArtResponseData
import morkato.api.response.data.ArtResponseData

import morkato.api.database.art.ArtUpdateData
import morkato.api.database.art.ArtCreateData
import morkato.api.database.guild.Guild

import morkato.api.validation.IdSchema
import jakarta.validation.Valid

@RestController
@RequestMapping("/arts/{guild_id}")
class ArtController {
  @GetMapping
  @Transactional
  fun findAllByGuildId(
    @PathVariable("guild_id") @IdSchema guild_id: String
  ) : List<ArtResponseData> {
    val guild = Guild.getReference(guild_id)
    val arts = guild.getAllArts()
    val attacks = if (arts.isNotEmpty()) guild.getAllAttacks() else listOf()
    return arts
      .asSequence()
      .map { art ->
        val artAttacks = attacks
          .asSequence()
          .filter { it.payload.artId == art.payload.id }
          .map(AttackArtResponseData::from)
          .toList()
        return@map ArtResponseData.from(art, artAttacks)
      }
      .toList()
  }
  @PostMapping
  @Transactional
  fun createArtByGuild(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @RequestBody @Valid data: ArtCreateData
  ) : ArtResponseData {
    val guild = Guild.getRefOrCreate(guild_id)
    val art = guild.createArt(data)
    return ArtResponseData.from(art, listOf<AttackArtResponseData>())
  }
  @GetMapping("/{id}")
  @Transactional
  fun getReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : ArtResponseData {
    val guild = Guild.getReference(guild_id)
    val art = guild.getArt(id.toLong())
    val attacks = art.getAllAttacks()
    return ArtResponseData.from(art, attacks.map(AttackArtResponseData::from))
  }
  @PutMapping("/{id}")
  @Transactional
  fun updateArtByGuild(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: ArtUpdateData
  ) : ArtResponseData {
    val guild = Guild.getReference(guild_id)
    val art = guild.getArt(id.toLong())
    val after = art.update(data)
    val attacks = after.getAllAttacks()
    return ArtResponseData.from(after, attacks.map(AttackArtResponseData::from))
  }
  @DeleteMapping("/{id}")
  @Transactional
  fun deleteArtByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : ArtResponseData {
    val guild = Guild.getReference(guild_id)
    val art = guild.getArt(id.toLong())
    val attacks = art.getAllAttacks()
    return ArtResponseData.from(art.delete(), attacks.map(AttackArtResponseData::from))
  }
}
