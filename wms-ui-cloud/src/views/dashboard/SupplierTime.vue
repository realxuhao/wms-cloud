<template>
  <Card title="供应商准时率">
    <template slot="header">
     
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
  name: 'SupplierTime',
  components: {
    Card,
  },
  data () {
    return {

    }
  },
  computed: {
    colorList:()=>colorList
  },
  methods: {

    handleSearch(){

    },
    async getData(){
     
    }
  },
  mounted(){
    this.getData()
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