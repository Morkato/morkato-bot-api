-- "flags" COLUMN IS ADDED INTO TABLE "players" IN VERSION 11.0!
-- "is_prodigy" COLUMN IS DELETED INTO TABLE "players" IN VERSION 11.0!
-- "has_mark" COLUMN IS DELETED INTO TABLE "players" IN VERSION 11.0!
CREATE TABLE "players" (
  "guild_id" discord_id_type NOT NULL,
  "id" discord_id_type NOT NULL,
  "ability_roll" uint_type NOT NULL,
  "family_roll" uint_type NOT NULL,
  "is_prodigy" BOOLEAN NOT NULL DEFAULT FALSE,
  "has_mark" BOOLEAN NOT NULL DEFAULT FALSE,
  "expected_family_id" id_type DEFAULT NULL,
  "expected_npc_kind" npc_type NOT NULL
);

CREATE TABLE "players_abilities" (
  "guild_id" discord_id_type NOT NULL,
  "player_id" discord_id_type NOT NULL,
  "ability_id" id_type NOT NULL
);

CREATE TABLE "players_families" (
  "guild_id" discord_id_type NOT NULL,
  "player_id" discord_id_type NOT NULL,
  "family_id" id_type NOT NULL
);

ALTER TABLE "players"
  ADD CONSTRAINT "player.pkey" PRIMARY KEY ("guild_id","id");
ALTER TABLE "players"
  ADD CONSTRAINT "player.guild" FOREIGN KEY ("guild_id") REFERENCES "guilds"("id")
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;
ALTER TABLE "players"
  ADD CONSTRAINT "player.family" FOREIGN KEY ("guild_id","expected_family_id") REFERENCES "families"("guild_id","id")
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

ALTER TABLE "players_families"
  ADD CONSTRAINT "player_family.pkey" PRIMARY KEY ("guild_id","player_id","family_id");
ALTER TABLE "players_families"
  ADD CONSTRAINT "player_family.guild" FOREIGN KEY ("guild_id") REFERENCES "guilds"("id")
  ON DELETE CASCADE
  ON UPDATE RESTRICT;
ALTER TABLE "players_families"
  ADD CONSTRAINT "player_family.player" FOREIGN KEY ("guild_id","player_id") REFERENCES "players"("guild_id","id")
  ON DELETE CASCADE
  ON UPDATE RESTRICT;
ALTER TABLE "players_families"
  ADD CONSTRAINT "player_family.family" FOREIGN KEY ("guild_id","family_id") REFERENCES "families"("guild_id","id")
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

 ALTER TABLE "players_abilities"
   ADD CONSTRAINT "player_ability.pkey" PRIMARY KEY ("guild_id","player_id","ability_id");
 ALTER TABLE "players_abilities"
   ADD CONSTRAINT "player_ability.guild" FOREIGN KEY ("guild_id") REFERENCES "guilds"("id")
   ON DELETE CASCADE
   ON UPDATE RESTRICT;
 ALTER TABLE "players_abilities"
   ADD CONSTRAINT "player_ability.player" FOREIGN KEY ("guild_id","player_id") REFERENCES "players"("guild_id","id")
   ON DELETE CASCADE
   ON UPDATE RESTRICT;
 ALTER TABLE "players_abilities"
   ADD CONSTRAINT "player_ability.ability" FOREIGN KEY ("guild_id","ability_id") REFERENCES "abilities"("guild_id","id")
   ON DELETE RESTRICT
   ON UPDATE RESTRICT;

ALTER TABLE "npcs"
  ADD COLUMN "player_id" discord_id_type
    DEFAULT NULL;
ALTER TABLE "npcs"
  ADD CONSTRAINT "npc.unique_player" UNIQUE ("guild_id","player_id");
ALTER TABLE "npcs"
  ADD CONSTRAINT "npc.player" FOREIGN KEY ("guild_id","player_id") REFERENCES "players"("guild_id","id")
  ON DELETE CASCADE
  ON UPDATE RESTRICT;

CREATE FUNCTION block_update_player() RETURNS TRIGGER AS $$
BEGIN
  IF OLD.expected_family_id IS NOT NULL THEN
    NEW.expected_family_id := OLD.expected_family_id;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE FUNCTION block_npc_player() RETURNS TRIGGER AS $$
BEGIN
  RAISE EXCEPTION 'Don''t update player_id wih new value';
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER "block_update_player"
  BEFORE UPDATE
  ON "players"
FOR EACH ROW
  EXECUTE FUNCTION block_update_player();
CREATE TRIGGER "npc_payer"
  BEFORE UPDATE OF "player_id"
  ON "npcs"
FOR EACH ROW
  EXECUTE FUNCTION block_npc_player();