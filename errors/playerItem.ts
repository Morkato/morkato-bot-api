import { AlreadyExistsError } from "./bases/alreadyexists"
import { ErrorType, ErrorParams } from "./bases/base"
import { NotFoundError } from "./bases/notfound"

export class PlayerItemNotFoundError extends NotFoundError {
  private player_id: string
  private guild_id: string
  private item_id: string
  
  constructor({
    message,
    errorLocationCode,
    player_id,
    guild_id,
    item_id
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    player_id: string
    guild_id: string
    item_id: string
  }) {
    super({
      message: message ?? `A relação entre o item (ID: ${item_id})) e o jogador (ID: ${player_id}) não existe nesse servidor (ID: ${guild_id}).`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.PLAYER_ITEM_NOTFOUND
    })

    this.player_id = player_id
    this.guild_id = guild_id
    this.item_id = item_id
  }

  getPlayerID() {
    return this.player_id;
  }

  getGuildID() {
    return this.guild_id;
  }

  getItemID() {
    return this.item_id;
  }
}

export class PlayerItemAlreadyExistsError extends AlreadyExistsError {
  private player_id: string
  private guild_id: string
  private item_id: string
  
  constructor({
    message,
    errorLocationCode,
    player_id,
    guild_id,
    item_id
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    player_id: string
    guild_id: string
    item_id: string
  }) {
    super({
      message: message ?? `A relação entre o item (ID: ${item_id})) e o jogador (ID: ${player_id}) já existe nesse servidor (ID: ${guild_id}).`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.PLAYER_ITEM_ALREADYEXISTS
    })

    this.player_id = player_id
    this.guild_id = guild_id
    this.item_id = item_id
  }

  getPlayerID() {
    return this.player_id;
  }

  getGuildID() {
    return this.guild_id;
  }

  getAttackID() {
    return this.item_id;
  }
}