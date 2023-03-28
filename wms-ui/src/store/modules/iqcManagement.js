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
    },
    async uploadEditSSCCStatusFile ({ commit }, formdata) {
      const data = await iqcManagementService.uploadEditSSCCStatusFile(formdata)
      return data
    },
    async saveEditSSCCStatusList ({ commit }, options) {
      const data = await iqcManagementService.saveEditSSCCStatusList(options)
      return data
    },
    async modifyQuantity ({ commit }, options) {
      const data = await iqcManagementService.modifyQuantity(options)
      return data
    },
    async issueJob ({ commit }, options) {
      const data = await iqcManagementService.issueJob(options)
      return data
    }

  }
}

export default iqcManagement
