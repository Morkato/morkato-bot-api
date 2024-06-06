import type { MorkatoAPIWebSocketServer } from './server'

import { MORKATOEPOCH } from 'models/schemas'
import { RoomWebSocket } from './room'
import { v4 as uuidv4 } from 'uuid'
import { WebSocket } from "ws"

export enum WSOpType {
  DISPATCH = 0,
  HEARTBEAT = 1,
  HEARTBEAT_ACK = 2,
  HELLO = 3
}

export type WSMessageType = {
  op: WSOpType
  e?: string
  d?: any
}

export class MorkatoWebSocketSession {
  readonly server: MorkatoAPIWebSocketServer
  readonly rooms: RoomWebSocket[] = []
  private socket: WebSocket | null = null
  readonly isBot: boolean
  public isAlive: boolean = true
  public disconnectedTime: number | null = null
  private isClosedByServer = false
  private latency = Number.POSITIVE_INFINITY
  private lastPingSended = Number.POSITIVE_INFINITY
  private connectedAt = Number.POSITIVE_INFINITY
  private currentTimeout: NodeJS.Timeout | null = null
  readonly id: string = uuidv4()

  constructor(server: MorkatoAPIWebSocketServer, socket?: WebSocket, isBot?: boolean) {
    if (socket) {
      this.socket = socket
    }

    this.server = server
    this.isBot = !!isBot
  }

  async on_connection(): Promise<void> {
    this.connectedAt = Date.now()
    
    this.ping()
    this.send({
      op: WSOpType.HELLO,
      d: {
        message: "This device is connected of gateway",
        session_id: this.id,
        heartbeat_tick: 5000
      }
    })
  }
  async on_message({op, e, d}: WSMessageType): Promise<void> {
    if (op === WSOpType.HEARTBEAT_ACK) {
      this.heartbeat_ack(d)
    } else if (op === WSOpType.HEARTBEAT) {
      this.send({
        op: WSOpType.HEARTBEAT_ACK,
        d: d
      })
    }
  }
  async on_close(): Promise<void> {
    if (!this.isClosedByServer) {
      this.disconnectedTime = Date.now()
      this.terminate()
    }
  }
  async on_error(error: Error): Promise<void> {}
  
  heartbeat_ack(token: string): void {
    if (this.lastPingSended === Number.POSITIVE_INFINITY) {
      return;
    }
    
    try {
      if (token !== this.id) {
        return;
      }

      this.alive()
      this.latency = Math.abs(Date.now() - (MORKATOEPOCH + this.lastPingSended))
      this.lastPingSended = Number.POSITIVE_INFINITY
    } catch(err) {
      return;
    }
  }

  terminate() {
    if (this.socket) {
      this.isClosedByServer = true
      this.socket.close()
      this.socket.terminate()
      this.socket = null
      if (this.currentTimeout) {
        clearTimeout(this.currentTimeout)
      }
      delete this.server.sessions[this.id]
      for (const room of this.rooms) {
        room.removeSession(this.id)
      }
    }
  }

  send({ op, e, d }: WSMessageType): void {
    this.socket?.send(JSON.stringify({ op, e, d }))
  }

  ping() {
    if (!this.socket) {
      return;
    }

    this.lastPingSended = Date.now() - MORKATOEPOCH
    this.send({
      op: WSOpType.HEARTBEAT,
      d: this.latency === Number.POSITIVE_INFINITY ? 999 : this.latency
    })

    if (this.currentTimeout) {
      clearTimeout(this.currentTimeout)
    }
    
    this.currentTimeout = setTimeout(() => {
      if (!this.isAlive) {
        this.terminate()
        this.currentTimeout = null
        return;
      }

      this.ping()
    }, 5000)
  }

  join(name: string) {
    const room = this.server.room(name)
    room.addSSession(this)
    this.rooms.push(room)
  }
  alive() {
    if (this.socket){
      this.isAlive = true
    }
  }

  getLatency() {
    return this.latency;
  }
}