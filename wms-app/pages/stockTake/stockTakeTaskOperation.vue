<template>
	<my-page nav-title="执行盘点">
		<view class="main" slot="page-main">
			<view class="content">
				<uni-forms :label-width="80" ref="binInForm" :rules="formRules" :modelValue="form"
					label-position="left">
					<uni-forms-item label="任务号">{{ materialInfo.taskNo }}</uni-forms-item>
					<uni-forms-item label="盘点类型"><uni-tag type="primary"
							:text="materialInfo.type === 1 ? '盲盘' : '明盘'"></uni-tag></uni-forms-item>
					<uni-forms-item label="库位">{{ materialInfo.binCode }}</uni-forms-item>
					<uni-forms-item label="物料名称">{{ materialInfo.materialName }}</uni-forms-item>
					<uni-forms-item label="物料编码">{{ materialInfo.materialCode }}</uni-forms-item>
					<uni-forms-item label="SSCC">{{ materialInfo.ssccNb }}</uni-forms-item>
					<uni-forms-item label="批次">{{ materialInfo.batchNb }}</uni-forms-item>
					<uni-forms-item
						label="库存量">{{ materialInfo.type === 1 ? '***' : materialInfo.stockQuantity }}</uni-forms-item>
					<uni-forms-item v-if="materialInfo.type === 0" label="是否一致" required><uni-data-checkbox
							v-model="form.isDiff" :localdata="diffList" /></uni-forms-item>
					<uni-forms-item v-if="materialInfo.type === 1 || !form.isDiff" label="盘点量" name="pdaTakeQuantity"
						required>
						<uni-easyinput v-model="form.pdaTakeQuantity" placeholder="请输入盘点量"></uni-easyinput>
					</uni-forms-item>
					<uni-forms-item v-if="!form.isDiff" label="备注" name="remark">
						<uni-easyinput type="textarea" v-model="form.remark" placeholder="请输入备注"></uni-easyinput>
					</uni-forms-item>
					<o-btn block class="submit-btn primary-button" :loading="submitLoading"
						@click="handlePost">提交</o-btn>
				</uni-forms>
			</view>

			<uni-popup ref="popup" :is-mask-click="false">
				<view class="result-content">
					<view class="result-status">
						<uni-icons custom-prefix="iconfont" class="success-color" type="icon-chenggong"
							size="32"></uni-icons>
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
				diffList: [{
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
					pdaTakeQuantity: {
						rules: [{
							required: true,
							errorMessage: '不能为空'
						}]
					}
				},
				form: {
					pdaTakeQuantity: undefined,
					isDiff: 1
				}
			};
		},
		onLoad(options) {
			this.getByMesBarCode(options.barCode);
		},

		methods: {
			async handleGoBack() {
				uni.navigateBack({
					delta: 1
				});
			},

			async getByMesBarCode(barCode) {
				try {
					const data = await this.$store.dispatch('stock/getStockTakeDetail', barCode);

					this.materialInfo = data;
				} catch (e) {
					this.$refs.message.error(e.message);
				}
			},
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
						detailId: this.materialInfo.id,
						isDiff: this.isDiff ? 1 : 0,
						pdaTakeQuantity: this.pdaTakeQuantity
					};
					await this.$store.dispatch('stock/operate', options);
					this.$refs.popup.open();
				} catch (e) {
					this.$refs.message.error(e.message);
				} finally {
					uni.hideLoading();
					this.submitLoading = false;
				}
			}
		},

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
					detailId: this.materialInfo.id,
					isDiff: this.isDiff ? 0 : 1,
					pdaTakeQuantity: this.form.pdaTakeQuantity
				};
				await this.$store.dispatch('stock/operate', options);
				this.$refs.popup.open();
			} catch (e) {
				this.$refs.message.error(e.message);
			} finally {
				uni.hideLoading();
				this.submitLoading = false;
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

	/deep/.uni-forms-item {
		margin-bottom: 2px !important;
	}
</style>