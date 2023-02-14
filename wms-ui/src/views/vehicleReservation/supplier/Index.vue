<template>
  <div class="wrapper">
    <div class="table-content">
      <a-tabs defaultActiveKey="1" @change="callback">
        <a-tab-pane key="1" tab="可预约订单列表">
          <PurchaseTable />
        </a-tab-pane>
        <a-tab-pane key="2" tab="已预约信息">已预约信息</a-tab-pane>
        <a-tab-pane key="3" tab="司机预约信息">司机预约信息</a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>

<script>
import PurchaseTable from './PurchaseTable.vue'

export default {
  name: 'vr-supplier',
  components: {
    PurchaseTable
  },
  props: {},
  data() {
    return {
      activeKey: 1,
    }
  },
  model: {},
  computed: {},
  methods: {
    callback(key){
      console.info(key)
    }
  },
}
</script>

<style lang="less" scoped>
</style>
