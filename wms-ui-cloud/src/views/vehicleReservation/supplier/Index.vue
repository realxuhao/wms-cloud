<template>
  <div class="wrapper">
    <div class="table-content">
      <a-row :gutter="24">        
        <a-col span="4" offset="20">
          <a-button style="float: right;" :type="errorBtbType" @click="handleSearchErrorNo">异常预约单查询</a-button>
        </a-col>
      </a-row>    
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
        <a-tab-pane key="3" tab="司机预约信息">
          <DriverDeliverTable
            :isVisible="!isVisibleSupplierName"
            :reloadDeliver="reloadDeliver"
            :supplierName="supplierName"
          />
        </a-tab-pane>
      </a-tabs>

      <a-modal v-model="errorListVisible" title="异常预约单列表">
        <a-list :grid="{ gutter: 16, column: 4 }" :data-source="errorList">
          <a-list-item slot="renderItem" slot-scope="item">
            {{ item }}
          </a-list-item>
        </a-list>
        <template slot="footer">
          <a-button key="back" @click="handleErrorCancel">
            关闭
          </a-button>
        </template>
      </a-modal>
      <!-- <a-modal
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
      </a-modal> -->
    </div>
  </div>
</template>

<script>
import PurchaseTable from './PurchaseTable.vue'
import ReserveTable from './ReserveTable.vue'
import DriverDeliverTable from './DriverDeliverTable.vue'

export default {
  name: 'VrSupplier',
  components: {
    PurchaseTable,
    ReserveTable,
    DriverDeliverTable
  },
  props: {},
  data () {
    return {
      /** 选中标签页 */
      activeKey: 1,
      /** 输入供应商名称弹窗是否显示 */
      isVisibleSupplierName: false,
      /** 输入的供应商名称，后续需要做精确查询 */
      supplierName: '',
      /** 重新加载可预约订单列表参数 */
      reloadPurchase: false,
      /** 重新加载已预约参数 */
      reloadReserve: false,
      /** 重新加载司机预约信息参数 */
      reloadDeliver: false,      
      errorList: [],
      errorListVisible: false,
      errorBtbType: 'dashed'

    }
  },
  model: {},
  computed: {},
  mounted () {
    this.supplierName = this.$store.getters.nickname.replace(/(^\s*)|(\s*$)/g,'')
    this.getErrorList()
  },
  methods: {
    handleClose () {
      this.isVisibleSupplierName = false
    },
    handleSubmitSupplierName () {
      if (this.supplierName === '') {
        this.$message.error('请输入供应商名称！')
        return
      }
      this.isVisibleSupplierName = false
    },
    async handleSearchErrorNo(){
      await this.getErrorList()
      this.errorListVisible = true
    },
    handleErrorCancel(){
      this.errorListVisible = false
    },
    async getErrorList(){
      const { data } = await this.$store.dispatch('supplierReserve/getErrorData', {supplierCode: this.supplierName})
      this.errorList = data == undefined ? [] : data
      if(this.errorList.length > 0){
        this.errorBtbType = 'danger'
      }else{
        this.errorBtbType = 'dashed'
      }
    },
    callback (key) {
      if (key === '1') {
        this.reloadPurchase = !this.reloadPurchase
      }
      if (key === '2') {
        this.reloadReserve = !this.reloadReserve
      }
      if (key === '3') {
        this.reloadDeliver = !this.reloadDeliver
      }
    }
  }
}
</script>

<style lang="less" scoped>
</style>
