import { iqcManagementService } from '@/api'

const iqcManagement = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getIqcQualityList ({ commit }, options) {
      const data = await iqcManagementService.getIqcQualityList(options)
      return data
    },
    async updateIqcQuality ({ commit }, options) {
      const data = await iqcManagementService.updateIqcQuality(options)
      return data
    }
  }
}

export default iqcManagement
