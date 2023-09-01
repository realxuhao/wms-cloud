<template>
	<my-page nav-title="SPDN移库发运">
		<view slot="page-main" style="height: 100%;width: 100%;">
			<uni-easyinput class="easyinput" suffixIcon="search" v-model="delivery" placeholder="Deliver搜索"
				@iconClick="handleSearch"></uni-easyinput>

			<AloysTab class="flex flex-column" :tabs="tabs" @change="onTabChange">
				<Pending ref="pending" slot="content0"></Pending>
				<Completed ref="complete" slot="content1"></Completed>
			</AloysTab>
		</view>

		<view class="action" @click="handleGotoScan"><uni-icons type="scan" size="28" color="#fff"></uni-icons></view>
	</my-page>
</template>

<script>
	import AloysTab from '@/components/aloys-tab/aloys-tab';
	import Pending from './spdnShipmentPending';
	import Completed from './spdnShipmentCompleted';

	export default {
		name: 'MaterialIn',
		components: {
			AloysTab,
			Pending,
			Completed
		},
		data() {
			return {
				tabs: [{
					title: '待发运'
				}, {
					title: '已发运'
				}],
				currentTabIndex: 0,
				delivery: ''
			};
		},
		methods: {
			onTabChange(id) {
				console.log(id);
			},
			handleGotoScan() {
				uni.navigateTo({
					url: '/pages/finishedProduct/spdnShipmentOperation'
				});
			},
			handleSearch() {
				this.$refs.pending.handleRefresh(this.delivery)
				this.$refs.complete.handleRefresh(this.delivery)
			}
		}
	};
</script>

<style lang="scss">
	/deep/.tabs {
		background: #fff;

		.tab-item {
			color: #999;
		}
	}

	/deep/.swiper-box {
		flex: 1;
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

	.easyinput {
		padding: 0px 8px;
		box-sizing: border-box;
		margin-top: 8px;
		margin-bottom: 8px;
	}
</style>