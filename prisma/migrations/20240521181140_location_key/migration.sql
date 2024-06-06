/*
  Warnings:

  - A unique constraint covering the columns `[guild_id,key]` on the table `locations` will be added. If there are existing duplicate values, this will fail.
  - Added the required column `key` to the `locations` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE "locations" ADD COLUMN     "key" TEXT NOT NULL;

-- CreateIndex
CREATE UNIQUE INDEX "locations_key" ON "locations"("guild_id", "key");
