-- Up Migration
CREATE TABLE "items" (
  "name" VARCHAR(32) NOT NULL,
  "key" VARCHAR(32) NOT NULL,
  "guild_id" VARCHAR(30) NOT NULL,
  "art_id" BIGINT DEFAULT NULL,
  "id" BIGINT NOT NULL,
  "description" VARCHAR(2048),
  "banner" TEXT DEFAULT NULL,
  "stack" INTEGER DEFAULT 1,
  "usable" BOOLEAN DEFAULT false,
  "equip" INTEGER DEFAULT 0,
  "updated_at" DATE DEFAULT NULL
);
ALTER TABLE "items"
  ADD CONSTRAINT "item.pkey" PRIMARY KEY ("guild_id", "id");
ALTER TABLE "items"
  ADD CONSTRAINT "item.key" UNIQUE ("guild_id", "key");
ALTER TABLE "items"
  ADD CONSTRAINT "item.guild" FOREIGN KEY ("guild_id") REFERENCES guilds("id") ON DELETE CASCADE;
ALTER TABLE "items"
  ADD CONSTRAINT "item.art" FOREIGN KEY ("guild_id", "art_id") REFERENCES arts("guild_id","id") ON DELETE RESTRICT;
CREATE INDEX "items_guild_id_id_index" ON "items" ("guild_id", "id");
CREATE FUNCTION update_art_ids_item() RETURNS TRIGGER AS $$
BEGIN
  UPDATE "items" SET art_id = NULL
  WHERE guild_id = OLD.guild_id
    AND art_id = OLD.id;
  RETURN OLD;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER "ArtDeleteItem" BEFORE DELETE ON "arts"
FOR EACH ROW
EXECUTE FUNCTION update_art_ids_item();
-- Down Migration
DROP TRIGGER "ArtDeleteItem" ON "arts";
DROP FUNCTION update_art_ids_item();
DROP INDEX "items_guild_id_id_index";
ALTER TABLE "items" DROP CONSTRAINT "item.guild";
ALTER TABLE "items" DROP CONSTRAINT "item.key";
ALTER TABLE "items" DROP CONSTRAINT "item.pkey";
DROP TABLE "items";
