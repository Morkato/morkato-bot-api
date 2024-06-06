import { BaseError, ErrorParams } from './base'

export class AlreadyExistsError extends BaseError {
  constructor({ message, type, errorLocationCode }: Omit<ErrorParams, 'statusCode'>) {
    super({
      message: message || 'Esse objeto já exists.',
      errorLocationCode: errorLocationCode,
      statusCode: 400,
      type: type
    });
  }
}