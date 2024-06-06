import type { ErrorParams } from "./bases/base"

import { AlreadyExistsError } from "./bases/alreadyexists"
import { NotFoundError } from "./bases/notfound"
import { ErrorType } from './bases/base'

export class ItemNotFoundError extends NotFoundError {
  private guild_id: string
  private item_id: string

  constructor({
    message,
    errorLocationCode,
    guild_id,
    item_id
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    guild_id: string
    item_id: string
  }) {
    super({
      message: message ?? `O item (ID: ${item_id}) não existe no servidor (ID: ${guild_id})`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.ITEM_NOTFOUND
    });

    this.guild_id = guild_id
    this.item_id = item_id
  }

  getGuildID() {
    return this.guild_id;
  }

  getItemID() {
    return this.item_id;
  }
}

export class ItemAlreadyExistsError extends AlreadyExistsError {
  private guild_id: string
  private item_name: string

  constructor({
    message,
    errorLocationCode,
    guild_id,
    item_name
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    guild_id: string
    item_name: string
  }) {
    super({
      message: message ?? `O item chamado: "${item_name}" já existe no servidor (ID: ${guild_id})`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.ITEM_ALREADYEXISTS
    });

    this.guild_id = guild_id
    this.item_name = item_name
  }

  getGuildID() {
    return this.guild_id;
  }

  getItemName() {
    return this.item_name;
  }
}