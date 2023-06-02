<template>
	<my-page nav-title="扫描SSCC码" :shadow="false" :border="false">
		<view class="content" slot="page-main">
			<view class="scan-input">
				<uni-easyinput
					type="textarea"
					suffixIcon="arrow-right"
					placeholderStyle="color:#333;font-size:14px"
					v-model="code"
					placeholder="如果标签损坏或扫码结果为空,请输入标签码"
					@iconClick="checkBinIn"
				></uni-easyinput>
			</view>
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
			this.code = data.code;
			this.checkBinIn();
		},
		async checkBinIn() {
			try {
				uni.showLoading();
				const data = await this.$store.dispatch('binIn/getByMesBarCode', this.code);
				if (!data && data.status === 1) {
					throw Error('已上架，请勿重复操作');
				}
				this.handleGotoOperation();
			} catch (e) {
				this.$refs.message.error(e.message);
			} finally {
				uni.hideLoading();
				Bus.$emit('startScan');
			}
		},
		handleGotoOperation() {
			Bus.$off('scancodedate', this.scanCodeCallback);
			uni.navigateTo({
				url: `/pages/binIn/operation?barCode=${this.code}`
			});
		}
	}
};
</script>

<style lang="scss" scoped>
::-webkit-input-placeholder {
	/* WebKit browsers */
	color: #999;
	font-size: 16px;
}

.scan-input {
	position: absolute;
	top: 10%;
	padding: 0 10px;
	box-sizing: border-box;
	width: 100%;
	/deep/.uni-easyinput__content {
		background: rgba(255, 255, 255, 0.8) !important;
		.uni-easyinput__content-textarea {
			font-size: 20px;
		}
	}
	/deep/.uniui-arrow-right {
		transform: translateY(16px);
		font-size: 28px !important;
		color: rgb(51, 51, 51) !important;
	}
}

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
