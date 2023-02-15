import { blackDriverService } from '@/api'

const blackDriver = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await blackDriverService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await blackDriverService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await blackDriverService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await blackDriverService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await blackDriverService.destroy(id)
      return data
    }
  }
}

export default blackDriver
