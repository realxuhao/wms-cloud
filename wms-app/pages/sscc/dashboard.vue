<template>
	<my-page nav-title="SSCC信息展示">
		<view class="main" slot="page-main">
			<view class="header m-b-8">
				<view class="text-line m-b-8 ">
					<view class="label">SSCC码：</view>
					{{ info.ssccNumber }}
				</view>

				<view class="text-line m-b-8 ">
					<view class="label">物料名称：</view>
					{{ info.materialName }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">物料编码：</view>
					{{ info.materialNb }}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">批次号：</view>
					{{ info.batchNb }}
				</view>
				<!-- <view class="text-line m-b-8 ">
					<view class="label">库存数量：</view>
					{{ info.expireDate }}
				</view> -->
				<view class="text-line m-b-8 ">
					<view class="label">有效期：</view>
					{{ info.expireDate }}
				</view>

				<view>
					<view class="text-line m-b-8 ">
						<view class="label">托盘编码：</view>
						{{ info.palletCode }}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">所在库位：</view>
						{{ info.binCode }}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">所在仓库：</view>
						{{ info.wareCode }}
					</view>
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
			info: false
		};
	},
	onLoad(options) {
		console.log(options);
		this.getByMesBarCode(options.barCode);
	},
	onLaunch() {
		Bus.$off('scancodedate');
	},
	methods: {
		async getByMesBarCode(barCode) {
			try {
				const data = await this.$store.dispatch('stock/getByMesBarCode', barCode);
				if (!data) {
					throw error({ message: '系统未找到此SSCC信息，请检查' });
				}
				this.info = data;
			} catch (e) {
				this.$refs.message.error(e.message);
			}
		}
	},
	mounted() {},
	watch: {}
};
</script>

<style lang="scss">
.main {
	height: 100%;
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
