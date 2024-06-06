import type { ErrorParams } from "./bases/base"

import { AlreadyExistsError } from "./bases/alreadyexists"
import { BaseError, ErrorType } from './bases/base'
import { NotFoundError } from "./bases/notfound"

export class PlayerNotFoundError extends NotFoundError {
  private guild_id: string
  private player_id: string

  constructor({
    message,
    errorLocationCode,
    guild_id,
    player_id
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    guild_id: string
    player_id: string
  }) {
    super({
      message: message ?? `O jogador (ID: ${player_id}) não existe neste servidor (ID: ${guild_id})`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.PLAYER_NOTFOUND
    }); 

    this.guild_id = guild_id
    this.player_id = player_id
  }

  getGuildID() {
    return this.guild_id;
  }

  getPlayerID() {
    return this.player_id;
  }
}

export class PlayerAlreadyExistsError extends AlreadyExistsError {
  private guild_id: string
  private player_id: string

  constructor({
    message,
    errorLocationCode,
    guild_id,
    player_id
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    guild_id: string
    player_id: string
  }) {
    super({
      message: message ?? `O jogador (ID: ${player_id}) já existe no servidor (ID: ${guild_id})`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.PLAYER_ALREADYEXISTS
    });

    this.guild_id = guild_id
    this.player_id = player_id
  }

  getGuildID() {
    return this.guild_id;
  }

  getPlayerID() {
    return this.player_id;
  }
}