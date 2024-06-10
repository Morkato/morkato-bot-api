import type { MorkatoAPP } from 'morkato/app'
import type { AttackDatabase } from '.'
import type { Client } from 'pg'

import { AttackAlreadyExistsError, ArtNotFoundError, InternalServerError } from 'errors'
import { attackInsertQueryBuilder } from 'models/queries/attacks'
import { formatAttack } from './formatters'
import { DatabaseError } from 'pg'
import { stripAll } from 'utils'

const locationCode = "models/attacks"

export function createAttack(app: MorkatoAPP, pg: Client): AttackDatabase["createAttack"] {
  const logger = app.getLoggerContext(locationCode)
  return async ({ name, guild_id, art_id, ...data }) => {
    const values: unknown[] = []
    const payload = Object.assign(data, {
      name: name,
      key: name !== undefined ? stripAll(name) : undefined,
      guild_id: guild_id,
      art_id: art_id
    })

    const sql = attackInsertQueryBuilder.sql({}, payload, values)
    logger.debug("SQL QUERY: %s with values: %s", sql, values)
    async function execute(guildCreated: boolean = false) {
      try {
        const { rows } = await pg.query(sql, values)
        logger.debug("RESULT CREATE QUERY: %s with payload: %s", sql, payload)
        const attack = formatAttack(rows[0])
        app.notify("attack.create", attack)
        return attack;
      } catch (err) {
        if (err instanceof DatabaseError) {
          if (err.constraint === 'attack.guild' && !guildCreated) {
            await app.database.createGuild({ id: guild_id })
            return await execute(true);
          } else if (err.constraint === 'attack.key') {
            throw new AttackAlreadyExistsError({
              errorLocationCode: locationCode,
              guild_id: guild_id,
              attack_name: name
            });
          } else if (err.constraint === 'attack.art') {
            throw new ArtNotFoundError({
              errorLocationCode: locationCode,
              guild_id: guild_id,
              art_id: art_id
            });
          }
          throw new InternalServerError({
            errorLocationCode: locationCode
          });
        }
        throw err;
      }
    }

    return await execute();
  }
}