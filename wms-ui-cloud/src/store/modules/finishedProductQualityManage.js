import { finishedProductQualityManageService } from '@/api'

const finishedProductQualityManage = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getQualityList ({ commit }, options) {
      const data = await finishedProductQualityManageService.getQualityList(options)
      return data
    },
    async updateIqcQuality ({ commit }, options) {
      const data = await finishedProductQualityManageService.updateIqcQuality(options)
      return data
    },
    async saveEditSSCCStatusList ({ commit }, options) {
      const data = await finishedProductQualityManageService.saveEditSSCCStatusList(options)
      return data
    },
    async validateStatus ({ commit }, options) {
      const data = await finishedProductQualityManageService.validateStatus(options)
      return data
    }
  }
}

export default finishedProductQualityManage
