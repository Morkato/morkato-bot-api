import { MorkatoWebSocketSession, WSMessageType } from "./session"
import { WebSocket, WebSocketServer } from "ws"
import { RoomWebSocket } from "./room"

export class MorkatoAPIWebSocketServer extends WebSocketServer{
  readonly sessions: Partial<Record<string, MorkatoWebSocketSession>> = {}
  readonly rooms: Partial<Record<string, RoomWebSocket>> = {}
  
  constructor(query: any) {
    super(query)

    this.on("connection", this.on_connection)
    this.on("close", this.on_close)
    this.on("error", this.on_error)
    this.on("headers", this.on_headers)
    this.on("listening", this.on_listening)
  }

  async on_connection(socket: WebSocket): Promise<void> {
    const session = this.createSession(socket)

    session.on_connection()
    socket.on("message", (data) => {
      try {
        const resolved = JSON.parse(data.toString())

        if (typeof resolved.op !== 'number') {
          return;
        }

        session.on_message(resolved)
      } catch(err) {

      }
    })
    socket.on("close", () => session.on_close())
    socket.on("error", (err) => session.on_error(err))
  }
  async on_error(socket: WebSocket): Promise<void> {}
  async on_close(socket: WebSocket): Promise<void> {}
  async on_headers(socket: WebSocket): Promise<void> {}
  async on_listening(socket: WebSocket): Promise<void> {}

  createSession(socket: WebSocket): MorkatoWebSocketSession {
    const session = new MorkatoWebSocketSession(this, socket, true)
    this.sessions[session.id] = session    

    return session;
  }
  room(name: string): RoomWebSocket {
    let room = this.rooms[name]

    if (room === undefined) {
      room = new RoomWebSocket(name)
      this.rooms[room.name] = room
    }

    return room;
  }
  send(query: WSMessageType, broadcast?: string) {
    for (const [key, value] of Object.entries(this.sessions)) {
      if (broadcast === key) {
        continue;
      }

      value?.send(query)
    }
  }
}