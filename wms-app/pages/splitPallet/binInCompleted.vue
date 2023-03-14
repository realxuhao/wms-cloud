<template>
	<view class="content">
	 <hr-pull-load
	         @refresh='handleRefresh'
	         @loadMore='handleLoadMore'
	         :height='-1'
	         :pullHeight='50'
	         :maxHeight='100'
	         :lowerThreshold='20'
	         :bottomTips='bottomTips'
	         :isAllowPull="true"
	         :isTab='false'
	         ref='hrPullLoad'>
	         <!-- 插入自己的数据-->
	           <view class="card" v-for="item in list" :key="item.id">
	             <view class="card-header">
	           	<text class="material-name">{{item.materialName}}</text>
	           	<text class="status">已上架</text>
	             </view>
	             <view class="card-text m-b-4">物料编码：{{item.materialNb}}</view>
				 <view class="card-text m-b-4">库位：{{item.sourceBinCode}}</view>
	             <view class="card-text sscc">
	           	<text>SSCC码：{{item.ssccNb}}</text>
	           	<text class="time">{{item.operateTime}}</text>
	             </view>
	             </view>
			<Empty v-if="!list.length"></Empty>
	</hr-pull-load>
		<Message ref="message"></Message>
	</view>
</template>

<script>
import Empty from '@/components/Empty'
import Message from '@/components/Message';
import hrPullLoad from '@/components/hr-pull-load/hr-pull-load';

export default {
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
	  bottomTips:'',
    };
  },
  created(){
  	  this.loadData()
  },
  methods: {
	async getList(){
		const options = {pageSize:this.pageSize,pageNum:this.pageNum,status:1}
		const {rows,total} = await this.$store.dispatch('splitPallet/getList',options)
		return {rows,total}
	},
	async loadData(){
		try{
			this.pageNum = 1
			const {rows,total} = await this.getList()
			this.list = rows
			this.total = total
		}catch(e){
			this.$refs.message.error(e.message)
		}
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
				const {rows} = await this.getList()
				
				this.list = this.list.concat(rows);
			} else {
				this.bottomTips = 'nomore'
			}
		}catch(e){
			console.log(e.message)
			this.$refs.message.error(e.message)
		}finally{
			this.$refs.hrPullLoad.reSet();
		}
	},
  },
};
</script>

<style lang="scss">
.content {
  padding: 8px 12px;
  box-sizing: border-box;
  height: 100%;
}

.card {
  padding: 12px;
  background: #fff;
  border-radius: 4px;
  margin-bottom: 4px;
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 12px;
    .material-name {
      color: $primary-color;
      font-size: 14px;
	  flex: 1;
    }
    .status {
	  display: inline-block;
      font-size: 14px;
      color: $uni-color-success;
	  width: 44px;
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
