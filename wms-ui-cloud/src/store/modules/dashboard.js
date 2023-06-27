import { dashboardService } from '@/api'

const comparison = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getBinInSummary ({ commit }, options) {
      const {data} = await dashboardService.getBinInSummary(options)
      return data
    },
    async getMissionToDoSummary ({ commit }, options) {
      const {data} = await dashboardService.getMissionToDoSummary(options)
      return data
    },
    async getOldMaterialSummary ({ commit }, options) {
      const {data} = await dashboardService.getOldMaterialSummary(options)
      return data
    },
    async exportOldMaterial ({ commit }, options) {
      const data = await dashboardService.exportOldMaterial(options)
      return data
    },
    async getExpiredMaterial ({ commit }, options) {
      const {data} = await dashboardService.getExpiredMaterial(options)
      return data
    },
    async exportExpiredMaterial ({ commit }, options) {
      const data = await dashboardService.exportExpiredMaterial(options)
      return data
    },
  }
}

export default comparison
