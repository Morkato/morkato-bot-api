package morkato.api.models.family

import jakarta.validation.constraints.NotNull
import morkato.api.database.npc.NpcType
import morkato.api.infra.validation.AttrSchema
import morkato.api.infra.validation.NameSchema
import morkato.api.infra.validation.DescriptionSchema
import morkato.api.infra.validation.BannerSchema

data class FamilyCreateData(
  @NameSchema @NotNull val name: String,
  @AttrSchema val percent: Int?,
  @NotNull val npc_kind: NpcType,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
) {
}