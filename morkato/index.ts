// import type { Router, AuthorizationChecker, BeforeHandler, AfterHandler } from './router'
// import type { Database } from 'models/database'
// import type { Handler } from 'express'
// import type { Express } from 'express'
// import type { Logger } from './logger'
// import type { Server } from 'http'

// import { MorkatoAPIWebSocketServer } from 'socket/server'
// import { RouteHandler } from './router'
// import { createLogger } from './logger'
// import { createServer } from 'http'
// import { Client } from "pg"

// import prepareDatabase from 'models/database'
// import express from 'express'

// export type LoggerContext = {
//   debug(message: string, ...args: unknown[]): void
//   info(message: string, ...args: unknown[]): void
//   warn(message: string, ...args: unknown[]): void
//   error(message: string, error?: Error,  ...args: unknown[]): void
//   critical(message: string, error?: Error,  ...args: unknown[]): void
// }

// export type Subscriber = (app: MorkatoAPP, ...data: any[]) => Promise<void>
// export type MorkatoAPP = {
//   startWebSocketServer(): void
//   connectDatabase(): Promise<void>
//   disconnectDatabase(): Promise<void>
//   startAPI(port: number): void
//   loadExtension(name: string): Promise<void>
//   loadController(name: string): void
//   notify(event: string, ...data: any[]): Promise<void>
//   subscribe(event: string, subscriber: Subscriber): void
//   inject(handler: Handler): Handler;
//   getLoggerContext(locode: string): LoggerContext
//   readonly database: Database
//   readonly express: Express
//   readonly socket: MorkatoAPIWebSocketServer
//   readonly server: Server
//   readonly dev: boolean
//   readonly logger: Logger
// }

// const locationCode = "morkato/index"

// function startAPI(app: MorkatoAPP, logger: Logger) {
//   return async (port: number) => {
//     app.server.listen(port, () => {
//       logger.info(locationCode, "API is listening...")
//     })
//   }
// }

// function startWebSocketServer(app: MorkatoAPP, logger: Logger) {
//   // @ts-ignore
//   let socket: MorkatoAPIWebSocketServer = null
//   return async () => {
//     if (!socket) {
//       socket = new MorkatoAPIWebSocketServer({ server: app.server })
//       logger.debug(locationCode, "Gateway is mounted!")
//       Object.assign(app, { socket: socket })
//     }
//   }
// }

// function startDatabase(app: MorkatoAPP, dev: boolean) {
//   const pg = new Client({
//     user: process.env.POSTGRES_USER,
//     password: process.env.POSTGRES_PASSWORD,
//     host: process.env.POSTGRES_HOST,
//     port: Number(process.env.POSTGRES_PORT),
//     database: dev ? process.env.POSTGRES_DEV_DB : process.env.POSTGRES_DB
//   })
//   const database = prepareDatabase(app, pg)
//   return {
//     async connectDatabase() {
//       app.logger.info(locationCode, "Connected database with url: postgres://%s:%s@%s:%s/%s", process.env.POSTGRES_USER, process.env.POSTGRES_PASSWORD, process.env.POSTGRES_HOST, process.env.POSTGRES_PORT, dev ? process.env.POSTGRES_DEV_DB : process.env.POSTGRES_DB)
//       await pg.connect()
//       Object.assign(app, { database })
//     },
//     async disconnectDatabase() {
//       await pg.end()
//       Object.assign(app, { database: undefined })
//     }
//   }
// }

// function loadExtension(app: MorkatoAPP, logger: Logger) {
//   return async (name: string) => {
//     const meta = require.resolve('../' + name)
//     const ext = require(meta)

//     logger.info(locationCode, "Loading Extension: %s", name)
//     await ext.default(app)
//   }
// }

// export default (dev: boolean = false): MorkatoAPP => {
//   function getLoggerContext(locode: string): LoggerContext {
//     return {
//       debug: (message, ...args) => logger.debug(locode, message, ...args),
//       info: (message, ...args) => logger.info(locode, message, ...args),
//       warn: (message, ...args) => logger.warn(locode, message, ...args),
//       error: (message, error, ...args) => logger.error(locode, message, error, ...args),
//       critical: (message, error, ...args) => logger.critical(locode, message, error, ...args)
//     }
//   }

//   const observers: Partial<Record<string, Subscriber[]>> = {}
//   const logger = createLogger(process.env.NODE_ENV ?? 'development')
//   const morkato: MorkatoAPP = { logger, getLoggerContext } as MorkatoAPP
//   const app = express()
//   const server = createServer(app)
//   const routes: RouteHandler[] = []

//   function createRouter(): Router {
//     let defaultAuthorizationHandler: AuthorizationChecker<any> | undefined = undefined

//     const before: BeforeHandler[] = []
//     const after: AfterHandler[] = []
//     return {
//       registry(method, route, callback) {
//         const handler = new RouteHandler(method, route, callback, morkato, defaultAuthorizationHandler)
//         routes.push(handler)
//         handler.registry(app)
//         handler.before(...before)
//         handler.after(...after)
//         return handler;
//       },
//       setAuthorization(checker) {
//         defaultAuthorizationHandler = checker
//       },
//       before(handler) {
//         before.push(handler)
//       },
//       after(handler) {
//         after.push(handler)
//       }
//     }
//   }

//   function loadController(name: string): void {
//     const router = createRouter()
//     const meta = require.resolve('../' + name)
//     const { setup } = require(meta)

//     logger.debug(locationCode, "Loading Controller: %s", name)
//     setup(router)
//   }
  
//   function inject(handler: Handler): Handler {
//     return async (req, res, next) => {
//       try {
//         const result = handler(req, res, next)
//         if ((result as any) instanceof Promise) {
//           await result;
//         }
//       } catch (error) {
//         await morkato.notify("request-error", req, res, error)
//       }
//     }
//   }

//   function subscribe(event: string, subscriber: Subscriber) {
//     logger.debug(locationCode, "Registry subscriber: %s", event)
//     const subscribers = observers[event]

//     if (subscribers === undefined) {
//       observers[event] = [subscriber] 
//       return;
//     }
//     subscribers.push(subscriber)
//   }

//   async function onerror(event: string, funcname: string, error: Error): Promise<void> {
//     function maybeSubscriber(subscriber: Subscriber) {
//       return subscriber(morkato, error)
//         .catch(error => logger.critical(locationCode, "An function (%S) handling error has throw error (%s): %s", error, funcname, error.name, error.message))
//     }
//     const subscribers = observers["error"] ?? []
//     logger.debug(locationCode, "Notify all observers with pending event (Totally: %s): %s", subscribers.length, event)
//     await Promise.all(subscribers.map(maybeSubscriber))
//   }

//   async function notify(event: string, ...data: unknown[]) {
//     function maybeSubscriber(subscriber: Subscriber) {
//       return subscriber(morkato, ...data)
//         .catch(error => onerror(event, subscriber.name, error))
//     }
//     const subscribers = observers[event] ?? []    
//     logger.debug(locationCode, "Notify all observers with pending event (Totally: %s): %s", subscribers.length, event)
//     await Promise.all(subscribers.map(maybeSubscriber))
//   }

//   Object.assign(morkato, {
//     startAPI: startAPI(morkato, logger),
//     startWebSocketServer: startWebSocketServer(morkato, logger),
//     loadExtension: loadExtension(morkato, logger),
//     server: server,
//     express: app

//   }, startDatabase(morkato, dev), {
//     loadController,
//     subscribe,
//     notify,
//     inject
//   })

//   return morkato;
// } 