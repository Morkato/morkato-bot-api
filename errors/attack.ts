import type { ErrorParams } from "./bases/base"

import { AlreadyExistsError } from "./bases/alreadyexists"
import { NotFoundError } from "./bases/notfound"
import { ErrorType } from './bases/base'

export class AttackNotFoundError extends NotFoundError {
  private attack_id: string
  private guild_id: string
  
  constructor({
    message,
    errorLocationCode,
    guild_id,
    attack_id
  }: Omit<ErrorParams, 'type' | 'statusCode'> & {
    guild_id: string
    attack_id: string
  }) {
    super({
      message: message ?? `O ataque (ID: ${attack_id}) não foi encontrado nesse servidor (ID: ${guild_id})`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.ATTACK_NOTFOUND
    });

    this.attack_id = attack_id
    this.guild_id = guild_id
  }

  getGuildID() {
    return this.guild_id;
  }
  getAttackID() {
    return this.attack_id;
  }
}

export class AttackAlreadyExistsError extends AlreadyExistsError {
  private attack_name: string
  private guild_id: string

  constructor({
    message,
    errorLocationCode,
    guild_id,
    attack_name
  }: Omit<ErrorParams, 'statusCode' | 'type'> & {
    guild_id: string
    attack_name: string
  }) {
    super({
      message: message ?? `O ataque chamado: ${attack_name} já existe no servidor (ID: ${guild_id}).`,
      errorLocationCode: errorLocationCode,
      type: ErrorType.ATTACK_ALREADYEXISTS
    });

    this.attack_name = attack_name
    this.guild_id = guild_id
  }

  getAttackName() {
    return this.attack_name;
  }

  getGuildID() {
    return this.guild_id;
  }
}