import { BaseError, ErrorParams } from './base'

export class ValidationError extends BaseError {
  constructor({ message, type, errorLocationCode }: Omit<ErrorParams, 'statusCode'>) {
    super({
      message: message || 'Um erro de validação ocorreu.',
      errorLocationCode: errorLocationCode,
      statusCode: 400,
      type: type
    });
  }
}