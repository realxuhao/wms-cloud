<template>
  <Card title="流程效率">
    <template slot="header">
      <a-form-model layout="inline">
        <a-form-model-item label="时间">
          <a-range-picker
            format="YYYY-MM-DD"
            v-model="queryForm.date"
          />
        </a-form-model-item>
  
        <a-form-model-item>
          <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
        </a-form-model-item>
      </a-form-model>
     
    </template>

    <template slot="main">
      <a-row :gutter="[24,24]">
        <a-col :span="24" >
          <div class="charts" style="height:480px" id="process-efficiency-fsmp"></div>
        </a-col>
        <a-col :span="24" >
          <div class="charts" style="height:480px" id="process-efficiency-ecn"></div>
        </a-col>
        <a-col :span="24" >
          <div class="charts" style="height:480px" id="process-efficiency-nmd"></div>
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
        date:[]
      },
      searchLoading:false,
    }
  },
  computed: {
  },
  methods: {
    async handleSearch(){
      this.searchLoading = true
      await this.getData()
      this.searchLoading = false
    },

    loadCharts(list,id){
      var myChart = echarts.init(document.getElementById(id))

      const xData = _.map(list,x=>x.date)
      const callMaterilList = _.map(list,x=>x.jiaoliao||0)
      const materialInList = _.map(list,x=>x.ruku||0)
      const sampleList = _.map(list,x=>x.quyang||0)

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
          type: 'category',
          boundaryGap: [0, 0.01],
          data: xData

        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '入库',
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
            data: materialInList
          },
          {
            name: '取样',
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
              }
            },
            data: sampleList
          },
          {
            name: '叫料',
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
              }
            },
            data: callMaterilList
          },
   
        ]
      }

      myChart.setOption(option)
    },
    async getData(){
      const data = await this.$store.dispatch('dashboard/getMissionToDoSummary',this.queryForm)
      this.list = data
      this.loadMaterialCharts()
    }
  },
  mounted(){
    const cellList = _.map(_.uniqBy(data,'cell'),'cell')
    const cellDataMap = {
      'FSMP':{children:[]},
      'NMD':{children:[]},
      'ECN':{children:[]}
    }
      _.each(cellList,x=>{
        _.each(data,y=>{
          if(x === y.cell){
            // const createTime = moment(y.createTime)
            // const formatString = this.dateType === 1 ? 'YYYY-MM':'YYYY-MM-DD'
            cellDataMap[x].children.push(item)
          }
        })
      })

    this.loadCharts(cellDataMap['FSMP'].children,'process-efficiency-fsmp')
    this.loadCharts(cellDataMap['NMD'].children,'process-efficiency-nmd')
    this.loadCharts(cellDataMap['ECN'].children,'process-efficiency-ecn')

  },
  watch:{

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