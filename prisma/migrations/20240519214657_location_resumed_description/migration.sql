/*
  Warnings:

  - You are about to drop the column `resumed_description` on the `locations` table. All the data in the column will be lost.
  - You are about to alter the column `description` on the `locations` table. The data in that column could be lost. The data in that column will be cast from `Text` to `VarChar(2048)`.

*/
-- AlterTable
ALTER TABLE "locations" DROP COLUMN "resumed_description",
ADD COLUMN     "resume_description" VARCHAR(128),
ALTER COLUMN "description" SET DATA TYPE VARCHAR(2048);
