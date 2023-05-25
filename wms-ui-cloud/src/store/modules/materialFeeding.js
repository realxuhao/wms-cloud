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
    async getCallList ({ commit }, options) {
      const data = await materialFeedingService.getCallList(options)
      return data
    },
    async upload ({ commit }, formdata) {
      const data = await materialFeedingService.upload(formdata)
      return data
    },
    async getDepartmentList ({ commit }, options) {
      const { rows } = await materialFeedingService.getDepartmentList({ ...options, pageSize: 0 })
      return rows
    },
    async getRuleList ({ commit }, options) {
      const data = await materialFeedingService.getRuleList({ ...options })
      return data
    },
    async checkStock ({ commit }, options) {
      const data = await materialFeedingService.checkStock({ ...options })
      return data
    },
    async callSystemStock ({ commit }, options) {
      const data = await materialFeedingService.callSystemStock({ ...options })
      return data
    },
    async updateQuantity ({ commit }, options) {
      const data = await materialFeedingService.updateQuantity(options)
      return data
    },
    async callPersonStock ({ commit }, options) {
      const data = await materialFeedingService.callPersonStock(options)
      return data
    },
    async getPickingOrderList ({ commit }, options) {
      const data = await materialFeedingService.getPickingOrderList(options)
      return data
    },
    async batchAddJob ({ commit }, options) {
      const data = await materialFeedingService.batchAddJob(options)
      return data
    },
    async cancelPickingOrder ({ commit }, options) {
      const data = await materialFeedingService.cancelPickingOrder(options)
      return data
    },
    async exportExcel ({ commit }, options) {
      const data = await materialFeedingService.exportExcel(options)
      return data
    },
    async exportCallExcel ({ commit }, options) {
      const data = await materialFeedingService.exportCallExcel(options)
      return data
    },
    async getStockInfo ({ commit }, options) {
      const data = await materialFeedingService.getStockInfo(options)
      return data
    },
    async addShiftTask ({ commit }, options) {
      const data = await materialFeedingService.addShiftTask(options)
      return data
    },
    async confirmMaterial ({ commit }, options) {
      const data = await materialFeedingService.confirmMaterial(options)
      return data
    },
    async cancelFeeding ({ commit }, options) {
      const data = await materialFeedingService.cancelFeeding(options)
      return data
    },
    async runCall ({ commit }, ids) {
      const data = await materialFeedingService.runCall(ids)
      return data
    },
    async issueCall ({ commit }, ids) {
      const data = await materialFeedingService.issueCall(ids)
      return data
    },
    async generateJobByCall ({ commit }, ids) {
      const data = await materialFeedingService.generateJobByCall(ids)
      return data
    },
    async callAdd ({ commit }, options) {
      const data = await materialFeedingService.callAdd(options)
      return data
    }

  }
}

export default materialFeeding
