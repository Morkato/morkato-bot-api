import type { ConnectionContext } from 'models/database'
import type { AttackDatabase } from '.'

import { AttackNotFoundError, InternalServerError } from 'errors'
import { attackDelQueryBuilder } from 'models/queries/attacks'
import { formatAttack } from './formatters'

export function delAttack({logger, locationCode, pg, dispatch}: ConnectionContext): AttackDatabase["delAttack"] {
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
    dispatch("attack.delete", attack)
    return attack;
  };
}