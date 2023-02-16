<template>
  <div class="wrapper">
    <div class="table-content">
      <a-tabs defaultActiveKey="1" @change="callback" v-show="!isVisibleSupplierName">
        <a-tab-pane key="1" tab="可预约订单列表">
          <PurchaseTable
            :isVisible="!isVisibleSupplierName"
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
        cancelText="关闭"
        @cancel="handleClose"
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
      supplierName: '供应商A'
    }
  },
  model: {},
  computed: {},
  methods: {
    handleClose () {
      this.isVisibleSupplierName = false
      this.supplierName = ''
    },
    handleSubmitSupplierName () {
      if (this.supplierName === '') {
        this.$message.error('请输入供应商名称！')
        return
      }
      this.isVisibleSupplierName = false
    },
    callback (key) {
      console.info(key)
    }
  }
}
</script>

<style lang="less" scoped>
</style>
