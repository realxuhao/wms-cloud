<template>
	<my-page nav-title="拆托上架">
		<view class="main" slot="page-main">
			<view class="header m-b-8">
				<view class="text-line m-b-8 ">
					<view class="label">SSCC码：</view>
					{{ materialInfo.ssccNumber }}
				</view>
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
				<view v-if="materialInfo.recommendBinCode">
					<view class="text-line m-b-8 ">
						<view class="label">托盘类型：</view>
						{{ materialInfo.palletType }}
					</view>
					<view class="text-line m-b-8 " v-show="materialInfo.palletCode">
						<view class="label">托盘编码：</view>
						{{ materialInfo.palletCode }}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">推荐库位：</view>
						{{ materialInfo.recommendBinCode }}
					</view>
				</view>
			</view>

			<view class="content">
				<uni-forms :label-width="80" ref="binInForm" :rules="binInFormRules" :modelValue="binInForm"
					label-position="left">
					<uni-forms-item v-if="!materialInfo.palletCode" label="托盘编码" name="palletTypeCode" required>
						<uni-easyinput v-model="binInForm.palletTypeCode" placeholder="请扫描托盘编码"
							@focus="handleSetEditFieldName('binInForm.palletTypeCode')"></uni-easyinput>
					</uni-forms-item>
					<uni-forms-item label="目标库位" name="recommendBinCode" required>
						<uni-easyinput v-model="binInForm.recommendBinCode" placeholder="请扫描目标库位"
							@focus="handleSetEditFieldName('binInForm.recommendBinCode')"></uni-easyinput>
					</uni-forms-item>
					<o-btn block class="submit-btn primary-button" :loading="submitLoading"
						@click="handlePostBinIn">提交</o-btn>
				</uni-forms>
			</view>

			<uni-popup ref="alertDialog" type="dialog">
				<uni-popup-dialog type="info" cancelText="关闭" confirmText="确认" title="通知" content="扫描库位与推荐库位不一致，是否提交!"
					@confirm="onSubmitBinIn"></uni-popup-dialog>
			</uni-popup>

			<uni-popup ref="popup" :is-mask-click="false">
				<view class="result-content">
					<view class="result-status">
						<uni-icons custom-prefix="iconfont" class="success-color" type="icon-chenggong"
							size="32"></uni-icons>
						<text class=" text success-color">上架成功</text>
					</view>
					<view class="data-box">
						<view class="text-line m-b-8">
							<view class="label">上架库位：</view>
							{{ binInForm.recommendBinCode }}
						</view>
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

	function convertPalletList(rows) {
		const list = rows.map(item => {
			const text = `类型：${item.type}；宽：${item.width}；高：${item.height}`;
			return {
				text,
				value: item.type
			};
		});
		return list;
	}

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
				binInFormRules: {
					recommendBinCode: {
						rules: [{
							required: true,
							errorMessage: '目标库位不能为空'
						}]
					},
					palletTypeCode: {
						rules: [{
							required: true,
							errorMessage: '托盘编码不能为空'
						}]
					}
				},
				binInForm: {
					mesBarCode: undefined,
					recommendBinCode: undefined,
					palletTypeCode: undefined
				},

				editFieldName: 'binInForm.palletTypeCode' //'binInForm.mesBarCode','binInForm.recommendBinCode'
			};
		},
		onLoad(options) {
			this.barCode = options.barCode;
			this.binInForm.barCode = options.barCode;
			this.getByMesBarCode(options.barCode);

			this.initScanCode();
		},
		onLaunch() {
			Bus.$off('scancodedate');
		},
		methods: {
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
				uni.navigateBack({
					delta: 1
				});
			},

			async lodaData() {
				// this.getPalletList()
			},
			async getByMesBarCode(barCode) {
				try {
					const data = await this.$store.dispatch('splitPallet/getBinInInfo', barCode);

					if (data.palletCode) {
						this.editFieldName = 'binInForm.recommendBinCode';
					}
					this.materialInfo = data;

					if (data && data.status === 3) {
						throw Error('已上架，请勿重复操作');
					}
				} catch (e) {
					this.$refs.message.error(e.message);
				}
			},
			async handlePostBinIn() {
				this.$refs.binInForm
					.validate()
					.then(res => {
						if (this.binInForm.recommendBinCode !== this.materialInfo.recommendBinCode) {
							this.$refs.alertDialog.open();
							return;
						}
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
						actualBinCode: this.binInForm.recommendBinCode,
						palletCode: this.binInForm.palletTypeCode
					};
					const data = await this.$store.dispatch('splitPallet/postBinIn', options);
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