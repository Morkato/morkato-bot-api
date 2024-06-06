import { AlreadyExistsError } from "./bases/alreadyexists"
import { ErrorType, ErrorParams } from "./bases/base"
import { NotFoundError } from "./bases/notfound"

export class GuildNotFoundError extends NotFoundError {
  private guild_id: string

  constructor({
    message,
    errorLocationCode,
    guild_id
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    guild_id: string
  }) {
    super({
      message: message ?? `O servidor (ID: ${guild_id}) não foi encontrado.`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.GUILD_NOTFOUND
    });

    this.guild_id = guild_id
  }

  getGuildID() {
    return this.guild_id;
  }
}

export class GuildAlreadyExistsError extends AlreadyExistsError {
  private guild_id: string

  constructor({
    message,
    errorLocationCode,
    guild_id
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    guild_id: string
  }) {
    super({
      message: message ?? `O servidor (ID: ${guild_id}) já existe.`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.GUILD_ALREADYEXISTS
    });

    this.guild_id = guild_id
  }

  getGuildID() {
    return this.guild_id;
  }
}