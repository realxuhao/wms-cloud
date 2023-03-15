<template>
		<my-page nav-title="打包">
			<view class="main" slot="page-main">
				<view class="header m-b-8">
					<view class="text-line m-b-8 ">
						<view class="label">SSCC码：</view>
						{{materialInfo.ssccNumber}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">物料名称：</view>
						{{materialInfo.materialName}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">
							物料编码：
						</view>
						{{materialInfo.materialNb}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">
							总打包数：
						</view>
						{{afterPackingCount}}
					</view>
				</view>
				
				<view class="content">
					<view class="header-box">
						<view class="steps">
							<text class="current-step-index">{{stepIndex}}</text>
							<text>/{{afterPackingCount}}</text>
						</view>
					</view>
					
					<uni-section class="mb-10" title="当前托FJ" type="line">
						<template v-slot:right>
							<uni-tag text="拆"></uni-tag>
						  </template>
						<uni-list v-show="currentTaskBarCodeList.length">
							<uni-list-item v-for="(item,index) in currentTaskBarCodeList" :key="item" :title="item" ellipsis="1">
								<template v-slot:footer>
									<uni-icons @click="handleDelete(index)" custom-prefix="custom-icon" type="closeempty" size="18" color="#541b86"></uni-icons>
								</template>
							</uni-list-item>
						</uni-list>
						<view v-show="!currentTaskBarCodeList.length">请扫描成品标签二维码</view>
						<view class="footer-box">
							<o-btn size="sm" @click="handleClean">清空</o-btn>
							<o-btn size="sm" type="primary" @click="handleNext" :loading="submitLoading">下一托</o-btn>
						</view>
					</uni-section>
					
				</view>
			</view>
			<Message ref="message"></Message>
		</my-page>
	</view>
</template>

<script>
	import Message from '@/components/Message'
	import Bus from '@/utils/bus'
	import _ from 'lodash'
	
	export default {
		components:{
			Message,
		},
		data() {
			return {
				submitLoading:false,
				materialInfo:{},
				
				taskList:[
					{palletQuantity:10,isSplit:'拆',afterPacking:0,prodOrder:1100247},
					{palletQuantity:10,isSplit:'',afterPacking:10,prodOrder:1100248},
				],
				allScanBarCodeList:[],
				currentTaskBarCodeList:['202403226690063911100247521031104222032911260000501','202403226690063911100247521031104222032911262000050','202403226690063911103024752103110422203291126000050'],
				stepIndex:1,
			};
		},
		computed:{
			allTaskCount(){
				const count =_.sumBy( _.filter(this.taskList,x=>x.isSplit !=='拆'),'palletQuantity')
				return count
			},
			afterPackingCount(){
				const count =_.sumBy(this.taskList,'afterPacking')
				return count
			},
			taskListCountMap(){
				return _.reduce((dict,item)=>{
					dict[item.prodOrder] = item.palletQuantity
					return dict
				},{})
			}
		},
		onLoad(options){
			this.initScanCode()
		},
		onLaunch() {
			Bus.$off("scancodedate");
		},
		methods:{
			onReset(){
				this.currentTaskBarCodeList = []
			},
			onCheck(){
				const allScanBarCodeMap = _.map(this.allScanBarCodeList,x=>x.substring(10,7))
				
				_.each(_.uniq(allScanBarCodeMap),item=>{
					const count =  _.filter(allScanBarCodeMap,x=>x===item).length
					if(count>this.taskListCountMap[item]){
						throw(`编号“${count}”扫描次数不一致`)
					}
				})
				
			},
			handleDelete(index){
				if(this.submitLoading){
					this.$refs.message.info('正在提交不允许编辑')
					return
				}
				this.currentTaskBarCodeList.splice(index,1)
			},
			handleClean(){
				
			},
			async initScanCode(){
				Bus.$on('scancodedate',(data)=>{
					
				})
			},
			async handleNext(){
				try{
					this.submitLoading = true
					this.stepIndex +=1
					this.onReset()
				}catch(e){
					//TODO handle the exception
				}finally{
					this.submitLoading = false
				}
			},
			async handleGoBack(){
				uni.navigateBack({delta:1})	
			},
		
			async lodaData(){
			},
		
			
			async onSubmit(){
				
			}
		},
		mounted() {
			this.lodaData()
		},
		watch:{
			
		}
	}
</script>

<style lang="scss" scoped>
.main{
		height: 100%;
		padding: 8px;
		box-sizing: border-box;
		display: flex;
		flex-direction: column;
	}

.header{
	background: #fff;
	padding: 8px;
	border-radius: 4px;
}

.content{
	background: #fff;
	padding: 8px 8px 8px;
	border-radius: 4px;
	.header-box{
		display: flex;
		.steps{
			flex: 1;
			display: flex;
			justify-content: flex-end;
			align-items: center;
			font-size: 15px;
			color: #666;
			.current-step-index{
				color: #2979ff;
				// font-weight: bold;
			}
		}
	}
	
	.footer-box{
		margin-top: 8px;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
}

/deep/.uni-list-item__container{
	padding: 12px 4px;
}


</style>
