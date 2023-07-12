<template>
	<view class="page-wrapper">
		<view class="bar"></view>
		<view class="page-header" v-if="showNavBar">
			<slot name="page-header">
				<uni-nav-bar :shadow="shadow" :border="border" :title="navTitle" left-icon="left"
					@clickLeft="handleBack" />
			</slot>
		</view>
		<view class="page-main">
			<slot name="page-main"></slot>
		</view>
		<slot></slot>
	</view>
</template>
<script>
	export default {
		props: {
			shadow: {
				type: Boolean,
				default: true
			},
			border: {
				type: Boolean,
				default: true
			},
			navTitle: {
				type: String,
				required: false
			},
			showNavBar: {
				type: Boolean,
				default: true,
				required: false
			},
			noReturn: {
				type: Boolean,
				default: false,
			}
		},
		methods: {
			handleBack() {
				if (this.noReturn) {
					this.$emit('returnCallBack')
					return
				}

				uni.navigateBack({
					delta: 1
				})
			}
		}
	}
</script>
<style lang="scss">
	.page-wrapper {
		background: transparent;
	}

	.bar {
		height: var(--status-bar-height);
		background: rgb(0, 0, 0);
	}

	.page-wrapper {
		display: flex;
		flex-direction: column;
		height: 100vh;
	}

	.page-main {
		flex: 1;
		overflow: auto;
		background: rgb(241, 242, 247);
	}
</style>