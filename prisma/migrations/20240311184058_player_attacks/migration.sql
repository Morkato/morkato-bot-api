-- CreateTable
CREATE TABLE "player_attacks" (
    "guild_id" TEXT NOT NULL,
    "player_id" TEXT NOT NULL,
    "attack_id" TEXT NOT NULL,

    CONSTRAINT "player_attacks_pkey" PRIMARY KEY ("guild_id","player_id","attack_id")
);

-- AddForeignKey
ALTER TABLE "player_attacks" ADD CONSTRAINT "fkey.guild_id" FOREIGN KEY ("guild_id") REFERENCES "guilds"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "player_attacks" ADD CONSTRAINT "fkey.player_id" FOREIGN KEY ("guild_id", "player_id") REFERENCES "players"("guild_id", "id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "player_attacks" ADD CONSTRAINT "fkey.attack_id" FOREIGN KEY ("guild_id", "attack_id") REFERENCES "attacks"("guild_id", "id") ON DELETE CASCADE ON UPDATE CASCADE;
