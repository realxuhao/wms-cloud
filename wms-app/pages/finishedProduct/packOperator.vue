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
					<view class="label">Prod-order：</view>
					{{ prodOrder }}
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
						<o-btn size="sm" @click="handleClean">清空</o-btn>
						<o-btn size="sm" type="primary" @click="handleNext" :loading="submitLoading">下一托</o-btn>
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
			submitLoading: false,
			materialInfo: {},
			taskId: undefined,
			
			taskList: [],
			taskIndex: 0,
			allScanBarCodeList: [],
			currentTaskBarCodeList: [
				{
					type: '拆',
					value: '202403226690063911100247521031104222032911260000501'
				},
				{
					type: '拆',
					value: '2024032266900639111002475210311042220329112262000050'
				},
				{
					type: '',
					value: '2024032266900639111030247521031104222032911260020050'
				}
			],
			takeDownTaskBarCodeList: [],
			stepIndex: 1,
			tagInverted: true
		};
	},
	computed: {
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
			return `${taskList[taskIndex].prodOrder}-${taskList[taskIndex].SAPCode}`;
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
			const count = _.sumBy(this.taskList, 'palletQuantity');
			return count;
		},
		afterPackingCount() {
			const count = _.sumBy(this.taskList, 'afterPacking');
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
			const allScanBarCodeMap = _.map(this.allScanBarCodeList, x => x.substring(10, 7));

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
		handleClean() {
			this.onReset();
		},
		async initScanCode() {
			Bus.$on('scancodedate', data => {
				const item = { type: '', value: data };
				if (!this.tagInverted) {
					item.type = '拆';
					this.takeDownTaskBarCodeList.push(data);
				}
				this.currentTaskBarCodeList.push(item);
			});
		},
		async handleNext() {
			if (!this.currentTaskBarCodeList.length) {
				this.$refs.message.error('请扫描成品标签二维码');
				return;
			}

			try {
				this.submitLoading = true;
				this.stepIndex += 1;
				const { afterPacking } = this.taskList[this.taskIndex]||{}
				if (this.stepIndex > afterPacking) {
					this.taskIndex += 1;
				}

				//请求 生成记录
				this.onReset();
			} catch (e) {
				//TODO handle the exception
			} finally {
				this.submitLoading = false;
			}
		},
		async handleGoBack() {
			uni.navigateBack({ delta: 1 });
		},

		async lodaData() {
			const options = { id: this.taskId };
			const {data:{ rows, total }} = await this.$store.dispatch('finishedProduct/getTaskList', options);
			this.taskList=rows;
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
