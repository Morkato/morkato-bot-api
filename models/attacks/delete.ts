import type { MorkatoAPP } from 'morkato'
import type { AttackDatabase } from '.'
import type { Client } from 'pg'

import { AttackNotFoundError, InternalServerError } from 'errors'
import { attackDelQueryBuilder } from 'models/queries/attacks'
import { formatAttack } from './formatters'

const locationCode = "models/attacks"

export function delAttack(app: MorkatoAPP, pg: Client): AttackDatabase["delAttack"] {
  const logger = app.getLoggerContext(locationCode)
  return async ({ guild_id, id }) => {
    const values: unknown[] = []
    const where = {
      "attack.guild_id": guild_id,
      "attack.id": id
    }

    const sql = attackDelQueryBuilder.sql(where, values)
    logger.debug("SQL DELETE QUERY: %s with values: %s", sql, values)
    const { rows, rowCount } = await pg.query(sql, values)
    logger.debug("RESULT DELETE QUERY: %s where: %s", rows, where)
    
    if (!rowCount || rowCount === 0) {
      throw new AttackNotFoundError({
        errorLocationCode: locationCode,
        guild_id: guild_id,
        attack_id: id
      });
    } else if (rowCount > 1) {
      throw new InternalServerError({
        errorLocationCode: locationCode
      });
    }

    const attack = formatAttack(rows[0])
    app.notify("attack.delete", attack)
    return attack;
  };
}