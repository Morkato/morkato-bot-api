-- RenameForeignKey
ALTER TABLE "locations" RENAME CONSTRAINT "locations_guild_id_fkey" TO "fkey.guild_id";

-- RenameForeignKey
ALTER TABLE "locations" RENAME CONSTRAINT "locations_guild_id_location_id_fkey" TO "fkey.location_id";
