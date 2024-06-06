import { BaseError } from "./bases/base"

import { InternalServerError } from "./bases/internal-server-error"
import { TooManyRequestsError } from "./bases/too-many-requests"
import { AlreadyExistsError } from "./bases/alreadyexists"
import { UnauthorizedError } from "./bases/unauthorized"
import { ValidationError } from "./bases/validation"
import { NotFoundError } from "./bases/notfound"

import { PlayerAttackAlreadyExistsError, PlayerAttackNotFoundError } from './playerAttack'
import { PlayerItemAlreadyExistsError, PlayerItemNotFoundError } from './playerItem'
import { PlayerArtAlreadyExistsError, PlayerArtNotFoundError } from './playerArt'
import { LocationAlreadyExistsError, LocationNotFoundError } from "./location"
import { AttackAlreadyExistsError, AttackNotFoundError } from "./attack"
import { PlayerAlreadyExistsError, PlayerNotFoundError } from "./player"
import { GuildAlreadyExistsError, GuildNotFoundError } from "./guild"
import { ItemAlreadyExistsError, ItemNotFoundError } from "./item"
import { ArtAlreadyExistsError, ArtNotFoundError } from "./art"

export {
  BaseError,

  TooManyRequestsError,
  InternalServerError,
  AlreadyExistsError,
  UnauthorizedError,
  ValidationError,
  NotFoundError,

  PlayerAttackAlreadyExistsError,
  PlayerAttackNotFoundError,
  PlayerItemAlreadyExistsError,
  PlayerItemNotFoundError,
  PlayerArtAlreadyExistsError,
  PlayerArtNotFoundError,
  LocationAlreadyExistsError,
  LocationNotFoundError,
  AttackAlreadyExistsError,
  AttackNotFoundError,
  PlayerAlreadyExistsError,
  PlayerNotFoundError,
  GuildAlreadyExistsError,
  GuildNotFoundError,
  ItemAlreadyExistsError,
  ItemNotFoundError,
  ArtAlreadyExistsError,
  ArtNotFoundError
}

export default Object.freeze({
  BaseError,

  TooManyRequestsError,
  InternalServerError,
  AlreadyExistsError,
  UnauthorizedError,
  ValidationError,
  NotFoundError,

  PlayerAttackAlreadyExistsError,
  PlayerAttackNotFoundError,
  PlayerArtAlreadyExistsError,
  PlayerArtNotFoundError,
  PlayerItemAlreadyExistsError,
  PlayerItemNotFoundError,
  LocationAlreadyExistsError,
  LocationNotFoundError,
  AttackAlreadyExistsError,
  AttackNotFoundError,
  PlayerAlreadyExistsError,
  PlayerNotFoundError,
  GuildAlreadyExistsError,
  GuildNotFoundError,
  ItemAlreadyExistsError,
  ItemNotFoundError,
  ArtAlreadyExistsError,
  ArtNotFoundError
});