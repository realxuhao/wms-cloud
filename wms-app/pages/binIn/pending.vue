<template>
  <view class="content">
	  <hr-pull-load
	           @refresh='handleRefresh'
	           @loadMore='handleLoadMore'
	           :height='-1'
	           :pullHeight='50'
	           :maxHeight='100'
	           :lowerThreshold='20'
	           :bottomTips="bottomTips"
	           :isAllowPull="true"
	           :isTab='false'
	           ref='hrPullLoad'>
	           <!-- 插入自己的数据-->
	            <view class="card" v-for="item in list" :key="item.id">
	              <view class="card-header">
	                <text class="material-name">{{item.materialName}}</text>
	                <text class="status">待上架</text>
	              </view>
	              <view class="card-text m-b-4">物料编码：{{item.materialNb}}</view>
	              <view class="card-text sscc">
	                <text>SSCC码：{{item.ssccNumber}}</text>
	                <text class="time">{{item.uploadTime}}</text>
	              </view>
	            </view>
	            <Empty v-show="!list.length"></Empty>
	            <Message ref="message"></Message>
	  </hr-pull-load>
   
  </view>
</template>

<script>
import Empty from '@/components/Empty'
import Message from '@/components/Message'
import hrPullLoad from '@/components/hr-pull-load/hr-pull-load';
	
export default {
	name:'MaterialInPending',
	components:{
		Empty,
		Message,
		hrPullLoad
	},
  data() {
    return {
	  list:[],
	  total:0,
	  pageSize:10,
	  pageNum:1,
	  bottomTips:''
    };
  },
  created(){
  	  this.loadData()
  },
  methods: {
	  async getPendingBinInList(){
		const options = {pageSize:this.pageSize,pageNum:this.pageNum,state:0}
		const {rows,total} = await this.$store.dispatch('binIn/getPendingBinInList',options)
		return {rows,total}
	  },
	  async handleRefresh(){
	  	await this.loadData()
	  	this.$refs.hrPullLoad.reSet();
	  },
	  
	  async handleLoadMore() {
	  	try{
	  		const length = this.list.length;
	  		if (length < this.total) {
				this.bottomTips = 'loading'
	  			this.pageNum +=1
	  			const {rows} = await this.getPendingBinInList()
	  			this.list = this.list.concat(rows);
	  		} else {
	  			this.bottomTips = 'nomore'
	  		}
	  	}catch(e){
	  		this.$refs.message.error(e.message)
	  	}finally{
	  		this.$refs.hrPullLoad.reSet();
	  	}
	  },
	
	async loadData(){
		try{
			this.pageNum = 1
			const {rows,total} = await this.getPendingBinInList()
			this.list = rows
			this.total = total
		}catch(e){
			this.$refs.message.error(e.message)
		}
	}
  },
};
</script>

<style lang="scss">
.content {
  padding: 8px 12px;
  box-sizing: border-box;
  height: 100%;

  .scan {
    margin-bottom: 8px;
  }
}

.card {
  // box-shadow: #888 0px 0px 3px 1px;
  padding: 12px;
  background: #fff;
  border-radius: 4px;
  margin-bottom: 4px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    .material-name {
      color: $primary-color;
      font-size: 14px;
    }

    .status {
      font-size: 14px;
      color: $uni-color-error;
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
