<template>
  <my-page nav-title="装车扫码">
	  <view class="main" slot="page-main">
		<uni-notice-bar single text="请对准条形码进行扫描" />
		  <view class="header"><text class="m-r-4">已扫描</text><text class="error-color m-r-4">{{codeList.length}}</text>&nbsp;托</view>
		  <uni-list class="m-b-12">
		  	<uni-list-item v-for="(item,index) in codeList" :key="index">
				<template slot="body">
					<view class="content">
						<view class="item ellipsis">
							<text class="m-r-8 grey-color sscc">{{index+1}}.</text><text>{{item}}</text>
						</view>
						<o-btn size="sm" @click="handleDelete">删除</o-btn>
					</view>
				</template>
			</uni-list-item>
		  </uni-list>
		  
		  <view class="submit-btn">
			  <o-btn class="primary-button"
			   @click="handleSubmit"
			  :loading="submitLoading" :disabled="codeList.length===0" block >提交</o-btn>
		  </view>
	  </view>
	  <Message ref="message"></Message>
	  <uni-popup ref="popup" type="dialog">
	  	<uni-popup-dialog before-close type="info" cancelText="取消" confirmText="确认" title="确定删除吗?"  @confirm="handleConfirmDelete"
	  	@close="$refs.popup.close()">
	  	</uni-popup-dialog>
	  </uni-popup>
  </my-page>
</template>

<script>
	import Message from '@/components/Message'
	import Bus from '@/utils/bus'
	import _ from 'lodash'
	export default {
		components:{
			Message	
		},
	  data() {
		return {
			codeList:[],
			deleteCurrentCode:'',
			submitLoading:false
		};
	  },
	  onShow(){
		Bus.$on('scancodedate',this.scanCodeCallback)
	  },
	  destroyed() {
		Bus.$off("scancodedate");
	  },
	  methods: {
		  async scanCodeCallback(data){
			if(_.findIndex(this.codeList,x=>x===data.code) < 0){
				this.codeList.push(data.code)
			}
		  },
		  handleDelete(code){
			this.deleteCurrentCode = code
			this.$refs.popup.open()
		  },
		  handleConfirmDelete(){
			  this.$refs.popup.close()
			  const index = _.findIndex(this.codeList,x=>x === this.deleteCurrentCode)
			  this.codeList.splice(index,1)
		  },
		  async handleSubmit(){
			  try{
				this.submitLoading = true
			  	await this.$store.dispatch('kanban/generateOrder',this.codeList)
				this.$refs.message.success('提交成功')
				this.codeList = []
			  }catch(e){
				this.$refs.message.error(e.message)
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
			font-size: 18px;
			font-weight: bold;
		}
	}
	.content{
		width: 100%;
		display: flex;
		align-items: center;
		justify-content: space-between;
		.item{
			flex: 1;
			min-width: 0;
		}
	}
	
	.submit-btn{
		padding: 0px 12px;
	}
</style>
