<template>
	<my-page nav-title="库存调整">
		<view class="main" slot="page-main">
			<view class="content">
				<uni-forms :label-width="80" ref="binInForm" :rules="formRules" :modelValue="form" label-position="left">
					<uni-forms-item label="库位">{{ materialInfo.binCode }}</uni-forms-item>
					<uni-forms-item label="物料名称">{{ materialInfo.materialName }}</uni-forms-item>
					<uni-forms-item label="物料编码">{{ materialInfo.materialNb }}</uni-forms-item>
					<uni-forms-item label="SSCC">{{ materialInfo.ssccNumber }}</uni-forms-item>
					<uni-forms-item label="批次">{{ materialInfo.batchNb }}</uni-forms-item>
					<uni-forms-item label="总库存" name="totalStock" required><uni-easyinput v-model="form.totalStock" placeholder="请输入总库存"></uni-easyinput></uni-forms-item>
					<uni-forms-item label="冻结库存" name="freezeStock" required>
						<uni-easyinput v-model="form.freezeStock" placeholder="请输入冻结库存"></uni-easyinput>
					</uni-forms-item>
					<uni-forms-item label="可用库存" name="availableStock" required>
						<uni-easyinput v-model="form.availableStock" placeholder="请输入可用库存"></uni-easyinput>
					</uni-forms-item>
					<o-btn block class="submit-btn primary-button" :loading="submitLoading" @click="handlePost">提交</o-btn>
				</uni-forms>
			</view>

			<uni-popup ref="popup" :is-mask-click="false">
				<view class="result-content">
					<view class="result-status">
						<uni-icons custom-prefix="iconfont" class="success-color" type="icon-chenggong" size="32"></uni-icons>
						<text class=" text success-color">完成</text>
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
			diffList: [
				{
					text: '是',
					value: 1
				},
				{
					text: '否',
					value: 0
				}
			],
			submitLoading: false,
			materialInfo: {},
			formRules: {
				totalStock: {
					rules: [
						{
							required: true,
							errorMessage: '不能为空'
						}
					]
				},
				availableStock: {
					rules: [
						{
							required: true,
							errorMessage: '不能为空'
						}
					]
				},
				freezeStock: {
					rules: [
						{
							required: true,
							errorMessage: '不能为空'
						}
					]
				}
			},
			form: {
				freezeStock: 0,
				availableStock: 0,
				totalStock: 0
			},
			list: []
		};
	},
	onLoad(options) {
		const materialInfo = JSON.parse(options.info);
		this.materialInfo = materialInfo;
		this.form.freezeStock = materialInfo.freezeStock;
		this.form.availableStock = materialInfo.availableStock;
		this.form.totalStock = materialInfo.totalStock;
	},

	methods: {
		async handleGoBack() {
			uni.navigateBack({ delta: 1 });
		},

		async lodaData() {},

		async handlePost() {
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
					...this.form,
					ssccNumber: this.materialInfo.ssccNumber
				};
				await this.$store.dispatch('stock/editStock', options);
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
