import 'dotenv/config'
import { LoggerLevel } from 'morkato/logger'
import { MorkatoAPP } from "morkato/app"

export async function getAPP(ambient: string): Promise<MorkatoAPP> {
  const app = new MorkatoAPP(ambient)
  app.logger.setDefaultFormatter("[%datetime] [%levelname: %localcode] %message")
  app.logger.ambient("development", new Set([
    LoggerLevel.DEBUG,
    LoggerLevel.INFO,
    LoggerLevel.WARNING,
    LoggerLevel.ERROR,
    LoggerLevel.WARNING
  ]))
  app.logger.ambient("production", new Set([
    LoggerLevel.INFO,
    LoggerLevel.WARNING,
    LoggerLevel.ERROR,
    LoggerLevel.WARNING
  ]))

  app.databaseAmbient("development", "morkato-dev")
  app.databaseAmbient("production", "morkato")

  app.logger.info("morkato/server", "This is ambient: %s", process.env.NODE_ENV ?? "development")
  await app.loadExtension("app/extensions/router")
  await app.loadExtension("app/extensions/observers")
  return app;
}

async function main() {
  const app = await getAPP(process.env.NODE_ENV ?? 'development')
  await app.start(5500) // Listening in ws://localhost:5500 and Listening in http://localhost:5500
}

if (require.main === module) {
  main()
}