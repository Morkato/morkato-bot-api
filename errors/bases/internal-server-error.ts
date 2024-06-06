import { BaseError, ErrorParams } from "./base"

export class InternalServerError extends BaseError {
  constructor({ message, statusCode, errorLocationCode, type }: ErrorParams) {
    super({
      message: message || 'Um erro interno não esperado aconteceu.',
      statusCode: statusCode || 500,
      errorLocationCode: errorLocationCode,
      type: type
    });
  }
}