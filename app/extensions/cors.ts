import type { MorkatoAPP } from 'morkato'
import cors from 'cors'

export default (app: MorkatoAPP) => {
  app.express.use(cors({
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
  }))
}