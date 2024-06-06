-- Up Migration
CREATE TABLE "locations" (
  "guild_id" VARCHAR(30) NOT NULL,
  "id" VARCHAR(30) NOT NULL,
  "key" VARCHAR(32) NOT NULL,
  "name" VARCHAR(32) NOT NULL,
  "location_id" VARCHAR(30) DEFAULT NULL,
  "description" VARCHAR(2048) DEFAULT NULL,
  "resume_description" VARCHAR(128) DEFAULT NULL,
  "access" INTEGER DEFAULT 0,
  "banner" TEXT DEFAULT NULL,
  "updated_at" DATE DEFAULT NULL
);
ALTER TABLE "locations"
  ADD CONSTRAINT "local.pkey" PRIMARY KEY ("guild_id", "id");
ALTER TABLE "locations"
  ADD CONSTRAINT "local.key" UNIQUE ("guild_id", "key");
ALTER TABLE "locations"
  ADD CONSTRAINT "local.guild" FOREIGN KEY ("guild_id") REFERENCES guilds("id") ON DELETE CASCADE;
ALTER TABLE "locations"
  ADD CONSTRAINT "local.parent" FOREIGN KEY ("guild_id", "location_id") REFERENCES locations("guild_id","id") ON DELETE RESTRICT;
CREATE INDEX "locations_guild_id_id" ON "locations" ("guild_id", "id");
CREATE FUNCTION update_location_id() RETURNS TRIGGER AS $$
BEGIN
  UPDATE "locations" SET location_id = NULL
  WHERE guild_id = OLD.guild_id
    AND location_id = OLD.id;
  RETURN OLD;
END;
$$ LANGUAGE plpgsql; 
CREATE TRIGGER "LocationDelete" BEFORE DELETE ON "locations"
FOR EACH ROW
EXECUTE FUNCTION update_location_id();
-- Down Migration
DROP INDEX "locations_guild_id_id";
DROP TRIGGER "LocationDelete" ON "locations";
DROP FUNCTION update_location_id();
ALTER TABLE "locations" DROP CONSTRAINT "local.guild";
ALTER TABLE "locations" DROP CONSTRAINT "local.key";
DROP TABLE "locations";