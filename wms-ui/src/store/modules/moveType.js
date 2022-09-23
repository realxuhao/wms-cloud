import { moveTypeService } from '@/api'

const moveType = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await moveTypeService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await moveTypeService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await moveTypeService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await moveTypeService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await moveTypeService.destroy(id)
      return data
    }
  }
}

export default moveType
