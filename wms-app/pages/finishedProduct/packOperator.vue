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
						<uni-tag text="拆" :inverted="tagInverted" type="primary" @click="tagInverted = !tagInverted"></uni-tag>
					</template>
					<uni-list v-show="currentTaskBarCodeList.length">
						<uni-list-item v-for="(item, index) in currentTaskBarCodeList" :key="item.sscc" ellipsis="1">
							<template v-slot:body>
								<view class="list-item">
									<view class="tag-box"><uni-tag v-show="item.type === '拆'" text="拆" :inverted="true" type="primary"></uni-tag></view>
									<view class="ellipsis">{{ item.sscc }}</view>
								</view>
							</template>
							<template v-slot:footer>
								<uni-icons @click="handleDelete(index)" custom-prefix="custom-icon" type="closeempty" size="18" color="#541b86"></uni-icons>
							</template>
						</uni-list-item>
					</uni-list>
					<view v-show="!currentTaskBarCodeList.length" class="empty">请扫描成品标签二维码</view>
					<view class="footer-box">
						<o-btn size="sm" @click="$refs.cleanDialog.open()">清空打包任务</o-btn>
						<o-btn size="sm" v-if="afterPackingCount != stepIndex" type="primary" @click="handleNext" :loading="submitLoading">下一托</o-btn>
						<o-btn size="sm" v-else type="primary" @click="handleOk" :loading="submitLoading">完成</o-btn>
					</view>
				</uni-section>
			</view>
		</view>
		<Message ref="message"></Message>
		<uni-popup ref="alertDialog" type="dialog">
			<uni-popup-dialog
				type="error"
				cancelText="关闭"
				confirmText="清空打包任务"
				title="通知"
				content="扫描标签次数未满足要求,请检查!"
				@confirm="handleClean"
				@close="$refs.alertDialog.close()"
			></uni-popup-dialog>
		</uni-popup>

		<uni-popup ref="cleanDialog" type="dialog">
			<uni-popup-dialog
				type="info"
				cancelText="取消"
				confirmText="确认"
				title="通知"
				content="请确认清空打包任务?"
				@confirm="handleClean"
				@close="$refs.cleanDialog.close()"
			></uni-popup-dialog>
		</uni-popup>
	</my-page>
</template>

<script>
import Message from '@/components/Message';
import Bus from '@/utils/bus';
import _ from 'lodash';

