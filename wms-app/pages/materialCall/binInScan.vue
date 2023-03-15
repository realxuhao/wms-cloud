<template>
	<my-page nav-title="扫描SSCC码" :shadow="false" :border="false">
		<view class="content" slot="page-main">
			<image src="/static/sku-phone.png" class="m-b-8"></image>
			<text>请将激光扫描头对准SSCC码区域</text>
		</view>
		<Message ref="message"></Message>
		<uni-popup ref="distributionLocation" type="dialog">
			<uni-popup-dialog before-close type="info" cancelText="继续扫描" confirmText="上架" title="通知" @confirm="handleGotoBinInOperation" @close="handleDialogClose">
				<view class="text-align">
					<text class="label m-r-8">推荐库位:</text>
					<text>{{ info.recommendBinCode }}</text>
				</view>
			</uni-popup-dialog>
		</uni-popup>

		<uni-popup ref="distributionLine" type="dialog">
			<uni-popup-dialog before-close type="info" cancelText="分配库位" confirmText="配送产线" title="是否配送产线？" @confirm="handleToLine" @close="handleToAllocateBinIn">
				<view>
					<view class="text-align m-b-4">
						<text class="label m-r-8">订单号:</text>
						<text>{{ barCodeInfo.orderNumber }}</text>
					</view>
					<view class="text-align m-b-4">
						<text class="label m-r-8">动作类型:</text>
						<text>{{ moveTypeMap[barCodeInfo.type] }}</text>
					</view>
					<view class="text-align">
						<text class="label m-r-8">Cell:</text>
						<text>{{ barCodeInfo.cell }}</text>
					</view>
				</view>
			</uni-popup-dialog>
		</uni-popup>

		<uni-popup ref="submitLine" type="dialog">
			<uni-popup-dialog before-close type="success" cancelText="返回" confirmText="继续扫描" title="已提交" @confirm="handleContinue" @close="handleGoBack">
				<view>请立即配送产线！</view>
			</uni-popup-dialog>
		</uni-popup>
	</my-page>
</template>

<script>
import Message from '@/components/Message';
import Bus from '@/utils/bus';

const moveTypeMap = {
	0: '整托',
	1: '拆托'
};

export default {
	components: {
		Message
	},
	onShow() {
		Bus.$on('scancodedate', this.scanCodeCallback);
	},
	destroyed() {
		Bus.$off('scancodedate', this.scanCodeCallback);
	},
	data() {
		return {
			code: '',
			info: {},
			barCodeInfo: {}
		};
	},
	computed: {
		moveTypeMap: () => moveTypeMap
	},
	// mounted(){
	// 	this.checkTask('20240322669006391110024752103110422203291126000050')
	// },
	methods: {
		handleGoBack() {
			uni.navigateBack({
				delta: 1
			});
		},
		async scanCodeCallback(data) {
			Bus.$emit('stopScan');
			this.code = data.code;
			this.checkTask(data.code);
		},
		async handleToAllocateBinIn() {
			this.$refs.distributionLine.close();
			this.submitAllocateBin(this.code);
		},
		async submitAllocateBin(barCode) {
			const data = await this.$store.dispatch('kanban/allocateBin', barCode);
			this.info = data;
			this.$refs.distributionLocation.open();
		},
		async checkTask(barCode) {
			try {
				uni.showLoading();
				const data = await this.$store.dispatch('kanban/checkKanbanTask', { mesBarCode: barCode });
				this.barCodeInfo = data;
				this.$refs.distributionLine.open();
			} catch (e) {
				if (e.code === 600) {
					this.submitAllocateBin(barCode);
					return;
				}

				this.$refs.message.error(e.message);
			} finally {
				Bus.$emit('startScan');
				uni.hideLoading();
			}
		},
		handleGoto() {
			Bus.$off('scancodedate', this.scanCodeCallback);
			uni.navigateTo({
				url: `/pages/materialIn/operator?barCode=${this.code}`
			});
		},
		handleGotoBinInOperation() {
			Bus.$off('scancodedate', this.scanCodeCallback);
			uni.navigateTo({
				url: `/pages/binIn/operation?barCode=${this.code}`
			});
		},
		handleContinue() {
			Bus.$emit('startScan');
			this.$refs.submitLine.close();
		},
		async submitDeliver() {
			try {
				uni.showLoading({
					title: '正在提交'
				});
				await this.$store.dispatch('kanban/deliver', { sscc: this.barCodeInfo.ssccNumber });
				this.$refs.distributionLine.close();
				this.$refs.submitLine.open();
			} catch (e) {
				this.$refs.message.error(e.message);
			} finally {
				uni.hideLoading();
			}
		},
		async handleToLine() {
			const { type, ssccNumber, quantity } = this.barCodeInfo;
			if (this.barCodeInfo.type === 0) {
				this.submitDeliver();
			} else {
				Bus.$off('scancodedate', this.scanCodeCallback);
				uni.navigateTo({
					url: `/pages/wareTransshipment/binInOperation?ssccNumber=${ssccNumber}&quantity=${quantity}&mesBarCode=${this.code}`
				});
			}
		},
		handleDialogClose() {
			this.$refs.distributionLocation.close();
		}
	}
};
</script>

<style lang="scss">
.wrapper {
	display: flex;
	flex-direction: column;
}
/deep/.uni-navbar--shadow {
	box-shadow: none;
}
/deep/.uni-navbar--border {
	border: none;
}

.content {
	height: 100%;
	background-color: $primary-color;
	flex: 1;
	display: flex;
	align-items: center;
	// justify-content: center;
	flex-direction: column;
	image {
		width: 180px;
		// height: 160px;
		margin-top: 120px;
		margin-bottom: 32px;
	}
	text {
		color: #fff;
		font-size: 16px;
	}
}
</style>
