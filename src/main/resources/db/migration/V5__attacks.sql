-- V4 CREATE TABLE ATTACKS
--  // Intents: Inteções do ataque.

--  // 0: Nenhuma
--  // (1 << 2): Indesviável
--  // (1 << 3): Indefensável
--  // (1 << 4): Em área
--  // (1 << 5): Não contra atacável
--  // (1 << 6): Contra Ataque
--  // (1 << 7): Usável para defesa

CREATE SEQUENCE "attack_snowflake_seq";
CREATE TABLE "attacks" (
  "name" name_type NOT NULL,
  "key" key_type NOT NULL,
  "id" id_type NOT NULL DEFAULT snowflake_id('attack_snowflake_seq'),
  "guild_id" discord_id_type NOT NULL,
  "art_id" id_type NOT NULL,
  "name_prefix_art" name_prefix_art_type DEFAULT NULL,
  "description" description_type DEFAULT NULL,
  "resume_description" resume_description_type DEFAULT NULL,
  "banner" banner_type DEFAULT NULL,
  "damage" attr_type DEFAULT 1,
  "breath" attr_type DEFAULT 1,
  "blood" attr_type DEFAULT 1,
  "intents" INTEGER NOT NULL DEFAULT 0,
  "updated_at" TIMESTAMP DEFAULT NULL
);
ALTER TABLE "attacks"
  ADD CONSTRAINT "attack.pkey" PRIMARY KEY ("guild_id","id");
ALTER TABLE "attacks"
  ADD CONSTRAINT "attack.key" UNIQUE ("art_id","key");
ALTER TABLE "attacks"
  ADD CONSTRAINT "attack.art" FOREIGN KEY ("guild_id","art_id") REFERENCES "arts"("guild_id","id")
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

CREATE INDEX "attack_index_pkey" ON "attacks"("guild_id","id");