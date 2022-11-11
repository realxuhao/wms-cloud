<template>
	<view class="wrapper">
		<view class="header">
			<view>
				<view class="bar"></view>
				<uni-nav-bar
				  title="WMS 仓储管理"
				/>
			</view>
			
			<view class="list header-box">
				<view class="list-item" @click="handleGoto('/pages/materialIn/index')">
					<uni-icons custom-prefix="iconfont" class="icon icon-ruku" type="icon-ruku"></uni-icons>
					<view class="text">原材料入库</view>
				</view>
				<view class="list-item" @click="handleGoto('/pages/binIn/binIn')">
					<uni-icons custom-prefix="iconfont" class="icon icon-shangjia" type="icon-shangjia2"></uni-icons>
					<view class="text">原材料上架</view>
				</view>
				<view class="list-item">
					
				</view>
			</view>
		</view>
		
		<view class="action-content">
			<!-- 开发中敬请期待 -->
		</view>
		<ScanCode></ScanCode>
	</view>
</template>

<script>
	import ScanCode from '@/components/ScanCode'
	export default{
		components:{
			ScanCode
		},
		methods:{
			handleGoto(url){
				uni.navigateTo({
					url
				})
			}
		}
	}
</script>

<style lang="scss">
	.wrapper{
		min-height: 100vh;
		background: rgb(241,242,247);
	}
	.header{
		background: $primary-color;
		padding:0px 8px;
		height: 160px;
		border-bottom-left-radius:60px ;
		border-bottom-right-radius: 60px;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		.header-box{
		}
		
	}
	
	.list{
		width: 100%;
		padding: 20px 0;
		display: flex;
		justify-content: space-between;
		border-radius: 4px;
		background: #fff;
		box-shadow: 0 2px 10px 0 #ccc;
		.list-item{
			flex: 1;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			color: #333;
			.icon{
				margin-bottom: 8px;
				font-size: 32px !important;
				&.icon-ruku{
					color:rgb(46,151,222) !important;
				}
				&.icon-shangjia{
					color:rgb(90,238,216) !important;
				}
			}
			.text{
				font-size: 14px;
			}
		}
		
	}
	
	.action-content{
		font-size: #ccc;
		margin-top: 40px;
		text-align: center;
	}
	
	/deep/.uni-navbar--border{
		border-bottom-color:transparent !important;
	}
</style>