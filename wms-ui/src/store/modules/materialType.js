import { materialTypeService } from '@/api'

const materialType = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await materialTypeService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await materialTypeService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await materialTypeService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await materialTypeService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await materialTypeService.destroy(id)
      return data
    }
  }
}

export default materialType
