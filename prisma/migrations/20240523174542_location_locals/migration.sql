-- DropForeignKey
ALTER TABLE "locations" DROP CONSTRAINT "fkey.location_id";

-- AddForeignKey
ALTER TABLE "locations" ADD CONSTRAINT "fkey.location_id" FOREIGN KEY ("guild_id", "location_id") REFERENCES "locations"("guild_id", "id") ON DELETE SET DEFAULT ON UPDATE CASCADE;
