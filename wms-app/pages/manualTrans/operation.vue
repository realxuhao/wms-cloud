<template>
	<my-page nav-title="库内转储">
		<view class="main" slot="page-main">
			<view class="header m-b-8">
				<view class="text-line m-b-8 ">
					<view class="label">库位：</view>
					{{ materialInfo.binCode }}
				</view>
				<!-- 	<view class="text-line m-b-8 ">
					<view class="label">物料名称：</view>
					{{ materialInfo.materialName }}
				</view> -->
				<view class="text-line m-b-8 ">
					<view class="label">物料编码</view>
					{{ materialInfo.materialNb }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">SSCC</view>
					{{ materialInfo.ssccNumber }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">批次：</view>
					{{ materialInfo.batchNb }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">总库存：</view>
					{{ materialInfo.totalStock }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">冻结库存：</view>
					{{ materialInfo.freezeStock }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">可用库存：</view>
					{{ materialInfo.availableStock }}
				</view>
			</view>

			<view class="content">
				<uni-forms :label-width="80" ref="binInForm" :rules="formRules" :modelValue="form"
					label-position="left">
					<uni-forms-item label="转储动作" required><uni-data-checkbox v-model="form.type"
							:localdata="typeList" /></uni-forms-item>

					<template v-if="form.type === 1">
						<uni-forms-item label="存储区" name="actualCode" required>
							<uni-data-picker ref="picker" v-model="area" popup-title="请选择存储区" :localdata="areaList"
								@change="handleAreaChange"></uni-data-picker>
						</uni-forms-item>
					</template>

					<template v-if="form.type === 0">
						<uni-forms-item label="库位编码" name="actualCode" required>
							<uni-easyinput v-model="form.actualCode" placeholder="请输入或扫描库位编码"></uni-easyinput>
						</uni-forms-item>
					</template>

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
	import Bus from '@/utils/bus';

	export default {
		components: {
			Message
		},
		data() {
			return {
				typeList: [{
						text: '区域',
						value: 1
					},
					{
						text: '库位',
						value: 0
					}
				],
				submitLoading: false,
				materialInfo: {},
				formRules: {
					actualCode: {
						rules: [{
							required: true,
							errorMessage: '不能为空'
						}]
					}
				},
				form: {
					actualCode: '',
					mesBarCode: 0,
					type: 0
				},
				list: [],
				areaList: [],
				area: undefined
			};
		},
		onLoad(options) {
			this.form.mesBarCode = options.barCode;
			this.getInfo(options.barCode);
			this.type = 0


			this.initScanCode()
		},
		onLaunch() {
			Bus.$off('scancodedate');
		},
		methods: {
			async initScanCode() {
				Bus.$on('scancodedate', data => {
					console.log(this.type)
					if (this.type === 0) {
						const code = data.code.trim();
						this.form.actualCode = code;
					}
				});
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
			handleAreaChange(val) {
				const {
					detail: {
						value
					}
				} = val;
				this.form.actualCode = value[0].value;
			},
			async handleGoBack() {
				uni.navigateBack({
					delta: 1
				});
			},
			async getInfo(barCode) {
				try {
					uni.showLoading();
					const data = await this.$store.dispatch('stock/getInfoByMesBarCode', barCode);
					this.materialInfo = data;
				} catch (e) {
					this.$refs.message.error(e.message);
				} finally {
					uni.hideLoading();
				}
			},

			async lodaData() {
				this.getWareList();
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
						...this.form,
						ssccNumber: this.materialInfo.ssccNumber
					};
					await this.$store.dispatch('manualTrans/trans', options);
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
			'form.type': function() {
				this.area = [];
				this.form.actualCode = undefined;
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