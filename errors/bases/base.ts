import type { Request } from "express"

export interface ErrorParams {
  message?: string
  statusCode?: number
  errorLocationCode?: string
  type?: ErrorType | string
}

export enum ErrorType {
  GENERIC = "generic.unknown",
  GENERIC_NOTFOUND = "generic.notfound",
  GENERIC_ALREADYEXISTS = "generic.alreadyexists",

  GUILD_NOTFOUND = "guild.notfound",
  GUILD_ALREADYEXISTS = "guild.alreadyexists",

  ART_NOTFOUND = "art.notfound",
  ART_ALREADYEXISTS = "art.alreadyexists",

  ATTACK_NOTFOUND = "attack.notfound",
  ATTACK_ALREADYEXISTS = "attack.alreadyexists",

  ITEM_NOTFOUND = "item.notfound",
  ITEM_ALREADYEXISTS = "item.alreadyexists",

  PLAYER_NOTFOUND = "player.notfound",
  PLAYER_ALREADYEXISTS = "player.alreadyexists",

  PLAYER_ITEM_NOTFOUND = "player-item.notfound",
  PLAYER_ITEM_ALREADYEXISTS = "player-item.alreadyexists",
  PLAYER_INVENTORY_EMPTY = "player-inventory.empty",

  PLAYER_ART_NOTFOUND = "player-art.notfound",
  PLAYER_ART_ALREADYEXISTS = "player-art.alreadyexists",

  PLAYER_ATTACK_NOTFOUND = "player-attack.notfound",
  PLAYER_ATTACK_ALREADYEXISTS = "player-attack.alreadyexists",
  
  LOCATION_NOTFOUND = "location.notfound",
  LOCATION_ALREADYEXISTS = "location.alreadyexists"
}

export class BaseError extends Error {
  statusCode?: number
  errorLocationCode?: string
  type: ErrorType | string

  constructor({
    message,
    statusCode,
    errorLocationCode,
    type
  }: ErrorParams) {
    super();
    this.name = this.constructor.name;
    this.message = message || "Base error";
    this.statusCode = statusCode || 500;
    this.errorLocationCode = errorLocationCode;
    this.type = type ?? ErrorType.GENERIC
  }

  getJSONResponse(req: Request): Record<string, unknown> {
    return {
      message: this.message,
      location: this.errorLocationCode ?? null,
      type: this.type,
      name: this.name
    }
  }
}