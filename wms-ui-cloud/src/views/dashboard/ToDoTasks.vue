<template>
  <Card title="待办任务">
    <template slot="header">
      <a-form-model layout="inline">
        <a-form-model-item>
          <a-radio-group buttonStyle="solid" size="small" v-model="type">
            <a-radio-button :value="1">
              仓库
            </a-radio-button>
            <a-radio-button :value="0">
              Cell
            </a-radio-button>
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item label="仓库编码" v-show="type===1">
          <a-select show-search v-model="queryForm.wareCode" style="width: 200px" placeholder="仓库编码">
            <a-select-option v-for="item in wareList" :key="item.id" :value="item.code">
              {{ item.code }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="Cell" v-show="type===0">
          <a-select show-search v-model="queryForm.cell" style="width: 200px" placeholder="Cell">
            <a-select-option v-for="item in cellList" :key="item" :value="item">
              {{ item }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item>
          <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
        </a-form-model-item>
      </a-form-model>
     
    </template>

    <template slot="main">
      <a-row :gutter="[24,24]">
        <a-col :span="24" >
          <div class="charts" id="to-do-tasks-material"></div>
        </a-col>
      </a-row>
    </template>
  </Card>
</template>

<script>
import * as echarts from 'echarts'
import Card from './Card.vue'
import _ from 'lodash'

import colorList from '@/utils/echartsColor'

export default {
  name: 'ToDoTasks',
  components: {
    Card,
  },
  data () {
    return {
      wareDataMap:{},
      wareList:[],
      cellList:['All','ECN','NMD','FSMP'],

      type:1,
      list:[],

      queryForm:{
        wareCode:'All',
        cell:''
      },
      searchLoading:false,
    }
  },
  computed: {
  },
  methods: {
    /** 获取仓库List */
    async getWareList () {
      try {
        const data = await this.$store.dispatch('ware/getOptionList')
        this.wareList = [{code:'All',id:-1},...data.data]
        // if(data.data.length>0){
        //   this.queryForm.wareCode = data.data[0].code
        // }

        this.getData()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async handleSearch(){
      this.searchLoading = true
      await this.getData()
      this.searchLoading = false
    },

    loadMaterialCharts(){
      var myChart = echarts.init(document.getElementById('to-do-tasks-material'))

      const xData = _.map(this.list,x=>this.type === 1 ?x.wareCode:x.cell)
      const toBeReceivedList = _.map(this.list,x=>x.toBeReceived||0)
      const toBeMovedList = _.map(this.list,x=>x.toBeMove||0)
      const toBeCallList = _.map(this.list,x=>x.toBeCall||0)
      const toBeBinList = _.map(this.list,x=>x.toBeBin||0)

      const option = {
        color:colorList,
        title: {
          // text: 'World Population'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          boundaryGap: [0, 0.01]
        },
        yAxis: {
          type: 'category',
          data: xData
        },
        series: [
          {
            name: '待入库',
            type: 'bar',
            silent: true,
            barWidth: 16,
            tooltip:{
              show:true
            },
            label: {
              show: true,
              formatter(item){
                return item.value ? item.value:''
              },
              
            },
            stack: 'Ad',
            data: toBeReceivedList
          },
          {
            name: '待上架',
            type: 'bar',
            silent: true,
            barWidth: 16,
            stack: 'Ad',
            tooltip:{
              show:true
            },
            label: {
              show: true,
              formatter(item){
                return item.value ? item.value:''
              }
            },
            data: toBeBinList
          },
          {
            name: '待叫料',
            type: 'bar',
            silent: true,
            barWidth: 16,
            stack: 'Ad',
            tooltip:{
              show:true
            },
            label: {
              show: true,
              formatter(item){
                return item.value ? item.value:''
              }
            },
            data: toBeCallList
          },
          {
            name: '待移库',
            type: 'bar',
            silent: true,
            barWidth: 16,
            stack: 'Ad',
            tooltip:{
              show:true
            },
            label: {
              show: true,
              formatter(item){
                return item.value ? item.value:''
              }
            },
            data: toBeMovedList
          },
        ]
      }

      myChart.setOption(option)
    },
    async getData(){
      const data = await this.$store.dispatch('dashboard/getMissionToDoSummary',this.queryForm)
      this.list = data
      this.loadMaterialCharts()
      // const wareList = _.map(_.uniqBy(data,'wareCode'),'wareCode')
      // this.wareList = wareList
      // this.checkedList = wareList

      // const wareDataMap = {}
      // _.each(wareList,x=>{
      //   if(!wareDataMap[x]){
      //     wareDataMap[x] = {
      //       children:[]
      //     }
      //   }

      //   _.each(data,y=>{
      //     if(x === y.wareCode){
      //       const createTime = moment(y.createTime)
      //       const formatString = this.dateType === 1 ? 'YYYY-MM':'YYYY-MM-DD'
      //       const item = {
      //         label: createTime.format(formatString),
      //         total:y.totalBin,
      //         used:y.materialOccupyBin,
      //         percent:y.percent
      //       }
      //       wareDataMap[x].children.push(item)
      //     }
      //   })
      // })

      // this.wareDataMap = wareDataMap
    }
  },
  mounted(){
    this.getWareList()
    // this.getData()
  },
  watch:{
    type(val){
      if(val===1){
        this.queryForm.cell = undefined
        this.queryForm.wareCode = 'All'
      }else{
        this.queryForm.wareCode = undefined
        this.queryForm.cell = 'All'
      }
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