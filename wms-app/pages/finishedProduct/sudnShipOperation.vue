<template>
	<my-page nav-title="SUDN发运">
		<view class="main" slot="page-main">
			<view class="header m-b-8">
				<view class="text-line m-b-8 ">
					<view class="label">Delivery：</view>
					{{ info.delivery }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">Material：</view>
					{{ info.material }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">Item：</view>
					{{ info.item }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">Deliver quantity：</view>
					{{ info.deliveryQuantity }}
				</view>

			</view>

			<view class="content">
				<uni-forms :label-width="80" ref="form" :rules="formRules" :modelValue="form" label-position="left">
					<uni-forms-item label="车牌号" name="carNb" required>
						<uni-easyinput v-model="form.carNb" placeholder="请输入车牌号"></uni-easyinput>
					</uni-forms-item>
					<uni-forms-item label="发运数量" name="shipQuantity" required>
						<uni-easyinput type="number" v-model="form.shipQuantity" placeholder="请输入发运数量"></uni-easyinput>
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
						<text class=" text success-color">发运成功</text>
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
				info: {},
				formRules: {
					carNb: {
						rules: [{
							required: true,
							errorMessage: '车牌号'
						}]
					},
					quantity: {
						rules: [{
							required: true,
							errorMessage: '发运数量'
						}]
					}
				},
				form: {
					sudnId: undefined,
					carNb: undefined,
					shipQuantity: undefined
				},

			};
		},
		onLoad(options) {
			this.form.sudnId = options.sudnId;
			this.getByMesBarCode(options.sudnId);
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
					const data = await this.$store.dispatch('finishedProduct/sudnShipGetById', this.form);

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


					const data = await this.$store.dispatch('finishedProduct/sudnShip', this.form);
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