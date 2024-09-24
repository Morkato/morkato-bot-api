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

import morkato.api.database.guild.Guild
import morkato.api.database.art.ArtUpdateData
import morkato.api.database.art.ArtCreateData
import morkato.api.database.attack.Attack
import morkato.api.database.art.Art

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
    val arts = Art.findAllByGuildId(guild_id)
    val attacks = if (arts.isNotEmpty()) Attack.findAllByGuildId(guild_id) else listOf()
    return arts
      .asSequence()
      .map { art ->
        val artAttacks = attacks
          .asSequence()
          .filter { it.artId == art.id }
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
    val art = Art.create(data, guild)
    return ArtResponseData.from(art, listOf<AttackArtResponseData>())
  }
  @GetMapping("/{id}")
  @Transactional
  fun getReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : ArtResponseData {
    val art = Art.getReference(guild_id, id.toLong())
    val attacks = Attack.findAllByGuildIdAndArtId(guild_id, art.id)
    return ArtResponseData.from(art, attacks.map(AttackArtResponseData::from))
  }
  @PutMapping("/{id}")
  @Transactional
  fun updateArtByGuild(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: ArtUpdateData
  ) : ArtResponseData {
    val art = Art.getReference(guild_id, id.toLong())
    val attacks = Attack.findAllByGuildIdAndArtId(guild_id, art.id)
    return ArtResponseData.from(art.update(data), attacks.map(AttackArtResponseData::from))
  }
  @DeleteMapping("/{id}")
  @Transactional
  fun deleteArtByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : ArtResponseData {
    val art = Art.getReference(guild_id, id.toLong());
    val attacks = Attack.findAllByGuildIdAndArtId(guild_id, art.id);
    return ArtResponseData.from(art.delete(), attacks.map(AttackArtResponseData::from));
  }
}
