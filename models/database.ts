import type { LoggerContext, MorkatoAPP } from 'morkato/app'
import type { LocationDatabase } from './locations'
import type { AttackDatabase } from './attacks'
import type { GuildDatabase } from './guilds'
import type { ItemDatabase } from './items'
import type { ArtDatabase } from './arts'
import type { Client } from "pg"

import prepareLocation from './locations'
import prepareAttack from './attacks'
import prepareGuild from './guilds'
import prepareItem from './items'
import prepareArt from './arts'

export type ConnectionContext = Readonly<{
  dispatch(event: string, ...data: unknown[]): Promise<void>
  getLogger(locode: string): LoggerContext
  readonly locationCode: string
  readonly logger: LoggerContext
  readonly pg: Client
  readonly models: Database
}>

export type Database = 
  & LocationDatabase
  & AttackDatabase
  & GuildDatabase  
  & ArtDatabase
  & ItemDatabase

export function prepareDatabase({ dispatch, getLogger, pg }: Pick<ConnectionContext, 'dispatch' | 'pg' | 'getLogger'>): Database {
  const database = {} as Database
  function getContext(locode: string): ConnectionContext {
    return Object.assign({
      locationCode: locode,
      logger: getLogger(locode),
      getLogger: getLogger,
      dispatch: dispatch,
      pg: pg,
      models: database
    });
  }

  return Object.freeze(
    Object.assign(database,
      prepareLocation(getContext("models/locations")),
      prepareAttack(getContext("models/attacks")),
      prepareGuild(getContext("models/guilds")),
      prepareItem(getContext("models/items")),
      prepareArt(getContext("models/arts"))
    )
  );
}

export default prepareDatabase;