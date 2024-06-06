import { AlreadyExistsError } from "./bases/alreadyexists"
import { ErrorParams, ErrorType } from "./bases/base"
import { NotFoundError } from "./bases/notfound"

export class LocationNotFoundError extends NotFoundError {
  private guild_id: string
  private location_id: string

  constructor({
    message,
    errorLocationCode,
    guild_id,
    location_id
  }: Pick<ErrorParams, 'message' | 'errorLocationCode'> & {
    guild_id: string
    location_id: string
  }) {
    super({
      message: message ?? `A localização (ID: ${location_id}) não existe no servidor (ID: ${guild_id}).`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.LOCATION_NOTFOUND
    })

    this.location_id = location_id
    this.guild_id = guild_id
  }

  getGuildID() {
    return this.guild_id;
  }
  getLocationID() {
    return this.location_id;
  }
}

export class LocationAlreadyExistsError extends AlreadyExistsError {
  private guild_id: string
  private location_id: string

  constructor({
    message,
    errorLocationCode,
    guild_id,
    location_id
  }: Pick<ErrorParams, 'message' | 'errorLocationCode'> & {
    guild_id: string
    location_id: string
  }) {
    super({
      message: message ?? `A localização (ID: ${location_id}) já existe no servidor (ID: ${guild_id}).`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.LOCATION_ALREADYEXISTS
    })

    this.location_id = location_id
    this.guild_id = guild_id
  }

  getGuildID() {
    return this.guild_id;
  }
  getLocationID() {
    return this.location_id;
  }
}