import { onRequestError } from 'app/observers/request-error'
import type { MorkatoAPP } from "morkato/app"

export default async (app: MorkatoAPP) => {
  app.subscribe("request-error", onRequestError)
}