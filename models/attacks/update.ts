import type { ConnectionContext } from 'models/database'
import type { AttackDatabase } from '.'

import { AttackNotFoundError, InternalServerError } from 'errors'
import { attackUpdateQueryBuilder } from 'models/queries/attacks'
import { literal } from 'models/sql-builder'
import { formatAttack } from './formatters'
import { stripAll } from 'utils'

export function updateAttack({logger, models, locationCode, pg, dispatch}: ConnectionContext): AttackDatabase["updateAttack"] {
  return async ({ guild_id, id, name, ...data }) => {
    const values: unknown[] = []
    const where = {
      "attack.guild_id": guild_id,
      "attack.id": id
    }
    const payload = Object.assign(data, {
      name: name,
      key: name === undefined ? undefined : stripAll(name),
      updated_at: literal("NOW()")
    })

    const before = await models.getAttack({ guild_id, id })
    const sql = attackUpdateQueryBuilder.sql(where, payload, values)
    logger.debug("SQL QUERY: 5s with values: %s", sql, values)
    const { rows, rowCount } = await pg.query(sql, values)
    logger.debug("RESULT UPDATE QUERY: %s with payload: %s where: %s", rows, payload, where)

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
    dispatch("attack.update", before, attack)
    return attack;
  }
}