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

	}
}

export default finishedProduc
