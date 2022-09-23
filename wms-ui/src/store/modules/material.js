import { materialService } from '@/api'

const material = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await materialService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await materialService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await materialService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await materialService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await materialService.destroy(id)
      return data
    }
  }
}

export default material
