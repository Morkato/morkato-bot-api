import type { MorkatoAPP } from 'morkato/app'
import type { AttackDatabase } from './attacks'
import type { GuildDatabase } from './guilds'
import type { ItemDatabase } from './items'
import type { ArtDatabase } from './arts'
import type { Client } from "pg"

import prepareAttack from './attacks'
import prepareGuild from './guilds'
import prepareItem from './items'
import prepareArt from './arts'

export type Database = 
  & AttackDatabase
  & GuildDatabase  
  & ArtDatabase
  & ItemDatabase

export function prepareDatabase(app: MorkatoAPP, pg: Client): Database {  
  const database = Object.assign({},
    prepareAttack(app, pg),
    prepareGuild(app, pg),
    prepareItem(app, pg),
    prepareArt(app, pg),
  ) as Database

  return Object.freeze(database);
}

export default prepareDatabase;