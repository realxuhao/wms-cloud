<template>
	<view class="wrapper">
		<view class="header">
			<view class="bar"></view>
			<uni-nav-bar>
			 <view class="nav-title" @click="handleOpenPicker">
				<text class="nav-title-name">{{plantName||'请选择工厂/仓库'}}</text>
				<uni-icons class="icon" type="bottom" size="16"></uni-icons>
			 </view>
			 </uni-nav-bar>
		</view>
		
		<view class="main">
			<uni-section title="常用" type="line" class="m-b-12">
				<view class="list header-box">
					<view class="list-item" @click="handleGoto('/pages/materialIn/index')">
						<uni-icons custom-prefix="iconfont" class="icon icon-ruku" type="icon-ruku"></uni-icons>
						<view class="text">原材料入库</view>
					</view>
					<view class="list-item" @click="handleGoto('/pages/binIn/binIn')">
						<uni-icons custom-prefix="iconfont" class="icon icon-shangjia" type="icon-jiechubangding"></uni-icons>
						<view class="text">原材料上架</view>
					</view>
				</view>
			</uni-section>
			<uni-section title="生产叫料" type="line" class="m-b-12">
				<view class="list header-box">
					<view class="list-item" @click="handleGoto('/pages/materialCall/binDown')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiajia" type="icon-xiajia" color="BLUE"></uni-icons>
						<view class="text">拣配下架</view>
					</view>
					<view class="list-item" @click="handleGoto('/pages/materialCall/receiving')">
						<uni-icons custom-prefix="iconfont" class="icon icon-purchasereceipt" type="icon-purchasereceipt" color="#069314"></uni-icons>
						<view class="text">生产收料</view>
					</view>
					<view class="list-item" @click="handleGoto('/pages/wareShift/cancellingStocks')">
						<uni-icons custom-prefix="iconfont" class="icon icon-chejiantuiliaojiaojie" type="icon-chejiantuiliao" color="#ED1820"></uni-icons>
						<view class="text">产线退料</view>
					</view>
				</view>
			</uni-section>
			<uni-section title="仓内管理" type="line"  class="m-b-12">
				<view class="list header-box">
					<view class="list-item" @click="handleGoto('/pages/manualTrans/binDown')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiajia" type="icon-xiajia" color="BLUE"></uni-icons>
						<view class="text">转储下架</view>
					</view>
					<view class="list-item" @click="handleGoto('/pages/manualTrans/binIn')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiehuo" type="icon-shangjia2" color="#1afa29"></uni-icons>
						<view class="text">转储上架</view>
					</view>	
				</view>
			</uni-section>
			
			<uni-section title="移库业务" type="line">
				<view class="list header-box">
					<view class="list-item" @click="handleGoto('/pages/wareShift/binDown')">
						<uni-icons custom-prefix="iconfont" class="icon baocaixiajiabeiliao" type="icon-baocaixiajiabeiliao" color="#00ADD4"></uni-icons>
						<view class="text">移库下架</view>
					</view>
					<view class="list-item" @click="handleGoto('/pages/materialCall/shipment')">
						<uni-icons custom-prefix="iconfont" class="icon icon-wuliufahuo" type="icon-wuliufahuo" color="#009A9A"></uni-icons>
						<view class="text">移库发运</view>
					</view>
					<view class="list-item" @click="handleGoto('/pages/wareShift/receiving')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiehuo" type="icon-discharge" color="#00ADD4"></uni-icons>
						<view class="text">移库收货</view>
					</view>
					<view class="list-item" @click="handleGoto('/pages/wareShift/binIn')">
						<uni-icons custom-prefix="iconfont" class="icon icon-xiehuo" type="icon-shangjia2" color="#1afa29"></uni-icons>
						<view class="text">移库上架</view>
					</view>
					
				</view>
			</uni-section>
			
			
		</view>
		
		<!-- <view class="action-content">
			<view class="text" @click="handleGoto('/pages/picking/picking')">拣配下架</view>
			<view class="text" @click="handleGoto('/pages/receivingMaterial/receivingMaterial')">生产收料</view>
			<view class="text" @click="handleGoto('/pages/wareTransshipment/shipment')">发运</view>
			<view class="text" @click="handleGoto('/pages/wareTransshipment/receiving')">收货</view>
			<view class="text" @click="handleGoto('/pages/wareTransshipment/binIn')">上架</view>
			<view class="text" @click="handleGoto('/pages/wareShift/wareShift')">移库下架</view>
		</view>
		 -->
		
		
		<ScanCode></ScanCode>
		<uni-data-picker
			ref="picker"
			popup-title="请选择工厂/仓库" 
			:localdata="dataTree"
			@change="handleChangePlant"
		>
		</uni-data-picker>
	</view>
</template>

<script>
	import ScanCode from '@/components/ScanCode'
	import _ from 'lodash'
	
	export default{
		components:{
			ScanCode,
		},
		data(){
			return {
				dataTree:[],
				plantName:'',
			}
		},
		computed:{
			
		},
		methods:{
			async loadPlantList(){
				const data = await this.$store.dispatch('plant/getList')
				const plantList = _.uniqBy(data,['factoryCode'])
				const list = []
				_.each(plantList,(plant,index)=>{
					const plantIndex = index+1
					const obj = {text:plant.factoryCode,children:[],value:`${plantIndex}-${index}`}
					_.each(data,(item,itemIndex) =>{
						if(item.factoryCode === plant.factoryCode){
							const ware = {text:item.code,value:`${plantIndex}-${itemIndex+1}`,code:item.code}
							obj.children.push(ware)
						}
					})
					list.push(obj)
				})
				
				this.dataTree = list
			},

			handleGoto(url){
				if(!this.plantName){
					uni.showToast({
						title: '请选择工厂/仓库',
						icon:'none',
						duration: 2000
					})
					return
				}
				
				uni.navigateTo({url})
			},
			handleChangePlant(val){
				const {detail:{value}} = val
				const plantName = `${value[0].text}/${value[1].text}`
				
				const wareCode = value[1].text
				uni.setStorageSync('plant',{
						name:plantName,
						wareCode
					})
				
				this.plantName = plantName
			},
			handleOpenPicker(){
				this.$refs.picker.show()
			}
		},
		mounted() {
			const plant = uni.getStorageSync('plant')
			this.plantName = plant.name
			
			this.loadPlantList()
		}
	}
</script>

<style lang="scss">
	.wrapper{
		min-height: 100vh;
		background: rgb(241,242,247);
		box-sizing: border-box;
	}
	.header{
		padding-top: var(--status-bar-height);
		background: $primary-color;
		display: flex;
		justify-content: center;
	}
	
	.main{
		padding: 8px 8px;
		box-sizing: border-box;
		.uni-section{
			box-shadow: 0 2px 10px 0 #ccc;
		}
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
	
	/deep/.uni-navbar--border{
		border-bottom-color:transparent !important;
	}
	
	/deep/.uni-navbar__header-container{
		justify-content: center;
		align-items: center;
		.nav-title{
			font-size: 18px;
			color: #fff;
			text-align: center;
			.icon{
				color: #fff !important;
				margin-left: 8px;
			}
		}
	}
	
	/deep/.uni-data-tree-input{
		display: none;
		z-index: -1;
	}
</style>