export default {
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
			takeDownTaskBarCodeList: [],
			stepIndex: 1,
			tagInverted: true
		};
	},
	computed: {
		prodOrderStr() {
			const prodOrderStr = _.join(_.map(_.filter(this.taskList, x => x.isDisassembled), x => x.prodOrder), ',');
			return prodOrderStr;
		},
		allTaskCount() {
			const count = _.sumBy(_.filter(this.taskList, x => !x.isDisassembled), 'palletQuantity');
			return count;
		},
		splitPalletCount() {
			const count = _.sumBy(_.filter(this.taskList, x => x.isDisassembled), 'palletQuantity');
			return _.ceil(count);
		},
		sapCode() {
			const str = _.join(_.map(_.filter(this.taskList, x => x.isDisassembled), 'sapCode'), ',');
			return str;
		},
		title({ normalTaskList, taskIndex }) {
			if (!normalTaskList.length) {
				return '';
			}
			return `${normalTaskList[taskIndex].prodOrder}-${normalTaskList[taskIndex].sapCode}`;
		},
		firstTaskInfo() {
			if (this.taskList.length) {
				return _.first(this.taskList);
			}
			return {};
		},
		prodOrder() {
			return _.get(this.firstTaskInfo, 'prodOrder');
		},
		shippingMark() {
			return _.get(this.firstTaskInfo, 'shippingMark');
		},
		etoPo() {
			return _.get(this.firstTaskInfo, 'etoPo');
		},
		palletQuantityCount() {
			const count = _.sumBy(this.taskList, item => Number(item.palletQuantity));
			return _.ceil(count);
		},

		afterPackingCount() {
			const count = _.sumBy(this.taskList, item => Number(item.afterPacking));
			return count;
		},
		taskListCountMap() {
			return _.reduce(
				this.taskList,
				(dict, item) => {
					dict[item.prodOrder] = item.palletQuantity;
					return dict;
				},
				{}
			);
		}
	},
	onLoad(options) {
		this.taskId = options.id;

		this.initScanCode();
	},
	onLaunch(options) {
		console.log(options);
		Bus.$off('scancodedate');
	},
	methods: {
		onReset() {
			this.currentTaskBarCodeList = [];
			this.allScanProdOrderList = [];
		},
		 onSubmitCheck() {
			 if (!this.currentTaskBarCodeList.length) {
				throw ({message:`请扫描成品标签二维码`});
			 }
			 
			 if(this.allScanProdOrderList>this.palletQuantityCount || this.allScanProdOrderList<this.palletQuantityCount){
				 throw ({message:`扫描标签次数未满足要求,请检查`});
			 }
			 
			// console.log('this.taskListCountMap',this.taskListCountMap)
			// const normalAllScanProdOrderMap = _.map(_.filter(this.allScanProdOrderList,x=>x.type !== '拆'), x => x.prodOrder);
			// _.each(_.uniq(normalAllScanProdOrderMap), item => {
			// 	const count = _.filter(normalAllScanProdOrderMap, x => x === item & x.type !=='拆').length;
			// 	if (count < this.taskListCountMap[item] || count > this.taskListCountMap[item]) {
			// 		throw ({message:`扫描标签次数未满足要求,请检查`});
			// 	}
			// 	// if (count > this.taskListCountMap[item]) {
			// 	// 	throw `编号“${count}”扫描次数不一致`;
			// 	// }
			// });

			// const splitAllScanProdOrderMap = _.map(_.filter(this.allScanProdOrderList,x=>x.type === '拆'), x => x.prodOrder);
			// _.each(_.uniq(splitAllScanProdOrderMap), item => {
			// 	const count = _.filter(splitAllScanProdOrderMap, x => x === item & x.type ==='拆').length;
			// 	if (count < this.taskListCountMap[item]) {
			// 		throw ({message:`扫描标签次数未满足要求,请检查`});
			// 	}
			// });
		},
		onNextCheck() {
			if (!this.currentTaskBarCodeList.length) {
				throw { message: '请扫描成品标签二维码' };
			}

			const { prodOrder } = this.normalTaskList[this.taskIndex] || {};
			if (!_.filter(this.currentTaskBarCodeList, x => x.prodOrder === prodOrder).length) {
				throw { message: '请扫描当前托码' };
			}
		},
		handleDelete(index) {
			if (this.submitLoading) {
				this.$refs.message.info('正在提交不允许编辑');
				return;
			}
			this.currentTaskBarCodeList.splice(index, 1);
		},
		async handleClean() {
			try {
				const deleteParam = { id: this.taskId };
				await this.$store.dispatch('finishedProduct/deleteMultiPackageHistory', deleteParam);
				this.$refs.message.success('清空成功');
				this.onReset();
				this.onReloadPage();
			} catch (e) {
				this.$refs.message.error(e.message);
			}
		},
		async initScanCode() {
			Bus.$on('scancodedate', data => {
				data.code = data.code.replace(/\r|\n/gi, '');
				console.log(data.code);
				const prodOrder = data.code.substr(10, 9);
				const sscc = data.code.substr(data.code.length - 18, 18);
				if (_.find(this.allScanProdOrderList, x => x.sscc === sscc)) {
					this.$refs.message.error('当前托已存在');
					return;
				}

				const splitProdOrder = _.split(this.prodOrderStr, ',');
				const { prodOrder: currentProdOrder } = this.normalTaskList[this.taskIndex] || {};
				const currentScanProdOrderList = [currentProdOrder, ...splitProdOrder];
				console.log(currentScanProdOrderList);
				console.log(currentScanProdOrderList.includes(prodOrder));
				console.log(prodOrder);
				if (!currentScanProdOrderList.includes(prodOrder)) {
					this.$refs.message.error('批次号校验错误');
					return;
				}

				const item = { type: '', prodOrder, sscc };
				if (!this.tagInverted) {
					item.type = '拆';
					this.takeDownTaskBarCodeList.push(item);
				}
				this.currentTaskBarCodeList.push(item);
				// this.currentTaskBarCodeList = _.uniqBy(this.currentTaskBarCodeList,'value')
				this.allScanProdOrderList.push(item);
			});
		},
		async handleNext() {
			try {
				this.onNextCheck();

				uni.showLoading({
					title: '提交中'
				});
				this.submitLoading = true;

				const { afterPacking } = this.normalTaskList[this.taskIndex] || {};
				if (this.stepIndex > afterPacking) {
					this.taskIndex += 1;
				}
				//请求 生成记录
				const ssccNumbers = _.join(_.map(this.currentTaskBarCodeList, x => x.sscc), ',');
				const lastOne = this.afterPackingCount === this.stepIndex ? 1 : 0;
				const options = { ssccNumbers, historyIndex: this.stepIndex, lastOne, shippingTaskId: this.taskId };
				await this.$store.dispatch('finishedProduct/addPackageHistory', options);

				this.onReset();
				this.stepIndex += 1;
			} catch (e) {
				console.log(e);
				this.$refs.message.error(e.message);
			} finally {
				this.submitLoading = false;
				uni.hideLoading();
			}
		},
		onReloadPage() {
			this.stepIndex = 1;
			this.lodaData();
		},
		async handleOk() {
			try {
				this.onSubmitCheck();

				uni.showLoading({
					title: '提交中'
				});
				this.submitLoading = true;

				const ssccNumbers = _.join(_.map(this.currentTaskBarCodeList, x => x.sscc), ',');
				const lastOne = 1;
				const options = { ssccNumbers, historyIndex: this.stepIndex, lastOne, shippingTaskId: this.taskId };
				const result = await this.$store.dispatch('finishedProduct/addPackageHistory', options);
				if (result.code === 200) {
					this.$refs.message.success('清空成功');
					this.handleGoBack();
				} else {
					this.$refs.message.error(result.msg);
				}
				//最后做校验

				//如果失败，页面应该有个弹窗  重新做：掉API删除所有记录，刷新整个页面，返回：返回上一页
			} catch (e) {
				console.log(e.message);
				//TODO handle the exception
				// this.$refs.message.error(e.message);
				this.$refs.alertDialog.open();
			} finally {
				this.submitLoading = false;
				uni.hideLoading();
			}
		},
		async handleGoBack() {
			uni.navigateBack({ delta: 1 });
		},
		deleteAll() {
			const deleteParam = { id: this.taskId };
			this.deleteData = this.$store.dispatch('finishedProduct/deleteMultiPackageHistory', deleteParam);
		},
		async lodaData() {
			const options = { id: this.taskId };
			const { data } = await this.$store.dispatch('finishedProduct/getTaskList', options);
			this.taskList = data;
			this.normalTaskList = _.filter(data, x => !x.isDisassembled);
		},

		async onSubmit() {}
	},
	mounted() {
		console.log(this.taskId);
		this.lodaData();
	},
	watch: {}
};
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
			width: 28px;
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
