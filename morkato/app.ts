import type { RouteCallback } from './controller'
import type { Database } from 'models/database'
import type { Express } from 'express'
import type { Logger } from './logger'
import type { Server } from 'http'

import { MorkatoAPIWebSocketServer } from 'socket/server'
import { Controller, RouteHandler } from './controller'
import { createLogger } from './logger'
import { createServer } from 'http'
import { Client } from 'pg'

import prepareDatabase from 'models/database'
import express from 'express'

export type Subscriber = (app: MorkatoAPP, ...data: any[]) => Promise<void>
export type LoggerContext = {
  debug(message: string, ...args: unknown[]): void
  info(message: string, ...args: unknown[]): void
  warn(message: string, ...args: unknown[]): void
  error(message: string, error?: Error,  ...args: unknown[]): void
  critical(message: string, error?: Error,  ...args: unknown[]): void
}

export class MorkatoAPP {
  public readonly observers: Partial<Record<string, Subscriber[]>> = {}
  public readonly ambient: string = process.env.NODE_ENV ?? "development"
  public readonly logger: Logger = createLogger(this.ambient)
  private readonly loggerContext: LoggerContext = this.getLoggerContext("morkato/app")
  public readonly dev: boolean = this.ambient === 'development'
  public readonly app: Express = express()
  public readonly server: Server = createServer(this.app)
  public readonly controllers: Controller[] = []
  public readonly socket: MorkatoAPIWebSocketServer = new MorkatoAPIWebSocketServer({server: this.server})
  private isRunning: boolean = false
  public readonly pg: Client = new Client({
    user: process.env.POSTGRES_USER,
    password: process.env.POSTGRES_PASSWORD,
    host: process.env.POSTGRES_HOST,
    port: Number(process.env.POSTGRES_PORT),
    database: this.dev ? process.env.POSTGRES_DEV_DB : process.env.POSTGRES_DB
  })
  // @ts-ignore
  public readonly database: Database = undefined

  public getLoggerContext(locode: string): LoggerContext {
    return {
      debug: (message, ...args) => this.logger.debug(locode, message, ...args),
      info: (message, ...args) => this.logger.info(locode, message, ...args),
      warn: (message, ...args) => this.logger.warn(locode, message, ...args),
      error: (message, error, ...args) => this.logger.error(locode, message, error, ...args),
      critical: (message, error, ...args) => this.logger.critical(locode, message, error, ...args)
    }
  }

  public startAPI(port: number) {
    if (this.isRunning) {
      return;
    }
    this.server.listen(port, () => {
      this.loggerContext.info("API is listening...")
    })
  }
  
  public closeAPI() {
    if (!this.isRunning) {
      return;
    }
    this.server.close()
  }

  public async connectDatabase(): Promise<void> {
    this.loggerContext.info("Connect database with url: postgres://%s:%s@%s:%s/%s", process.env.POSTGRES_USER, process.env.POSTGRES_PASSWORD, process.env.POSTGRES_HOST, process.env.POSTGRES_PORT, this.dev ? process.env.POSTGRES_DEV_DB : process.env.POSTGRES_DB)
    await this.pg.connect()
    Object.assign(this, { database: prepareDatabase(this, this.pg) })
  }

  public async disconnectDatabase(): Promise<void> {
    await this.pg.end()
    Object.assign(this, { database: undefined })
  }

  public async start(port: number): Promise<void> {
    await this.connectDatabase()
    this.startAPI(port)

    this.isRunning = true
    this.notify("ready")
  }

  public async close(): Promise<void> {
    await this.disconnectDatabase()
    this.closeAPI()

    this.notify("close")
  }

  public onerror(event: string, funcname: string, error: Error): Promise<void> {
    if (event === 'error') {
      this.loggerContext.critical("An function (%S) handling error has throw error (%s): %s", error, funcname, error.name, error.message)
      console.error(error)
      this.close()
      return Promise.resolve();
    }

    return this.notify("error", funcname, error);
  }
  
  public notify(event: string, ...data: unknown[]): Promise<void> {
    const app = this
    function maybeSubscriber(subscriber: Subscriber) {
      return subscriber(app, ...data)
        .catch(error => app.onerror(event, subscriber.name, error))
    }
    const subscribers = this.observers[event]
    this.loggerContext.debug("Notify all observers with pending event (Totally: %s): %s", (Array.isArray(subscribers) ? subscribers.length : 0), event)
    if (!subscribers) {
      return Promise.resolve();
    }    
    return Promise.all(subscribers.map(maybeSubscriber)).then(() => {});
  }

  public subscribe(event: string, subscriber: Subscriber): void {
    this.loggerContext.debug("Registry subscriber: %s", event)
    const subscribers = this.observers[event]

    if (subscribers === undefined) {
      this.observers[event] = [subscriber]
      return;
    }
    subscribers.push(subscriber)
  }

  public async loadExtension(name: string): Promise<void> {
    const meta = require.resolve('../' + name)
    const ext = require(meta)

    this.loggerContext.info("Loading Extension: %s", name)
    await ext.default(this)
  }

  public loadController(name: string): Controller {
    const meta = require.resolve('../' + name)
    const { setup } = require(meta)

    this.loggerContext.info("Loading Controller: %s", name)
    const logger = this.getLoggerContext(name)
    const controller = new Controller(this.app, this.notify.bind(this), (() => this.database).bind(this), name, logger)
    setup(controller, logger)
    return controller;
  }

  public use(route: string, callback: RouteCallback): void {
    const handler = new RouteHandler(callback, this.notify.bind(this), (() => this.database).bind(this))
    this.loggerContext.debug("Registry middleware (*): %s", route)
    this.app.use(route, handler.invoke.bind(handler))
  }
}