<template>
  <view class="content">
    <view class="card" v-for="item in list" :key="item.id">
      <view class="card-header">
        <text class="material-name">{{item.materialName}}</text>
        <text class="status">待入库</text>
      </view>
      <view class="card-text m-b-4">物料编码：{{item.materialNb}}</view>
      <view class="card-text sscc">
        <text>SSCC码：{{item.ssccNumber}}</text>
        <text class="time">{{item.uploadTime}}</text>
      </view>
    </view>
	<Empty v-show="!list.length"></Empty>
	<Message ref="message"></Message>
  </view>
</template>

<script>
import Empty from '@/components/Empty'
import Message from '@/components/Message'
	
export default {
	components:{
		Empty,
		Message
	},
  data() {
    return {
	  list:[],
	  total:0
    };
  },
  created(){
  	  this.loadData()
  },
  methods: {
	async loadPendingMaterialList(){
		try{
			const {rows,total} = await this.$store.dispatch('materialIn/getPendingMaterialList')
			this.list = rows
			this.total = total
		}catch(e){
			this.$refs.message.error(e.message)
		}
	},
	async loadData(){
		this.loadPendingMaterialList()
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
