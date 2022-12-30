<template>
  <my-page nav-title="产线退料">
	<view slot="page-main" class="main">
		<view class="list header-box">
			<view class="list-item" @click="handleGoto('/pages/wareShift/cancellingStocksScan')">
				<uni-icons class="icon" type="plusempty" color="BLUE"></uni-icons>
				<view class="text">新增</view>
			</view>
			<view class="list-item" @click="handleGoto('/pages/wareShift/cancellingStocksConfirm')">
				<uni-icons custom-prefix="iconfont" class="icon icon-purchasereceipt" type="icon-purchasereceipt" color="#069314"></uni-icons>
				<view class="text">确认</view>
			</view>
			<view class="list-item" >
				<uni-icons custom-prefix="iconfont" class="icon icon-chejiantuiliaojiaojie" type="icon-chejiantuiliao" color="#ED1820"></uni-icons>
				<view class="text">上架</view>
			</view>
		</view>
	</view>
		 <!-- <AloysTab slot="page-main" class="flex flex-column"  :tabs="tabs" @change="onTabChange">
		    <Pending slot="content0"></Pending>
		    <Completed slot="content1"></Completed>
		  </AloysTab> -->
		 <!-- <view class="action" @click="handleGotoScan">
			  <uni-icons type="scan" size="28" color="#fff"></uni-icons>
		  </view> -->
  </my-page>
</template>

<script>
import AloysTab from "@/components/aloys-tab/aloys-tab";
import Pending from "./cancellingStocksPending.vue";
import Completed from "./cancellingStocksCompleted.vue";

export default {
  name: "binIn",
  components: {
    AloysTab,
    Pending,
    Completed,
  },
  data() {
    return {
      tabs: [{ title: "异常料" }, { title: "正常料" }],
      currentTabIndex: 0,
    };
  },
  methods: {
    onTabChange(id) {
      console.log(id);
    },
	handleGoto(url){
		uni.navigateTo({url})
	},
  },
};
</script>

<style lang="scss">
.main{
	padding: 12px 10px;
	box-sizing: border-box;
}
	
/deep/.tabs {
  background: #fff;
  .tab-item {
    color: #999;
  }
}  

/deep/.swiper-box{
	  flex: 1;
  }
  
  .action{
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
	  background: rgba(84,27,134,0.7);
	  box-shadow: 0 1px 3px 2px rgba(0,0,0,0.5);
  }
  
  .list{
  	width: 100%;
  	padding: 12px 0;
  	display: flex;
  	justify-content: space-between;
  	border-radius: 4px;
  	background: #fff;
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
  		.icon-img{
  			width: 32px;
  			height: 32px;
  			margin-bottom: 8px;
  		}
  		.text{
  			font-size: 14px;
  		}
  	}
  	
  }
</style>
