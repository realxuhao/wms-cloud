<template>
	<my-page nav-title="移库上架">
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
					<uni-forms-item label="目标类型" name="type" required>
						<uni-data-checkbox @change="onTypeChange" v-model="binInForm.type" :localdata="dataCheckBox" />
					</uni-forms-item>
					<uni-forms-item label="目标库位" name="binCode" required v-if="binInForm.type === 0">
						<uni-easyinput v-model="binInForm.binCode" placeholder="请扫描目标库位"></uni-easyinput>
					</uni-forms-item>
					<uni-forms-item label="目标区域" name="areaCode" required v-if="binInForm.type === 1">
						<!-- <uni-easyinput v-model="binInForm.areaCode" placeholder="请扫描目标区域"></uni-easyinput> -->
						<uni-data-picker ref="picker" v-model="area" popup-title="请选择存储区" :localdata="areaList"
							@change="handleAreaChange"></uni-data-picker>
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
					<!-- 	<view class="data-box">
						<view class="text-line m-b-8">
							<view class="label">上架库位：</view>
							{{ binInForm.recommendBinCode }}
						</view>
					</view> -->
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

	const filedNameMap = {
		0: 'binInForm.binCode',
		1: 'binInForm.areaCode'
	};
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
					type: {
						rules: [{
							required: true,
							errorMessage: '不能为空'
						}]
					},
					recommendBinCode: {
						rules: [{
							required: true,
							errorMessage: '不能为空'
						}]
					},
					binCode: {
						rules: [{
							required: true,
							errorMessage: '不能为空'
						}]
					},
					areaCode: {
						rules: [{
							required: true,
							errorMessage: '不能为空'
						}]
					}
				},
				binInForm: {
					mesBarCode: undefined,
					binCode: undefined,
					areaCode: undefined,
					type: 0
				},
				dataCheckBox: [{
						text: '库位',
						value: 0
					},
					{
						text: '区域',
						value: 1
					}
				],
				area: undefined,
				areaList: [],
				editFieldName: 'binInForm.binCode' //'binInForm.mesBarCode','binInForm.recommendBinCode'
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
			handleAreaChange(val) {
				const {
					detail: {
						value
					}
				} = val;
				this.binInForm.areaCode = value[0].value;
			},
			async getWareList() {
				const plant = uni.getStorageSync('plant');
				const wareCode = plant.wareCode;
				const data = await this.$store.dispatch('wareShift/getWareList', {
					wareCode
				});
				this.areaList = _.map(data, x => ({
					text: x.name,
					value: x.code
				}));
			},
			onTypeChange(val) {
				this.area = undefined
				// this.editFieldName = filedNameMap[val];
				this.binInForm.binCode = undefined;
				this.binInForm.areaCode = undefined;
			},
			async initScanCode() {
				Bus.$on('scancodedate', data => {
					const code = data.code.trim();
					if (this.binInForm.type === 0) {
						_.set(this, 'binInForm.binCode', code);
					}
					// if (this.editFieldName) {
					// }
				});
			},
			async handleGoBack() {
				uni.navigateBack({
					delta: 1
				});
			},

			async getByMesBarCode(barCode) {
				try {
					const data = await this.$store.dispatch('finishedProduct/productShiftGetBinInInfo', barCode);

					this.materialInfo = data;

					if (data && data.status === 1) {
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
						// : this.binInForm.recommendBinCode,
						// palletCode: this.binInForm.palletTypeCode,
						...this.binInForm,
						sscc: this.materialInfo.ssccNumber
					};
					await this.$store.dispatch('finishedProduct/postBinIn', options);
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
			this.getWareList();
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