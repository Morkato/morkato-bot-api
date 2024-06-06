import { AlreadyExistsError } from "./bases/alreadyexists"
import { ErrorType, ErrorParams } from "./bases/base"
import { NotFoundError } from "./bases/notfound"

export class PlayerArtNotFoundError extends NotFoundError {
  private player_id: string
  private guild_id: string
  private art_id: string
  
  constructor({
    message,
    errorLocationCode,
    player_id,
    guild_id,
    art_id
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    player_id: string
    guild_id: string
    art_id: string
  }) {
    super({
      message: message ?? `A relação entre a arte (ID: ${art_id})) e o jogador (ID: ${player_id}) não existe nesse servidor (ID: ${guild_id}).`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.PLAYER_ART_NOTFOUND
    })

    this.player_id = player_id
    this.guild_id = guild_id
    this.art_id = art_id
  }

  getPlayerID() {
    return this.player_id;
  }

  getGuildID() {
    return this.guild_id;
  }

  getArtID() {
    return this.art_id;
  }
}

export class PlayerArtAlreadyExistsError extends AlreadyExistsError {
  private player_id: string
  private guild_id: string
  private art_id: string
  
  constructor({
    message,
    errorLocationCode,
    player_id,
    guild_id,
    art_id
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    player_id: string
    guild_id: string
    art_id: string
  }) {
    super({
      message: message ?? `A relação entre a arte (ID: ${art_id})) e o jogador (ID: ${player_id}) já existe nesse servidor (ID: ${guild_id}).`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.PLAYER_ART_ALREADYEXISTS
    })

    this.player_id = player_id
    this.guild_id = guild_id
    this.art_id = art_id
  }

  getPlayerID() {
    return this.player_id;
  }

  getGuildID() {
    return this.guild_id;
  }

  getArtID() {
    return this.art_id;
  }
}