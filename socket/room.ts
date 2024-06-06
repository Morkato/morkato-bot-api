import type { MorkatoWebSocketSession, WSMessageType } from './session'

export class RoomWebSocket {
  readonly rooms: Record<string, RoomWebSocket> = {}
  readonly sessions: Record<string, MorkatoWebSocketSession> = {}
  readonly name: string
  
  constructor(name: string) {
    this.name = name
  }

  room(name: string): RoomWebSocket {
    let room = this.rooms[name]

    if (room === undefined) {
      room = new RoomWebSocket(name)
      this.rooms[room.name] = room
    }

    return room;
  }
  addSSession(session: MorkatoWebSocketSession) {
    this.sessions[session.id] = session
  }
  removeSession(session_id: string) {
    delete this.sessions[session_id]
  }
  send(query: WSMessageType) {
    Object.values(this.sessions).forEach(session => session.send(query))
  }
}