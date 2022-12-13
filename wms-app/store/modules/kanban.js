import { kanbanService } from '@/api'
import { removeToken } from '@/utils/storage'

const kanban = {
 namespaced: true,
  state: {
  },

  mutations: {
  },

  actions: {
	async generateOrder({ commit }, options) {
	  const {data} = await kanbanService.generateOrder(options)
	  return data
	},
	async getTranshipmentOrder({ commit }, options) {
	  const {data} = await kanbanService.getTranshipmentOrder(options)
	  return data
	},
	async confirmOrder({ commit }, options) {
	  const {data} = await kanbanService.confirmOrder(options)
	  return data
	},
	async checkKanbanTask({ commit }, options) {
	  const {data} = await kanbanService.checkKanbanTask(options)
	  return data
	},
	async allocateBin({ commit }, options) {
	  const {data} = await kanbanService.allocateBin(options)
	  return data
	},
	async deliver({ commit }, options) {
	  const {data} = await kanbanService.deliver(options)
	  return data
	},
	async splitPallet({ commit }, options) {
	  const {data} = await kanbanService.splitPallet(options)
	  return data
	},
	async parsedBarCode({ commit }, mesBarCode) {
	  const {data} = await kanbanService.parsedBarCode(mesBarCode)
	  return data
	},
	async getWaitingBinIn({ commit }, options) {
	  const {data} = await kanbanService.getWaitingBinIn(options)
	  return data
	},
	
  }
}

export default kanban

