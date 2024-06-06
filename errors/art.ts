import { AlreadyExistsError } from "./bases/alreadyexists"
import { ErrorType, ErrorParams } from "./bases/base"
import { NotFoundError } from "./bases/notfound"

export class ArtNotFoundError extends NotFoundError {
  private guild_id: string
  private art_id: string
  
  constructor({
    message,
    errorLocationCode,
    guild_id,
    art_id
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    guild_id: string,
    art_id: string
  }) {
    super({
      message: message ?? `A arte (ID: ${art_id}) não foi encontrada no servidor (ID: ${guild_id})`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.ART_NOTFOUND
    })

    this.guild_id = guild_id
    this.art_id = art_id
  }

  getGuildID() {
    return this.guild_id;
  }

  getArtID() {
    return this.art_id;
  }
}

export class ArtAlreadyExistsError extends AlreadyExistsError {
  private guild_id: string
  private art_name: string
  
  constructor({
    message,
    errorLocationCode,
    guild_id,
    art_name
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    guild_id: string,
    art_name: string
  }) {
    super({
      message: message ?? `A arte (Nome: ${art_name}) foi encontrada no servidor (ID: ${guild_id})`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.ART_ALREADYEXISTS
    })

    this.guild_id = guild_id
    this.art_name = art_name
  }

  getGuildID() {
    return this.guild_id;
  }

  getArtName() {
    return this.art_name;
  }
}