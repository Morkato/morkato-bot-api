/*
  Warnings:

  - A unique constraint covering the columns `[guild_id,surname]` on the table `players` will be added. If there are existing duplicate values, this will fail.

*/
-- CreateIndex
CREATE UNIQUE INDEX "player_surname" ON "players"("guild_id", "surname");
