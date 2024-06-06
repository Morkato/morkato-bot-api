import type { MorkatoAPP } from 'morkato'

import { json } from 'express'
import logging from '../middleware/logger'
import _public from '../middleware/public'
import auth from '../middleware/auth'

export default (app: MorkatoAPP) => {
  app.express.use(_public(app))
  app.express.use(json())
  app.express.use(auth(app))
  app.express.use(logging(app))
}