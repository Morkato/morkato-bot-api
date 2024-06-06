import 'dotenv/config'

import { onRequestError } from 'app/observers/request-error'
import { LoggerLevel } from 'morkato/logger'
import morkato from "morkato"
import {
  plAttack,
  plItem,
  plArt,
  player,
  guild,
  attack,
  item,
  art
} from 'app/observers/gateway'

async function main() {
  const app = morkato(process.env.NODE_ENV === 'development')
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

  app.logger.info("morkato/server", "This is ambient: %s", process.env.NODE_ENV ?? "development")
  await app.loadExtension("app/extensions/cors")
  await app.loadExtension("app/extensions/middlewares")
  await app.loadExtension("app/extensions/router")
  await app.connectDatabase()
  
  app.subscribe("request-error", onRequestError)
  app.subscribe("player-attack.create", plAttack.create)
  app.subscribe("player-attack.delete", plAttack.delete)
  app.subscribe("player-item.create", plItem.create)
  app.subscribe("player-item.update", plItem.update)
  app.subscribe("player-item.delete", plItem.delete)
  app.subscribe("player-art.create", plArt.create)
  app.subscribe("player-art.delete", plArt.delete)
  app.subscribe("player.create", player.create)
  app.subscribe("player.update", player.update)
  app.subscribe("player.delete", player.delete)
  app.subscribe("attack.create", attack.create)
  app.subscribe("attack.update", attack.update)
  app.subscribe("attack.delete", attack.delete)
  app.subscribe("guild.create", guild.create)
  app.subscribe("guild.update", guild.update)
  app.subscribe("guild.delete", guild.delete)
  app.subscribe("item.create", item.create)
  app.subscribe("item.update", item.update)
  app.subscribe("item.delete", item.delete)
  app.subscribe("art.create", art.create)
  app.subscribe("art.update", art.update)
  app.subscribe("art.delete", art.delete)

  app.startWebSocketServer() // Listening in ws://localhost:5500
  app.startAPI(5500) // Listening in http://localhost:5500
}

main()