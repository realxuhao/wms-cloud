<template>
  <div class="wrapper">
    <div class="table-content">
      <a-tabs defaultActiveKey="1" @change="callback" v-show="!isVisibleSupplierName">
        <a-tab-pane key="1" tab="可预约订单列表">
          <PurchaseTable
            :isVisible="!isVisibleSupplierName"
            :reloadPurchase="reloadPurchase"
            :supplierName="supplierName"
          />
        </a-tab-pane>
        <a-tab-pane key="2" tab="已预约信息">
          <ReserveTable
            :isVisible="!isVisibleSupplierName"
            :reloadReserve="reloadReserve"
            :supplierName="supplierName"
          />
        </a-tab-pane>
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
import ReserveTable from './ReserveTable.vue'

export default {
  name: 'VrSupplier',
  components: {
    PurchaseTable,
    ReserveTable
  },
  props: {},
  data () {
    return {
      /** 选中标签页 */
      activeKey: 1,
      /** 输入供应商名称弹窗是否显示 */
      isVisibleSupplierName: true,
      /** 输入的供应商名称，后续需要做精确查询 */
      supplierName: '供应商A',
      /** 重新加载可预约订单列表参数 */
      reloadPurchase: false,
      /** 重新加载已预约参数 */
      reloadReserve: false
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
      if (key === '1') {
        this.reloadPurchase = !this.reloadPurchase
      }
      if (key === '2') {
        this.reloadReserve = !this.reloadReserve
      }
    }
  }
}
</script>

<style lang="less" scoped>
</style>
