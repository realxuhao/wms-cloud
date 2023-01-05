<template>
  <my-page nav-title="产线退料确认">
	  <view class="main" slot="page-main">
		  <view class="flex">
			  <view class="header">总共&nbsp;<text class="active">{{list.length}}</text>&nbsp;托;</view>
			  <view class="header">已选择&nbsp;<text class="active">{{hasCheckedItems}}</text>&nbsp;托</view>
		  </view>
		
		  <uni-list class="m-b-12">
		  	<uni-list-item v-for="(item,index) in list" :key="index">
				<template slot="body">
					<view class="order-main">
						<MyRadio class="m-r-8" v-model="item.checked">
							<view class="order-content">
								<view class="title m-b-4">
									{{item.materialName}}
								</view>
								<view class="desc m-b-4">
									<text class="label">物料编码:</text>{{item.materialNb}}
								</view>
								<view class="desc m-b-4">
									<text class="label">仓库:</text>{{item.wareCode}}
								</view>
								<view class="desc m-b-4" v-show="item.areaCode">
									<text class="label">区域:</text>{{item.areaCode}}
								</view>
								<view class="desc m-b-4">
									<text class="label">SSCC码:</text>{{item.ssccNumber}}
								</view>
								<view class="desc">
									<text class="label">状态:</text><uni-tag  :text="typeMap[item.type].text" :type="typeMap[item.type].color" size="mini" />
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
			  :loading="submitLoading" :disabled="!hasCheckedItems" block>提交</o-btn>
		  </view>
	  </view>
	  <uni-popup ref="popup" type="dialog">
	  	<uni-popup-dialog type="info" title="提示" content="请确认提交"  @close="$refs.popup.close()" @confirm="handleSubmit">
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

	const typeMap = {
		0:{
			text:'正常退料',
			color:'success'
		},
		1:{
			text:'异常退料',
			color:'warning'
		}
	}
	export default {
		name:'transhipmentOrderMaterialIn',
		components:{
			Message,
			MyRadio
		},
	  data() {
		return {
			list:[],
			submitLoading:false,
			barCode:''
		};
	  },
	  computed:{
		hasCheckedItems(){
			return _.filter(this.list, 'checked').length;
		},
		typeMap(){
			return typeMap
		}
	  },
	  onLoad(options){
	  },
	  async onPullDownRefresh(){
		  try{
		  	await this.getList()
		  }catch(e){
		  	//TODO handle the exception
		  }finally{
			  uni.stopPullDownRefresh()
		  }
	  },
	  mounted() {
	  	this.getList()
	  },
	  methods: {
		  async getList(){
			 const {rows} = await this.$store.dispatch('wareShift/getReturnMaterialList',{status:0,pageSize:0})
			 this.list = rows
		  },
		  async handleSubmit(){
			  try{
				uni.showLoading({
					title:'正在提交'
				})
				this.submitLoading = true
				const sscc = _.map(_.filter(this.list,x=>x.checked),x=>x.ssccNumber)
			  	await this.$store.dispatch('wareShift/returnMaterialConfirm',sscc)
				this.$refs.message.success('提交成功')
				this.$refs.popup.close()
				this.getList()
			  }catch(e){
				  uni.startPullDownRefresh()
				this.$refs.message.error(e.message)
			  	//TODO handle the exception
			  }finally{
				this.submitLoading = false
				uni.hideLoading()
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
			.label{
				margin-right: 4px;
			}
		}
	}
</style>
