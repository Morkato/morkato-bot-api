package morkato.api.model.player

import morkato.api.database.npc.NpcType

data class PlayerPayload(
  val guildId: String,
  val id: String,
  val abilityRoll: Int,
  val familyRoll: Int,
  val isProdigy: Boolean,
  val hasMark: Boolean,
  val expectedFamilyId: Long?,
  val expectedNpcType: NpcType
) {
  fun extend(data: PlayerUpdateData) : PlayerPayload {
    return extend(
      abilityRoll = data.ability_roll,
      familyRoll = data.family_roll,
      isProdigy = data.is_prodigy,
      expectedFamilyId = data.family_id?.toLong(),
      hasMark = data.has_mark
    )
  }
  fun extend(
    abilityRoll: Int?,
    familyRoll: Int?,
    isProdigy: Boolean?,
    hasMark: Boolean?,
    expectedFamilyId: Long?
  ) : PlayerPayload {
    return PlayerPayload(
      guildId, id,
      abilityRoll = abilityRoll ?: this.abilityRoll,
      familyRoll = familyRoll ?: this.familyRoll,
      isProdigy = isProdigy ?: this.isProdigy,
      hasMark = hasMark ?: this.hasMark,
      expectedFamilyId = expectedFamilyId ?: this.expectedFamilyId,
      expectedNpcType = this.expectedNpcType
    )
  }
}