import {
	wareShiftService
} from '@/api'

const wareShift = {
	namespaced: true,
	state: {},

	mutations: {},

	actions: {

		async getList({
			commit
		}, parameter) {
			const {
				data
			} = await wareShiftService.getList({
				...parameter
			})
			return data
		},
		async innerReceivingList({
			commit
		}, parameter) {
			const {
				data
			} = await wareShiftService.innerReceivingList({
				...parameter
			})
			return data
		},
		async getWaitingBinIn({
			commit
		}, parameter) {
			const {
				data
			} = await wareShiftService.getWaitingBinIn({
				...parameter
			})
			return data
		},
		async binDown({
			commit
		}, barCode) {
			const {
				data
			} = await wareShiftService.binDown(barCode)
			return data
		},
		async getByMesBarCode({
			commit
		}, barCode) {
			const {
				data
			} = await wareShiftService.allocateBin(barCode)
			return data
		},
		async getReturnMaterialList({
			commit
		}, options) {
			const {
				data
			} = await wareShiftService.getReturnMaterialList(options)
			return data
		},
		async addMaterialReturn({
			commit
		}, options) {
			const {
				data
			} = await wareShiftService.addMaterialReturn(options)
			return data
		},
		async getCellList({
			commit
		}, options) {
			const {
				rows
			} = await wareShiftService.getCellList({
				...options,
				pageSize: 0
			})
			return rows
		},
		async getWareList({
			commit
		}, options) {
			const {
				data: {
					rows
				}
			} = await wareShiftService.getWareList({
				...options,
				pageSize: 0
			})
			return rows
		},
		async returnMaterialConfirm({
			commit
		}, options) {
			const data = await wareShiftService.returnMaterialConfirm(options)
			return data
		},
		async getAllocateBin({
			commit
		}, mesBarcode) {
			const {
				data
			} = await wareShiftService.getAllocateBin(mesBarcode)
			return data
		},
		async postBinIn({
			commit
		}, options) {
			const {
				data
			} = await wareShiftService.postBinIn(options)
			return data
		},

		async shiftBinIn({
			commit
		}, options) {
			const {
				data
			} = await wareShiftService.shiftBinIn(options)
			return data
		},

		async getOneBinDown({
			commit
		}, options) {
			const {
				data
			} = await wareShiftService.getOneBinDown(options)
			return data
		},
		async splitPallet({
			commit
		}, options) {
			const {
				data
			} = await wareShiftService.splitPallet(options)
			return data
		},
		
		async getTransInfo({
			commit
		}, barCode) {
			const {
				data
			} = await wareShiftService.getTransInfo(barCode)
			return data
		},
		async getOne({
			commit
		}, barCode) {
			const {
				data
			} = await wareShiftService.getOne(barCode)
			return data
		},
		async batchBinIn({
			commit
		}, options) {
			const {
				data
			} = await wareShiftService.batchBinIn(options)
			return data
		},
	}
}

export default wareShift
