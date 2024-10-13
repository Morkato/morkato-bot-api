-- PRIMARY KEY FOR TABLE "npcs_arts" WILL BE ADDED IN VERSION 12.0!
--  // Flags: Flags do ataque.

--  // 0: Nenhuma
--  // (1 << 1): Prodígio
--  // (1 << 2): Marca
--  // (1 << 3): Berserk Mode

ALTER TABLE "npcs"
  ADD COLUMN "flags" uint_type
    NOT NULL
    DEFAULT 0;
ALTER TABLE "players"
  ADD COLUMN "flags" uint_type
    NOT NULL
    DEFAULT 0;
ALTER TABLE "players"
  ADD COLUMN "prodigy_roll" uint_type
    NOT NULL
    DEFAULT 1;
ALTER TABLE "players"
  ADD COLUMN "mark_roll" uint_type
    NOT NULL
    DEFAULT 1;
ALTER TABLE "players"
  ADD COLUMN "berserk_roll" uint_type
    NOT NULL
    DEFAULT 1;

CREATE TABLE "npcs_arts" (
  "guild_id" discord_id_type NOT NULL,
  "npc_id" id_type NOT NULL,
  "art_id" id_type NOT NULL,
  "exp" attr_type NOT NULL DEFAULT 0
);

ALTER TABLE "npcs_arts"
  ADD CONSTRAINT "npc_art.npc" FOREIGN KEY ("guild_id","npc_id") REFERENCES "npcs"("guild_id","id")
  ON DELETE CASCADE
  ON UPDATE RESTRICT;
ALTER TABLE "npcs_arts"
  ADD CONSTRAINT "npc_art.art" FOREIGN KEY ("guild_id","art_id") REFERENCES "arts"("guild_id","id")
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

ALTER TABLE "players"
  DROP COLUMN "is_prodigy" CASCADE;
ALTER TABLE "players"
  DROP COLUMN "has_mark" CASCADE;
ALTER TABLE "npcs"
  DROP COLUMN "prodigy" CASCADE;
ALTER TABLE "npcs"
  DROP COLUMN "mark" CASCADE;

ALTER DOMAIN "energy_type"
  DROP CONSTRAINT "energy_type_check";
ALTER DOMAIN "energy_type"
  ADD CONSTRAINT "energy_type_check" CHECK(VALUE >= 0 AND VALUE <= 250);