import { BaseError, ErrorParams } from './base'

export class BadRequestError extends BaseError {
  constructor({ message, type, errorLocationCode }: Omit<ErrorParams, 'statusCode'>) {
    super({
      message: message || 'Você fez alguma coisa errada.',
      errorLocationCode: errorLocationCode,
      statusCode: 400,
      type: type
    });
  }
}