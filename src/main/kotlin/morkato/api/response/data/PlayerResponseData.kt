package morkato.api.response.data

import morkato.api.database.foreign.AbilityNpc
import morkato.api.database.foreign.PlayerAbility
import morkato.api.database.foreign.PlayerFamily
import morkato.api.database.player.Player
import morkato.api.database.npc.NpcType
import morkato.api.database.npc.Npc

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
  companion object {
    fun from(player: Player, cachedNpc: Npc? = null) : PlayerResponseData {
      val payload = player.payload
      val npc: Npc? = cachedNpc ?: player.getReferredNpc()
      val abilities = player.getAllAbilities()
      val families = player.getAllFamilies()
      val npcAbilities
        = if (npc != null) npc
          .getAllAbilities()
          .asSequence()
          .map(AbilityNpc::abilityId)
          .map(Long::toString)
          .toList()
        else listOf<String>()
      return PlayerResponseData(
        guild_id = player.guild.id,
        id = payload.id,
        ability_roll = payload.abilityRoll,
        family_roll = payload.familyRoll,
        is_prodigy = payload.isProdigy,
        has_mark = payload.hasMark,
        expected_npc_type = payload.expectedNpcType,
        family_id = payload.expectedFamilyId?.toString(),
        abilities = abilities
          .asSequence()
          .map(PlayerAbility::abilityId)
          .map(Long::toString)
          .toList(),
        families = families
          .asSequence()
          .map(PlayerFamily::familyId)
          .map(Long::toString)
          .toList(),
        npc = if (npc != null) NpcResponseData.from(npc, npcAbilities) else null
      )
    }
  }
}
