<template>
	<my-page nav-title="仓库转运上架">
		<view class="content" slot="page-main">
			<hr-pull-load
				@refresh="handleRefresh"
				@loadMore="handleLoadMore"
				:height="-1"
				:pullHeight="50"
				:maxHeight="100"
				:lowerThreshold="20"
				:bottomTips="bottomTips"
				:isAllowPull="true"
				:isTab="false"
				ref="hrPullLoad"
			>
				<!-- 插入自己的数据-->
				<view class="card" v-for="item in list" :key="item.id">
					<view class="card-header">
						<text class="material-name">{{ item.materialName }}</text>
						<text class="status">待上架</text>
					</view>
					<view class="card-text m-b-4">物料编码：{{ item.materialNb }}</view>
					<view class="card-text sscc">
						<text>SSCC码：{{ item.ssccNb }}</text>
						<text class="time">{{ item.createTime }}</text>
					</view>
				</view>
				<Empty v-if="!list.length"></Empty>
			</hr-pull-load>
			<Message ref="message"></Message>
		</view>

		<view class="action" @click="handleGotoScan"><uni-icons type="scan" size="28" color="#fff"></uni-icons></view>
	</my-page>
</template>

<script>
import Empty from '@/components/Empty';
import Message from '@/components/Message';
import hrPullLoad from '@/components/hr-pull-load/hr-pull-load';

export default {
	components: {
		Empty,
		Message,
		hrPullLoad
	},
	data() {
		return {
			list: [],
			total: 0,
			pageSize: 10,
			pageNum: 1,
			bottomTips: ''
		};
	},
	created() {
		this.loadData();
	},
	methods: {
		async getList() {
			const options = { pageSize: this.pageSize, pageNum: this.pageNum };
			const { rows, total } = await this.$store.dispatch('kanban/getWaitingBinIn', options);
			return { rows, total };
		},
		handleGotoScan() {
			uni.navigateTo({
				url: '/pages/wareTransshipment/binInScan'
			});
		},
		async loadData() {
			try {
				this.pageNum = 1;
				const { rows, total } = await this.getList();
				this.list = rows;
				this.total = total;
			} catch (e) {
				this.$refs.message.error(e.message);
			}
		},
		async handleRefresh() {
			await this.loadData();
			this.$refs.hrPullLoad.reSet();
		},

		async handleLoadMore() {
			try {
				const length = this.list.length;
				if (length < this.total) {
					this.bottomTips = 'loading';
					this.pageNum += 1;
					const { rows } = await this.getList();

					this.list = this.list.concat(rows);
				} else {
					this.bottomTips = 'nomore';
				}
			} catch (e) {
				console.log(e.message);
				this.$refs.message.error(e.message);
			} finally {
				this.$refs.hrPullLoad.reSet();
			}
		}
	}
};
</script>

<style lang="scss">
.content {
	padding: 8px 12px;
	box-sizing: border-box;
	height: 100%;
}

.card {
	padding: 12px;
	background: #fff;
	border-radius: 4px;
	margin-bottom: 4px;
	.card-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 12px;
		.material-name {
			color: $primary-color;
			font-size: 14px;
		}
		.status {
			font-size: 14px;
			color: $uni-color-error;
		}
	}
	.card-text {
		font-size: 12px;
		color: #999;
	}
	.sscc {
		display: flex;
		justify-content: space-between;
	}
}

.action {
	border: 1px solid $uni-border-color;
	width: 60px;
	height: 60px;
	display: flex;
	justify-content: center;
	align-items: center;
	border-radius: 50%;
	color: #fff;
	position: fixed;
	bottom: 60px;
	right: 40px;
	background: rgba(84, 27, 134, 0.7);
	box-shadow: 0 1px 3px 2px rgba(0, 0, 0, 0.5);
}
</style>
