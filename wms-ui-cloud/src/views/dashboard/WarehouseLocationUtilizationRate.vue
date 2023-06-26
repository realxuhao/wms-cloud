<template>
  <Card title="库存使用率">
    <template slot="header">
      <div class="content-box">
        <div class="date-change">
          <a-radio-group buttonStyle="solid" size="small" v-model="dateType">
            <a-radio-button :value="1">
              月
            </a-radio-button>
            <a-radio-button :value="0">
              日
            </a-radio-button>
          </a-radio-group>
          <div style="width: 200px;">
            <a-range-picker 
              @panelChange="handlePanelChange"
              v-model="date"
              :format="dateType === 1?'YYYY-MM':'YYYY-MM-DD'"
              :mode="dateType === 1?['month','month']:['date','date']"
              @change="onDateChange" />
          </div>
        </div>
      </div>
     
    </template>

    <template slot="main">
      <div class="ware-list">
        <a-checkbox-group v-model="checkedList" :options="wareList" />
      </div>

      <a-row :gutter="[24,24]">
        <a-col :span="12" v-for="(wareCode,index) in checkedList" :key="wareCode">
          <WarehouseLocationUtilizationRateItem :color="colorList[index]" :title="wareCode" :id="'warehouse-location-utilization-rate-item'+wareCode" :list="wareDataMap[wareCode].children"></WarehouseLocationUtilizationRateItem>
        </a-col>
      </a-row>
    </template>
  </Card>
</template>

<script>
import Card from './Card.vue'
import WarehouseLocationUtilizationRateItem from './WarehouseLocationUtilizationRateItem'
import _ from 'lodash'
import moment from 'moment'

import colorList from '@/utils/echartsColor'

export default {
  name: 'Index',
  components: {
    Card,
    WarehouseLocationUtilizationRateItem
  },
  data () {
    return {
      checkedList:[],
      plainOptions:['Apple', 'Orange'],

      dateType:1,
      date:[],

      allData:[],
      wareDataMap:{},
      wareList:[],
      createTimeStart:'',
      createTimeEnd:''
    }
  },
  computed: {
    colorList:()=>colorList
  },
  methods: {
    onDateChange(value){
      console.log(value,'onDateChange')
      this.createTimeStart = moment(value[0]).format('YYYY-MM-DD').toString()+' 00:00:00'
      this.createTimeEnd = moment(value[1]).format('YYYY-MM-DD').toString()+' 23:59:59'

      this.getData()
    },
    handlePanelChange(value){
      this.date = value

      this.createTimeStart = moment(value[0]).format('YYYY-MM')
      this.createTimeEnd = moment(value[1]).format('YYYY-MM')

      this.getData()
    },
    async getData(){
      const options = {type:this.dateType,createTimeStart:this.createTimeStart,createTimeEnd:this.createTimeEnd}
      const data = await this.$store.dispatch('dashboard/getBinInSummary',options)

      const wareList = _.map(_.uniqBy(data,'wareCode'),'wareCode')
      this.wareList = wareList
      this.checkedList = wareList

      const wareDataMap = {}
      _.each(wareList,x=>{
        if(!wareDataMap[x]){
          wareDataMap[x] = {
            children:[]
          }
        }

        _.each(data,y=>{
          if(x === y.wareCode){
            const createTime = moment(y.createTime)
            const formatString = this.dateType === 1 ? 'YYYY-MM':'YYYY-MM-DD'
            const item = {
              label: createTime.format(formatString),
              total:y.totalBin,
              used:y.materialOccupyBin,
              percent:y.percent
            }
            wareDataMap[x].children.push(item)
          }
        })
      })

      this.wareDataMap = wareDataMap
    }
  },
  mounted(){
    this.getData()
  },
  watch:{
    dateType(){
      this.date = []
    }
  }
}
</script>

<style lang="less" scoped>
/deep/.ware-list{
  // width: 100%;
  // margin: 0 auto;
  display: flex;
  justify-content: center;
  margin-bottom: 12px;
}

/deep/.content-box{
  display: flex;
  flex-direction: column;
  position: relative;
}
/deep/.date-change{
  display: flex;
  justify-content: flex-start;
  align-items: center;
  .ant-radio-group{
    margin-right: 2px !important;
  }
}

/deep/.ant-radio-button-wrapper:first-child{
  border: none;
}
/deep/.ant-radio-button-wrapper{
  border: none;
}

/deep/.ant-radio-button-wrapper:not(:first-child)::before{
  display: none;
}

// /deep/.ant-col{
//   height: 100%;
// }

// /deep/.ant-row{
//   min-height: 300px;
//   width: 100%;
// }
</style>