-- Up Migration
CREATE TABLE "attacks" (
  "name" VARCHAR(32) NOT NULL,
  "key" VARCHAR(32) NOT NULL,
  "guild_id" VARCHAR(30) NOT NULL,
  "id" BIGINT NOT NULL,
  "art_id" BIGINT NOT NULL,
  "title" VARCHAR(96),
  "name_prefix_art" VARCHAR(32),
  "description" VARCHAR(2048),
  "resume_description" VARCHAR(128),
  "banner" TEXT,
  "damage" INTEGER NOT NULL DEFAULT 1,
  "breath" INTEGER NOT NULL DEFAULT 1,
  "blood" INTEGER NOT NULL DEFAULT 1,
  "intents" INTEGER NOT NULL DEFAULT 0,
  "updated_at" DATE DEFAULT NULL
);
ALTER TABLE "attacks"
  ADD CONSTRAINT "attack.guild" FOREIGN KEY ("guild_id") REFERENCES guilds("id") ON DELETE CASCADE;
ALTER TABLE "attacks"
  ADD CONSTRAINT "attack.art" FOREIGN KEY ("guild_id", "art_id") REFERENCES arts("guild_id","id") ON DELETE CASCADE;
ALTER TABLE "attacks"
  ADD CONSTRAINT "attack.pkey" PRIMARY KEY ("guild_id", "id");
ALTER TABLE "attacks"
  ADD CONSTRAINT "attack.key" UNIQUE ("guild_id", "art_id", "key");
CREATE INDEX "attacks_guild_id_id_index" ON "attacks" ("guild_id", "id");
-- Down Migration
DROP INDEX "attacks_guild_id_id_index";
ALTER TABLE "attacks" DROP CONSTRAINT "attack.key";
ALTER TABLE "attacks" DROP CONSTRAINT "attack.pkey";
ALTER TABLE "attacks" DROP CONSTRAINT "attack.art";
ALTER TABLE "attacks" DROP CONSTRAINT "attack.guild";
DROP TABLE "attacks";