class CustomError extends Error {
  constructor (message, properties) {
    super(message)
    this.name = 'CustomError'
    const { message: msg, name, stack, ...props } = properties
    for (const p in props) {
      if (props.hasOwnProperty(p)) {
        this[p] = props[p]
      }
    }
    Error.captureStackTrace(this, CustomError)
  }
}

class ApiError extends CustomError {
  constructor (message, properties) {
    super(message, properties)
    this.name = 'ApiError'
  }
}

export {
  CustomError,
  ApiError
}
