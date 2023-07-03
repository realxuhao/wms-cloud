<template>
	<my-page nav-title="SUDN拣配任务">
		<AloysTab slot="page-main" class="flex flex-column" :tabs="tabs" @change="onTabChange">
			<Pending :sudnId="params.id" slot="content0"></Pending>
			<Completed :sudnId="params.id" slot="content1">
			</Completed>
		</AloysTab>
		<view class="action" @click="handleGotoScan"><uni-icons type="scan" size="28" color="#fff"></uni-icons></view>
	</my-page>
</template>

<script>
	import AloysTab from '@/components/aloys-tab/aloys-tab';
	import Pending from './sudnPickPending';
	import Completed from './sudnPickCompleted';

	export default {
		name: 'Sudn',
		components: {
			AloysTab,
			Pending,
			Completed
		},
		onLoad(options) {
			this.params = options
		},
		data() {
			return {
				tabs: [{
					title: '未完成'
				}, {
					title: '已完成'
				}],
				currentTabIndex: 0,

				params: {
					id: null
				}
			};
		},
		methods: {
			onTabChange(id) {
				console.log(id);
			},
			handleGotoScan() {
				uni.navigateTo({
					url: `/pages/finishedProduct/sudnPickScan?id=${this.params.id}`
				});
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
</style>