package morkato.api.infra.repository

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update

import morkato.api.exception.model.GuildNotFoundError
import morkato.api.infra.tables.guilds

object GuildRepository {
  public data class GuildPayload(
    val id: String,
    val humanInitialLife: Long,
    val oniInitialLife: Long,
    val hybridInitialLife: Long,
    val breathInitial: Long,
    val bloodInitial: Long,
    val familyRoll: Int,
    val abilityRoll: Int,
    val rollCategoryId: String?,
    val offCategoryId: String?
  ) {
    public constructor(row: ResultRow) : this(
      row[guilds.id],
      row[guilds.human_initial_life],
      row[guilds.oni_initial_life],
      row[guilds.hybrid_initial_life],
      row[guilds.breath_initial],
      row[guilds.blood_initial],
      row[guilds.family_roll],
      row[guilds.ability_roll],
      row[guilds.roll_category_id],
      row[guilds.off_category_id]
    ) {}
  }
  private object DefaultValue {
    const val humanInitialLife: Long = 1000
    const val oniInitialLife: Long = 500
    const val hybridInitialLife: Long = 1500
    const val breathInitial: Long = 500
    const val bloodInitial: Long = 1000
    const val familyRoll: Int = 3
    const val abilityRoll: Int = 3
  }
  fun getPayloadWithDefaults(
    id: String,
    humanInitialLife: Long? = null,
    oniInitialLife: Long? = null,
    hybridInitialLife: Long? = null,
    breathInitial: Long? = null,
    bloodInitial: Long? = null,
    familyRoll: Int? = null,
    abilityRoll: Int? = null,
    rollCategoryId: String? = null,
    offCategoryId: String? = null
  ) : GuildPayload {
    return GuildPayload(
      id = id,
      humanInitialLife = humanInitialLife ?: DefaultValue.humanInitialLife,
      oniInitialLife = oniInitialLife ?: DefaultValue.oniInitialLife,
      hybridInitialLife = hybridInitialLife ?: DefaultValue.hybridInitialLife,
      breathInitial = breathInitial ?: DefaultValue.breathInitial,
      bloodInitial = bloodInitial ?: DefaultValue.bloodInitial,
      familyRoll = familyRoll ?: DefaultValue.familyRoll,
      abilityRoll = abilityRoll ?: DefaultValue.abilityRoll,
      rollCategoryId = rollCategoryId,
      offCategoryId = offCategoryId
    )
  }

  fun findById(id: String) : GuildPayload {
    return try {
      GuildPayload(
        guilds
          .selectAll()
          .where({ guilds.id eq id })
          .limit(1)
          .single()
      )
    } catch (exc: NoSuchElementException) {
      val extra: Map<String, Any?> = mapOf("id" to id)
      throw GuildNotFoundError(extra)
    }
  }
  fun createGuild(
    id: String,
    humanInitialLife: Long? = null,
    oniInitialLife: Long? = null,
    hybridInitialLife: Long? = null,
    breathInitial: Long? = null,
    bloodInitial: Long? = null,
    familyRoll: Int? = null,
    abilityRoll: Int? = null,
    rollCategoryId: String? = null,
    offCategoryId: String? = null
  ) : GuildPayload {
    guilds.insert {
      it[this.id] = id
      it[this.roll_category_id] = rollCategoryId
      it[this.off_category_id] = offCategoryId
      if (humanInitialLife != null) {
        it[this.human_initial_life] = humanInitialLife
      }
      if (oniInitialLife != null) {
        it[this.oni_initial_life] = oniInitialLife
      }
      if (hybridInitialLife != null) {
        it[this.hybrid_initial_life] = hybridInitialLife
      }
      if (breathInitial != null) {
        it[this.breath_initial] = breathInitial
      }
      if (bloodInitial != null) {
        it[this.blood_initial] = bloodInitial
      }
      if (familyRoll != null) {
        it[this.family_roll] = familyRoll
      }
      if (abilityRoll != null) {
        it[this.ability_roll] = abilityRoll
      }
    }
    return this.getPayloadWithDefaults(
      id = id,
      humanInitialLife = humanInitialLife,
      oniInitialLife = oniInitialLife,
      hybridInitialLife = hybridInitialLife,
      breathInitial = breathInitial,
      bloodInitial = bloodInitial,
      familyRoll = familyRoll,
      abilityRoll = abilityRoll,
      rollCategoryId = rollCategoryId,
      offCategoryId = offCategoryId
    )
  }
  fun findByIdOrCreate(id: String) : GuildPayload {
    return try {
      this.findById(id)
    } catch (exc: GuildNotFoundError) {
      this.createGuild(id)
    }
  }
  fun updateGuild(
    id: String,
    humanInitialLife: Long?,
    oniInitialLife: Long?,
    hybridInitialLife: Long?,
    breathInitial: Long?,
    bloodInitial: Long?,
    familyRoll: Int?,
    abilityRoll: Int?,
    rollCategoryId: String?,
    offCategoryId: String?
  ) {
    guilds.update({
      guilds.id eq id
    }) {
      if (humanInitialLife != null) {
        it[this.human_initial_life] = humanInitialLife
      }
      if (oniInitialLife != null) {
        it[this.oni_initial_life] = oniInitialLife
      }
      if (hybridInitialLife != null) {
        it[this.hybrid_initial_life] = hybridInitialLife
      }
      if (breathInitial != null) {
        it[this.breath_initial] = breathInitial
      }
      if (bloodInitial != null) {
        it[this.blood_initial] = bloodInitial
      }
      if (familyRoll != null) {
        it[this.family_roll] = familyRoll
      }
      if (abilityRoll != null) {
        it[this.ability_roll] = abilityRoll
      }
      if (rollCategoryId != null) {
        it[this.roll_category_id] = rollCategoryId
      }
      if (offCategoryId != null) {
        it[this.off_category_id] = offCategoryId
      }
    }
  }
}