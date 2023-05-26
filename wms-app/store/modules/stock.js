import {
	stockService
} from '@/api'

const stock = {
	namespaced: true,
	state: {},

	mutations: {},

	actions: {
		async getInfoByMesBarCode({
			commit
		}, mesBarCode) {
			const {
				data
			} = await stockService.getStockInfoByMesBarCode(mesBarCode)
			return data
		},
		async getInfoBySscc({
			commit
		}, sscc) {
			const {
				data
			} = await stockService.getStockInfoBySscc(sscc)
			return data
		},
		async getByMesBarCode({
			commit
		}, mesBarCode) {
			const {
				data
			} = await stockService.getStockInfoByMesBarCode(mesBarCode)
			return data
		},
		async getByBinCode({
			commit
		}, binCode) {
			const {
				data
			} = await stockService.getByBinCode(binCode)
			return data
		},
		async addSplit({
			commit
		}, options) {
			const {
				data
			} = await stockService.addSplit(options)
			return data
		},

		async getStockTakeList({
			commit
		}, options) {
			const {
				data
			} = await stockService.getStockTakeList(options)
			return data
		},
		async getStockTakeDetailList({
			commit
		}, options) {
			const {
				data
			} = await stockService.getStockTakeDetailList(options)
			return data
		},
		async getStockTakeDetail({
			commit
		}, options) {
			const {
				data
			} = await stockService.getStockTakeDetail(options)
			return data
		},
		async operate({
			commit
		}, options) {
			const {
				data
			} = await stockService.operate(options)
			return data
		},
		async editStock({
			commit
		}, options) {
			const {
				data
			} = await stockService.editStock(options)
			return data
		},

	}
}

export default stock
