import type { MorkatoAPP } from './morkato/app'
import { getAPP } from './server'
import migrate from 'node-pg-migrate'
import 'dotenv-expand/config'

const ambient = "tests"
const port = 9900
const APIURL = "http://localhost:" + port
let runningAPP: MorkatoAPP | undefined
declare global {
  export function getRunningAPP(): MorkatoAPP
  export const APIURL: string
}

global.getRunningAPP =
function getRunningAPP() {
  if (runningAPP === undefined) {
    throw new Error("APP not running.");
  }
  return runningAPP;
};

(global as any).APIURL = APIURL

beforeAll(async () => {
  jest.setTimeout(25 * 1000)
  const app: MorkatoAPP = await getAPP(ambient)
  app.logger.ambient(ambient, new Set())
  app.databaseAmbient(ambient, "morkato-jest-tests")
  await app.start(9900)
  runningAPP = app
})

afterAll(async () => {
  if (runningAPP) {
    await runningAPP.disconnectDatabase()
    runningAPP.closeAPI()
    runningAPP = undefined
  }
})