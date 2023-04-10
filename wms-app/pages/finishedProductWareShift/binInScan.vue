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
	},
	destroyed() {
		Bus.$off('scancodedate');
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
			this.checkMaterialIn(data.code);
		},
		async checkMaterialIn(barCode) {
			try {
				uni.showLoading();
				const data = await this.$store.dispatch('finishedProduct/productShiftGetBinInInfo', barCode);
				if (data.status === 2) {
					this.handleGoto();
				} else {
					throw Error('此托已上架或为非上架托，请确认');
				}
			} catch (e) {
				this.$refs.message.error(e.message);
			} finally {
				Bus.$emit('startScan');
				uni.hideLoading();
			}
		},
		handleGoto() {
			Bus.$off('scancodedate', this.scanCodeCallback);
			uni.navigateTo({
				url: `/pages/finishedProductWareShift/binInOperation?barCode=${this.code}`
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
