/*
  Warnings:

  - You are about to drop the column `players_count` on the `guilds` table. All the data in the column will be lost.

*/
-- AlterTable
ALTER TABLE "guilds" DROP COLUMN "players_count",
ADD COLUMN     "default_player_blood" INTEGER NOT NULL DEFAULT 500,
ADD COLUMN     "default_player_breath" INTEGER NOT NULL DEFAULT 500,
ADD COLUMN     "default_player_force" INTEGER NOT NULL DEFAULT 0,
ADD COLUMN     "default_player_life" INTEGER NOT NULL DEFAULT 100,
ADD COLUMN     "default_player_resistance" INTEGER NOT NULL DEFAULT 0,
ADD COLUMN     "default_player_velocity" INTEGER NOT NULL DEFAULT 1;

-- CreateTable
CREATE TABLE "locations" (
    "guild_id" TEXT NOT NULL,
    "id" TEXT NOT NULL,
    "name" VARCHAR(32) NOT NULL,
    "category_id" TEXT NOT NULL,
    "location_id" TEXT,
    "description" TEXT,
    "resumed_description" TEXT,
    "access" SMALLINT NOT NULL,

    CONSTRAINT "locations_pkey" PRIMARY KEY ("guild_id","id")
);

-- AddForeignKey
ALTER TABLE "locations" ADD CONSTRAINT "locations_guild_id_fkey" FOREIGN KEY ("guild_id") REFERENCES "guilds"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "locations" ADD CONSTRAINT "locations_guild_id_location_id_fkey" FOREIGN KEY ("guild_id", "location_id") REFERENCES "locations"("guild_id", "id") ON DELETE RESTRICT ON UPDATE CASCADE;
