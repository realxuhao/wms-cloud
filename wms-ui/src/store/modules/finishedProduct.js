import { finishedProductService } from '@/api'

const finishedProduc = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async planImport ({ commit }, options) {
      const data = await finishedProductService.planImport(options)
      return data
    },
    async genTask ({ commit }, options) {
      const data = await finishedProductService.genTask(options)
      return data
    },
    async getTaskList ({ commit }, options) {
      const data = await finishedProductService.getTaskList(options)
      return data
    },
    async getAllPlanList ({ commit }, options) {
      const data = await finishedProductService.getAllPlanList(options)
      return data
    },
    async destroyPlan ({ commit }, ids) {
      const data = await finishedProductService.destroyPlan(ids)
      return data
    },
    async getHistoryRecord ({ commit }, options) {
      const data = await finishedProductService.getHistoryRecord(options)
      return data
    }
  }
}

export default finishedProduc
