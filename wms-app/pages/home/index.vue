<template>
	<view class="wrapper">
		<view class="header">
			<view class="bar"></view>
			<uni-nav-bar>
				<view class="nav-title" @click="handleOpenPicker">
					<text class="nav-title-name">{{ plantName || '请选择工厂/仓库' }}</text>
					<uni-icons class="icon" type="bottom" size="16"></uni-icons>
				</view>
				<o-btn size="sm" block class="download-btn" @click="downWgt" :loading="downloadLoading">更新APP</o-btn>
			</uni-nav-bar>
		</view>

		<view class="main">
			<uni-section title="采购原材料" v-if="$hasPermi(['app:material:in', 'app:material:binIn', 'app:material:batchBinIn'])" type="line" class="m-b-12">
				<view class="list header-box">
					<view class="list-item" v-if="$hasPermi(['app:material:in'])" @click="handleGoto('/pages/materialIn/index')">
						<uni-icons custom-prefix="iconfont" class="icon icon-ruku" type="icon-ruku"></uni-icons>
						<view class="text">原材料入库</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:material:binIn'])" @click="handleGoto('/pages/binIn/binIn')">
						<uni-icons custom-prefix="iconfont" class="icon icon-shangjia" type="icon-jiechubangding"></uni-icons>
						<view class="text">原材料上架</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:material:batchBinIn'])" @click="handleGoto('/pages/binIn/batchBinIn')">
						<uni-icons custom-prefix="iconfont" class="icon icon-fenpishangjia" type="icon-fenpishangjia"></uni-icons>
						<view class="text">按批上架</view>
					</view>
				</view>
			</uni-section>
			<uni-section
				title="生产叫料"
				type="line"
				class="m-b-12"
				v-if="$hasPermi(['app:materialCall:binDown', 'app:materialCall:receiving', 'app:materialCall:cancellingStocks'])"
			>
				<view class="list header-box">
					<view class="list-item" v-if="$hasPermi(['app:materialCall:binDown'])" @click="handleGoto('/pages/materialCall/binDown')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiajia" type="icon-xiajia" color="BLUE"></uni-icons>
						<view class="text">拣配下架</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:materialCall:receiving'])" @click="handleGoto('/pages/materialCall/receiving')">
						<uni-icons custom-prefix="iconfont" class="icon icon-purchasereceipt" type="icon-purchasereceipt" color="#069314"></uni-icons>
						<view class="text">生产收料</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:materialCall:cancellingStocks'])" @click="handleGoto('/pages/wareShift/cancellingStocks')">
						<uni-icons custom-prefix="iconfont" class="icon icon-chejiantuiliaojiaojie" type="icon-chejiantuiliao" color="#ED1820"></uni-icons>
						<view class="text">产线退料</view>
					</view>
				</view>
			</uni-section>
			<uni-section
				title="仓内管理"
				v-if="$hasPermi(['app:manualTrans:binDown', 'app:manualTrans:binIn', 'app:manualTrans:scan', 'app:splitPallet:binIn', 'app:scan:scan', 'app:location:scan'])"
				type="line"
				class="m-b-12"
			>
				<view class="list header-box">
					<view class="list-item" v-if="$hasPermi(['app:manualTrans:binDown'])" @click="handleGoto('/pages/manualTrans/binDown')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiajia" type="icon-xiajia" color="BLUE"></uni-icons>
						<view class="text">转储下架</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:manualTrans:binIn'])" @click="handleGoto('/pages/manualTrans/binIn')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiehuo" type="icon-shangjia2" color="#1afa29"></uni-icons>
						<view class="text">转储上架</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:manualTrans:scan'])" @click="handleGoto('/pages/splitPallet/scan')">
						<uni-icons custom-prefix="iconfont" class="icon icon-chaifen" color="#1afa29"></uni-icons>
						<view class="text">拆托</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:splitPallet:binIn'])" @click="handleGoto('/pages/splitPallet/binIn')">
						<uni-icons custom-prefix="iconfont" class="icon icon-shangjia" color="#1afa29"></uni-icons>
						<view class="text">拆托上架</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:scan:scan'])" @click="handleGoto('/pages/sscc/scan')">
						<uni-icons custom-prefix="iconfont" class="icon " type="icon-SSCC" color="#1296db"></uni-icons>
						<view class="text">SSCC查询</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:location:scan'])" @click="handleGoto('/pages/location/scan')">
						<uni-icons custom-prefix="iconfont" class="icon " type="icon-kuwei" color="#2664CB"></uni-icons>
						<view class="text">库位查询</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:location:adjust'])" @click="handleGoto('/pages/adjust/adjustList')">
						<uni-icons custom-prefix="iconfont" class="icon icon-kucuntiaozheng" type="icon-kucuntiaozheng" color="#2664CB"></uni-icons>
						<view class="text">库存调整</view>
					</view>
				</view>
			</uni-section>

			<uni-section
				title="移库业务"
				v-if="$hasPermi(['app:wareShift:binDown', 'app:materialCall:shipment', 'app:wareShift:receiving', 'app:wareShift:binIn'])"
				type="line"
				class="m-b-12"
			>
				<view class="list header-box">
					<view class="list-item" v-if="$hasPermi(['app:wareShift:binDown'])" @click="handleGoto('/pages/wareShift/binDown')">
						<uni-icons custom-prefix="iconfont" class="icon baocaixiajiabeiliao" type="icon-baocaixiajiabeiliao" color="#00ADD4"></uni-icons>
						<view class="text">移库下架</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:materialCall:shipment'])" @click="handleGoto('/pages/materialCall/shipment')">
						<uni-icons custom-prefix="iconfont" class="icon icon-wuliufahuo" type="icon-wuliufahuo" color="#009A9A"></uni-icons>
						<view class="text">移库发运</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:wareShift:receiving'])" @click="handleGoto('/pages/wareShift/receiving')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiehuo" type="icon-discharge" color="#00ADD4"></uni-icons>
						<view class="text">移库收货</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:wareShift:binIn'])" @click="handleGoto('/pages/wareShift/binIn')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiehuo" type="icon-shangjia2" color="#1afa29"></uni-icons>
						<view class="text">移库上架</view>
					</view>
				</view>
			</uni-section>

			<uni-section title="IQC质检" v-if="$hasPermi(['app:iqc:binDown', 'app:iqc:sample', 'app:iqc:binIn', 'app:iqc:stockTake'])" type="line" class="m-b-12">
				<view class="list header-box" v-if="$hasPermi(['app:iqc:binDown'])">
					<view class="list-item" @click="handleGoto('/pages/IQC/binDown')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiajia" color="#00ADD4"></uni-icons>
						<view class="text">下架</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:iqc:sample'])" @click="handleGoto('/pages/IQC/sample')">
						<uni-icons custom-prefix="iconfont" class="icon icon-zhijian" color="#43B36B"></uni-icons>
						<view class="text">抽样</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:iqc:binIn'])" @click="handleGoto('/pages/IQC/binIn')">
						<uni-icons custom-prefix="iconfont" class="icon icon-shangjia" color="#00ADD4"></uni-icons>
						<view class="text">上架</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:iqc:stockTake'])" @click="handleGoto('/pages/stockTake/stockTake')">
						<uni-icons size="24" custom-prefix="iconfont" class="icon icon-kucunpandian" color="rgb(46,151,222)"></uni-icons>
						<view class="text">盘点</view>
					</view>
				</view>
			</uni-section>

			<uni-section title="成品管理" v-if="$hasPermi(['app:finishedProduct:packTask'])" type="line" class="m-b-12">
				<view class="list header-box">
					<view class="list-item" v-if="$hasPermi(['app:finishedProduct:packTask'])" @click="handleGoto('/pages/finishedProduct/packTask')">
						<uni-icons custom-prefix="iconfont" class="icon icon-dabao" color="#00ADD4"></uni-icons>
						<view class="text">成品打包</view>
					</view>
				</view>
			</uni-section>

			<uni-section
				title="成品"
				v-if="$hasPermi(['app:finishedProduct:productIn', 'app:finishedProduct:shipment', 'app:finishedProduct:receiving', 'app:finishedProduct:binIn'])"
				type="line"
				class="m-b-12"
			>
				<view class="list header-box">
					<view class="list-item" v-if="$hasPermi(['app:finishedProduct:productIn'])" @click="handleGoto('/pages/finishedProduct/productIn')">
						<uni-icons custom-prefix="iconfont" class="icon icon-ruku" type="icon-ruku"></uni-icons>
						<view class="text">成品入库</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:finishedProduct:shipment'])" @click="handleGoto('/pages/finishedProductWareShift/shipment')">
						<uni-icons custom-prefix="iconfont" class="icon icon-wuliufahuo" type="icon-wuliufahuo" color="#009A9A"></uni-icons>
						<view class="text">移库发运</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:finishedProduct:receiving'])" @click="handleGoto('/pages/finishedProductWareShift/receiving')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiehuo" type="icon-discharge" color="#00ADD4"></uni-icons>
						<view class="text">移库收货</view>
					</view>
					<view class="list-item" v-if="$hasPermi(['app:finishedProduct:binIn'])" @click="handleGoto('/pages/finishedProductWareShift/binIn')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiehuo" type="icon-shangjia2" color="#1afa29"></uni-icons>
						<view class="text">移库上架</view>
					</view>
				</view>
			</uni-section>

			<!-- 	<uni-section title="成品移库业务" type="line" class="m-b-12">
				<view class="list header-box">
					<view class="list-item" @click="handleGoto('/pages/finishedProductWareShift/shipment')">
						<uni-icons custom-prefix="iconfont" class="icon icon-wuliufahuo" type="icon-wuliufahuo" color="#009A9A"></uni-icons>
						<view class="text">移库发运</view>
					</view>
					<view class="list-item" @click="handleGoto('/pages/finishedProductWareShift/receiving')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiehuo" type="icon-discharge" color="#00ADD4"></uni-icons>
						<view class="text">移库收货</view>
					</view>
					<view class="list-item" @click="handleGoto('/pages/finishedProductWareShift/binIn')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiehuo" type="icon-shangjia2" color="#1afa29"></uni-icons>
						<view class="text">移库上架</view>
					</view>
				</view>
			</uni-section> -->
			<uni-data-picker ref="picker" popup-title="请选择工厂/仓库" :localdata="dataTree" @change="handleChangePlant"></uni-data-picker>
		</view>

		<ScanCode></ScanCode>
		<AppVersion></AppVersion>
	</view>
