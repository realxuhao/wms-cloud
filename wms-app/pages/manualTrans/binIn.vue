<template>
	<my-page nav-title="移库上架">
		<AloysTab slot="page-main" class="flex flex-column" :tabs="tabs" @change="onTabChange">
			<Pending slot="content0"></Pending>
			<Completed slot="content1"></Completed>
		</AloysTab>
		<view class="action" @click="handleGotoScan"><uni-icons type="scan" size="28" color="#fff"></uni-icons></view>
	</my-page>
</template>

<script>
import AloysTab from '@/components/aloys-tab/aloys-tab';
import Pending from './binInPending.vue';
import Completed from './binInCompleted.vue';

export default {
	name: 'binIn',
	components: {
		AloysTab,
		Pending,
		Completed
	},
	data() {
		return {
			tabs: [{ title: '待上架' }, { title: '已上架' }],
			currentTabIndex: 0
		};
	},
	methods: {
		onTabChange(id) {
			console.log(id);
		},
		handleGotoScan() {
			uni.navigateTo({
				url: '/pages/manualTrans/binInScan'
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
