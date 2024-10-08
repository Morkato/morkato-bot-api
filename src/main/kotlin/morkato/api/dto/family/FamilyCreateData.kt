package morkato.api.dto.family

import jakarta.validation.constraints.NotNull
import morkato.api.dto.validation.DescriptionSchema
import morkato.api.dto.validation.BannerSchema
import morkato.api.dto.validation.AttrSchema
import morkato.api.dto.validation.NameSchema
import morkato.api.model.npc.NpcType

data class FamilyCreateData(
  @NameSchema @NotNull val name: String,
  @AttrSchema val percent: Int?,
  @NotNull val npc_kind: NpcType,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
);
