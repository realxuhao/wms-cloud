import { dashboardService } from '@/api'

const comparison = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getBinInSummary ({ commit }, options) {
      const { data } = await dashboardService.getBinInSummary(options)
      return data
    },
    async getMissionToDoSummary ({ commit }, options) {
      const { data } = await dashboardService.getMissionToDoSummary(options)
      return data
    },
    async getProMissionToDoSummary ({ commit }, options) {
      const { data } = await dashboardService.getProMissionToDoSummary(options)
      return data
    },
    async getOldMaterialSummary ({ commit }, options) {
      const { data } = await dashboardService.getOldMaterialSummary(options)
      return data
    },
    async exportOldMaterial ({ commit }, options) {
      const data = await dashboardService.exportOldMaterial(options)
      return data
    },
    async getExpiredMaterial ({ commit }, options) {
      console.log(options)
      const { data } = await dashboardService.getExpiredMaterial(options)
      return data
    },
    async exportExpiredMaterial ({ commit }, options) {
      const data = await dashboardService.exportExpiredMaterial(options)
      return data
    },
    async getWareShiftList ({ commit }, options) {
      const { data } = await dashboardService.getWareShiftList(options)
      return data
    },
    async getWorkload
      ({ commit }, options) {
      const { data } = await dashboardService.getWorkload(options)
      return data
    },

    async exportWorkload
      ({ commit }, options) {
      const { data } = await dashboardService.exportWorkload(options)
      return data
    },
    async getWorkloadPro
      ({ commit }, options) {
      const { data } = await dashboardService.getWorkloadPro(options)
      return data
    },

    async exportWorkloadPro
      ({ commit }, options) {
      const { data } = await dashboardService.exportWorkloadPro(options)
      return data
    },
    async processEfficiency
      ({ commit }, options) {
      const { data } = await dashboardService.processEfficiency(options)
      return data
    },
    async proInOutStock
      ({ commit }, options) {
      const { data } = await dashboardService.proInOutStock(options)
      return data
    },
    async proInOutStockExport
      ({ commit }, options) {
      const  data  = await dashboardService.proInOutStockExport(options)
      return data
    },

    

  }
}

export default comparison
