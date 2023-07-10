<template>
	<my-page nav-title="SPDN退货到工厂">
		<view class="main" slot="page-main">
			<view class="header m-b-8">
				<view class="text-line m-b-8 ">
					<view class="label">SSCC：</view>
					{{ info.ssccNb }}
				</view>
				<!-- <view class="text-line m-b-8 ">
					<view class="label">Material：</view>
					{{ info.material }}
				</view> -->
				<!-- 	<view class="text-line m-b-8 ">
					<view class="label">Item：</view>
					{{ info.item }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">Deliver quantity：</view>
					{{ info.deliveryQuantity }}
				</view>
 -->
			</view>

			<view class="content">
				<uni-forms :label-width="80" ref="form" :rules="formRules" :modelValue="form" label-position="left">
					<uni-forms-item label="退货数量" name="quantity" required>
						<uni-easyinput type="number" v-model="form.quantity" placeholder="请输入退货数量"></uni-easyinput>
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
						<text class=" text success-color">提交成功</text>
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

	const localKey = 'sudn-ship-operation';
	export default {
		components: {
			Message
		},
		data() {
			return {
				submitLoading: false,
				info: {},
				formRules: {
					quantity: {
						rules: [{
							required: true,
							errorMessage: '数量'
						}]
					}
				},
				form: {
					qrCode: '',
					quantity: undefined,
					type: 1
				},

			};
		},
		onLoad(options) {
			this.form.qrCode = options.barCode;
			this.getByMesBarCode();
		},

		methods: {


			async handleGoBack() {
				uni.navigateBack({
					delta: 1
				});
			},

			async lodaData() {
				// this.getPalletList()
			},
			async getByMesBarCode() {
				try {
					// const data = await this.$store.dispatch('finishedProduct/sudnPickGetByQrCode', this.form);
					const data = await this.$store.dispatch('material/parsedBarCode', this.form.qrCode);
					this.info = data;

				} catch (e) {
					this.$refs.message.error(e.message);
				}
			},
			async handlePost() {
				this.$refs.form
					.validate()
					.then(res => {

						this.onSubmit();
					})
					.catch(err => {});
			},
			async onSubmit() {
				try {
					uni.showLoading({
						title: '正在提交'
					});
					this.submitLoading = true;

					await this.$store.dispatch('finishedProduct/addProductReturn', this.form);
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