-- Up Migration
-- Intents: Inteções do ataque (players.intents).
  
  -- 0: Nenhuma
  -- (1 << 2): Indesviável
  -- (1 << 3): Indefensável
  -- (1 << 4): Em área
  -- (1 << 5): Combável
  -- (1 << 6): Não contra atacável
  -- (1 << 7): Contra Ataque
  -- (1 << 8): Dano Somável
  -- (1 << 9): Fôlego Somável
  -- (1 << 10): Sangue Somável
  -- (1 << 11): Usável para defesa

CREATE TABLE "players" (
  "name" VARCHAR(32) NOT NULL,
  "surname" VARCHAR(32) NOT NULL,
  "guild_id" VARCHAR(30) NOT NULL,
  "id" VARCHAR(30) NOT NULL,
  "type" playertype NOT NULL,
  "location_id" VARCHAR(35) DEFAULT NULL,
  "life" NUMERIC(12, 2) NOT NULL DEFAULT 1,
  "breath" NUMERIC(12, 2) NOT NULL DEFAULT 1,
  "blood" NUMERIC(12, 2) NOT NULL DEFAULT 1,
  "exp" NUMERIC(12, 2) NOT NULL DEFAULT 1,
  "force" NUMERIC(12, 2) NOT NULL DEFAULT 1,
  "resistance" NUMERIC(12, 2) NOT NULL DEFAULT 1,
  "velocity" NUMERIC(12, 2) NOT NULL DEFAULT 1,
  "appearance" TEXT,
  "updated_at" DATE DEFAULT NULL
);
CREATE TABLE "inventory" (
  "player_id" VARCHAR(30) NOT NULL,
  "guild_id" VARCHAR(30) NOT NULL,
  "item_id" BIGINT NOT NULL,
  "stack" SMALLINT NOT NULL DEFAULT 1,
  "created_at" DATE DEFAULT NOW()
);
CREATE TABLE "player_arts" (
  "player_id" VARCHAR(30) NOT NULL,
  "guild_id" VARCHAR(30) NOT NULL,
  "art_id" BIGINT NOT NULL,
  "created_at" DATE NOT NULL DEFAULT NOW()
);
CREATE TABLE "player_attacks" (
  "player_id" VARCHAR(30) NOT NULL,
  "guild_id" VARCHAR(30) NOT NULL,
  "attack_id" BIGINT NOT NULL,
  "created_at" DATE NOT NULL DEFAULT NOW()
);
ALTER TABLE "players"
  ADD CONSTRAINT "player.pkey" PRIMARY KEY ("guild_id", "id");
ALTER TABLE "players"
  ADD CONSTRAINT "player.surname" UNIQUE ("guild_id", "surname");
ALTER TABLE "players"
  ADD CONSTRAINT "player.guild" FOREIGN KEY ("guild_id") REFERENCES "guilds" ("id") ON DELETE CASCADE;
ALTER TABLE "inventory"
  ADD CONSTRAINT "inv.pkey" PRIMARY KEY ("guild_id", "player_id", "item_id");
ALTER TABLE "inventory"
  ADD CONSTRAINT "inv.guild" FOREIGN KEY ("guild_id") REFERENCES "guilds" ("id") ON DELETE CASCADE;
ALTER TABLE "inventory"
  ADD CONSTRAINT "inv.player" FOREIGN KEY ("guild_id", "player_id") REFERENCES "players" ("guild_id", "id") ON DELETE CASCADE;
ALTER TABLE "inventory"
  ADD CONSTRAINT "inv.item" FOREIGN KEY ("guild_id", "item_id") REFERENCES "items" ("guild_id", id) ON DELETE RESTRICT;
ALTER TABLE "player_arts"
  ADD CONSTRAINT "part.pkey" PRIMARY KEY ("guild_id", "player_id", "art_id");
ALTER TABLE "player_arts"
  ADD CONSTRAINT "part.guild" FOREIGN KEY ("guild_id") REFERENCES "guilds" ("id") ON DELETE CASCADE;
ALTER TABLE "player_arts"
  ADD CONSTRAINT "part.player" FOREIGN KEY ("guild_id", "player_id") REFERENCES "players" ("guild_id", "id") ON DELETE CASCADE;
ALTER TABLE "player_arts"
  ADD CONSTRAINT "part.art" FOREIGN KEY ("guild_id", "art_id") REFERENCES "arts" ("guild_id", "id") ON DELETE CASCADE;
ALTER TABLE "player_attacks"
  ADD CONSTRAINT "patk.pkey" PRIMARY KEY ("guild_id", "player_id", "attack_id");
ALTER TABLE "player_attacks"
  ADD CONSTRAINT "patk.guild" FOREIGN KEY ("guild_id") REFERENCES "guilds" ("id") ON DELETE CASCADE;
ALTER TABLE "player_attacks"
  ADD CONSTRAINT "patk.player" FOREIGN KEY ("guild_id", "player_id") REFERENCES "players" ("guild_id", "id") ON DELETE CASCADE;
ALTER TABLE "player_attacks"
  ADD CONSTRAINT "patk.attack" FOREIGN KEY ("guild_id", "attack_id") REFERENCES "attacks" ("guild_id", "id") ON DELETE CASCADE;
-- Down Migration
ALTER TABLE "player_attacks" DROP CONSTRAINT "patk.attack";
ALTER TABLE "player_attacks" DROP CONSTRAINT "patk.player";
ALTER TABLE "player_attacks" DROP CONSTRAINT "patk.guild";
ALTER TABLE "player_arts" DROP CONSTRAINT "part.art";
ALTER TABLE "player_arts" DROP CONSTRAINT "part.player";
ALTER TABLE "player_arts" DROP CONSTRAINT "part.guild";
ALTER TABLE "inventory" DROP CONSTRAINT "inv.item";
ALTER TABLE "inventory" DROP CONSTRAINT "inv.player";
ALTER TABLE "inventory" DROP CONSTRAINT "inv.guild";
ALTER TABLE "players" DROP CONSTRAINT "player.guild";
ALTER TABLE "players" DROP CONSTRAINT "player.surname";
DROP TABLE "player_attacks";
DROP TABLE "player_arts";
DROP TABLE "inventory";
DROP TABLE "players";