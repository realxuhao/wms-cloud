<template>
	<my-page nav-title="原材料入库">
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
				<view class="text-line  m-b-8">
					<view class="label">批次：</view>
					{{ materialInfo.batchNb }}
				</view>
				<view class="text-line m-b-8">
					<view class="label">总托数：</view>
					<!-- <uni-tag size="small"  :circle="false" :text="materialInfo.totalPallet+''"></uni-tag> -->
					{{ materialInfo.totalPallet }}
				</view>
				<view class="text-line m-b-8">
					<view class="label">总数量：</view>
					<!-- <uni-tag size="small"  :circle="false" :text="materialInfo.totalPallet+''"></uni-tag> -->
					{{ materialInfo.totalQuantity }}
				</view>
				<view class="text-line m-b-8">
					<view class="label">单位：</view>
					<!-- <uni-tag size="small"  :circle="false" :text="materialInfo.totalPallet+''"></uni-tag> -->
					{{ materialInfo.unit }}
				</view>
				<view class="text-line m-b-8" v-show="materialInfo.checkType !== 2">
					<view class="label">抽样件数：</view>
					{{ materialInfo.checkQuantity }}
					<!-- <uni-tag size="small"  :circle="false" :text="materialInfo.checkQuantity+''"></uni-tag> -->
				</view>
				<view class="text-line ">
					<view class="label">抽样方式：</view>
					<!-- <uni-tag size="small"  :circle="false" :text="materialInfo.checkTypeDesc"></uni-tag> -->
					{{ materialInfo.checkTypeDesc }}
				</view>
				<view class="text-line" v-show="materialInfo.checkType === 0">
					<view class="label">地称：</view>
					<o-btn size="xs" type="primary"
						@click="$refs.picker.show()">{{currentWeighbridge.text||'请选择称'}}</o-btn>
				</view>
			</view>
			<view class="content">
				<uni-forms :label-width="80" v-if="materialInfo.checkType === 1" ref="numberform" :rules="numberRules"
					:modelValue="numberFormData" label-position="left">
					<uni-forms-item label="原托数" name="originalPalletQuantity" required>
						<uni-easyinput type="number" v-model="numberFormData.originalPalletQuantity"
							placeholder="原托数" />
					</uni-forms-item>
					<uni-forms-item label="实际件数" name="actualQuantity" required>
						<uni-easyinput type="number" v-model="numberFormData.actualQuantity" placeholder="实际抽样件数" />
					</uni-forms-item>
					<uni-forms-item label="数数结果" name="actualResult" required><uni-easyinput
							v-model="numberFormData.actualResult" placeholder="数数结果" /></uni-forms-item>

					<o-btn block class="submit-btn primary-button" :loading="submitLoading"
						@click="handleNumberSubmit">提交</o-btn>
				</uni-forms>

				<uni-forms :label-width="80" v-if="materialInfo.checkType === 0" ref="weightform"
					:rules="weightFormRules" :modelValue="weightFormData" label-position="left">
					<uni-forms-item label="原托数" name="originalPalletQuantity" required>
						<uni-easyinput type="number" v-model="weightFormData.originalPalletQuantity"
							placeholder="原托数" />
					</uni-forms-item>
					<view class="count-item m-b-8" v-for="(item, index) in weightList" :key="index">
						<view>
							<uni-forms-item label="重量" :name="'weight' + item" required :rules="[
									{
										required: true,
										errorMessage: '请输入称重重量'
									}
								]">
								<uni-easyinput v-model="weightFormData[`weight${item}`]"
									placeholder="请输入重量"></uni-easyinput>
								<o-btn size="xs" type="primary" class="weight-action"
									@click="getWeight(index)">获取重量</o-btn>
							</uni-forms-item>
							<uni-forms-item class="no-margin" label="件数" :name="'number' + item" required :rules="[
									{
										required: true,
										errorMessage: '请输入称重件数'
									}
								]">
								<uni-easyinput v-model="weightFormData[`number${item}`]" placeholder="请输入件数" />
							</uni-forms-item>
						</view>

						<view class="action">
							<uni-tag @click="handleDeleteWeight(item)" v-show="weightList.length > 1" type="warning"
								class="action-delte  m-b-8" text="删除" />
							<uni-tag type="success	" class="action-add" @click="handleAddWeight" text="新增" />
						</view>
					</view>
					<o-btn block class="submit-btn primary-button" :loading="submitLoading"
						@click="handleWeightSubmit">提交</o-btn>
				</uni-forms>

				<uni-forms :label-width="80" v-if="materialInfo.checkType === 2" ref="exemptionform"
					:rules="exemptionRules" :modelValue="exemptionFormData" label-position="left">
					<uni-forms-item label="原托数" name="originalPalletQuantity" required>
						<uni-easyinput type="number" v-model="exemptionFormData.originalPalletQuantity"
							placeholder="原托数" />
					</uni-forms-item>

					<o-btn block class="submit-btn primary-button" :loading="submitLoading"
						@click="handleExemptionSubmit">提交</o-btn>
				</uni-forms>

				<!-- <view class="exxmption-content">
				<view class="text-center">此物料为免检物料，请点击“提交”入库！</view>
				<o-btn block class="submit-btn primary-button" :loading="submitLoading" @click="handleExemptionSubmit">提交</o-btn>
			</view> -->
			</view>

			<uni-data-picker ref="picker" popup-title="请选择称" :localdata="weighbridgeList"
				@change="handleChangeWeighbridge"></uni-data-picker>
		</view>

		<Message ref="message"></Message>

		<uni-popup ref="popup" :is-mask-click="false">
			<view class="result-content">
				<view class="result-status">
					<uni-icons custom-prefix="iconfont" :class="resultData.checkFlag ? 'success-color' : 'error-color'"
						:type="resultData.checkFlag ? 'icon-chenggong' : 'icon-shibai'" size="32"></uni-icons>
					<text class="text"
						:class="resultData.checkFlag ? 'success-color' : 'error-color'">{{ resultData.checkFlag ? '入库成功' : '入库失败' }}</text>
				</view>

				<view class="data-box">
					<view v-show="materialInfo.checkType !== 2">
						<view class="text-line m-b-8">
							<view class="label">实际抽样件数：</view>
							{{ resultData.actualQuantity }}
						</view>
						<view class="text-line m-b-8">
							<view class="label">目标抽样件数：</view>
							{{ resultData.checkQuantity }}
						</view>
					</view>

					<view v-show="materialInfo.checkType === 0">
						<view class="text-line m-b-8">
							<view class="label">实际重量每件：</view>
							{{ resultData.averageResult }} ({{ resultData.unit }})
						</view>
						<view class="text-line">
							<view class="label">目标重量每件：</view>
							[{{ resultData.minStandard }} —————— {{ resultData.maxStandard }}]（{{ resultData.unit }})
						</view>
					</view>

					<view v-show="materialInfo.checkType === 1">
						<view class="text-line m-b-8">
							<view class="label">实际数量每件：</view>
							{{ resultData.averageResult }} ({{ resultData.unit }})
						</view>
						<view class="text-line">
							<view class="label">目标数量每件：</view>
							[{{ resultData.minStandard }},{{ resultData.maxStandard }}]（{{ resultData.unit }})
						</view>
					</view>
				</view>
				<o-btn block class="primary-button" v-if="resultData.checkFlag" @click="handleGoBack">返回</o-btn>
				<o-btn block class="primary-button" v-else @click="handleOk">确定</o-btn>
			</view>
		</uni-popup>


	</my-page>
