<template>
  <my-page nav-title="移库收货">
	  <view class="main" slot="page-main">
		  <view class="flex">
			  <view class="header">总共&nbsp;<text class="active">{{orderList.length}}</text>&nbsp;托;</view>
			  <view class="header">已选择&nbsp;<text class="active">{{hasCheckedItems}}</text>&nbsp;托</view>
		  </view>
		
		  <uni-list class="m-b-12">
		  	<uni-list-item v-for="(item,index) in orderList" :key="index">
				<template slot="body">
					<view class="order-main">
						<MyRadio class="m-r-8" v-model="item.checked">
							<view class="order-content">
								<view class="title m-b-4">
									{{item.materialName}}
								</view>
								<view class="desc m-b-4">
									<text>物料编码:{{item.materialNb}}</text>
								</view>
								<view class="desc">
									<text>SSCC码:{{item.ssccNumber}}</text>
								</view>
								
							</view>
						</MyRadio>
						
					</view>
				</template>
			</uni-list-item>
		  </uni-list>
		  
		  <view class="submit-btn">
			  <o-btn class="primary-button"
			   @click="$refs.popup.open()"
			  :loading="submitLoading" :disabled="!hasCheckedItems" block type="primary">提交</o-btn>
		  </view>
	  </view>
	  <uni-popup ref="popup" type="dialog">
	  	<uni-popup-dialog type="warn" title="是否确认收货?" :before-close="true" @close="$refs.popup.close()" @confirm="handleSubmit">
			<!-- <view class="header">已选择&nbsp;<text class="active">{{hasCheckedItems}}</text>&nbsp;托</view> -->
		</uni-popup-dialog>
	  </uni-popup>
	  <Message ref="message"></Message>
  </my-page>
</template>

<script>
	import Message from '@/components/Message'
	import MyRadio from '@/components/my-radio/my-radio'
	import _ from 'lodash'
	export default {
		name:'transhipmentOrderMaterialIn',
		components:{
			Message,
			MyRadio
		},
	  data() {
		return {
			orderList:[{materialName:'葡萄糖',materialNb:'1231283213',ssccNumber:'213137183718237821'}],
			submitLoading:false,
			barCode:''
		};
	  },
	  computed:{
		hasCheckedItems(){
			return _.filter(this.orderList, 'checked').length;
		}  
	  },
	  onLoad(options){
		this.barCode = options.barCode
		this.getTranshipmentOrder()
	  },
	  methods: {
		  async getTranshipmentOrder(){
			 const data = await this.$store.dispatch('kanban/getTranshipmentOrder',{mesbarCode:this.barCode})
			 console.log(data)
			 this.orderList = data
		  },
		  async handleSubmit(){
			  try{
				this.submitLoading = true
				const sscc = _.map(_.filter(this.orderList,x=>x.checked),x=>x.ssccNumber)
			  	await this.$store.dispatch('kanban/confirmOrder',{sscc})
				this.$refs.message.success('提交成功')
				this.$refs.popup.close()
			  }catch(e){
				this.$refs.message.error(e.message)
			  	//TODO handle the exception
			  }finally{
				this.submitLoading = false
			  }
		  }
	  },
	};
</script>

<style scoped lang="scss">
	.main{
		background: #fff;
		box-sizing: border-box;
		padding: 8px 0;
		height: 100%;
		overflow-y: auto;
	}
	.header{
		color: #000;
		padding: 0 0 12px 12px;
		display: flex;
		align-items: center;
		.active{
			color: $uni-color-error;
			font-size: 18px;
			font-weight: bold;
		}
	}
	
	.submit-btn{
		padding: 0px 12px;
	}
	
	.order-main{
		width: 100%;
		display: flex;
		align-items: center;
		.desc{
			color: #999;
			font-size: 12px;
			display: flex;
			justify-content: space-between;
		}
	}
</style>
