<template>
  <Card title="供应商准时率">
    <template slot="header">     
    </template>
    <template slot="main">      
      <a-form layout="inline" class="search-content" style="margin-left: 20px;">
        <a-row :gutter="16">
          <a-col :span="10">
            <a-form-model-item label="供应商名称">              
              <a-select placeholder="请选择供应商名称" v-model="searchName" showSearch>
                <a-select-option v-for="(name, index) in supplierNameList" :key="index" :value="name" >{{ name }}</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="5">
            <a-form-model-item label="年份">
              <a-input v-model="searchYear" placeholder="年份" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col span="4">
            <span class="table-page-search-submitButtons" >
              <a-button type="primary" @click="getData" :loading="searchLoading"><a-icon type="search" />查询</a-button>
              <a-button style="margin-left: 8px" type="primary" @click="downloadExcel" :loading="exportLoading"><a-icon type="download" />导出签到数据</a-button>
              
            </span>
          </a-col>
        </a-row>
      </a-form>
      <div style="margin-top: 50px;height: 500px;" class="charts" :id="id"></div>
    </template>
  </Card>
</template>

<script>
import Card from './Card.vue'
import * as echarts from 'echarts'
import { download } from '@/utils/file'
import _ from 'lodash'
import moment from 'moment'
import { DatePicker } from 'antd'

import colorList from '@/utils/echartsColor'

export default {
  name: 'SupplierTime',
  components: {
    Card,
  },
  data () {
    return {
      id: 'supplier-time',
      listOfData: [],
      searchName: null,
      searchYear: 0,
      searchLoading: false,
      exportLoading: false,
      supplierNameList: []
    }
  },
  computed: {
    colorList:()=>colorList
  },
  methods: {
    /** 初始化Option */
    async preOptionList () {
      const supplierNameData = await this.$store.dispatch('purchase/getSupplierName')
      this.supplierNameList = supplierNameData.data
      if(this.supplierNameList.length > 0){
        this.searchName = this.supplierNameList[0]
        this.getData()
      }else{        
        this.initCharts([0,0,0,0,0,0,0,0,0,0,0,0], [0,0,0,0,0,0,0,0,0,0,0,0])
      }
    },
    async getData(){
      if(this.searchYear == null || this.searchYear == ''){
        this.$message.error('请输入查询年份!')
        return
      }
      this.listOfData = []
      this.searchLoading = true
      try {
        const { data } = await this.$store.dispatch('driverDeliver/getOnceSupplierOnTime', {supplierName: this.searchName, searchYear: this.searchYear})
        this.listOfData = data
        let xData = []
        let yData = []
        for(let i = 0; i < this.listOfData.length; i++){
          xData = [...xData, this.listOfData[i].monthOn + '月']
          yData = [...yData, Number(this.listOfData[i].onTimeRatio)]          
        }
        this.initCharts(xData, yData)
      } catch (error) {
        this.$message.error(error.message)
      }
      this.searchLoading = false
    },
    initCharts(xData, yData){
      var myChart = echarts.init(document.getElementById(this.id))

        // 指定图表的配置项和数据
       const option = {
        title:{
          text:this.title,
          left:'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          top: 30,
          right: 20,
          bottom: 20,
          left:20,
          containLabel: true
        },
        xAxis: [
          {
            data: xData,
            axisTick: {
              show: false
            },
            axisLine: {
              show: false
            },
            axisLabel: {
              show: true,
              fontSize: 10,
              // color: '#ccc'
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '准时率',
            min: 0,
            max: 100,
            interval: 20,
            splitLine: {
              show: false,
            },
            axisLabel: {
              formatter: '{value} %'
            }
          }
        ],
        series: [
          {
            name: '供应商准时率',
            type: 'line',
            itemStyle: {
              color:'#481d88'
            },
            tooltip: {
              valueFormatter: function (value) {
                return value + ' %'
              }
            },
            itemStyle : { normal: {label : {show: true}}},  
            data: yData
          }
        ]
      }


      myChart.setOption(option)
    },
    async downloadExcel(){
      try {
        this.exportLoading = true
        const blobData = await this.$store.dispatch('driverDeliver/exportOnTime', {supplierName: '', searchYear: this.searchYear})
        download(blobData, '供应商准时率列表')
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      } finally {
        this.exportLoading = false
      }

    }
  
  },
  mounted(){
    this.preOptionList()
    this.searchYear = new Date().getFullYear()
  },

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