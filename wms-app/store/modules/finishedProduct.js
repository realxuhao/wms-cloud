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
		async getAllTaskList({
			commit
		}, options) {
			const data = await finishedProductService.getAllTaskList(options)
			return data
		},

		async getProductInList({
			commit
		}, options) {
			const {
				data
			} = await finishedProductService.getProductInList(options)
			return data
		},
		
		
		async getOneProductIn({
			commit
		}, options) {
			const {
				data
			} = await finishedProductService.getOneProductIn(options)
			return data
		},
		async postProductIn({
			commit
		}, options) {
			const {
				data
			} = await finishedProductService.postProductIn(options)
			return data
		},
		async getProductShiftList({
			commit
		}, options) {
			const {
				data
			} = await finishedProductService.getProductShiftList(options)
			return data
		},
		async getProductReceivingList({
			commit
		}, options) {
			const {
				data
			} = await finishedProductService.getProductReceivingList(options)
			return data
		},
		async postShift({
			commit
		}, options) {
			const {
				data
			} = await finishedProductService.postShift(options)
			return data
		},
		async productShiftGetBinInInfo({
			commit
		}, options) {
			const {
				data
			} = await finishedProductService.productShiftGetBinInInfo(options)
			return data
		},
		async mainReceiveConfirm({ commit }, options) {
			const {data} = await finishedProductService.mainReceiveConfirm(options)
			return data
		},
		async postBinIn({
			commit
		}, options) {
			const {
				data
			} = await finishedProductService.postBinIn(options)
			return data
		},
	}
}

export default finishedProduc
