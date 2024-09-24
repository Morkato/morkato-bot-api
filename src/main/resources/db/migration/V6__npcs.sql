-- ALL DATA IN TABLE "npcs" WILL BE DELETED IN VERSION 7.0! FOR ADD NEW CONSTRAINTS.
-- "surname" COLUMN IN TABLE "npcs" THE TYPE WILL BE REPLACED BY NEW DOMAIN "surname_type" (AVAILABLE IN VERSION 7.0).
-- "guild_id" COLUMN IN TABLE "npcs" WILL BE ADDED (AVAILABLE IN VERSION 7.0).
CREATE SEQUENCE "npc_snowflake_seq";
CREATE DOMAIN npc_type AS TEXT CHECK (VALUE ~ 'HUMAN|ONI|HYBRID');
CREATE DOMAIN energy_type AS INTEGER CHECK (VALUE >= 0 AND VALUE <= 100);
CREATE DOMAIN surname_type AS key_type CHECK (VALUE <> 'self' AND VALUE <> 'this');
CREATE TABLE "npcs" (
  "name" name_type NOT NULL,
  "type" npc_type NOT NULL,
  "surname" surname_type NOT NULL,
  "guild_id" discord_id_type NOT NULL,
  "id" id_type NOT NULL DEFAULT snowflake_id('npc_snowflake_seq'),
  "energy" energy_type NOT NULL DEFAULT 100,
  "max_life" attr_type NOT NULL DEFAULT 0,
  "max_breath" attr_type NOT NULL DEFAULT 0,
  "max_blood" attr_type NOT NULL DEFAULT 0,
  "current_life" attr_type NOT NULL DEFAULT 0,
  "current_breath" attr_type NOT NULL DEFAULT 0,
  "current_blood" attr_type NOT NULL DEFAULT 0,
  "icon" banner_type DEFAULT NULL,
  "updated_at" TIMESTAMP DEFAULT NULL
);

ALTER TABLE "npcs"
  ADD CONSTRAINT "npc.pkey" PRIMARY KEY ("guild_id","id");
ALTER TABLE "npcs"
  ADD CONSTRAINT "npc.surname" UNIQUE ("guild_id", "surname");
ALTER TABLE "npcs"
  ADD CONSTRAINT "npc.guild" FOREIGN KEY ("guild_id") REFERENCES "guilds"("id")
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

ALTER TABLE "npcs"
  ADD CONSTRAINT "npc.current_life" CHECK ("max_life" >= "current_life");
ALTER TABLE "npcs"
  ADD CONSTRAINT "npc.current_breath" CHECK ("max_breath" >= "current_breath");
ALTER TABLE "npcs"
  ADD CONSTRAINT "npc.current_blood" CHECK ("max_blood" >= "current_blood");

CREATE INDEX "npc_index_pkey" ON "npcs"("guild_id","id");