<template>
	<my-page nav-title="原材料上架">
		<view class="main" slot="page-main">
			<view class="header m-b-8">
				<view class="text-line m-b-8 ">
					<view class="label">物料名称：</view>
					{{ materialInfo.materialName }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">物料编码：</view>
					{{ materialInfo.materialNb }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">批次号：</view>
					{{ materialInfo.batchNb }}
				</view>
			</view>

			<view class="content">
				<uni-forms :label-width="80" ref="binInForm" :rules="formRules" :modelValue="form" label-position="left">
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
import _ from 'lodash';

export default {
	components: {
		Message
	},
	data() {
		return {
			submitLoading: false,
			generatePalletCodeLoading: false,
			materialInfo: {},
			barCode: undefined,
			palletTypeList: [],
			palletForm: {},
			formRules: {
				areaCode: {
					rules: [
						{
							required: true,
							errorMessage: '不能为空'
						}
					]
				}
			},
			form: {
				areaCode: undefined
			},

			areaList: [],
			area: undefined
		};
	},
	onLoad(options) {
		this.barCode = options.barCode;
		this.getByMesBarCode(options.barCode);
	},

	methods: {
		async getWareList() {
			const plant = uni.getStorageSync('plant');
			const wareCode = plant.wareCode;
			const data = await this.$store.dispatch('wareShift/getWareList', { wareCode });
			this.areaList = _.map(data, x => ({ text: x.name, value: x.code }));
		},
		handleAreaChange(val) {
			const {
				detail: { value }
			} = val;
			this.form.areaCode = value[0].value;
		},
		async handleGoBack() {
			uni.navigateBack({ delta: 1 });
		},

		async lodaData() {
			this.getWareList();
		},
		async getByMesBarCode(barCode) {
			try {
				const data = await this.$store.dispatch('material/parsedBarCode', barCode);
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

				const options = {
					mesBarCode: this.barCode,
					areaCode: this.form.areaCode
				};
				await this.$store.dispatch('binIn/batchBinIn', options);
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
