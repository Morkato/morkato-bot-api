import { StringView } from "./view"

export enum LoggerLevel {
  DEBUG = 0,
  INFO = 1,
  WARNING = 2,
  ERROR = 3,
  CRITICAL = 4
}

const directiveLevel: Record<LoggerLevel, (...args: any[]) => void> = {
  [LoggerLevel.DEBUG]: console.debug,
  [LoggerLevel.INFO]: console.log,
  [LoggerLevel.WARNING]: console.warn,
  [LoggerLevel.ERROR]: console.error,
  [LoggerLevel.CRITICAL]: console.error
}

const levelsName: Record<LoggerLevel, string> = {
  [LoggerLevel.DEBUG]: "DEBUG",
  [LoggerLevel.INFO]: "INFO",
  [LoggerLevel.WARNING]: "WARNING",
  [LoggerLevel.ERROR]: "ERROR",
  [LoggerLevel.CRITICAL]: "CRITICAL"
}

export interface Logger {
  setDefaultFormatter(formatter: string): void
  setFormatter(amb: string, formatter: string): void
  ambient(amb: string, levels: Set<LoggerLevel>): void
  
  debug(locode: string, message: string, ...args: unknown[]): void
  info(locode: string, message: string, ...args: unknown[]): void
  warn(locode: string, message: string, ...args: unknown[]): void
  error(locode: string, message: string, error?: Error,  ...args: unknown[]): void
  critical(locode: string, message: string, error?: Error,  ...args: unknown[]): void
}

export function createLogger(amb: string): Logger {
  const ambients: Record<string, Set<LoggerLevel>> = {}
  const formatters: Record<string, string> = {}
  let defaultFormatter = "%message%"
  let currentAmbient = amb
  let dateFormatter = "%Y/%m/%d %H:%M:%S"
  
  function ambient(amb: string, levels: Set<LoggerLevel>): void {
    ambients[amb] = levels
  }

  function setDefaultFormatter(formatter: string): void {
    defaultFormatter = formatter
  }

  function setFormatter(amb: string, formatter: string): void {
    formatters[amb] = formatter
  }

  function formatText(text: string, args: string[], kwargs: Record<string, unknown>): string {
    const newText = StringView.formatLoggerSintaxe(text, args, kwargs)
    return newText;
  }

  function _log(level: LoggerLevel, locode: string, message: string, error?: Error, ...args: unknown[]): void {
    const ambContext = ambients[currentAmbient]
    if (!ambContext || !ambContext.has(level)) {
      return;
    }
    
    const kwargs = {
      message: StringView.formatLoggerSintaxe(message, args, {}),
      level: level,
      levelname: levelsName[level],
      datetime: StringView.timeFormat(new Date(), dateFormatter),
      localcode: locode
    }

    const formatter = formatters[currentAmbient] ?? defaultFormatter
    const content = StringView.formatLoggerSintaxe(formatter, [], kwargs)
    const directive = directiveLevel[level]
    directive(content)
  }

  return Object.assign({ setDefaultFormatter, setFormatter, ambient }, {
    debug: (locode: string, message: string, ...args: unknown[]) => _log(LoggerLevel.DEBUG, locode, message, undefined, ...args),
    info: (locode: string, message: string, ...args: unknown[]) => _log(LoggerLevel.INFO, locode, message, undefined, ...args),
    warn: (locode: string, message: string, ...args: unknown[]) => _log(LoggerLevel.WARNING, locode, message, undefined, ...args),
    error: (locode: string, message: string, error: Error,  ...args: unknown[]) => _log(LoggerLevel.ERROR, locode, message, error, ...args),
    critical: (locode: string, message: string, error: Error,  ...args: unknown[]) => _log(LoggerLevel.ERROR, locode, message, error, ...args)
  });
}