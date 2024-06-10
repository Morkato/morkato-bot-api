import type { MorkatoAPP } from 'morkato/app'

// import locationRouter from '../routes/location'
// import playerRouter from '../routes/player'
// import attackRouter from '../routes/attack'
// import guildRouter from '../routes/guild'
// import itemRouter from '../routes/item'
// import artRouter from '../routes/art'
// import notfound from 'notfound'
import { NotFoundError } from 'errors'
import express from 'express'
import cors from 'cors'

function getCors() {
  return cors({
    origin: [ 'http://localhost:3000' ],
    methods: ["GET", "POST", "PUT", "DELETE"],
    allowedHeaders: [
      'Content-Type',
      'Content-Length',
      'Content-Take',
      'Content-Skip',
      'Authorization',
      'X-Access-Control'
    ]
  })
}

export default (app: MorkatoAPP) => {
  const jsonHandler = express.json()
  const corsHandler = getCors()

  app.use('/', async (req, res, {next}) => jsonHandler(req, res, next))
  app.use('/', async (req, res, {next}) => corsHandler(req, res, next))

  app.loadController("app/controllers/arts")
  app.loadController("app/controllers/etc")

  app.use('*', (req, res) => Promise.reject(
    new NotFoundError({
      message: "Opps! Parece que você encontrou uma rota que não existe."
    })
  ))
}