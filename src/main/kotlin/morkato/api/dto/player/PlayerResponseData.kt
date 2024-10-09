package morkato.api.dto.player

import morkato.api.dto.npc.NpcResponseData
import morkato.api.infra.repository.NpcAbilityRepository
import morkato.api.model.npc.Npc
import morkato.api.model.npc.NpcType
import morkato.api.model.player.Player

data class PlayerResponseData(
  val guild_id: String,
  val id: String,
  val ability_roll: Int,
  val family_roll: Int,
  val is_prodigy: Boolean,
  val has_mark: Boolean,
  val expected_npc_type: NpcType,
  val family_id: String?,
  val abilities: List<String>,
  val families: List<String>,
  val npc: NpcResponseData?
) {
  public constructor(player: Player, abilities: List<String>, families: List<String>, npc: Npc?) : this(
    player.guild.id,
    player.id,
    player.abilityRoll,
    player.familyRoll,
    player.isProdigy,
    player.hasMark,
    player.expectedNpcType,
    player.expectedFamilyId?.toString(),
    abilities,
    families,
    if (npc != null) NpcResponseData(
      npc,
      npc.getAllAbilities()
        .map(NpcAbilityRepository.NpcAbilityPayload::abilityId)
        .map(Long::toString)
        .toList()
    ) else null
  );
}