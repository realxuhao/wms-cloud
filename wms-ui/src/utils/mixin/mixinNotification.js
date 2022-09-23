import notification from 'ant-design-vue/es/notification'

const mixinNotification = {
  methods: {
    errorHandler (error, options) {
      if (!error.isHandled) {
        notification.error(options)
      }
    },
    successHandler (options) {
      notification.success(options)
    }
  }
}

export { mixinNotification }
