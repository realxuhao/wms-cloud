import { materialFeedingService } from '@/api'

const materialFeeding = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getPaginationList ({ commit }, options) {
      const data = await materialFeedingService.getList(options)
      return data
    },

    async upload ({ commit }, formdata) {
      const data = await materialFeedingService.upload(formdata)
      return data
    },
    async getDepartmentList ({ commit }, options) {
      const { rows } = await materialFeedingService.getDepartmentList({ ...options, pageSize: 0 })
      return rows
    }
  }
}

export default materialFeeding