</template>

<script>
import ScanCode from '@/components/ScanCode';
import { fileService } from '@/api/file.service';
import AppVersion from '@/components/app-version/app-version';
import _ from 'lodash';

export default {
	components: {
		ScanCode,
		AppVersion
	},
	onLoad() {},
	data() {
		return {
			dataTree: [],
			plantName: '',
			downloadLoading: false,
			downloadUrl: 'https://www.nutricia-home.com/api/file/download/wms/apk/wmsnew.apk',
			isForceUpdate: false,
			downloadNum: ''
		};
	},
	computed: {},
	methods: {
		async downWgt() {
			let that = this;

			if (this.downloadLoading) {
				return;
			}
			this.downloadLoading = true;

			uni.showLoading({
				title: '更新中……'
			});

			const { data: downloadUrl } = await fileService.getFileUrl('wmsnew.apk');

			const downloadTask = uni.downloadFile({
				//执行下载
				url: downloadUrl, //下载地址
				timeout: 1000 * 30, //30秒超时时间
				success: downloadResult => {
					//下载成功
					that.showdownLine = false;
					uni.hideLoading();
					console.log('downloadResult.statusCode' + downloadResult.statusCode);
					if (downloadResult.statusCode == 200) {
						console.log('更新中');
						uni.showModal({
							title: '',
							content: '更新成功，确定现在重启吗？',
							confirmText: '重启',
							confirmColor: '#541b86',
							showCancel: false,
							success: function(res) {
								if (res.confirm == true) {
									plus.runtime.install(
										//安装
										downloadResult.tempFilePath,
										{
											force: true
										},
										function(res) {
											utils.showToast('更新成功，重启中');
											plus.runtime.restart();
										}
									);
								}
							}
						});
					}
				},
				fail: err => {
					uni.hideLoading();
					that.showdownLine = false;
					that.$u.toast(err.errMsg);
					console.log(err);
				},
				complete: com => {
					this.downloadLoading = false;
					console.log(com);
				}
			});

			// 下载进度
			downloadTask.onProgressUpdate(res => {
				that.downloadNum = res.progress;
			});
		},
		async loadPlantList() {
			const data = await this.$store.dispatch('plant/getList');
			const plantList = _.uniqBy(data, 'factoryCode');
			console.log(plantList);
			const list = [];
			_.each(plantList, (plant, index) => {
				const plantIndex = index + 1;
				const obj = { text: plant.factoryCode, children: [], value: `${plantIndex}-${index}` };
				_.each(data, (item, itemIndex) => {
					if (item.factoryCode === plant.factoryCode) {
						const ware = { text: item.code, value: `${plantIndex}-${itemIndex + 1}`, code: item.code };
						obj.children.push(ware);
					}
				});
				list.push(obj);
			});

			this.dataTree = list;
		},

		handleGoto(url) {
			if (this.downloadLoading) {
				return;
			}

			if (!this.plantName) {
				uni.showToast({
					title: '请选择工厂/仓库',
					icon: 'none',
					duration: 2000
				});
				return;
			}

			uni.navigateTo({ url });
		},
		handleChangePlant(val) {
			const {
				detail: { value }
			} = val;
			const plantName = `${value[0].text}/${value[1].text}`;

			const wareCode = value[1].text;
			uni.setStorageSync('plant', {
				name: plantName,
				wareCode
			});

			this.plantName = plantName;
		},
		handleOpenPicker() {
			this.$refs.picker.show();
		}
	},
	mounted() {
		const plant = uni.getStorageSync('plant');
		this.plantName = plant.name;

		this.loadPlantList();
	}
};
</script>

