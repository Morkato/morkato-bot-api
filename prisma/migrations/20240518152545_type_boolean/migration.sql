/*
  Warnings:

  - You are about to drop the column `exclude` on the `arts` table. All the data in the column will be lost.
  - You are about to drop the column `exclude` on the `attacks` table. All the data in the column will be lost.
  - The `usable` column on the `items` table would be dropped and recreated. This will lead to data loss if there is data in the column.

*/
-- AlterTable
ALTER TABLE "arts" DROP COLUMN "exclude";

-- AlterTable
ALTER TABLE "attacks" DROP COLUMN "exclude";

-- AlterTable
ALTER TABLE "items" DROP COLUMN "usable",
ADD COLUMN     "usable" BOOLEAN NOT NULL DEFAULT false;

-- DropEnum
DROP TYPE "Bool";
