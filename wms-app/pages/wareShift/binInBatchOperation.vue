<template>
	<my-page nav-title="整车上架">
		<view class="main" slot="page-main">
			<view class="header m-b-8">
				<view class="text-line m-b-8 ">
					<view class="label">车牌号：</view>
					{{ materialInfo.carNb }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">运单号：</view>
					{{ materialInfo.orderNumber }}
				</view>
			</view>

			<view class="content">
				<uni-forms :label-width="80" ref="binInForm" :rules="binInFormRules" :modelValue="binInForm" label-position="left">
					<!-- 	<uni-forms-item label="区域编码" name="areaCode" required>
						<uni-easyinput v-model="binInForm.areaCode" placeholder="请扫描区域编码"></uni-easyinput>
					</uni-forms-item>
 -->
					<uni-forms-item label="存储区" name="areaCode" required>
						<uni-data-picker ref="picker" v-model="area" popup-title="请选择存储区" :localdata="areaList" @change="handleAreaChange"></uni-data-picker>
					</uni-forms-item>

					<o-btn block class="submit-btn primary-button" :loading="submitLoading" @click="handlePostBinIn">提交</o-btn>
				</uni-forms>
			</view>

			<uni-popup ref="popup" :is-mask-click="false">
				<view class="result-content">
					<view class="result-status">
						<uni-icons custom-prefix="iconfont" class="success-color" type="icon-chenggong" size="32"></uni-icons>
						<text class=" text success-color">上架成功</text>
					</view>
					<o-btn block class="primary-button" @click="handleGoBack">返回</o-btn>
				</view>
			</uni-popup>
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
			barCode: undefined,
			binInFormRules: {
				areaCode: {
					rules: [
						{
							required: true,
							errorMessage: '不能为空'
						}
					]
				}
			},
			binInForm: {
				mesBarCode: undefined,
				areaCode: undefined
			},
			areaList: [],
			area: undefined,
			editFieldName: 'binInForm.areaCode' //'binInForm.mesBarCode','binInForm.recommendBinCode'
		};
	},
	onLoad(options) {
		this.binInForm.mesBarCode = options.barCode;
		this.getByMesBarCode(options.barCode);

		this.initScanCode();
	},
	onLaunch() {
		Bus.$off('scancodedate');
	},
	methods: {
		async getWareList() {
			const plant = uni.getStorageSync('plant');
			const wareCode = plant.wareCode;
			const data = await this.$store.dispatch('wareShift/getWareList', { wareCode });
			this.areaList = _.map(data, x => ({ text: x.name, value: x.code }));
		},
		handleSetEditFieldName(editFieldName) {
			this.editFieldName = editFieldName;
		},
		async initScanCode() {
			Bus.$on('scancodedate', data => {
				const code = data.code.trim();
				if (this.editFieldName) {
					_.set(this, this.editFieldName, code);
				}
			});
		},
		async handleGoBack() {
			uni.navigateBack({ delta: 1 });
		},
		handleAreaChange(val) {
			const {
				detail: { value }
			} = val;
			this.binInForm.areaCode = value[0].value;
		},
		async lodaData() {
			this.getWareList();
		},
		async getByMesBarCode(barCode) {
			try {
				const data = await this.$store.dispatch('wareShift/getTransInfo', barCode);
				this.materialInfo = data;
			} catch (e) {
				this.$refs.message.error(e.message);
			}
		},
		async handlePostBinIn() {
			this.$refs.binInForm
				.validate()
				.then(res => {
					this.onSubmitBinIn();
				})
				.catch(err => {});
		},
		async onSubmitBinIn() {
			try {
				uni.showLoading({
					title: '正在提交'
				});
				this.submitLoading = true;

				await this.$store.dispatch('wareShift/batchBinIn', this.binInForm);
				this.$refs.popup.open();
			} catch (e) {
				this.$refs.message.error(e.message);
			} finally {
				uni.hideLoading();
				this.submitLoading = false;
			}
		}
	},
	mounted() {
		this.lodaData();
	},
	watch: {
		'binInForm.palletTypeCode'(value) {
			this.editFieldName = 'binInForm.recommendBinCode';
		}
	}
};
</script>

<style lang="scss">
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
	padding: 8px 8px 40px;
	border-radius: 4px;
}

/deep/.uni-data-tree {
	background: #fff;
}

.result-content {
	width: 324px;
	padding: 12px;
	box-sizing: border-box;
	background: #fff;
	border-radius: 4px;
	.result-status {
		color: $uni-color-success;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 16px;
		.text {
			margin-left: 8px;
			font-size: 14px;
		}
	}
	.label {
		width: 100px;
	}
	.data-box {
		margin-bottom: 16px;
		padding: 0px 8px;
	}
}
.flex {
	.custom-input {
		flex: 1;
	}
}
</style>