<style lang="scss">
.wrapper {
	height: 100vh;
	background: rgb(241, 242, 247);
	box-sizing: border-box;
	display: flex;
	flex-direction: column;
}
.header {
	padding-top: var(--status-bar-height);
	background: $primary-color;
	display: flex;
	justify-content: center;
	position: relative;
}

.main {
	padding: 8px 8px;
	box-sizing: border-box;
	flex: 1;
	min-height: 0px;
	overflow-y: auto;
	.uni-section {
		box-shadow: 0 2px 10px 0 #ccc;
	}
}

.list {
	width: 100%;
	padding: 12px 0;
	display: flex;
	justify-content: flex-start;
	border-radius: 4px;
	background: #fff;
	flex-wrap: wrap;
	.list-item {
		width: 25%;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		color: #333;
		margin-bottom: 16px;
		.icon {
			margin-bottom: 8px;
			font-size: 32px !important;
			&.icon-ruku {
				color: rgb(46, 151, 222) !important;
			}
			&.icon-shangjia {
				color: rgb(90, 238, 216) !important;
			}
			&.icon-kucuntiaozheng {
				font-size: 26px !important;
			}
			&.icon-kucunpandian {
				font-size: 28px !important;
			}
			&.icon-fenpishangjia {
				color: rgb(13, 143, 233) !important;
				font-size: 34px !important;
			}
		}
		.icon-img {
			width: 32px;
			height: 32px;
			margin-bottom: 8px;
		}
		.text {
			font-size: 14px;
		}
	}
}

/deep/.uni-navbar--border {
	border-bottom-color: transparent !important;
}

/deep/.uni-navbar__header-container {
	justify-content: center;
	align-items: center;
	.nav-title {
		font-size: 18px;
		color: #fff;
		text-align: center;
		.icon {
			color: #fff !important;
			margin-left: 8px;
		}
	}
	.download-btn {
		position: absolute;
		right: -16px;
	}
}

/deep/.uni-data-tree-input {
	display: none;
	z-index: -1;
}
</style>
