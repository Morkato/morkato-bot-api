-- Up Migration
CREATE TABLE "arts" (
  "name" VARCHAR(32) NOT NULL,
  "key" VARCHAR(32) NOT NULL,
  "guild_id" VARCHAR(30) NOT NULL,
  "id" BIGINT NOT NULL,
  "type" ArtType NOT NULL,
  "description" VARCHAR(2048),
  "banner" text,
  "updated_at" DATE DEFAULT NULL
);
ALTER TABLE "arts"
  ADD CONSTRAINT "art.guild" FOREIGN KEY ("guild_id") REFERENCES guilds("id") ON DELETE CASCADE;
ALTER TABLE "arts"
  ADD CONSTRAINT "art.pkey" PRIMARY KEY ("guild_id", "id");
ALTER TABLE "arts"
  ADD CONSTRAINT "art.key" UNIQUE ("guild_id", "key");
CREATE INDEX "arts_guild_id_id_index" ON "arts" ("guild_id", "id");
-- Down Migration
DROP INDEX "arts_guild_id_id_index";
ALTER TABLE "arts" DROP CONSTRAINT "art.key";
ALTER TABLE "arts" DROP CONSTRAINT "art.pkey";
ALTER TABLE "arts" DROP CONSTRAINT "art.guild";
DROP TABLE "arts";