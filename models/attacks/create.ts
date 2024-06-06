import type { MorkatoAPP } from 'morkato'
import type { AttackDatabase } from '.'
import type { Client } from 'pg'

import { AttackAlreadyExistsError, ArtNotFoundError, InternalServerError } from 'errors'
import { attackInsertQueryBuilder } from 'models/queries/attacks'
import { formatAttack } from './formatters'
import { DatabaseError } from 'pg'
import { stripAll } from 'utils'

const locationCode = "models/attacks"

export function createAttack(app: MorkatoAPP, pg: Client): AttackDatabase["createAttack"] {
  return async ({ name, guild_id, art_id, ...data }) => {
    const values: unknown[] = []
    const payload = Object.assign(data, {
      name: name,
      key: name !== undefined ? stripAll(name) : undefined,
      guild_id: guild_id,
      art_id: art_id
    })

    const sql = attackInsertQueryBuilder.sql({}, payload, values)
    async function execute(guildCreated: boolean = false) {
      try {
        const { rows: [attack] } = await pg.query(sql, values)
        return formatAttack(attack);
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