import type { Database } from '../../models/database'

import { guildQueryBuilder, guildInsertQueryBuilder, guildDeleteQueryBuilder } from '../../models/queries/guilds'
import { ValidationError } from '../../errors'

// "GUILD ID": 199999999999999 IS EXCLUSIVITY FOR TESTS!

const guildID = "199999999999999"
const invalidIds = ["guild-id-invalid-id", "123"]
const expectedInsertQuery = /INSERT INTO "guilds"\("id"\) VALUES \('199999999999999'\) RETURNING \*;/

function getDatabase() {
  return (global as any).database;
}

describe("SQL Builder GUILD QUERIES", () => {
  describe("GUILD CREATE SQL BUILDER", () => {
    test("VALID GUILD CREATE: SQL BUILD GUILD ID: " + guildID, () => {
      const query = guildInsertQueryBuilder.toSQL({
        "id": guildID
      })
  
      expect(query).toMatch(expectedInsertQuery)
    })

    describe("INVALID GUILD CREATE", () => {
      test("SQL BUILD EMPTY KEY GUILD ID", () => {
        expect.assertions(3)
        try {
          // IF USER DON'T REMEMBER TO PASS "guild_id" KEY
          const query = guildInsertQueryBuilder.toSQL({ })
        } catch (err) {
          const error: ValidationError = err as any
          expect(error).toBeInstanceOf(ValidationError)
          expect(error.type).toBe("generic.unknown")
          expect(error.message).toBe("Um erro de validação ocorreu.")
        }
      })
      test("SQL BUILD INVALID GUILD ID", () => {
        // IF USER PASSED KEY: "guild_id" WITH INVALID VALUE
        expect.assertions(invalidIds.length * 4)
        for (const invalid of invalidIds) {
          try {
            const query = guildInsertQueryBuilder.toSQL({
              id: invalid
            })
          } catch (err) {
            const error: ValidationError = err as any
            expect(error).toBeInstanceOf(ValidationError)
            expect(error.type).toBe("string.pattern.base")
            expect(error.errorLocationCode).toBe('MODEL:VALIDATOR:FINAL_SCHEMA')
            expect(error.statusCode).toBe(400)
          }
        }
      })
    })
  })
})