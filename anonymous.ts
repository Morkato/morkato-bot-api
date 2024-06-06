import type { User } from "app/middleware/auth"

const anonymous: User = Object.freeze({
  name: 'anonymous',
  authorization: 'anonymous',
  roles: []
})

export default anonymous;