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

    const sql = attackUpdateQueryBuilder.sql(where, payload, values)
    const before = await app.database.getAttack({ guild_id, id })
    const { rows: [result], rowCount } = await pg.query(sql, values)

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

    const attack = formatAttack(result)
    app.notify("attack.update", [before, attack])
    return attack;
  }
}