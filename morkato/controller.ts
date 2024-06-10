import type { Express, Request, Response, NextFunction } from 'express'
import type { LoggerContext, MorkatoAPP } from './app'
import type { Database } from 'models/database'

import { InternalServerError } from 'errors'

export type PayloadType<UserType> = { user: UserType, params: Record<string, string>, models: Database, dispatch: MorkatoAPP["notify"], next: NextFunction }
export type RouteCallback<UserType = any> = (req: Request, res: Response, payload: PayloadType<UserType>) => Promise<void>
export type BeforeHandler<UserType = any> = (req: Request, payload: PayloadType<UserType>) => Promise<void>
export type AfterHandler<UserType = any> = (req: Request, payload: PayloadType<UserType>) => Promise<void>
export type AuthorizationHandler<T> = (req: Request) => Promise<T>
export type RouteHandlerMethod = "GET" | "POST" | "PUT" | "DELETE"

export class Controller {
  private readonly beforeHandlers: BeforeHandler[] = []
  private readonly afterHandlers: AfterHandler[] = []

  constructor(
    private readonly express: Express,
    private dispatch: MorkatoAPP["notify"],
    private getModels: () => Database,
    private name: string,
    private readonly logger: LoggerContext
  ) {}

  public readonly before = this.beforeHandlers.push
  public readonly after = this.afterHandlers.push

  public get(route: string, callback: RouteCallback): void {
    this.logger.debug("Registry route (GET): %s", route)
    const handler = new RouteHandler(callback, this.dispatch, this.getModels)
    this.express.get(route, handler.invoke.bind(handler))
  }

  public post(route: string, callback: RouteCallback): void {
    this.logger.debug("Registry route (POST): %s", route)
    const handler = new RouteHandler(callback, this.dispatch, this.getModels)
    this.express.post(route, handler.invoke.bind(handler))
  }

  public put(route: string, callback: RouteCallback): void {
    this.logger.debug("Registry route (PUT): %s", route)
    const handler = new RouteHandler(callback, this.dispatch, this.getModels)
    this.express.put(route, handler.invoke.bind(handler))
  }

  public delete(route: string, callback: RouteCallback): void {
    this.logger.debug("Registry route (DELETE): %s", route)
    const handler = new RouteHandler(callback, this.dispatch, this.getModels)
    this.express.delete(route, handler.invoke.bind(handler))
  }
}

export class RouteHandler<UserType = any> {
  private isReset = false
  constructor(
    private readonly callback: RouteCallback<UserType>,
    private readonly dispatch: MorkatoAPP['notify'],
    private readonly getModels: () => Database
  ) {}

  public prepareParams(req: Request): Record<string, string> {
    return new Proxy(req.params, {
      get(target, attr) {
        if (typeof attr === 'symbol') {
          throw new InternalServerError({});
        }

        const value = target[attr]
        if (value === undefined) {
          throw new InternalServerError({
            message: `Don't exists key: "${attr}" in target.`
          });
        }
        return value;
      }
    });
  }
  
  public async invoke(req: Request, res: Response, next: NextFunction): Promise<void> {
    try {
      const params = this.prepareParams(req)
      const payload: PayloadType<UserType> = { user: Object.create(null), params, models: this.getModels(), dispatch: this.dispatch, next: next }
      await this.callback(req, res, payload)
    } catch (error) {
      await this.onerror(req, res, next, error as Error)
    }
  }
  
  public async onerror(req: Request, res: Response, next: NextFunction, error: Error): Promise<void> {
    await this.dispatch("request-error", req, res, error, this.getReset(req, res, next))
  }

  public getReset(req: Request, res: Response, next: NextFunction) {
    return (() => {
      if (this.isReset) {
        throw new InternalServerError({});
      }

      this.isReset = true
      return this.invoke(req, res, next);
    }).bind(this);
  }
}