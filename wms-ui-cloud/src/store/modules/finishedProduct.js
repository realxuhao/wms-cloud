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
    },
    async importSPDN ({ commit }, formdata) {
      const data = await finishedProductService.importSPDN(formdata)
      return data
    },
    async importSUDN ({ commit }, formdata) {
      const data = await finishedProductService.importSUDN(formdata)
      return data
    },
    async spdnList ({ commit }, options) {
      const data = await finishedProductService.spdnList(options)
      return data
    },
    async approveSpdnList ({ commit }, options) {
      const data = await finishedProductService.approveSpdnList(options)
      return data
    },
    async spdnStocklist ({ commit }, options) {
      const data = await finishedProductService.spdnStocklist(options)
      return data
    },
    async deleteSpdn ({ commit }, options) {
      const data = await finishedProductService.deleteSpdn(options)
      return data
    },
    async SUQAIQCManagementList ({ commit }, options) {
      const data = await finishedProductService.SUQAIQCManagementList(options)
      return data
    },
    async changeSUQAStatus ({ commit }, options) {
      const data = await finishedProductService.changeSUQAStatus(options)
      return data
    },
    async validateSUQAStatus ({ commit }, options) {
      const data = await finishedProductService.validateSUQAStatus(options)
      return data
    },
    async spdnPicklist ({ commit }, options) {
      const data = await finishedProductService.spdnPicklist(options)
      return data
    },
    async sudnList ({ commit }, options) {
      const data = await finishedProductService.sudnList(options)
      return data
    },
    async sudnDelete ({ commit }, options) {
      const data = await finishedProductService.sudnDelete(options)
      return data
    },
    async sudnGenerate ({ commit }, options) {
      const data = await finishedProductService.sudnGenerate(options)
      return data
    },
    async sudnUpdateQuantity ({ commit }, options) {
      const data = await finishedProductService.sudnUpdateQuantity(options)
      return data
    },
    async sudnPickList ({ commit }, options) {
      const data = await finishedProductService.sudnPickList(options)
      return data
    },
    async sudnPickBatchIssue ({ commit }, ids) {
      const data = await finishedProductService.sudnPickBatchIssue(ids)
      return data
    },

    async exportSUDNPickExcel ({ commit }, options) {
      const data = await finishedProductService.exportSUDNPickExcel(options)
      return data
    },



    // spdnPicklist
  }
}

export default finishedProduct
