<template>
	<my-page nav-title="SUDN发运">
		<AloysTab slot="page-main" class="flex flex-column" :tabs="tabs" @change="onTabChange">
			<Pending @on-click="handleGoto" slot="content0"></Pending>
			<Completed slot="content1"></Completed>
		</AloysTab>
	</my-page>
</template>

<script>
	import AloysTab from '@/components/aloys-tab/aloys-tab';
	import Pending from './sudnShipPending';
	import Completed from './sudnShipCompleted';

	export default {
		name: 'SudnShip',
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
					title: '已完成'
				}],
				currentTabIndex: 0
			};
		},
		methods: {
			onTabChange(id) {
				console.log(id);
			},
			handleGoto(item) {
				uni.navigateTo({
					url: `/pages/finishedProduct/sudnShipOperation?sudnId=${item.id}`
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