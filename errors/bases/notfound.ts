import { BaseError, ErrorType, ErrorParams } from "./base"

export class NotFoundError extends BaseError {
  constructor({ message, type, errorLocationCode }: Omit<ErrorParams, 'statusCode'>) {
    super({
      message: message || 'Não foi possível encontrar este recurso no sistema.',
      statusCode: 404,
      type: type ?? ErrorType.GENERIC_NOTFOUND,
      errorLocationCode: errorLocationCode
    });
  }
}