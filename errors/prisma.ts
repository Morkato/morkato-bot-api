import { PrismaClientKnownRequestError } from "@prisma/client/runtime/library"
import { ErrorType } from "./bases/base"

export const anonymous = 'generic.unknown'

const uKeysErrorType: Partial<Record<string, string>> = {
  Guild: ErrorType.GUILD_ALREADYEXISTS,
  Art: ErrorType.ART_ALREADYEXISTS,
  Item: ErrorType.ITEM_ALREADYEXISTS,
  Attack: ErrorType.ATTACK_ALREADYEXISTS,
  Player: ErrorType.PLAYER_ALREADYEXISTS,
  PlayerArt: ErrorType.PLAYER_ART_ALREADYEXISTS,
  PlayerAttack: ErrorType.PLAYER_ATTACK_ALREADYEXISTS,
  PlayerItem: ErrorType.PLAYER_ITEM_ALREADYEXISTS,
  Location: ErrorType.LOCATION_ALREADYEXISTS
}

const opNotFoundDepsRecordError: Partial<Record<string, string>> = {
  Guild: ErrorType.GUILD_NOTFOUND,
  Art: ErrorType.ART_NOTFOUND,
  Item: ErrorType.ITEM_NOTFOUND,
  Attack: ErrorType.ATTACK_NOTFOUND,
  Player: ErrorType.PLAYER_NOTFOUND,
  PlayerArt: ErrorType.PLAYER_ART_NOTFOUND,
  PlayerAttack: ErrorType.PLAYER_ATTACK_NOTFOUND,
  PlayerItem: ErrorType.PLAYER_ITEM_NOTFOUND,
  Location: ErrorType.LOCATION_NOTFOUND
}

const fKeysErrorType = {
  fkey: {
    guild_id: opNotFoundDepsRecordError['Guild'],
    art_id: opNotFoundDepsRecordError['Art'],
    item_id: opNotFoundDepsRecordError['Item'],
    parent_id: opNotFoundDepsRecordError['Attack'],
    player_id: opNotFoundDepsRecordError['Player'],
    location_id: opNotFoundDepsRecordError['Location']
  }
}

const errorCodeHandlers: Partial<Record<string, (err: PrismaClientKnownRequestError) => string>> = {
  P2002(err) {
    const { meta } = err

    if (!err) {
      return anonymous;
    }

    return uKeysErrorType[(meta as any).modelName] ?? anonymous;
  },
  P2003(err) {
    try {
      const fields = ((err.meta as any).field_name as string).split(' ', 1)[0].split(/\./g)
      let type: any = fKeysErrorType

      for (let field of fields) {
        type = type[field];
      }

      return type ?? anonymous;
    } catch (err) {
      return anonymous;
    }
  },
  P2025(err) {
    const { meta } = err

    if (!meta) {
      return anonymous;
    }

    return opNotFoundDepsRecordError[(meta as any).modelName] ?? anonymous;
  }
}

export function prismaError(err: PrismaClientKnownRequestError | unknown): string {
  try {
    if (!(err !instanceof PrismaClientKnownRequestError)) {
      return anonymous;
    }

    const handler = errorCodeHandlers[err.code]
  
    if (!handler) {
      return anonymous;
    }

    return handler(err);
  } catch (err) {
    console.error(err)

    return anonymous;
  }
}

export const errors: Record<string, (...args: string[]) => Error> = {}

// export const errors = {
//   'generic.unknown': (location: string) => new InternalServerError({
//     message: `O servidor encontrou uma situação na qual não sabe lidar.`,
//     errorLocationCode: location,
//     type: 'generic.unknown'
//   }),
//   'guild.notfound': (id: string) => new NotFoundError({
//     message: `O servidor (ID: ${id}) requer configuração.`,
//     type: 'guild.notfound'
//   }),
//   'art.notfound': (gid: string, id: string) => new NotFoundError({
//     message: `A arte (ID: ${id}) não existe nessa guilda (ID: ${gid}).`,
//     type: 'art.notfound'
//   }),
//   'item.notfound': (gid: string, id: string) => new NotFoundError({
//     message: `O item (ID: ${id}) não existe nessa guilda (ID: ${gid}).`,
//     type: 'item.notfound'
//   }),
//   'attack.notfound': (gid: string, id: string) => new NotFoundError({
//     message: `O ataque (ID: ${id}) não existe nessa guilda (ID: ${gid}).`,
//     type: 'attack.notfound'
//   }),
//   'player.notfound': (gid: string, id: string) => new NotFoundError({
//     message: `O player (ID: ${id}) não existe nessa guilda (ID: ${gid}).`,
//     type: 'player.notfound'
//   }),
//   'guild.alreadyexists': (id: string) => new AlreadyExistsError({
//     message: `O servidor (ID: ${id}) já está configurado.`,
//     type: 'guild.alreadyexists'
//   }),
//   'art.alreadyexists': (gid: string, name: string) => new AlreadyExistsError({
//     message: `A arte (Nome: ${name}) já existe nessa guilda (ID: ${gid}).`,
//     type: 'art.alreadyexists'
//   }),
//   'item.alreadyexists': (gid: string, name: string) => new AlreadyExistsError({
//     message: `O item (Nome: ${name}) já existe nessa guilda (ID: ${gid}).`,
//     type: 'item.alreadyexists'
//   }),
//   'attack.alreadyexists': (gid: string, name: string) => new AlreadyExistsError({
//     message: `O ataque (Nome: ${name}) já existe nessa guilda (ID: ${gid}).`,
//     type: 'attack.alreadyexists'
//   }),
//   'player.alreadyexists': (gid: string, name: string) => new AlreadyExistsError({
//     message: `O player (Nome: ${name}) já existe nessa guilda (ID: ${gid}).`,
//     type: 'player.alreadyexists'
//   }),
//   'player-item.notfound': (gid: string, pid: string, iid: string) => new AlreadyExistsError({
//     message: `Não existe o item: ${iid} no inventário do player: ${pid} da guilda: ${gid}`,
//     type: 'player-item.notfound'
//   }),
//   'player-item.alreadyexists': (gid: string, pid: string, iid: string) => new AlreadyExistsError({
//     message: `O item: ${iid} já existe no inventário do player: ${pid} da guilda: ${gid}`,
//     type: 'player-item.alreadyexists'
//   })
// }