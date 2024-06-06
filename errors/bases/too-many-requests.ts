import { BaseError, ErrorParams } from './base'

export class TooManyRequestsError extends BaseError {
  constructor({ message, type, errorLocationCode }: Omit<ErrorParams, 'statusCode'>) {
    super({
      message: message || 'Você realizou muitas requisições recentemente.',
      errorLocationCode: errorLocationCode,
      statusCode: 429,
      type: type
    });
  }
}