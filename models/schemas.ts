import { schemas as utilSchemas } from 'utils'
import { ValidationError } from 'errors'
import Joi from 'joi'

export const MORKATOEPOCH = 1716973200000

function uint() {
  return Joi.number().integer().min(0).max(Number.MAX_SAFE_INTEGER - 1)
}

export function validate(
  object: unknown,
  key: (keyof typeof schemas) | string,
  required: boolean
) {
  if (object === undefined) {
    if (required) {
      throw new ValidationError({})
    }
    return undefined;
  }

  try {
    object = JSON.parse(JSON.stringify(object));
  } catch (error) {
    throw new ValidationError({
      message: 'Não foi possível interpretar o valor enviado.',
      errorLocationCode: 'MODEL:VALIDATOR:ERROR_PARSING_JSON'
    })
  }
  const getSchema = schemas[key as keyof typeof schemas]
  const finalSchema = getSchema()
  const { error, value } = finalSchema.validate(object, {
    stripUnknown: true,
    abortEarly: false
  })

  if (error) {
    if (required) {
      throw new ValidationError({
        message: error.details[0].message,
        errorLocationCode: 'MODEL:VALIDATOR:FINAL_SCHEMA',
        type: error.details[0].type
      })
    }
    return undefined;
  }

  return value;
}

export const schemas = {
  name: () => Joi.string().regex(/^\s*[^:0-9][^:]{1,31}\s*$/).trim(),
  key: () => Joi.string().regex(/^[^0-9\s][0-9a-z_\-\.]{1,31}$/),
  surname: () => Joi.string().regex(/^[^0-9A-Z][a-z0-9\-_.]{1,31}$/).trim(),
  intents: () => Joi.number(),
  created_by: () => utilSchemas.id,
  id: () => Joi.string().regex(/^[0-9]{15,}$/).custom(validate_id),
  discord_id: () => Joi.string().regex(/^[0-9]{15,30}$/),
  default_player_life: () => uint(),
  default_player_breath: () => uint(),
  default_player_blood: () => uint(),
  default_player_resistance: () => uint(),
  default_player_force: () => uint(),
  default_player_velocity: () => uint(),
  art_type: () => Joi.string().trim().valid('RESPIRATION', 'KEKKIJUTSU', 'FIGHTING_STYLE'),
  player_type: () => Joi.string().trim().valid('HUMAN', 'ONI', 'HYBRID'),
  resume_description: () => Joi.string().trim().allow(null).min(1).max(128),
  name_prefix_art: () => Joi.string().trim().allow(null).min(1).max(32),
  description: () => Joi.string().trim().allow(null).min(1).max(4096),
  required_exp: () => uint(),
  credibility: () => uint(),
  damage: () => uint(),
  life: () => uint(),
  breath: () => uint(),
  blood: () => uint(),
  cash: () => Joi.number().min(0).max(Math.abs((Number.MAX_SAFE_INTEGER - 1) / 100)).custom((val, helper) => Math.round(val * 100) / 100),
  exp: () => uint(),
  force: () => uint(),
  resistance: () => uint(),
  velocity: () => uint(),
  stack: () => uint().min(1).max(4096),
  usable: () => Joi.boolean(),
  access: () => uint().max((1 << 16) - 1),
  title: () => Joi.string().trim().allow(null).min(1).max(96),
  appearance: () => Joi.string().trim().allow(null).uri(),
  banner: () => Joi.string().trim().allow(null).uri()
}

function validate_id(value: string, helper: Joi.CustomHelpers<number>) {
  const resolvedID = BigInt(value)
  const bits = BigInt(23)
  const timestamp = MORKATOEPOCH + Number(resolvedID >> bits)
  const created_at = new Date(timestamp)

  if (created_at.getTime() > Date.now()) {
    throw helper.error("snowflake.invalid");
  }

  return value;
}

export default validate;