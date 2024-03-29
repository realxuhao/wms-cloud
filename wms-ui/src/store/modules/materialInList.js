import { materialInListService } from '@/api'

const materialInList = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const { data: { rows } } = await materialInListService.getList({ ...options, pageSize: 0 })
      return rows
    },
    async getPaginationList ({ commit }, options) {
      const data = await materialInListService.getList(options)
      return data
    },
    async getPaginationReceiveList ({ commit }, options) {
      const data = await materialInListService.getReceiveList(options)
      return data
    },
    async upload ({ commit }, formdata) {
      const data = await materialInListService.upload(formdata)
      return data
    },
    async uploadBatchUpdate ({ commit }, formdata) {
      const data = await materialInListService.uploadBatchUpdate(formdata)
      return data
    },
    async deleteMaterialReveive ({ commit }, id) {
      const data = await materialInListService.deleteMaterialReveive(id)
      return data
    }
  }
}

export default materialInList
