<template>
		<my-page nav-title="新增产线退料">
			
			<view class="main" slot="page-main">
				<view class="header m-b-8">
					<view class="text-line m-b-8 ">
						<view class="label">批次：</view>
						{{materialInfo.batchNb}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">有效期：</view>
						{{materialInfo.expireDate}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">单位：</view>
						{{materialInfo.unit}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">SSCC码：</view>
						{{materialInfo.ssccNb}}
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
				</view>
			
				<view class="content">		
					<uni-forms  :label-width="80" ref="form" :rules="formRules" :modelValue="form" label-position="left">
						<uni-forms-item label="Cell" name="cell" required>
							<uni-data-picker
								ref="picker"
								popup-title="请选择Cell" 
								:localdata="cellList"
								@change="handleChangeCell"
							>
							</uni-data-picker>
						</uni-forms-item>
						<uni-forms-item label="状态" name="type" required>
							<uni-data-checkbox v-model="form.type" @change="handleTypeChange" :localdata="radioList"></uni-data-checkbox>
						</uni-forms-item>
						<uni-forms-item  label="仓库" name="wareCode" required >
							<uni-data-picker
								ref="picker"
								popup-title="请选择仓库" 
								:localdata="dataTree"
								@change="handleChangePlant"
							>
							</uni-data-picker>
						</uni-forms-item>
						<uni-forms-item  label="存储区" name="areaCode" required v-if="form.type === 1">
							<uni-data-picker
								ref="picker"
								v-model="area"
								popup-title="请选择存储区" 
								:localdata="areaList"
								@change="handleAreaChange"
							>
							</uni-data-picker>
						</uni-forms-item>
						<uni-forms-item  label="数量" name="quantity" required >
							<uni-easyinput type="number" v-model="form.quantity" placeholder="数量" />
						</uni-forms-item>
						
						<o-btn block class="submit-btn primary-button" :loading="submitLoading"  @click="handlePost">提交</o-btn>
					</uni-forms>
					
				</view>
			</view>
			<Message ref="message"></Message>
			<uni-popup ref="alertDialog" type="dialog">
				<uni-popup-dialog type="info" cancelText="取消" confirmText="确定" title="提示" content="请确认提交" @confirm="onSubmit"
				></uni-popup-dialog>
			</uni-popup>
		</my-page>
	</view>
</template>

<script>
	import Message from '@/components/Message'
	import Bus from '@/utils/bus'
	import _ from 'lodash'
	
	const radioList = [
		{
			text: '正常退料',
			value: 0
		},
		{
			text: '异常退料',
			value: 1
		},
	]
	export default {
		components:{
			Message,
		},
		data() {
			return {
				submitLoading:false,
				materialInfo:{},
				barCode:undefined,
				formRules:{
					wareCode:{
						rules: [
							{
								required: true,
								errorMessage: '不能为空',
							},
						]
					},
					areaCode:{
						rules: [
							{
								required: true,
								errorMessage: '不能为空',
							},
						]
					},
					cell:{
						rules: [
							{
								required: true,
								errorMessage: '不能为空',
							},
						]
					},
					quantity:{
						rules: [
							{
								required: true,
								errorMessage: '不能为空',
							},
						]
					}
				},
				form:{
					quantity:undefined,
					type:0,
					areaCode:undefined,
					wareCode:undefined,
					cell:undefined,
					plantNb:undefined,
				},
				materialInfo:{},
				dataTree:[],
				plantList:[],
				cellList:[],
				areaList:[],
				area:undefined,
			};
		},
		computed:{
			radioList(){
				return radioList
			}
		},
		onLoad(options){
			this.barCode = options.barCode
			this.getMaterialInfo(options.barCode)
		},
		methods:{
			handleTypeChange(e){
				const {data} = e.detail
				if(data.value === 1){
					this.getWareList()
				}
			},
			async getCellList(){
				const data = await this.$store.dispatch('wareShift/getCellList')
				this.cellList = _.map(data,x=>({text:x.name,value:x.id}))
			},
			async getWareList(){
				const data = await this.$store.dispatch('wareShift/getWareList',{wareCode:this.form.wareCode})
				console.log(data)
				this.areaList = _.map(data,x=>({text:x.name,value:x.code}))
			},
			async getMaterialInfo(barCode){
				const data = await this.$store.dispatch('material/parsedBarCode',barCode)
				this.materialInfo = data
			},
			async handleGoBack(){
				uni.navigateBack({delta:1})	
			},
			handleChangePlant(val){
				const {detail:{value}} = val
				
				const factoryCode = value[0].text
				const factory = _.find(this.plantList,['factoryCode',factoryCode])
				this.form.plantNb = factory.factoryCode
				
				const wareCode = value[1].text
				const ware = _.find(this.plantList,['code',wareCode])
				this.form.wareCode =ware.code
			},
			handleChangeCell(val){
				const {detail:{value}} = val
				this.form.cell = value[0].text
			},
			handleAreaChange(val){
				const {detail:{value}} = val
				this.form.areaCode = value[0].value
			},
			async lodaData(){
				this.loadPlantList()
				this.getCellList()
			},
			async loadPlantList(){
				const data = await this.$store.dispatch('plant/getList')
				this.plantList = data
				
				const uniqList = _.uniqBy(data,['factoryCode'])
				const list = []
				_.each(uniqList,(plant,index)=>{
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
			async handlePost(){
				this.$refs.form
				  .validate()
				  .then((res) => {
					this.$refs.alertDialog.open()
				  })
				  .catch((err) => {});
			},
			async onSubmit(){
				try{
					uni.showLoading({
						title:'正在提交'
					})
					this.submitLoading = true
					
					const options = {
						...this.form,
						mesBarCode:this.barCode
					}
					const data = await this.$store.dispatch('wareShift/addMaterialReturn',options)
					this.$refs.message.success('提交成功')
					this.handleGoBack()
				}catch(e){
					this.$refs.message.error(e.message)
				}finally{
					uni.hideLoading()
					this.submitLoading = false
				}
				
			}
		},
		mounted() {
			this.lodaData()
		}
	}
</script>

<style lang="scss">
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
	padding: 8px 8px 40px;
	border-radius: 4px;
}

/deep/.uni-data-tree{
	background: #fff;
}


	
	.result-content{
		width: 324px;
		padding: 12px;
		box-sizing: border-box;
		background: #fff;
		border-radius: 4px;
		.result-status{
			color: $uni-color-success;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-bottom: 16px;
			.text{
				margin-left: 8px;
				font-size: 14px;
			}
			
		}
		.label{
			width: 100px;
		}
		.data-box{
			margin-bottom: 16px;
			padding: 0px 8px;
		}
	}
	.flex{
		.custom-input{
			flex: 1;
		}
	}
</style>
