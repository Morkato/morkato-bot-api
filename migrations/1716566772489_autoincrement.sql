-- Up Migration
CREATE SEQUENCE arts_snowflake_seq;
CREATE SEQUENCE attacks_snowflake_seq;
CREATE SEQUENCE items_snowflake_seq;
ALTER TABLE "arts"
  ALTER COLUMN "id" SET DEFAULT snowflake_id('arts_snowflake_seq');
ALTER TABLE "attacks"
  ALTER COLUMN "id" SET DEFAULT snowflake_id('attacks_snowflake_seq');
ALTER TABLE "items"
  ALTER COLUMN "id" SET DEFAULT snowflake_id('items_snowflake_seq');
-- Down Migration
DROP SEQUENCE "items_snowflake_seq";
DROP SEQUENCE "attacks_snowflake_seq";
DROP SEQUENCE "arts_snowflake_seq";
ALTER TABLE "arts"
  ALTER COLUMN "id" DROP DEFAULT;
ALTER TABLE "attacks"
  ALTER COLUMN "id" DROP DEFAULT;
ALTER TABLE "items"
  ALTER COLUMN "id" DROP DEFAULT;