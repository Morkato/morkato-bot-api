import type { MorkatoAPP } from 'morkato'

// import locationRouter from '../routes/location'
// import playerRouter from '../routes/player'
import attackRouter from '../routes/attack'
import guildRouter from '../routes/guild'
// import itemRouter from '../routes/item'
import artRouter from '../routes/art'
import notfound from 'notfound'

const locationCode = "app/router"

export default (app: MorkatoAPP) => {
  app.express.get("/ping", (req, res) => {
    res.status(200).json(Date.now())
  })
  
  app.express.use('/arts', artRouter(app))
  app.express.use('/guilds', guildRouter(app))
  // app.express.use('/items', itemRouter(app))
  app.express.use('/attacks', attackRouter(app))
  // app.express.use('/players', playerRouter(app))
  // app.express.use('/locations', locationRouter(app))
  app.express.use('*', (req, res) => notfound(req, res))
  app.logger.info(locationCode, "All routes have been configured.")
}