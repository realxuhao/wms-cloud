<template>
	<button :class="['o-btn',Size,Block,Square,Round]" :plain="Plain" :disabled="Disabled"
	@tap="onClick" :loading="Loading" hover-class="click" :type="Bg">

		<text v-if="iconFont&&!Loading" class="icon" :class="icon"></text>
		<image v-if="iconImg&&!Loading" class="icon" mode="aspectFit" :src="icon"></image>
		
		<text v-if="Loading&&loadingText">{{loadingText}}</text>
		<text v-else><slot /></text>

	</button>
</template>

<script>
	export default {
		name:'oBtn',
		// #ifdef MP-WEIXIN
		options:{ virtualHost:true },
		// #endif

		props:{
			size:{
				type:String,
				default:'md'
			},
			block:{
				type:[String,Boolean],
				default:false
			},
			type:{
				type:String,
				default:''
			},
			disabled:{
				type:[String,Boolean],
				default:false
			},
			loading:{
				type:[String,Boolean],
				default:false
			},
			loadingText:{
				type:String,
				default:''
			},
			square:{
				type:[String,Boolean],
				default:false
			},
			round:{
				type:[String,Boolean],
				default:false
			},
			icon:{
				type:String,
				default:''
			},
			plain:{
				type:[String,Boolean],
				default:false
			}
		},
		data() {
			return {
				iconFont: '',
				iconImg:'',
			}
		},
		computed:{
			Bg(){
				if(['red','green','blue','orange','yellow','brown','pink',
				'purple','black','grey','gray','white','default','primary',
				'success','info','warning','error','cyan'].includes(this.type)) return this.type;
				else return 'default'
			},
			Size(){
				if(['xs','sm','md','lg'].includes(this.size)) return this.size;
				else return 'md'
			},
			Block(){
				if(!this.block || this.block==='false') return '';
				else return 'block'
			},
			Round(){
				if(!this.round || this.round==='false') return '';
				else return 'round'
			},
			Square(){
				if(!this.square || this.square==='false') return '';
				else return 'square'
			},
			Disabled(){
				if(!this.disabled || this.disabled==='false') return false;
				else return true
			},
			Plain(){
				if(!this.plain || this.plain==='false') return false;
				else return true
			},
			Loading(){
				if(!this.loading || this.loading==='false') return false;
				else return true
			},
		},
		methods: {
			onClick(e){ this.$emit('click',{}) },
			Icon(){
				if(this.icon.includes('.')){
					this.iconImg = this.icon;
				} else {
					this.iconFont = this.icon;
				}
			}
		},
		created() {
			this.Icon();
		}
	}
</script>
<style lang="scss" scoped>

	$color:(
		'red' #F56C6C #fff,'green' #22C55E #fff,'blue' #409EFF #fff,'danger' #F56C6C #fff,
		'orange' #F97316 #fff,'yellow' #e7a43f #fff,'brown' #a5673f #fff,
		'pink' #EC4899 #fff,'purple' #ab47bc #fff,'black' #333 #fff,
		'grey' #78909C #fff,'gray' #BDBDBD #fff,'white' #fff #888,
		'default' #fff #333,'primary' #409EFF #fff,'success' #22C55E #fff,
		'info' #78909C #fff, 'warning' #e7a43f #fff, 'error' #F56C6C #fff,
		'cyan' #06B6D4 #fff,
	);

	.o-btn{
		position: relative;
		display: inline-flex;
		flex-direction:row;
		align-items: center;
		box-sizing: border-box;
		height:84rpx;line-height:78rpx;
		padding:0 30rpx;font-size:28rpx;
		color:#333;overflow:hidden;
		font-weight: 500;
		text-align:center;
		border-radius:8rpx;
		cursor:pointer;
		transition:opacity 0.2s;
		border:2rpx solid #eaedec;
		background-color:#fff;
		vertical-align: middle;
		&.o-btn{margin:4rpx;}
		&[type=default]{
			border-color: #d9d9d9;
		}
		
		text.icon{
			display: inline-block;
			width:36rpx;
			height:36rpx;
			margin-right: 6rpx;
			vertical-align: bottom;
		}
		image.icon{
			width:36rpx;
			height:36rpx;
			margin-right: 8rpx;
			display: block;
			vertical-align: bottom;
		}
		
		&::after{opacity:0;}
		&[class*='line-']{border-width:2rpx;}
		&[class*='lines-']{border-width:4rpx;}
		&.xs{height:50rpx;font-size:20rpx;line-height:44rpx;padding:0 16rpx;}
		&.sm{height:64rpx;font-size:24rpx;line-height:58rpx;padding:0 24rpx;}
		&.lg{height:96rpx;font-size:32rpx;line-height:90rpx;padding:0 48rpx;}
		&.block{display:flex;justify-content: center;align-items: center;}
		&[class*='bg-']{color:#fff;}
		&[disabled]{opacity:0.6;}
		&[plain]{
			background-color: #fff !important;
			@each $name, $val, $fontColor in $color{
				&[type=#{$name}]{color:$val;border-color:$val;}
			}
		}
		&[loading]{
			text{display: inline-block;}
			&.xs::before{width:24rpx;height:24rpx;}
			&.sm::before{width:28rpx;height:28rpx;}
			&::before,&.md::before{}
			&.lg::before{width:40rpx;height:40rpx;}
		}
	}
	
	.square{border-radius:0 !important;}
	.round{border-radius:2000rpx !important;}
	
	@each $name, $val, $fontColor in $color{
		button[type=#{$name}]{
			background-color:$val ;
			border-color:$val;
			color:$fontColor;
		}
	}

	.click:after{
		content:'';
		opacity:0.8;
		left:0;right:0;
		top:0;bottom:0;
		z-index:9999;
		background-color: rgba(0, 0, 0, 0.06);
		position:absolute;
		border-radius:0;
		border-width:0;
	}

</style>