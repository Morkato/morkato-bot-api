-- Up Migration
CREATE TABLE "guilds" (
  "id" VARCHAR(30) PRIMARY KEY NOT NULL,
  "default_player_life" INTEGER DEFAULT 100 NOT NULL,
  "default_player_breath" INTEGER DEFAULT 500 NOT NULL,
  "default_player_blood" INTEGER DEFAULT 500 NOT NULL,
  "default_player_resistance" INTEGER DEFAULT 0 NOT NULL,
  "default_player_force" INTEGER DEFAULT 0 NOT NULL,
  "default_player_velocity" INTEGER DEFAULT 1 NOT NULL
);
-- Down Migration
DROP TABLE "guilds";