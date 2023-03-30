import {
	finishedProductService
} from '@/api'

const finishedProduc = {
	namespaced: true,
	state: {},

	mutations: {

	},

	actions: {
		async getTaskList({
			commit
		}, options) {
			const data = await finishedProductService.getTaskList(options)
			return data
		},
		async addPackageHistory({
			commit
		}, options) {
			const data = await finishedProductService.addPackageHistory(options)
			return data
		},
		async deleteMultiPackageHistory({
			commit
		}, options) {
			const data = await finishedProductService.deleteMultiPackageHistory(options)
			return data
		},
		async deletePackageHistory({
			commit
		}, options) {
			const data = await finishedProductService.deletePackageHistory(options)
			return data
		},
		
	}
}

export default finishedProduc
