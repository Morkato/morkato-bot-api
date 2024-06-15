-- Up Migration
CREATE SEQUENCE "shop_snowflake_seq";
CREATE TABLE "shops" (
  "guild_id" VARCHAR(30) NOT NULL,
  "id" BIGINT DEFAULT snowflake_id('shop_snowflake_seq'),
  "location_id" VARCHAR(30) NOT NULL,
  "name" VARCHAR(32) NOT NULL,
  "key" VARCHAR(32) NOT NULL,
  "description" VARCHAR(2048),
  "banner" TEXT
);

CREATE TABLE "shop_items" (
  "guild_id" VARCHAR(30) NOT NULL,
  "shop_id" BIGINT NOT NULL,
  "item_id" BIGINT NOT NULL,
  "value" NUMERIC(12,2) NOT NULL DEFAULT 0,
  "created_at" DATE DEFAULT NOW()
);

ALTER TABLE "shops"
  ADD CONSTRAINT "shop.pkey" PRIMARY KEY ("guild_id", "id");  
ALTER TABLE "shops"
  ADD CONSTRAINT "shop.key" UNIQUE ("guild_id", "key");
ALTER TABLE "shops"
  ADD CONSTRAINT "shop.guild" FOREIGN KEY ("guild_id") REFERENCES "guilds"("id");
ALTER TABLE "shops"
  ADD CONSTRAINT "shop.location" FOREIGN KEY ("guild_id", "location_id") REFERENCES "locations"("guild_id", "id");

ALTER TABLE "shop_items"
  ADD CONSTRAINT "shop_items.pkey" PRIMARY KEY ("guild_id", "shop_id", "item_id");
ALTER TABLE "shop_items"
  ADD CONSTRAINT "shop_items.shop" FOREIGN KEY ("guild_id", "shop_id") REFERENCES "shops"("guild_id", "id");
ALTER TABLE "shop_items"
  ADD CONSTRAINT "shop_items.item" FOREIGN KEY ("guild_id", "item_id") REFERENCES "items"("guild_id", "id");

-- Down Migration
ALTER TABLE "shops"
  DROP CONSTRAINT "shop.location";
ALTER TABLE "shops"
  DROP CONSTRAINT "shop.guild";
ALTER TABLE "shops"
  DROP CONSTRAINT "shop.key";
ALTER TABLE "shop_items"
  DROP CONSTRAINT "shop_items.item";
ALTER TABLE "shop_items"
  DROP CONSTRAINT "shop_items.shop";
DROP TABLE "shop_items";
DROP TABLE "shops";
DROP SEQUENCE "shop_snowflake_seq";