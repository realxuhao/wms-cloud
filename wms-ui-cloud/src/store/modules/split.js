import { splitService } from '@/api'

const split = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async list ({ commit }, options) {
      const data = await splitService.getList(options)
      return data
    }
  }
}

export default split
