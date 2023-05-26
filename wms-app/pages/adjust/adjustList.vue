<template>
	<my-page nav-title="库存调整">
		<view class="main" slot="page-main">
			<view class="header m-b-8" @click="handleGoto(item)" v-for="(item, index) in list" :key="index">
				<view class="text-line m-b-8 ">
					<view class="label">SSCC码：</view>
					{{ item.ssccNumber }}
				</view>

				<view class="text-line m-b-8 ">
					<view class="label">物料名称：</view>
					{{ item.materialName }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">物料编码：</view>
					{{ item.materialNb }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">批次号：</view>
					{{ item.batchNb }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">数量：</view>
					{{ item.totalStock }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">有效期：</view>
					{{ item.expireDate }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">所在库位：</view>
					{{ item.binCode }}
				</view>
			</view>
		</view>
		<Message ref="message"></Message>
	</my-page>
</template>

<script>
import Message from '@/components/Message';
import Bus from '@/utils/bus';
import _ from 'lodash';

export default {
	components: {
		Message
	},
	data() {
		return {
			list: []
		};
	},
	onLoad(options) {
		this.getList(options.code);
	},
	onLaunch() {
		Bus.$off('scancodedate');
	},
	methods: {
		async getList(code) {
			try {
				const data = await this.$store.dispatch('stock/getByBinCode', code);
				this.list = data;
			} catch (e) {
				this.$refs.message.error(e.message);
			}
		},
		handleGoto(item) {
			uni.navigateTo({
				url: `/pages/adjust/adjustOperation?info=${JSON.stringify(item)}`
			});
		}
	},
	mounted() {},
	watch: {}
};
</script>

<style lang="scss">
.main {
	// height: 100%;
	padding: 8px;
	box-sizing: border-box;
	display: flex;
	flex-direction: column;
}

.header {
	background: #fff;
	padding: 8px;
	border-radius: 4px;
}

.content {
	background: #fff;
	padding: 8px 8px 40px;
	border-radius: 4px;
}

/deep/.uni-data-tree {
	background: #fff;
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
.flex {
	.custom-input {
		flex: 1;
	}
}
</style>
