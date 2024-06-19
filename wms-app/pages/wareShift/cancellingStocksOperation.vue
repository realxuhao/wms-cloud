<template>
	<my-page nav-title="新增产线退料">
		<view class="main" slot="page-main">
			<view class="header m-b-8">
				<view class="text-line m-b-8 ">
					<view class="label">批次：</view>
					{{ materialInfo.batchNb }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">有效期：</view>
					{{ materialInfo.expireDate }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">单位：</view>
					{{ materialInfo.unit }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">SSCC码：</view>
					{{ materialInfo.ssccNb }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">物料名称：</view>

					{{ materialInfo.materialName }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">物料编码：</view>
					{{ materialInfo.materialNb }}
				</view>
			</view>

			<view class="content">
				<uni-forms :label-width="100" ref="form" :rules="formRules" :modelValue="form" label-position="left">
					<uni-forms-item label="数量" name="quantity" required><uni-easyinput type="number" v-model="form.quantity" placeholder="数量" /></uni-forms-item>
					<uni-forms-item label="生产需求号" name="orderNumber"><uni-easyinput v-model="form.orderNumber" placeholder="生产需求号" /></uni-forms-item>
					<o-btn block class="submit-btn primary-button" :loading="submitLoading" @click="handlePost">提交</o-btn>
				</uni-forms>
			</view>
		</view>
		<Message ref="message"></Message>
		<uni-popup ref="alertDialog" type="dialog">
			<uni-popup-dialog type="info" cancelText="取消" confirmText="确定" title="提示" :content="dialogContent" @confirm="onSubmit"></uni-popup-dialog>
		</uni-popup>
	</my-page>
</template>

<script>
import Message from '@/components/Message';
// import Bus from '@/utils/bus';
import _ from 'lodash';

const radioList = [
	{
		text: '正常退料',
		value: 0
	},
	{
		text: '异常退料',
		value: 1
	}
];
export default {
	components: {
		Message
	},
	data() {
		return {
			submitLoading: false,
			materialInfo: {},
			barCode: undefined,
			formRules: {
				quantity: {
					rules: [
						{
							required: true,
							errorMessage: '不能为空'
						}
					]
				}
				// orderNumber: {
				// 	rules: [{
				// 		required: true,
				// 		errorMessage: '不能为空'
				// 	}]
				// },
			},
			form: {
				quantity: undefined,
				orderNumber: undefined
			},
			hasOrderNumber: false,
			orderNumber: false,
			materialInfo: {},
			dialogContent: '请确认提交'
		};
	},
	computed: {
		radioList() {
			return radioList;
		}
	},
	onLoad(options) {
		this.barCode = options.barCode;
		this.getMaterialInfo(options.barCode);
		this.getOrderNumber(options.barCode);
	},
	methods: {
		async getMaterialInfo(barCode) {
			const data = await this.$store.dispatch('material/parsedBarCode', barCode);
			this.materialInfo = data;
		},
		async getOrderNumber(barCode) {
			try {
				const data = await this.$store.dispatch('wareShift/getOrderNumber', barCode);
				this.form.orderNumber = data;
				this.orderNumber = data;
				this.hasOrderNumber = true;
			} catch (e) {
				console.log(e.message);
				//TODO handle the exception
			}
		},

		async handleGoBack() {
			uni.navigateBack({
				delta: 1
			});
		},

		async lodaData() {},

		async handlePost() {
			this.$refs.form
				.validate()
				.then(res => {
					this.dialogContent = this.hasOrderNumber && this.form.orderNumber !== this.orderNumber ? '已修改生产需求号，请确认提交' : '请确认提交';
					this.$refs.alertDialog.open();
				})
				.catch(err => {});
		},
		async onSubmit() {
			try {
				uni.showLoading({
					title: '正在提交'
				});
				this.submitLoading = true;

				const options = {
					...this.form,
					mesBarCode: this.barCode
				};
				await this.$store.dispatch('wareShift/addMaterialReturn', options);
				this.$refs.message.success('提交成功');
				this.handleGoBack();
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
