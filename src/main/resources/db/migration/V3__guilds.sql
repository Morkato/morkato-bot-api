CREATE TABLE "guilds" (
  "id" discord_id_type NOT NULL,
  "human_initial_life" attr_type NOT NULL DEFAULT 1000,
  "oni_initial_life" attr_type NOT NULL DEFAULT 500,
  "hybrid_initial_life" attr_type NOT NULL DEFAULT 1500,
  "breath_initial" attr_type NOT NULL DEFAULT 500,
  "blood_initial" attr_type NOT NULL DEFAULT 1000,
  "family_roll" attr_type NOT NULL DEFAULT 3,
  "ability_roll" attr_type NOT NULL DEFAULT 3
);
ALTER TABLE "guilds"
  ADD CONSTRAINT "guild.pkey" PRIMARY KEY ("id");