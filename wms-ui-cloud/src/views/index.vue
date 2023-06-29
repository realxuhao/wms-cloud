<template>
  <ToDoTasks></ToDoTasks>
  <!-- <Material></Material> -->
  <!-- <WarehouseLocationUtilizationRate></WarehouseLocationUtilizationRate> -->
  <!-- <WareShift></WareShift> -->
  <!-- <SPDN></SPDN> -->
  <!-- <SPDNStockList></SPDNStockList> -->
  <!-- <SUQAIQCManagement></SUQAIQCManagement> -->
  <!-- <SPDNPickList></SPDNPickList> -->

</template>

<script>
import ToDoTasks from './dashboard/ToDoTasks.vue'
import Material from './dashboard/Material.vue'
import WareShift from './dashboard/WareShift.vue'
import SPDN from './finishedProduct/SPDN.vue'
import SPDNStockList from './finishedProduct/SPDNStockList.vue'
import SPDNPickList from './finishedProduct/SPDNPickList.vue'




import WarehouseLocationUtilizationRate from './dashboard/WarehouseLocationUtilizationRate.vue'
import SUQAIQCManagement from './finishedProduct/SUQAIQCManagement.vue'


export default {
  name: 'Index',
  components: {
    ToDoTasks,
    WarehouseLocationUtilizationRate,
    Material,
    WareShift,
    SPDN,
    SPDNStockList,
    SUQAIQCManagement,
    SPDNPickList
  },
  data () {
    return {
    }
  },
  computed: {

  },
  created () {
    // this.getUser()
  },
  mounted () {
  },
  methods: {
  }
}
</script>

<style lang="less" scoped>
.wrapper{
  // height: calc(100vh - 200px);
  // background: #fff;
  // padding: 16px;
  // border-radius: 4px;
}



</style>
