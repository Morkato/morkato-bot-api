import type { ConnectionContext } from 'models/database'
import type { AttackDatabase } from '.'

import { attacksQueryBuilder, attackQueryBuilder } from 'models/queries/attacks'
import { AttackNotFoundError, InternalServerError } from 'errors'
import { formatAttack } from './formatters'

export function findAttack({logger, pg}: ConnectionContext): AttackDatabase["findAttack"] {
  return async ({ guild_id, art_id }) => {
    const values: unknown[] = []
    const where = {
      "attack.guild_id": guild_id,
      "attack.art_id": art_id
    }

    const sql = attacksQueryBuilder.sql(where, values)
    logger.debug("SQL QUERY: %s with values: %s", sql, values)
    const { rows } = await pg.query(sql, values)
    logger.debug("RESULT FIND QUERY: %s where: %s", rows, where)
    return rows.map(formatAttack);
  }
}

export function getAttack({logger, locationCode, pg}: ConnectionContext): AttackDatabase["getAttack"] {
  return async ({ guild_id, id }) => {
    const values: unknown[] = []
    const where = {
      "attack.guild_id": guild_id,
      "attack.id": id
    }

    const sql = attackQueryBuilder.sql(where, values)
    logger.debug("SQL QUERY: 5s with values: %s", sql, values)
    const { rowCount, rows } = await pg.query(sql, values)
    logger.debug("RESULT GET QUERY: %s where: %s", rows, where)

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

    return formatAttack(rows[0])
  }
}