import { binInService } from '@/api'

const binIn = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getPaginationList ({ commit }, options) {
      const data = await binInService.getList(options)
      return data
    },
    async delete ({ commit }, id) {
      const data = await binInService.delete(id)
      return data
    }
  }
}

export default binIn
