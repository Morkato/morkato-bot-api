import { BaseError, ErrorParams } from './base'

export class UnauthorizedError extends BaseError {
  constructor({ message, type, errorLocationCode }: Omit<ErrorParams, 'statusCode'>) {
    super({
      message: message || 'Usuário não autenticado.',
      errorLocationCode: errorLocationCode,
      statusCode: 401,
      type: type
    });
  }
}