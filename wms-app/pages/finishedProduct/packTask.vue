<template>
	<my-page nav-title="成品打包">
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
				<view class="card" v-for="item in list" :key="item.id" @click="handleGoto(item)">
					<view class="card-header">
						<text class="material-name">移库日期：{{ item.stockMovementDate }}</text>
						<!-- <text class="status">已入库</text> -->
					</view>
					<view class="card-text m-b-4">Shipping Mark：{{ item.shippingMark }}</view>
					<view class="card-text">
						<text>ETO PO：{{ item.etoPo }}</text>
					</view>
				</view>
				<Empty v-if="!list.length"></Empty>
			</hr-pull-load>
			<Message ref="message"></Message>
		</view>
	</my-page>
</template>

<script>
import Empty from '@/components/Empty';
import Message from '@/components/Message';
import hrPullLoad from '@/components/hr-pull-load/hr-pull-load';
export default {
	name: 'MaterialIn',
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
			const {data:{ rows, total }} = await this.$store.dispatch('finishedProduct/getTaskList', options);
			console.log(11,rows)
			return { rows, total };
		},
		handleGoto(record) {
			const url = `/pages/finishedProduct/packOperator?id=${record.id}`;
			uni.navigateTo({
				url
			});
		},
		async loadData() {
			try {
				this.pageNum = 1;
				const { rows, total } = await this.getList();
				this.list = rows;
				console.log('this.list1');
				console.log(this.list);
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
		align-items: flex-start;
		margin-bottom: 12px;
		.material-name {
			color: $primary-color;
			font-size: 14px;
			flex: 1;
		}
		.status {
			display: inline-block;
			font-size: 14px;
			color: $uni-color-success;
			width: 44px;
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
</style>
