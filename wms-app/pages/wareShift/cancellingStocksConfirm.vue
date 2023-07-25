<template>
	<my-page nav-title="产线退料确认">
		<view class="main" slot="page-main">
			<uni-forms class="form" :label-width="80" ref="form" :rules="formRules" :modelValue="form"
				label-position="left">
				<uni-forms-item label="仓库" name="wareCode" required>
					<uni-data-picker ref="picker" popup-title="请选择仓库" :localdata="dataTree"
						@change="handleChangePlant"></uni-data-picker>
				</uni-forms-item>
				<uni-forms-item label="存储区" name="areaCode" required>
					<uni-data-picker ref="picker" v-model="area" popup-title="请选择存储区" :localdata="areaList"
						@change="handleAreaChange"></uni-data-picker>
				</uni-forms-item>
			</uni-forms>

			<view class="flex">
				<view class="header">
					总共&nbsp;
					<text class="active">{{ list.length }}</text>
					&nbsp;托;
				</view>
				<view class="header">
					已选择&nbsp;
					<text class="active">{{ hasCheckedItems }}</text>
					&nbsp;托
				</view>
			</view>

			<uni-list class="m-b-12">
				<uni-list-item v-for="(item, index) in list" :key="index">
					<template slot="body">
						<view class="order-main">
							<MyRadio class="m-r-8" v-model="item.checked">
								<view class="order-content">
									<view class="title m-b-4">{{ item.materialName }}</view>
									<view class="desc m-b-4">
										<text class="label">物料编码:</text>
										{{ item.materialNb }}
									</view>
									<view class="desc m-b-4">
										<text class="label">仓库:</text>
										{{ item.wareCode }}
									</view>
									<view class="desc m-b-4" v-show="item.areaCode">
										<text class="label">区域:</text>
										{{ item.areaCode }}
									</view>
									<view class="desc m-b-4">
										<text class="label">SSCC码:</text>
										{{ item.ssccNumber }}
									</view>
									<!-- <view class="desc">
										<text class="label">状态:</text>
										<uni-tag :text="typeMap[item.type].text" :type="typeMap[item.type].color" size="mini" />
									</view> -->
								</view>
							</MyRadio>
						</view>
					</template>
				</uni-list-item>
			</uni-list>

			<view class="submit-btn"><o-btn class="primary-button" @click="handlePost" :loading="submitLoading"
					:disabled="!hasCheckedItems" block>提交</o-btn></view>
		</view>
		<uni-popup ref="popup" type="dialog">
			<uni-popup-dialog type="info" title="提示" content="请确认提交" @close="$refs.popup.close()"
				@confirm="handleSubmit">
				<!-- <view class="header">已选择&nbsp;<text class="active">{{hasCheckedItems}}</text>&nbsp;托</view> -->
			</uni-popup-dialog>
		</uni-popup>
		<Message ref="message"></Message>
	</my-page>
</template>

<script>
	import Message from '@/components/Message';
	import MyRadio from '@/components/my-radio/my-radio';
	import _ from 'lodash';

	const typeMap = {
		0: {
			text: '正常退料',
			color: 'success'
		},
		1: {
			text: '异常退料',
			color: 'warning'
		}
	};
	export default {
		name: 'transhipmentOrderMaterialIn',
		components: {
			Message,
			MyRadio
		},
		data() {
			return {
				list: [],
				submitLoading: false,
				barCode: '',

				formRules: {
					wareCode: {
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
				form: {
					areaCode: undefined,
					wareCode: undefined
				},
				dataTree: [],
				plantList: [],
				areaList: [],
				area: undefined
			};
		},
		computed: {
			hasCheckedItems() {
				return _.filter(this.list, 'checked').length;
			},
			typeMap() {
				return typeMap;
			}
		},
		onLoad(options) {},
		async onPullDownRefresh() {
			try {
				await this.getList();
			} catch (e) {
				//TODO handle the exception
			} finally {
				uni.stopPullDownRefresh();
			}
		},
		mounted() {
			this.getList();
			this.lodaData();
		},
		methods: {
			async getWareList() {
				const data = await this.$store.dispatch('wareShift/getWareList', {
					wareCode: this.form.wareCode
				});
				this.areaList = _.map(data, x => ({
					text: x.name,
					value: x.code
				}));
			},
			handleChangePlant(val) {
				const {
					detail: {
						value
					}
				} = val;

				const factoryCode = value[0].text;
				const factory = _.find(this.plantList, ['factoryCode', factoryCode]);
				this.form.plantNb = factory.factoryCode;

				const wareCode = value[1].text;
				const ware = _.find(this.plantList, ['code', wareCode]);
				this.form.wareCode = ware.code;

				this.getWareList();
			},
			handleAreaChange(val) {
				const {
					detail: {
						value
					}
				} = val;
				this.form.areaCode = value[0].value;
			},
			async loadPlantList() {
				const data = await this.$store.dispatch('plant/getList');
				this.plantList = data;

				const uniqList = _.uniqBy(data, 'factoryCode');
				const list = [];
				_.each(uniqList, (plant, index) => {
					const plantIndex = index + 1;
					const obj = {
						text: plant.factoryCode,
						children: [],
						value: `${plantIndex}-${index}`
					};
					_.each(data, (item, itemIndex) => {
						if (item.factoryCode === plant.factoryCode) {
							const ware = {
								text: item.code,
								value: `${plantIndex}-${itemIndex + 1}`,
								code: item.code
							};
							obj.children.push(ware);
						}
					});
					list.push(obj);
				});

				this.dataTree = list;
			},
			async lodaData() {
				this.loadPlantList();
			},
			async getList() {
				const {
					rows
				} = await this.$store.dispatch('wareShift/getReturnMaterialList', {
					status: 0,
					pageSize: 0
				});
				this.list = rows;
			},
			async handlePost() {
				this.$refs.form
					.validate()
					.then(res => {
						this.$refs.popup.open();
					})
					.catch(err => {});
			},

			async handleSubmit() {
				try {
					uni.showLoading({
						title: '正在提交'
					});
					this.submitLoading = true;
					const ssccNumbers = _.map(_.filter(this.list, x => x.checked), x => x.ssccNumber);
					const options = {
						...this.form,
						ssccNumbers
					};
					await this.$store.dispatch('wareShift/returnMaterialConfirm', options);
					this.$refs.message.success('提交成功');
					this.$refs.popup.close();
					this.getList();
				} catch (e) {
					uni.startPullDownRefresh();
					this.$refs.message.error(e.message);
					//TODO handle the exception
				} finally {
					this.submitLoading = false;
					uni.hideLoading();
				}
			}
		}
	};
</script>

<style scoped lang="scss">
	.main {
		background: #fff;
		box-sizing: border-box;
		padding: 8px 0;
		height: 100%;
		overflow-y: auto;
	}

	.header {
		color: #000;
		padding: 0 0 12px 12px;
		display: flex;
		align-items: center;

		.active {
			color: $uni-color-error;
			font-size: 18px;
			font-weight: bold;
		}
	}

	.submit-btn {
		padding: 0px 12px;
	}

	.order-main {
		width: 100%;
		display: flex;
		align-items: center;

		.desc {
			color: #999;
			font-size: 12px;
			display: flex;

			.label {
				margin-right: 4px;
			}
		}
	}

	.form {
		padding: 0px 12px;
	}
</style>