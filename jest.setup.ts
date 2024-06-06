import 'dotenv-expand/config'

import type { Database } from './models/database'

import { prepareDatabase } from './models/database'
import runMigrations from 'node-pg-migrate'
import { Client } from 'pg'

const databaseTestsName = "morkato-jest-tests"

let client: Client | undefined
export declare namespace global {
  let database: Database
}


beforeAll(async () => {
  jest.setTimeout(25 * 1000)

  const pg = new Client({
    connectionString: process.env.DATABASE_URL
  })

  await pg.connect()
  await pg.query(`DROP DATABASE IF EXISTS "${databaseTestsName}";`)
  await pg.query(`CREATE DATABASE "${databaseTestsName}";`)
  await pg.end()

  client = new Client({
    user: process.env.POSTGRES_USER,
    password: process.env.POSTGRES_PASSWORD,
    host: process.env.POSTGRES_HOST,
    port: Number(process.env.POSTGRES_PORT),
    database: databaseTestsName
  })

  await client.connect()
  await runMigrations({
    dbClient: client,
    direction: 'up',
    dir: "./database/migrations",
    migrationsTable: "pgmigrations"
  })

  const database = prepareDatabase({} as any, client)
  global.database = database
})

afterAll(async () => {
  if (client) {
    await client.end()
  }
})