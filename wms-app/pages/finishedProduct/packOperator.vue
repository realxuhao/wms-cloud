<template>
	<my-page nav-title="打包">
		<view class="main" slot="page-main">
			<view class="header m-b-8">
				<view class="text-line m-b-8 ">
					<view class="label">Shipping Mark：</view>
					{{ shippingMark }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">ETO PO：</view>
					{{ etoPo }}
				</view>
				<view class="text-line m-b-8 " v-show="prodOrderStr">
					<view class="label">
						需要拆托的
						<view>Prod-order：</view>
					</view>
					{{ prodOrderStr }}
				</view>
				<view class="text-line m-b-8 " v-show="sapCode">
					<view class="label">
						需要拆托的
						<view>SAP Code：</view>
					</view>
					{{ sapCode }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">拆托数：</view>
					{{ splitPalletCount }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">打包任务源托数：</view>
					{{ palletQuantityCount }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">打包任务目标托数：</view>
					{{ afterPackingCount }}
				</view>
			</view>

			<view class="content">
				<view class="header-box">
					<view class="steps">
						<text class="current-step-index">{{ stepIndex }}</text>
						<text>/{{ afterPackingCount }}</text>
					</view>
				</view>
				<uni-section class="mb-10" :title="`当前托${title}`" type="line">
					<!-- :title="`当前托${title}`" type="line" -->
					<template v-slot:right>
						<uni-tag :text="tagInverted?'扫描非拆托':'扫描拆托'" :inverted="tagInverted" type="primary"
							@click="tagInverted = !tagInverted"></uni-tag>
					</template>
					<uni-list v-show="currentTaskBarCodeList.length">
						<uni-list-item v-for="(item, index) in currentTaskBarCodeList" :key="item.sscc" ellipsis="1">
							<template v-slot:body>
								<view class="list-item">
									<view class="tag-box">
										<uni-tag v-show="item.type === '拆'" text="拆托" type="primary"></uni-tag>
									</view>
									<view class="ellipsis">{{ item.sscc }}</view>
								</view>
							</template>
							<template v-slot:footer>
								<uni-icons @click="handleDelete(index,item.sscc)" custom-prefix="custom-icon"
									type="closeempty" size="18" color="#541b86"></uni-icons>
							</template>
						</uni-list-item>
					</uni-list>
					<view v-show="!currentTaskBarCodeList.length" class="empty">请扫描成品标签二维码</view>
					<view class="footer-box">
						<o-btn size="sm" @click="$refs.cleanDialog.open()">清空打包任务</o-btn>
						<o-btn size="sm" v-if="afterPackingCount != stepIndex" type="primary" @click="handleNext"
							:loading="submitLoading">下一托</o-btn>
						<o-btn size="sm" v-else type="primary" @click="handleOk" :loading="submitLoading">完成</o-btn>
					</view>
				</uni-section>
			</view>
		</view>
		<Message ref="message"></Message>
		<uni-popup ref="alertDialog" type="dialog">
			<uni-popup-dialog type="error" cancelText="关闭" confirmText="清空打包任务" title="通知" :content="errorMessage"
				@confirm="handleClean" @close="$refs.alertDialog.close()"></uni-popup-dialog>
		</uni-popup>

		<uni-popup ref="cleanDialog" type="dialog">
			<uni-popup-dialog type="info" cancelText="取消" confirmText="确认" title="通知" :content="cleanDialogContent"
				@confirm="handleClean" @close="handleCleanDialogClose()"></uni-popup-dialog>
		</uni-popup>
	</my-page>
</template>

<script>
	import Message from '@/components/Message'
	import Bus from '@/utils/bus'
	import _ from 'lodash'

	export default {
		name: 'packOperator',
		components: {
			Message
		},
		data() {
			return {
				deleteData: undefined,
				submitLoading: false,
				materialInfo: {},
				taskId: undefined,

				taskList: [],
				normalTaskList: [],
				taskIndex: 0,
				allScanProdOrderList: [],
				currentTaskBarCodeList: [],
				// takeDownTaskBarCodeList: [],
				stepIndex: 1,
				tagInverted: true,
				returnPage: false,
				params: {},

				errorMessage: ''
			}
		},
		computed: {
			cleanDialogContent() {
				return this.returnPage ? '退出将会清空打包任务，是否继续?' : '请确认清空打包任务?'
			},
			prodOrderStr() {
				const prodOrderStr = _.join(_.map(_.filter(this.taskList, x => x.isDisassembled), x => x.prodOrder), ',')
				return prodOrderStr
			},
			allTaskCount() {
				const count = _.sumBy(_.filter(this.taskList, x => !x.isDisassembled), 'palletQuantity')
				return count
			},
			splitPalletCountMap() {
				const splitPalletList = _.filter(this.taskList, x => x.isDisassembled)
				const uniqSplitPalletList = _.uniqBy(splitPalletList, 'prodOrder')
				return _.map(uniqSplitPalletList, x => {
					return {
						prodOrder: x.prodOrder,
						count: _.ceil(_.sumBy(_.filter(splitPalletList, y => x.prodOrder === y.prodOrder),
							'palletQuantity'))

					}
				})
			},
			splitPalletCount() {
				const count = _.sumBy(_.filter(this.taskList, x => x.isDisassembled), 'palletQuantity')
				return _.ceil(count)
			},
			sapCode() {
				const str = _.join(_.map(_.filter(this.taskList, x => x.isDisassembled), 'sapCode'), ',')
				return str
			},
			title({
				normalTaskList,
				taskIndex
			}) {
				if (!normalTaskList.length) {
					return ''
				}
				return `${normalTaskList[taskIndex].prodOrder}-${normalTaskList[taskIndex].sapCode}`
			},
			firstTaskInfo() {
				if (this.taskList.length) {
					return _.first(this.taskList)
				}
				return {}
			},
			prodOrder() {
				return _.get(this.firstTaskInfo, 'prodOrder')
			},
			shippingMark() {
				return _.get(this.firstTaskInfo, 'shippingMark')
			},
			etoPo() {
				return _.get(this.firstTaskInfo, 'etoPo')
			},
			palletQuantityCount() {
				const count = _.sumBy(this.taskList, item => Number(item.palletQuantity))
				return _.ceil(count)
			},

			afterPackingCount() {
				const count = _.sumBy(this.taskList, item => Number(item.afterPacking))
				return count
			},
			taskListCountMap() {
				return _.reduce(
					this.taskList,
					(dict, item) => {
						dict[item.prodOrder] = item.palletQuantity
						return dict
					}, {}
				)
			}
		},
		onLoad(options) {
			this.taskId = options.id
			this.params = options
			this.initScanCode()

		},
		onLaunch(options) {
			Bus.$off('scancodedate')
		},
		methods: {
			async getHistoryRecord() {
				try {
					const options = {
						shippingTaskId: this.taskId,
						pageSize: 0
					}
					const {
						rows
					} = await this.$store.dispatch('finishedProduct/getHistoryRecord', options)

					if (!rows.length) {
						return
					}


					const allScanProdOrderList = []
					_.each(rows, x => {
						allScanProdOrderList.push(...JSON.parse(x.prodSscc || '[]'))
					})

					this.allScanProdOrderList = allScanProdOrderList

					this.stepIndex = rows.length + 1


					let taskIndex = 0
					let afterPackingCount = 0
					_.each(this.normalTaskList, (x, i) => {
						if (afterPackingCount === -1) {
							return
						}

						afterPackingCount = afterPackingCount + Number(x.afterPacking)
						console.log('afterPackingCount', afterPackingCount)
						if (afterPackingCount >= this.stepIndex) {
							taskIndex = i
							afterPackingCount = -1
						}
					})
					this.taskIndex = taskIndex
				} catch (e) {
					console.log(e)
					this.$refs.message.error(e.message)
				} finally {}
			},
			handleReturn() {

				this.returnPage = true
				this.$refs.cleanDialog.open()
			},
			handleCleanDialogClose() {
				this.returnPage = false
				this.$refs.cleanDialog.close()
			},
			onReset() {
				this.currentTaskBarCodeList = []
				this.allScanProdOrderList = []
				this.taskIndex = 0
			},
			onSubmitCheck() {
				if (!this.currentTaskBarCodeList.length) {
					this.errorMessage = '请扫描成品标签二维码'
					throw ({
						message: this.errorMessage
					})
				}


				if (_.uniqBy(_.filter(this.allScanProdOrderList, x => !x.type), 'sscc').length, 'sscc' < this
					.palletQuantityCount) {
					this.errorMessage = '扫描标签次数未满足要求,请检查'
					throw ({
						message: this.errorMessage
					})
				}



				if (this.prodOrderStr) {
					_.each(this.splitPalletCountMap, x => {
						const splitPalletSsccList = _.filter(this.allScanProdOrderList, y => y.prodOrder ===
							x.prodOrder)
						if (splitPalletSsccList.length < x.count) {
							this.errorMessage = '扫描拆托标签次数未满足要求,请检查'
							throw ({
								message: this.errorMessage
							})
						}
					})
				}

				const noSplitPalletSsccList = _.filter(this.allScanProdOrderList, x => !x.type)

				if (noSplitPalletSsccList.length !== this.afterPackingCount) {

					if (noSplitPalletSsccList.length > this.afterPackingCount) {
						this.errorMessage = '扫描非拆托标签次数大于目标次数,请检查'
					} else {
						this.errorMessage = '扫描非拆托标签次数小于目标次数,请检查'
					}

					throw ({
						message: this.errorMessage
					})
				}

			},
			onNextCheck() {
				if (!this.currentTaskBarCodeList.length) {
					throw {
						message: '请扫描成品标签二维码'
					}
				}

				const {
					prodOrder
				} = this.normalTaskList[this.taskIndex] || {}
				if (!_.filter(this.currentTaskBarCodeList, x => x.prodOrder === prodOrder).length) {
					throw {
						message: '请扫描当前托码'
					}
				}
			},
			handleDelete(index, sscc) {
				if (this.submitLoading) {
					this.$refs.message.info('正在提交不允许编辑')
					return
				}
				this.currentTaskBarCodeList.splice(index, 1)

				const allScanIndex = _.findIndex(this.allScanProdOrderList, x => x.sscc === sscc)
				this.allScanProdOrderList.splice(allScanIndex, 1)
			},
			async handleClean() {
				try {
					const deleteParam = {
						id: this.taskId
					}
					await this.$store.dispatch('finishedProduct/deleteMultiPackageHistory', deleteParam)
					this.$refs.message.success('清空成功')
					this.onReset()
					this.onReloadPage()
				} catch (e) {
					// this.$refs.message.error(e.message);
				} finally {
					if (this.returnPage) {
						this.handleGoBack()
					}
				}
			},


			onScanning(data) {
				data.code = data.code.replace(/\r|\n/gi, '')

				const prodOrder = data.code.substr(10, 9)

				if (!prodOrder || data.code.indexOf('36900639') < 0) {
					this.$refs.message.error('请扫描正确的成品二维码')
					return
				}

				const sscc = data.code.substr(data.code.length - 18, 18)

				// 判断当前托不能重复
				if (_.find(this.currentTaskBarCodeList, x => x.sscc === sscc)) {
					this.$refs.message.error('当前托已存在')
					return
				}

				const splitProdOrder = _.split(this.prodOrderStr, ',')
				const {
					prodOrder: currentProdOrder
				} = this.normalTaskList[this.taskIndex] || {}
				const currentScanProdOrderList = [currentProdOrder, ...splitProdOrder]
				if (!currentScanProdOrderList.includes(prodOrder)) {
					this.$refs.message.error('请扫描该批次二维码')
					return
				}

				let type = ''
				// 判断
				if (!this.tagInverted) {
					type = '拆'
					if (_.findIndex(splitProdOrder, x => x === prodOrder) === -1) {
						this.$refs.message.error('当前状态为“扫描拆托”，请扫描拆托二维码')
						return
					}
				} else {
					console.log('已进入')

					console.log(_.find(this.allScanProdOrderList, x => x.sscc === sscc))
					if (_.find(this.allScanProdOrderList, x => x.sscc === sscc)) {
						this.$refs.message.error('当前托已扫描')
						return
					}
				}

				// // 判断当前批次是否存在有拆托和非拆托
				// if (_.filter(splitProdOrder, x => x === prodOrder).length > 0 && _.findIndex(this.normalTaskList, x => x
				// 		.prodOrder === prodOrder) > -
				// 	1) {

				// 	// 如果拆托批次扫描字数大于等于1次，默认将扫描的数据都变成拆托
				// 	const sameBatch = _.filter(this.currentTaskBarCodeList, x => x.prodOrder === prodOrder)

				// 	if (sameBatch.length >= 1) {
				// 		type = '拆'
				// 	}
				// }

				const item = {
					type,
					prodOrder,
					sscc
				}

				this.currentTaskBarCodeList.push(item)
				this.allScanProdOrderList.push(item)
			},

			async initScanCode() {
				Bus.$on('scancodedate', this.onScanning)
			},
			async handleNext() {
				try {
					this.onNextCheck()

					uni.showLoading({
						title: '提交中'
					})
					this.submitLoading = true


					// //请求 生成记录
					const ssccNumbers = _.join(_.map(this.currentTaskBarCodeList, x => x.sscc), ',')
					const lastOne = this.afterPackingCount === this.stepIndex ? 1 : 0
					const options = {
						ssccNumbers,
						historyIndex: this.stepIndex,
						lastOne,
						shippingTaskId: this.taskId,
						prodSscc: JSON.stringify(this.currentTaskBarCodeList)
					}
					await this.$store.dispatch('finishedProduct/addPackageHistory', options)

					this.currentTaskBarCodeList = []
					this.stepIndex += 1

					let allAfterPacking = 0
					for (var i = 0; i < this.taskIndex + 1; i++) {
						const {
							afterPacking
						} = this.normalTaskList[i] || {}
						allAfterPacking += Number(afterPacking)
					}

					if (this.stepIndex > allAfterPacking) {
						this.taskIndex += 1
					}

					this.tagInverted = true

				} catch (e) {
					console.log(e)
					this.$refs.message.error(e.message)
				} finally {
					this.submitLoading = false
					uni.hideLoading()
				}
			},
			onReloadPage() {
				this.stepIndex = 1
				this.lodaData()
			},
			async handleOk() {
				try {
					this.onSubmitCheck()

					console.log(111)
					uni.showLoading({
						title: '提交中'
					})
					this.submitLoading = true

					const ssccNumbers = _.join(_.map(this.currentTaskBarCodeList, x => x.sscc), ',')
					const lastOne = 1
					const options = {
						ssccNumbers,
						historyIndex: this.stepIndex,
						lastOne,
						shippingTaskId: this.taskId,
						prodSscc: JSON.stringify(this.currentTaskBarCodeList)
					}
					const result = await this.$store.dispatch('finishedProduct/addPackageHistory', options)
					if (result.code === 200) {
						this.$refs.message.success('清空成功')
						this.handleGoBack()
					} else {
						this.$refs.message.error(result.msg)
					}
					//最后做校验

					//如果失败，页面应该有个弹窗  重新做：掉API删除所有记录，刷新整个页面，返回：返回上一页
				} catch (e) {
					this.$refs.alertDialog.open()
				} finally {
					this.submitLoading = false
					uni.hideLoading()
				}
			},
			async handleGoBack() {
				uni.navigateBack({
					delta: 1
				})
			},
			deleteAll() {
				const deleteParam = {
					id: this.taskId
				}
				this.deleteData = this.$store.dispatch('finishedProduct/deleteMultiPackageHistory', deleteParam)
			},
			async lodaData() {
				const options = {
					id: this.taskId
				}
				const {
					data
				} = await this.$store.dispatch('finishedProduct/getTaskList', options)
				this.taskList = data
				this.normalTaskList = _.filter(data, x => !x.isDisassembled)

				this.getHistoryRecord()
			},

			async onSubmit() {}
		},
		mounted() {
			this.lodaData()
		},
		watch: {}
	}
</script>

<style lang="scss" scoped>
	.main {
		height: 100%;
		padding: 8px;
		box-sizing: border-box;
		display: flex;
		flex-direction: column;
	}

	.header {
		background: #fff;
		padding: 8px;
		border-radius: 4px;
	}

	.content {
		background: #fff;
		padding: 8px 8px 8px;
		border-radius: 4px;

		.header-box {
			display: flex;

			.steps {
				flex: 1;
				display: flex;
				justify-content: flex-end;
				align-items: center;
				font-size: 15px;
				color: #666;

				.current-step-index {
					color: #2979ff;
					// font-weight: bold;
				}
			}
		}

		.footer-box {
			margin-top: 8px;
			display: flex;
			justify-content: space-between;
			align-items: center;
		}
	}

	/deep/.uni-list-item__container {
		padding: 12px 4px;

		.list-item {
			display: flex;
			align-items: center;
			min-width: 0;

			.tag-box {
				width: 42px;
				margin-right: 4px;
			}

			.ellipsis {
				flex: 1;
			}
		}
	}

	/deep/.text-line {
		.label {
			width: 128px;
		}
	}

	.empty {
		text-align: center;
		padding: 12px 0;
		color: $uni-text-color-grey;
	}
</style>