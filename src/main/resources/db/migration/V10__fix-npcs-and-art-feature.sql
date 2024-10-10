ALTER TABLE "npcs_abilities"
  DROP CONSTRAINT "npc_ability.family" RESTRICT;
ALTER TABLE "npcs_abilities"
   ADD CONSTRAINT "npc_ability.npc" FOREIGN KEY ("guild_id","npc_id") REFERENCES "npcs"("guild_id","id")
    ON DELETE CASCADE
    ON UPDATE RESTRICT;
ALTER TABLE "arts"
  ADD COLUMN "energy" energy_type
    DEFAULT 25;
ALTER TABLE "arts"
  ADD COLUMN "life" attr_type
    DEFAULT 1;
ALTER TABLE "arts"
  ADD COLUMN "breath" attr_type
    DEFAULT 1;
ALTER TABLE "arts"
  ADD COLUMN "blood" attr_type
    DEFAULT 1;