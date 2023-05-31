import { finishedProductService } from '@/api'

const finishedProduct = {
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
    async getDashboard ({ commit }, options) {
      const data = await finishedProductService.getDashboard(options)
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
    async deleteTask ({ commit }, ids) {
      const data = await finishedProductService.deleteTask(ids)
      return data
    },
    async completeTask ({ commit }, ids) {
      const data = await finishedProductService.completeTask(ids)
      return data
    },
    async getHistoryRecord ({ commit }, options) {
      const data = await finishedProductService.getHistoryRecord(options)
      return data
    },
    async getStorageList ({ commit }, options) {
      const data = await finishedProductService.getStorageList(options)
      return data
    },
    async uploadBatchUpdate ({ commit }, formdata) {
      const data = await finishedProductService.uploadBatchUpdate(formdata)
      return data
    }
  }
}

export default finishedProduct