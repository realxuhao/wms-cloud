<template>
	<my-page nav-title="扫描SSCC码" :shadow="false" :border="false">
		<view class="content" slot="page-main">
			<image src="/static/sku-phone.png" class="m-b-8"></image>
			<text>请将激光扫描头对准SSCC码区域</text>
		</view>
		<Message ref="message"></Message>
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
			// Bus.$emit('startScan')
		},
		destroyed() {
			Bus.$off('scancodedate', this.scanCodeCallback);
		},
		data() {
			return {
				code: ''
			};
		},
		methods: {
			async scanCodeCallback(data) {
				Bus.$emit('stopScan');
				this.code = data.code;
				this.handleGoto();
			},
			handleGoto() {
				Bus.$off('scancodedate', this.scanCodeCallback);
				uni.redirectTo({
					url: `/pages/finishedProduct/splitPallet?barCode=${this.code}`
				});
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
</style>