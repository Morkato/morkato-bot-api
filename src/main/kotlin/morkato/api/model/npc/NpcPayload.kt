package morkato.api.model.npc

data class NpcPayload(
  val guildId: String,
  val id: Long,
  val name: String,
  val type: NpcType,
  val familyId: Long,
  val surname: String,
  val energy: Int,
  val maxLife: Long,
  val maxBreath: Long,
  val maxBlood: Long,
  val currentLife: Long,
  val currentBreath: Long,
  val currentBlood: Long,
  val icon: String?
) {
  fun extend(data: NpcUpdateData) : NpcPayload {
    return extend(
      name = data.name,
      surname = data.surname,
      type = data.type,
      energy = data.energy,
      maxLife = data.max_life,
      maxBreath = data.max_breath,
      maxBlood = data.max_blood,
      currentLife = data.current_life,
      currentBreath = data.max_breath,
      currentBlood = data.max_blood,
      icon = data.icon
    )
  }
  fun extend(
    name: String?,
    surname: String?,
    type: NpcType?,
    energy: Int?,
    maxLife: Long?,
    maxBreath: Long?,
    maxBlood: Long?,
    currentLife: Long?,
    currentBreath: Long?,
    currentBlood: Long?,
    icon: String?
  ) : NpcPayload {
    return NpcPayload(
      guildId, id,
      familyId = familyId,
      name = name ?: this.name,
      surname =  surname ?: this.surname,
      type = type ?: this.type,
      energy = energy ?: this.energy,
      maxLife = maxLife ?: this.maxLife,
      maxBreath = maxBreath ?: this.maxBreath,
      maxBlood = maxBlood ?: this.maxBlood,
      currentLife = currentLife ?: this.currentLife,
      currentBreath = currentBreath ?: this.currentBreath,
      currentBlood = currentBlood ?: this.currentBlood,
      icon = icon ?: this.icon
    )
  }
}
