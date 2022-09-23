const mixinEvent = {
  methods: {
    stopPropagation (e) {
      e.stopPropagation()
    },
    preventDefault (e) {
      e.preventDefault()
    }
  }
}

export { mixinEvent }
