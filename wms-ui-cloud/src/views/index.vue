<template>
  <!-- <ToDoTasks></ToDoTasks> -->
  <WarehouseLocationUtilizationRate></WarehouseLocationUtilizationRate>
</template>

<script>
// import ToDoTasks from './dashboard/ToDoTasks.vue'
import WarehouseLocationUtilizationRate from './dashboard/WarehouseLocationUtilizationRate.vue'

export default {
  name: 'Index',
  components: {
    // ToDoTasks
    WarehouseLocationUtilizationRate
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
