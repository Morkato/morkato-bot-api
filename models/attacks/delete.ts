import type { MorkatoAPP } from 'morkato'
import type { AttackDatabase } from '.'
import type { Client } from 'pg'

import { AttackNotFoundError, InternalServerError } from 'errors'
import { attackDelQueryBuilder } from 'models/queries/attacks'
import { formatAttack } from './formatters'

const locationCode = "models/attacks"

export function delAttack(app: MorkatoAPP, pg: Client): AttackDatabase["delAttack"] {
  return async ({ guild_id, id }) => {
    const values: unknown[] = []
    const where = {
      "attack.guild_id": guild_id,
      "attack.id": id
    }

    const sql = attackDelQueryBuilder.sql(where, values)
    const { rows:[result], rowCount } = await pg.query(sql, values)
    
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
    app.notify("attack.delete", attack)
    return attack;
  };
}