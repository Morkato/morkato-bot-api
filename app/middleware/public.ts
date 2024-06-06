import type { Request, Response, NextFunction } from 'express'
import type { MorkatoAPP } from 'morkato';
import express from 'express'

export default (app: MorkatoAPP) => {
  return express.static(process.cwd() + '/app/public');
}