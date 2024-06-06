import type { MorkatoAPP } from 'morkato'
import type { AttackDatabase } from '.'
import type { Client } from 'pg'

import { AttackNotFoundError, InternalServerError } from 'errors'
import { attackUpdateQueryBuilder } from 'models/queries/attacks'
import { literal } from 'models/sql-builder'
import { formatAttack } from './formatters'
import { stripAll } from 'utils'

const locationCode = "models/attacks"

export function updateAttack(app: MorkatoAPP, pg: Client): AttackDatabase["updateAttack"] {
  const logger = app.getLoggerContext(locationCode)
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

    const before = await app.database.getAttack({ guild_id, id })
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
    app.notify("attack.update", before, attack)
    return attack;
  }
}