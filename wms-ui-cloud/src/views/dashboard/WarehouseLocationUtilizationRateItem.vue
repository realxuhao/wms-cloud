<template>
  <div class="charts" :id="id"></div>
</template>

<script>
import * as echarts from 'echarts'
import _ from 'lodash'
export default {
  props:{
    id:{
      type:String,
      default:'warehouse-location-utilization-rate-item'
    },
    list:{
      type:Array,
      default:()=>[]
    },
    title:{
      type:String,
      default:''
    },
    color:{
      type:String,
      default:'#26C0C0'
    }
  },
  methods:{
    initCharts(){
      var myChart = echarts.init(document.getElementById(this.id))

      const xData = _.map(this.list,x=>x.label)

      const percentList = _.map(this.list,x=>x.percent)
      const usedList = _.map(this.list,x=>x.used)
      const totalList = _.map(this.list,x=>x.total)

      // const 
        // 指定图表的配置项和数据
       const option = {
        title:{
          text:this.title,
          left:'center'
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
            name: '个',
            nameTextStyle: {
              // color: '#8CB5E2'
            },
            splitLine: {
              show: false,
            },
        
            axisLabel: {
              // color: '#8CB5E2'
            }
          },
          {
        type: 'value',
        name: '百分比',
        min: 0,
        max: 25,
        interval: 5,
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
        name: '百分比',
        type: 'line',
        yAxisIndex: 1,
        itemStyle: {
          color:'#481d88'
        },
        tooltip: {
          valueFormatter: function (value) {
            return value + ' %'
          }
        },
        data: percentList
      },
          {
            name: 'value',
            type: 'bar',
            itemStyle: {
              opacity: 0.7,
              color:this.color
            },
            z: 16,
            silent: true,
            barWidth: 16,
            barGap: '-100%',
            tooltip:{
              show:true
            },
            // 真实数据
            data: usedList,
          },
            {
            name: 'value',
            type: 'bar',
            itemStyle: {
            color: '#ccc',
              opacity: 0.5
            },
            z: 3,
            silent: true,
            barWidth: 16,
            barGap: '-100%',
            // 真实数据
            data: totalList,
          },
          {
            name: 'value',
            type: 'pictorialBar',
            symbolSize: [16, 3],
            symbolOffset: [0, 0],
            z: 3,
            symbolPosition: 'end',
            itemStyle: {
              color: 'rgba(48,228,255,1)',
              opacity: 1
            },
            // 设置最大数据，可以不设置data，默认为真实数据的最大值
            data: usedList,
          }
        ]
      }


      myChart.setOption(option)
    }
  },
  mounted(){
  },
  watch:{
    list:{
      handler(){
        this.$nextTick(()=>{
          this.initCharts()
        })
      },
      immediate:true
    }
  }
}
</script>