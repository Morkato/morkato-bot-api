/*
  Warnings:

  - A unique constraint covering the columns `[guild_id,art_id,key]` on the table `attacks` will be added. If there are existing duplicate values, this will fail.

*/
-- DropIndex
DROP INDEX "attack_key";

-- CreateIndex
CREATE UNIQUE INDEX "attack_key" ON "attacks"("guild_id", "art_id", "key");
