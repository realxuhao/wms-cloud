<template>
	<my-page nav-title="扫描SSCC码" :shadow="false" :border="false">
		<view class="content" slot="page-main">
			<image src="/static/sku-phone.png" class="m-b-8"></image>
			<text>请将激光扫描头对准SSCC码区域</text>
		</view>
		<Message ref="message"></Message>

		<uni-popup ref="submitPopup" type="dialog">
			<uni-popup-dialog before-close type="info" cancelText="取消" confirmText="确认" title="是否确认下架?"
				@confirm="handleBinDown" @close="handleCancel">
				<view>
					<view class="text-align m-b-4">
						<text class="label m-r-8">SSCC码:</text>
						<text>{{ info.sscc }}</text>
					</view>
					<view class="text-align m-b-4">
						<text class="label m-r-8">物料编码:</text>
						<text>{{ info.material }}</text>
					</view>
					<view class="text-align m-b-4">
						<text class="label m-r-8">库位:</text>
						<text>{{ info.binCode }}</text>
					</view>
					<view class="text-align m-b-4">
						<text class="label m-r-8">下架数量:</text>
						<text>{{ info.deliveryQuantity }}</text>
					</view>
				</view>
			</uni-popup-dialog>
		</uni-popup>
	</my-page>
</template>

<script>
	import Message from '@/components/Message';
	import Bus from '@/utils/bus';

	export default {
		components: {
			Message
		},
		onShow() {
			Bus.$on('scancodedate', this.scanCodeCallback);
		},
		destroyed() {
			Bus.$off('scancodedate');
		},
		data() {
			return {
				code: '',
				sudnId: '',
				info: {}
			};
		},
		onLoad(options) {
			this.sudnId = options.id
		},
		// mounted() {
		// 	this.getSample('11221025101012358660208716900568763172407093700006000669006391110024502')
		// },
		methods: {
			async scanCodeCallback(data) {
				Bus.$emit('stopScan');
				this.code = data.code.replace(/\r|\n/gi, '');
				this.getSample(data.code);
			},
			async getSample(barCode) {
				try {
					uni.showLoading();
					const data = await this.$store.dispatch('finishedProduct/sudnPickGetByQrCode', {
						barCode,
						sudnId: this.sudnId
					});
					if (data && data.status === 1) {
						this.$refs.submitPopup.open();
					} else {
						throw Error('此托已下架或为非下架托，请确认');
					}
					this.info = data;
				} catch (e) {
					this.$refs.message.error(e.message);
				} finally {
					uni.hideLoading();
					Bus.$emit('startScan');
				}
			},
			async handleBinDown(data) {
				try {
					uni.showLoading();
					await this.$store.dispatch('finishedProduct/sudnBinDown', {
						barCode: this.code,
						sudnId: this.sudnId
					});
					// await this.$store.dispatch('finishedProduct/sudnBinDown', this.code);
					this.$refs.message.success('下架成功');
					this.$refs.submitPopup.close();
				} catch (e) {
					this.$refs.message.error(e.message);
				} finally {
					uni.hideLoading();
				}
			},
			async handleCancel() {
				this.$refs.submitPopup.close();
				Bus.$emit('startScan');
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

	.label {
		width: 80px;
	}
</style>