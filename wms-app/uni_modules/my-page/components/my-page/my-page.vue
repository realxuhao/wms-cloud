<template>
	<view class="page-wrapper">
		<view class="bar"></view>
		<view class="page-header">
			<slot name="page-header">
				<uni-nav-bar
				  shadow
				  :title="navTitle"
				  left-icon="left"
				  @clickLeft="handleBack"
				/>
			</slot>
		</view>
		<view class="page-main">
			<slot name="page-main"></slot>
		</view>
		<slot></slot>
	</view>
</template>
<script>
	export default{
		props:{
			navTitle:{
				type:String,
				required:false
			}
		},
		methods:{
			handleBack(){
				uni.navigateBack({delta:1})
			}
		}
	}
</script>
<style lang="scss">
	.bar{
		height: var(--status-bar-height);
	}
	.page-wrapper{
		display: flex;
		flex-direction: column;
		height: 100vh;
	}
	.page-header{
		
	}
	.page-main{
		flex: 1;
		overflow: auto;
		background: rgb(241,242,247);
	}
</style>
