CREATE TABLE "players" (
  "id" discord_id_type NOT NULL,
  "guild_id" discord_id_type NOT NULL,
  "npc_id" id_type DEFAULT NULL,
  "ability_roll" uint_type NOT NULL,
  "family_roll" uint_type NOT NULL,
  "is_prodigy" BOOLEAN NOT NULL DEFAULT FALSE,
  "has_mark" BOOLEAN NOT NULL DEFAULT FALSE,
  "expected_npc_kind" npc_type NOT NULL
);

ALTER TABLE "players"
  ADD CONSTRAINT "player.pkey" PRIMARY KEY ("guild_id","id");
ALTER TABLE "players"
  ADD CONSTRAINT "player.guild" FOREIGN KEY ("guild_id") REFERENCES "guilds"("id")
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;
ALTER TABLE "players"
  ADD CONSTRAINT "player.npc" FOREIGN KEY ("guild_id","npc_id") REFERENCES "npcs"("guild_id","id")
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

ALTER TABLE "npcs"
  ADD COLUMN "player_id" discord_id_type
    DEFAULT NULL;
ALTER TABLE "npcs"
  ADD CONSTRAINT "npc.player" FOREIGN KEY ("guild_id","player_id") REFERENCES "players"("guild_id","id")
  ON DELETE CASCADE
  ON UPDATE RESTRICT;

CREATE FUNCTION sync_player_npc() RETURNS TRIGGER AS $$
BEGIN
  IF NEW.noc_id IS NULL THEN
    RETURN NEW;
  ELSIF OLD IS NOT NULL AND OLD.npc_id IS NOT NULL THEN
    RAISE EXCEPTION 'Don''t update npc_id with new value';
  END IF;
  UPDATE "npcs" SET "player_id"=NEW.id WHERE "guild_id"=NEW.guild_id AND "id"=NEW.npc_id;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER "player_npc"
  BEFORE INSERT OR UPDATE OF "npc_id"
  ON "players"
FOR EACH ROW
  EXECUTE FUNCTION sync_player_npc();