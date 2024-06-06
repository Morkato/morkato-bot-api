-- DropForeignKey
ALTER TABLE "attacks" DROP CONSTRAINT "fkey.art_id";

-- AddForeignKey
ALTER TABLE "attacks" ADD CONSTRAINT "fkey.art_id" FOREIGN KEY ("art_id", "guild_id") REFERENCES "arts"("id", "guild_id") ON DELETE CASCADE ON UPDATE CASCADE;
