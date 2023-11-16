<template>
	<my-page nav-title="拆托">
		<view class="main" slot="page-main">
			<view class="header m-b-8">
				<view class="text-line m-b-8 ">
					<view class="label">SSCC码：</view>
					{{ info.ssccNumber }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">物料编码：</view>
					{{ info.materialNb }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">物料名称：</view>
					{{ info.materialName }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">库位编码：</view>
					{{ info.binCode }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">最大拆托数量：</view>
					{{ info.availableStock }}
				</view>
			</view>

			<view class="content">
				<uni-forms :label-width="110" ref="form" :rules="rules" :modelValue="form" label-position="left">
					<uni-forms-item label="拆托SSCC码" name="newMesBarCode" required>
						<input class="custom-input" focus placeholder="扫描SSCC码" v-model="form.newMesBarCode"
							@focus="handleSetEditFieldName('form.newMesBarCode')" />
					</uni-forms-item>
					<uni-forms-item label="实际拆托数量" name="splitQuantity" required>
						<input class="custom-input" placeholder="实际拆托数量" v-model="form.splitQuantity" />
					</uni-forms-item>
					<o-btn block class="submit-btn primary-button" :loading="submitLoading"
						@click="handlePost">提交</o-btn>
				</uni-forms>
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
		name: 'addSplit',
		components: {
			Message
		},
		data() {
			return {
				params: {},
				code: '',
				submitLoading: false,
				rules: {
					newMesBarCode: {
						rules: [{
								required: true,
								errorMessage: '不能为空'
							}
						]
					},
					splitQuantity: {
						rules: [{
							required: true,
							errorMessage: '不能为空'
						}]
					}
				},
				info: {},
				form: {
					mesBarCode: '',
					newMesBarCode: '',
					splitQuantity: ''
				},
				parsedMesBarCode: {},

				editFieldName: 'form.newMesBarCode'
			};
		},
		onLoad(options) {
			// this.params = options
			this.getInfo(options.barCode);
			this.initScanCode();
			this.form.mesBarCode = options.barCode;

			// this.getMesBarCodeInfo('11221025101012091750208716900568763172407093700006000369006391113669798')
		},

		onLaunch() {
			Bus.$off('scancodedate');
		},
		methods: {
			async handleSetEditFieldName(editFieldName) {
				this.editFieldName = editFieldName;
			},
			async initScanCode() {
				Bus.$on('scancodedate', async data => {
					const code = data.code.trim();

					// if (this.editFieldName === 'form.newMesBarCode') {
					// 	await this.getMesBarCodeInfo(code);
					// 	// this.form.newMesBarCode = this.parsedMesBarCode.ssccNb;
					// 	// this.form.splitQuantity = this.parsedMesBarCode.quantity;
					// }
					this.form.newMesBarCode = code
				});
			},
			async getInfo(code) {
				const data = await this.$store.dispatch('finishedProduct/productStockGetByBarCode', code);
				this.info = data;
			},
			async getMesBarCodeInfo(code) {
				try {
					uni.showLoading({
						title: '识别中'
					});
					const data = await this.$store.dispatch('kanban/parsedBarCode', code);
					this.parsedMesBarCode = data;
				} catch (e) {
					this.$refs.message.error(e.message);
				} finally {
					uni.hideLoading();
				}
			},

			async handleGoBack() {
				uni.navigateBack({
					delta: 1
				});
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

					const options = {
						...this.form,
						sourceSsccNb: this.info.ssccNumber
					};
					await this.$store.dispatch('finishedProduct/addSplit', options);
					this.$refs.message.success('拆托完成');
					setTimeout(() => {
						uni.navigateBack(-1);
					}, 1000);
				} catch (e) {
					this.$refs.message.error(e.message);
				} finally {
					uni.hideLoading();
					this.submitLoading = false;
				}
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

		.label {
			width: 80px;
		}
	}

	.content {
		background: #fff;
		padding: 8px 8px 40px;
		border-radius: 4px;
	}

	/deep/.uni-data-tree {
		background: #fff;
	}

	.text-line {
		.label {
			width: 100px;
		}
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