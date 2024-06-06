import { AlreadyExistsError } from "./bases/alreadyexists"
import { ErrorType, ErrorParams } from "./bases/base"
import { NotFoundError } from "./bases/notfound"

export class PlayerAttackNotFoundError extends NotFoundError {
  private player_id: string
  private guild_id: string
  private attack_id: string
  
  constructor({
    message,
    errorLocationCode,
    player_id,
    guild_id,
    attack_id
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    player_id: string
    guild_id: string
    attack_id: string
  }) {
    super({
      message: message ?? `A relação entre o ataque (ID: ${attack_id})) e o jogador (ID: ${player_id}) não existe nesse servidor (ID: ${guild_id}).`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.PLAYER_ATTACK_NOTFOUND
    })

    this.player_id = player_id
    this.guild_id = guild_id
    this.attack_id = attack_id
  }

  getPlayerID() {
    return this.player_id;
  }

  getGuildID() {
    return this.guild_id;
  }

  getAttackID() {
    return this.attack_id;
  }
}

export class PlayerAttackAlreadyExistsError extends AlreadyExistsError {
  private player_id: string
  private guild_id: string
  private attack_id: string
  
  constructor({
    message,
    errorLocationCode,
    player_id,
    guild_id,
    attack_id
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    player_id: string
    guild_id: string
    attack_id: string
  }) {
    super({
      message: message ?? `A relação entre o ataque (ID: ${attack_id})) e o jogador (ID: ${player_id}) já existe nesse servidor (ID: ${guild_id}).`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.PLAYER_ATTACK_ALREADYEXISTS
    })

    this.player_id = player_id
    this.guild_id = guild_id
    this.attack_id = attack_id
  }

  getPlayerID() {
    return this.player_id;
  }

  getGuildID() {
    return this.guild_id;
  }

  getAttackID() {
    return this.attack_id;
  }
}