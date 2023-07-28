<template>
	<my-page nav-title="扫描SSCC码" :shadow="false" :border="false">
		<view class="content" slot="page-main">
			<image src="/static/sku-phone.png" class="m-b-8"></image>
			<text>请将激光扫描头对准SSCC码区域</text>
		</view>
		<Message ref="message"></Message>
		<uni-popup ref="distributionLine" type="dialog">
			<uni-popup-dialog before-close type="info" cancelText="上架" confirmText="配送产线" title="是否配送产线？" @confirm="handleToLine" @close="handleGoto">
				<view>
					<view class="text-align m-b-4">
						<text class="label m-r-8">SSCC:</text>
						<text>{{ barCodeInfo.ssccNb }}</text>
					</view>
					<view class="text-align m-b-4">
						<text class="label m-r-8">动作类型:</text>
						<text>{{ moveTypeMap[barCodeInfo.splitType] }}</text>
					</view>
					<!-- <view class="text-align">
						<text class="label m-r-8">Cell:</text>
						<text>{{ barCodeInfo.cell }}</text>
					</view> -->
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
		console.log('destroyed');
	},
	data() {
		return {
			code: '',
			barCodeInfo: {}
		};
	},
	computed: {
		moveTypeMap: () => moveTypeMap
	},
	methods: {
		async scanCodeCallback(data) {
			Bus.$emit('stopScan');
			this.code = data.code;
			this.code = '20250213669006391114282042103025072202141190001000'
			this.checkTask(data.code);
		},
		handleGoBack() {
			uni.navigateBack({
				delta: 1
			});
		},
		handleContinue() {
			Bus.$emit('startScan');
			this.$refs.submitLine.close();
		},
		async checkTask(barCode) {
			try {
				uni.showLoading();
				const data = await this.$store.dispatch('wareShift/getOne', barCode);
				this.barCodeInfo = data;
				if(data === null){
					throw Error('空');
				}
				console.log('111111111111111111111111111')
				if (data.status === 5) {
					if(data.splitType===1){
						const { type, ssccNb, splitQuality } = this.barCodeInfo;
						Bus.$off('scancodedate', this.scanCodeCallback);
						uni.redirectTo({
							url: `/pages/splitPallet/splitPallet?ssccNumber=${ssccNb}&quantity=${splitQuality}&mesBarCode=${this.code}`
						});
						Bus.$emit('startScan');
					}else{
						Bus.$off('scancodedate', this.scanCodeCallback);
						uni.navigateTo({
							url: `/pages/wareShift/binInOperation?barCode=${this.code}`
						});
						Bus.$emit('startScan');
					}
				} else {
					throw Error('此托不在待上架清单中，请检查');
				}
			} catch (e) {
				if (e.code === 600) {
					this.handleGoto();
					return;
				}
				this.$refs.message.error(e.message);
			} finally {
				Bus.$emit('startScan');
				uni.hideLoading();
			}
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
			if (this.barCodeInfo.splitType === 0) {
				this.submitDeliver();
			} else {
				Bus.$off('scancodedate', this.scanCodeCallback);
				uni.redirectTo({
					url: `/pages/splitPallet/splitPallet?ssccNumber=${ssccNumber}&quantity=${quantity}&mesBarCode=${this.code}`
				});
				Bus.$emit('startScan');
			}
		},
		handleGoto() {
			this.$refs.distributionLine.close();
			Bus.$off('scancodedate', this.scanCodeCallback);
			uni.navigateTo({
				url: `/pages/wareShift/binInOperation?barCode=${this.code}`
			});
			Bus.$emit('startScan');
		}
	}
	// mounted() {
	// 	this.scanCodeCallback({code:'20250213669006391110024585103025072202141190001000'})
	// }
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
