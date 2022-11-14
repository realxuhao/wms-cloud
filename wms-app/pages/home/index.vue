<template>
	<view class="wrapper">
		<view class="header">
			<view>
				<view class="bar"></view>
				<uni-nav-bar>
				 <view class="nav-title" @click="handleOpenPicker">
					<text class="nav-title-name">{{plantName||'请选择工厂/仓库'}}</text>
					<uni-icons class="icon" type="bottom" size="16"></uni-icons>
				 </view>
				 </uni-nav-bar>
			</view>
			
			<view class="list header-box">
				<view class="list-item" @click="handleGoto('/pages/materialIn/index')">
					<uni-icons custom-prefix="iconfont" class="icon icon-ruku" type="icon-ruku"></uni-icons>
					<view class="text">原材料入库</view>
				</view>
				<view class="list-item" @click="handleGoto('/pages/binIn/binIn')">
					<uni-icons custom-prefix="iconfont" class="icon icon-shangjia" type="icon-shangjia2"></uni-icons>
					<view class="text">原材料上架</view>
				</view>
				<view class="list-item">
					
				</view>
			</view>
		</view>
		
		<view class="action-content">
			<!-- 开发中敬请期待 -->
		</view>
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
				dataTree:[
					{
						text: "工厂1",
						value: "1-0",
						children:[
							{
								text: "1号库",
								value: "1-1"
							}
						]
					}
				],
				plantName:''
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
		background: $primary-color;
		padding:16px 8px 0px 8px;
		height: 160px;
		border-bottom-left-radius:60px ;
		border-bottom-right-radius: 60px;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		.header-box{
		}
		
	}
	
	.list{
		width: 100%;
		padding: 20px 0;
		display: flex;
		justify-content: space-between;
		border-radius: 4px;
		background: #fff;
		box-shadow: 0 2px 10px 0 #ccc;
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
			.text{
				font-size: 14px;
			}
		}
		
	}
	
	.action-content{
		font-size: #ccc;
		margin-top: 40px;
		text-align: center;
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