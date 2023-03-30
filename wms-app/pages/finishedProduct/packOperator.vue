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
				<view class="text-line m-b-8 ">
					<view class="label">需要拆托的</br>Prod-order：</view>
					{{ prodOrderList }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">SAP Code：</view>
					{{ sapCode }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">源托数：</view>
					{{ palletQuantityCount }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">目标托数：</view>
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
						<uni-list-item v-for="(item, index) in currentTaskBarCodeList" :key="item.value" ellipsis="1">
							<template v-slot:body>
								<view class="list-item">
									<view class="tag-box"><uni-tag v-show="item.type === '拆'" text="拆" :inverted="true" type="primary"></uni-tag></view>
									<view class="ellipsis">{{ item.value }}</view>
								</view>
							</template>
							<template v-slot:footer>
								<uni-icons @click="handleDelete(index)" custom-prefix="custom-icon" type="closeempty" size="18" color="#541b86"></uni-icons>
							</template>
						</uni-list-item>
					</uni-list>
					<view v-show="!currentTaskBarCodeList.length" class="empty">请扫描成品标签二维码</view>
					<view class="footer-box">
						<o-btn size="sm" @click="handleClean">清空打包任务</o-btn>
						<o-btn size="sm" v-if="afterPackingCount!=stepIndex" type="primary" @click="handleNext" :loading="submitLoading">下一托</o-btn>
						<o-btn size="sm" v-else type="primary" @click="handleOk" :loading="submitLoading">完成</o-btn>
					</view>
				</uni-section>
			</view>
		</view>
		<Message ref="message"></Message>
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
			prodOrderList: [],
			taskList: [],
			taskIndex: 0,
			allScanBarCodeList: [],
			currentTaskBarCodeList: [
				{
					type: '拆',
					value: '911260000501'
				},
				{
					type: '',
					value: '911260000502'
				},
			],
			takeDownTaskBarCodeList: [],
			stepIndex: 1,
			tagInverted: true
		};
	},
	computed: {
		getProdOrderList(){
			let prodOrders = '';
			this.taskList.forEach(data => {
			  if (data.isDisassembled === '拆') {
			    prodOrders += `${data.prodOrder},`;
			  }
			});			
			this.prodOrderList = prodOrders.slice(0, -1); // 去掉最后一个逗号
			console.log('list:'+this.prodOrderList);
		},
		allTaskCount() {
			const count = _.sumBy(_.filter(this.taskList, x => x.isSplit !== '拆'), 'palletQuantity');
			return count;
		},
		sapCode() {
			const str = _.join(_.map(this.taskList, 'sapCode'), ',');
			return str;
		},
		title({ taskList, taskIndex }) {
			if (!taskList.length) {
				return '';
			}
			return `${taskList[taskIndex].prodOrder}-${taskList[taskIndex].sapCode}`;
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
			return count;
		},
		afterPackingCount() {
			const count = _.sumBy(this.taskList, item => Number(item.afterPacking));
			return count;
		},
		taskListCountMap() {
			return _.reduce((dict, item) => {
				dict[item.prodOrder] = item.palletQuantity;
				return dict;
			}, {});
		}
	},
	onLoad(options) {
		this.taskId=options.id

		this.initScanCode();
	},
	onLaunch() {
		Bus.$off('scancodedate');
	},
	methods: {
		onReset() {
			this.currentTaskBarCodeList = [];
		},
		 onCheck() {
			const allScanBarCodeMap = _.map(this.allScanBarCodeList, x => x.substring(-1, 11));
			console.log(allScanBarCodeMap);
			_.each(_.uniq(allScanBarCodeMap), item => {
				const count = _.filter(allScanBarCodeMap, x => x === item).length;
				if (count > this.taskListCountMap[item]) {
					throw `编号“${count}”扫描次数不一致`;
				}
			});
		},
		handleDelete(index) {
			if (this.submitLoading) {
				this.$refs.message.info('正在提交不允许编辑');
				return;
			}
			this.currentTaskBarCodeList.splice(index, 1);
		},
		async handleClean() {
			try{
			    const deleteParam = { id: this.taskId };
				const data =  await this.$store.dispatch('finishedProduct/deleteMultiPackageHistory', deleteParam);
				this.$refs.message.success('清空成功');
				this.onReset();
				this.onReloadPage();
			}catch(e){
				this.$refs.message.error(e.message);
			}

		},
		async initScanCode() {
			Bus.$on('scancodedate', data => {
				const batchNb = data.code.substring(-1,11)
				const item = { type: '', value: batchNb };
				if (!this.tagInverted) {
					item.type = '拆';
					this.takeDownTaskBarCodeList.push(item);
				}
				this.currentTaskBarCodeList.push(item);
				this.currentTaskBarCodeList = Array.from(new Map(this.currentTaskBarCodeList.map(item => [item.value,item])).values());
				console.log(this.currentTaskBarCodeList)
			});
		},
		async handleNext() {
			if (!this.currentTaskBarCodeList.length) {
				this.$refs.message.error('请扫描成品标签二维码');
				return;
			}
			
			
			try {
				// this.onCheck()				
				// uni.showLoading({
				// 	title:'提交中'
				// })
				// this.submitLoading = true;
				
				const { afterPacking } = this.taskList[this.taskIndex]||{}
				if (this.stepIndex > afterPacking) {
					this.taskIndex += 1;
				}
				//请求 生成记录
				const ssccNumbers = _.join(_.map(this.currentTaskBarCodeList,x=>x.value),',')
				const lastOne = this.afterPackingCount===this.stepIndex?1:0
				const options = {ssccNumbers,historyIndex:this.stepIndex,lastOne,shippingTaskId:this.taskId}
				await this.$store.dispatch('finishedProduct/addPackageHistory', options);
				
				this.onReset();
				this.stepIndex += 1;
			} catch (e) {
				this.$refs.message.error(e.message);
			} finally {
				this.submitLoading = false;
				uni.hideLoading()
			}
		},
		onReloadPage(){
			this.stepIndex = 1
			this.lodaData()
		},
		async handleOk(){
			try{
			
				const ssccNumbers = _.join(_.map(this.currentTaskBarCodeList,x=>x.value),',')
				const lastOne = 1
				const options = {ssccNumbers,historyIndex:this.stepIndex,lastOne,shippingTaskId:this.taskId}
				const result = await this.$store.dispatch('finishedProduct/addPackageHistory', options);
				if (result.code === 200){
					this.$refs.message.success('清空成功');
					uni.redirectTo({
						url: `/pages/binIn/operation?barCode=${this.code}`
					});
				} else {
					this.$refs.message.error(result.msg);
				}
				//最后做校验
				
				//如果失败，页面应该有个弹窗  重新做：掉API删除所有记录，刷新整个页面，返回：返回上一页
				
			}catch(e){
				//TODO handle the exception
				this.$refs.message.error(e.message);
				
				
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
			const {data} = await this.$store.dispatch('finishedProduct/getTaskList', options);
			this.taskList=data;		
			this.getProdOrderList()
		},

		async onSubmit() { }
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
		width: 114px;
	}
}

.empty {
	text-align: center;
	padding: 12px 0;
	color: $uni-text-color-grey;
}
</style>
