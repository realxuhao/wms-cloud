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
import Bus from '@/utils/bus';
import Message from '@/components/Message';

export default {
	components: {
		Message
	},
	data() {
		return {
			code: ''
		};
	},
	onShow() {
		Bus.$on('scancodedate', this.scanCodeCallback);
	},
	destroyed() {
		Bus.$off('scancodedate');
	},
	methods: {
		async scanCodeCallback(data) {
			Bus.$emit('stopScan');
			this.handleGoto(data.code);
		},

		handleGoto(code) {
			uni.navigateTo({
				url: `/pages/stockTake/stockTakeTaskOperation?barCode=${code}`
			});
		}
	}
};
</script>

<style lang="scss">
.wrapper {
	display: flex;
	flex-direction: column;
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