</template>

<script>
	import Message from '@/components/Message';
	import _ from 'lodash'

	function convertWeightForm(weightForm) {
		const keys = Object.keys(weightForm);
		const weightList = keys.filter(item => item.indexOf('weight') > -1);
		const numberList = keys.filter(item => item.indexOf('number') > -1);

		const actualResult = weightList.reduce((count, item) => {
			const number = Number(weightForm[item]);
			return count + number;
		}, 0);
		const actualQuantity = numberList.reduce((count, item) => {
			const number = Number(weightForm[item]);
			return count + number;
		}, 0);

		return {
			weightTimes: weightList.length,
			actualResult,
			actualQuantity
		};
	}

	export default {
		components: {
			Message
		},
		name: 'MaterialCount',
		onLoad: function(options) {
			this.barCode = options.barCode;
			this.getMaterialInfo(options.barCode);
		},
		mounted() {
			this.getWeighbridgeList()
			// this.$refs.popup.open('center')
		},
		data() {
			return {
				barCode: '',
				materialInfo: {
					checkQuantity: '',
					checkTypeDesc: ''
				},

				numberFormData: {
					actualQuantity: undefined,
					actualResult: undefined
				},
				numberRules: {
					originalPalletQuantity: {
						rules: [{
							required: true,
							errorMessage: '请输入原托数'
						}]
					},
					actualQuantity: {
						rules: [{
							required: true,
							errorMessage: '请输入实际抽样件数'
						}]
					},
					actualResult: {
						rules: [{
							required: true,
							errorMessage: '请输入数数结果'
						}]
					}
				},
				weightFormData: {
					originalPalletQuantity: '',
					weight0: undefined,
					number0: undefined
				},
				weightFormRules: {
					originalPalletQuantity: {
						rules: [{
							required: true,
							errorMessage: '请输入原托数'
						}]
					},
					actualQuantity: {
						rules: [{
							required: true,
							errorMessage: '请输入实际件数'
						}]
					}
				},
				weightList: [0],

				exemptionFormData: {
					originalPalletQuantity: ''
				},
				exemptionRules: {
					originalPalletQuantity: {
						rules: [{
							required: true,
							errorMessage: '请输入原托数'
						}]
					}
				},

				resultData: {},

				submitLoading: false,

				// 称
				weighbridgeList: [{
					text: "一年级",
					value: "1-0",
				}, ],
				currentWeighbridge: {}
			};
		},
		methods: {
			handleChangeWeighbridge(e) {
				this.currentWeighbridge = e.detail.value[0]
			},
			async getWeight(index) {
				if (!this.currentWeighbridge.value) {
					// this.$refs.message.error('请先选择称')
					this.$refs.picker.show()
					return
				}

				try {
					const data = await this.$store.dispatch('binIn/getWeightByIp', this.currentWeighbridge.value)

					if (!data) {
						this.$refs.message.error('该称未产生数据')
						return
					}

					this.$set(this.weightFormData, `weight${index}`, data.weight)
				} catch (e) {
					this.$refs.message.error(e.message);
					//TODO handle the exception
				}
			},
			async getWeighbridgeList() {
				try {
					const data = await this.$store.dispatch('binIn/getWeightList')
					this.weighbridgeList = _.map(data, x => {
						return {
							text: x.code,
							value: x.ip
						}
					})
				} catch (e) {
					this.$refs.message.error(e.message);
					console.log(e)
					//TODO handle the exception
				}
			},
			handleOk() {
				this.$refs.popup.close();
			},
			async handleGoBack() {
				uni.navigateBack({
					delta: 1
				});
			},
			async getMaterialInfo(barCode) {
				try {
					uni.showLoading({
						title: '加载中'
					});
					const data = await this.$store.dispatch('materialIn/getAndCheckMaterialIn', barCode);
					this.materialInfo = data;
				} catch (e) {
					this.$refs.message.error(e.message);
				} finally {
					uni.hideLoading();
				}
			},
			handleAddWeight() {
				const maxCount = Math.max.apply(null, this.weightList);
				this.weightList.push(Number(maxCount) + 1);
			},
			handleDeleteWeight(index) {
				this.weightList.splice(index, 1);

				delete this.weightFormData[`weight${index}`];
				delete this.weightFormData[`number${index}`];
			},
			handleWeightSubmit() {
				this.$refs.weightform
					.validate()
					.then(res => {
						const data = convertWeightForm(res);

						this.postMaterialIn({
							...data,
							originalPalletQuantity: res.originalPalletQuantity
						});
					})
					.catch(err => {});
			},
			handleNumberSubmit() {
				this.$refs.numberform
					.validate()
					.then(res => {
						this.postMaterialIn(res);
					})
					.catch(err => {});
			},
			handleExemptionSubmit() {
				this.$refs.exemptionform
					.validate()
					.then(res => {
						this.postMaterialIn(res);
					})
					.catch(err => {});
			},
			async postMaterialIn(res) {
				try {
					uni.showLoading({
						title: '正在提交'
					});
					this.submitLoading = true;
					const data = await this.$store.dispatch('materialIn/postMaterialIn', {
						...res,
						mesBarCode: this.barCode
					});
					this.resultData = data;
					this.$refs.popup.open('center');
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

	.text-line {
		color: #333;
		font-size: 14px;
		display: flex;
		align-items: center;

		.label {
			width: 72px;
		}
	}

	.header {
		background: #fff;
		padding: 8px;
		border-radius: 4px;

		/deep/.uni-tag {
			display: inline-block;
			min-width: 80px;
		}
	}

	.content {
		background: #fff;
		padding: 8px 8px 40px;
		border-radius: 4px;
		flex: 1;

		.count-item {
			background: rgb(241, 242, 247);
			border-radius: 4px;
			padding: 10px 4px;
			display: flex;

			/deep/.uni-easyinput {
				width: 200px;
			}

			.action {
				// flex: 1;
				width: 100px;
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;

				.action-remove {
					display: none;
				}
			}
		}

		.count-item:nth-last-child(1) {
			.action-remove {
				display: block;
			}
		}

		.submit-btn {
			margin-top: 48px;
		}
	}

	/deep/.uni-forms {
		margin-bottom: 24px;
	}

	.no-margin {
		margin-bottom: 8px;
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

	/deep/.uni-data-tree-input {
		display: none;
		z-index: -1;
	}

	.weight-action {
		position: absolute;
		right: 0px;

	}
</style>