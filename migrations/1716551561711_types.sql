-- Up Migration
CREATE TYPE "arttype" AS ENUM ($pga$RESPIRATION$pga$, $pga$KEKKIJUTSU$pga$, $pga$FIGHTING_STYLE$pga$);
CREATE TYPE "playertype" AS ENUM ($pga$HUMAN$pga$, $pga$ONI$pga$, $pga$HYBRID$pga$);
-- Down Migration
DROP TYPE "arttype";
DROP TYPE "playertype";