/*
  Warnings:

  - Made the column `surname` on table `players` required. This step will fail if there are existing NULL values in that column.

*/
-- AlterTable
ALTER TABLE "players" ALTER COLUMN "surname" SET NOT NULL;
