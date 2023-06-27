import {
	binInService
} from '@/api'
import {
	removeToken
} from '@/utils/storage'

const binIn = {
	namespaced: true,
	state: {},

	mutations: {},

	actions: {
		async getPalletList({
			commit
		}, options) {
			const {
				data: {
					rows
				}
			} = await binInService.getPalletList({
				...options,
				pageSize: 0
			})
			return rows
		},
		async getPendingBinInList({
			commit
		}, parameter) {
			const {
				data
			} = await binInService.getPendingBinInList(parameter)
			return data
		},
		async getBinInHistoryList({
			commit
		}, parameter) {
			const {
				data
			} = await binInService.getBinInHistoryList(parameter)
			return data
		},
		async getByMesBarCode({
			commit
		}, barCode) {
			const {
				data
			} = await binInService.getByMesBarCode(barCode)
			return data
		},
		async postMaterialIn({
			commit
		}, parameter) {
			const {
				data
			} = await binInService.postMaterialIn(parameter)
			return data
		},
		async getPalletTypeCode({
			commit
		}, palletTypeId) {
			const {
				data
			} = await binInService.getPalletTypeCode(palletTypeId)
			return data
		},
		async generateInTask({
			commit
		}, options) {
			const {
				data
			} = await binInService.generateInTask(options)
			return data
		},
		async allocate({
			commit
		}, options) {
			const {
				data
			} = await binInService.allocate(options)
			return data
		},
		async postBinIn({
			commit
		}, options) {
			const {
				data
			} = await binInService.postBinIn(options)
			return data
		},
		async batchBinIn({
			commit
		}, options) {
			const {
				data
			} = await binInService.batchBinIn(options)
			return data
		},

		async getWeightList({
			commit
		}, options) {
			const {
				data
			} = await binInService.getWeightList(options)
			return data
		},
		async getWeightByIp({
			commit
		}, ip) {
			const {
				data
			} = await binInService.getWeightByIp(ip)
			return data
		},

	}
}

export default binIn