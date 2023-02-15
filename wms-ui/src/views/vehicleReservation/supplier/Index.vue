<template>
  <div class="wrapper">
    <div class="table-content">
      <a-tabs defaultActiveKey="1" @change="callback" v-show="!isVisibleSupplierName">
        <a-tab-pane key="1" tab="可预约订单列表">
          <PurchaseTable
            :supplierName="supplierName"
          />
        </a-tab-pane>
        <a-tab-pane key="2" tab="已预约信息">已预约信息</a-tab-pane>
        <a-tab-pane key="3" tab="司机预约信息">司机预约信息</a-tab-pane>
      </a-tabs>

      <a-modal
        v-drag-modal
        v-model="isVisibleSupplierName"
        title="提示"
        :width="500"
        :closable="false"
        :maskClosable="false"
        @cancel="close"
        @ok="handleSubmitSupplierName">
        <a-input
          placeholder="请输入供应商名称"
          v-model="supplierName"/>
      </a-modal>
    </div>
  </div>
</template>

<script>
import PurchaseTable from './PurchaseTable.vue'

export default {
  name: 'VrSupplier',
  components: {
    PurchaseTable
  },
  props: {},
  data () {
    return {
      activeKey: 1,
      isVisibleSupplierName: true,
      supplierName: ''
    }
  },
  model: {},
  computed: {},
  methods: {
    handleSubmitSupplierName () {
      this.isVisibleSupplierName = false
    },
    close () {
      this.$message.error('请输入供应商名称！')
      this.isVisibleSupplierName = true
    },
    callback (key) {
      console.info(key)
    }
  }
}
</script>

<style lang="less" scoped>
</style>
