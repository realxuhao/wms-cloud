<template>
  <my-page nav-title="原材料入库">
		  <AloysTab slot="page-main" class="flex flex-column"  :tabs="tabs" @change="onTabChange">
		    <Pending slot="content0"></Pending>
		    <Completed slot="content1"></Completed>
		  </AloysTab>
		  <view class="action" @click="handleGotoScan">
			  <uni-icons type="scan" size="28" color="#fff"></uni-icons>
		  </view>
	  </view>
	  
  </my-page>
</template>

<script>
import AloysTab from "@/components/aloys-tab/aloys-tab";
import Pending from "./pending";
import Completed from "./completed";

export default {
  name: "MaterialIn",
  components: {
    AloysTab,
    Pending,
    Completed,
  },
  data() {
    return {
      tabs: [{ title: "待入库" }, { title: "入库记录" }],
      currentTabIndex: 0,
    };
  },
  methods: {
    onTabChange(id) {
      console.log(id);
    },
	handleGotoScan() {
	  uni.navigateTo({
	    url: "/pages/scan/index",
	  });
	},
  },
};
</script>

<style lang="scss">
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
	  width: 48px;
	  height: 48px;
	  display: flex;
	  justify-content: center;
	  align-items: center;
	  border-radius: 50%;
	  color: #fff;
	  position: fixed;
	  bottom: 20px;
	  right: 20px;
	  background: rgba(84,27,134,0.7);
  }
</style>
