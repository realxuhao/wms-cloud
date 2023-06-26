<template>
  <Card title="待办任务">
    <template slot="header">
      <a-form-model layout="inline">
        <a-form-model-item label="仓库编码">
          <a-select show-search v-model="queryForm.wareCode" style="width: 200px" placeholder="仓库编码">
            <a-select-option v-for="item in wareList" :key="item.id" :value="item.code">
              {{ item.code }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="Cell">
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
        <a-col :span="12" >
         
        </a-col>
      </a-row>
    </template>
  </Card>
</template>

<script>
import Card from './Card.vue'
import _ from 'lodash'
import moment from 'moment'

import colorList from '@/utils/echartsColor'

export default {
  name: 'Index',
  components: {
    Card,
  },
  data () {
    return {
      wareDataMap:{},
      wareList:[],
      cellList:['ENC','NMD','FSMP'],

      queryForm:{
        wareCode:'',
        cell:'ECN'
      },
      searchLoading:false,
    }
  },
  computed: {
    colorList:()=>colorList
  },
  methods: {
    /** 获取仓库List */
    async getWareList () {
      try {
        const data = await this.$store.dispatch('ware/getOptionList')
        this.wareList = data.data
        if(data.data.length>0){
          this.queryForm.wareCode = data.data[0].code
        }

        this.getData()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleSearch(){

    },
    async getData(){
      const data = await this.$store.dispatch('dashboard/getMissionToDoSummary',this.queryForm)

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