<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="purchaseId"
        :pagination="false"
        size="small"
      >
      </a-table>

    </div>

  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'

const columns = [
  {
    title: '订单PO号',
    key: 'poCode',
    dataIndex: 'poCode',
    width: 120
  },
  {
    title: '订单行号',
    key: 'poItem',
    dataIndex: 'poItem',
    width: 100
  },
  {
    title: '供应商名称',
    key: 'supplierName',
    dataIndex: 'supplierName',
    width: 180
  },
  {
    title: '料号',
    key: 'materialCode',
    dataIndex: 'materialCode',
    width: 150
  },
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 200
  },
  {
    title: '需求数量',
    key: 'surplusQuantity',
    dataIndex: 'surplusQuantity',
    width: 130
  },
  {
    title: '送货数量',
    key: 'arriveQuantity',
    dataIndex: 'arriveQuantity',
    width: 130
  },
  {
    title: '单位',
    key: 'unit',
    dataIndex: 'unit',
    width: 100
  }
]

export default {
  name: 'VrSupplierReserveDetails',
  mixins: [mixinTableList],
  components: {
  },
  props: {
    isVisible: {
      type: Boolean,
      default () {
        return false
      }
    },
    reserveNo: {
      type: String,
      default () {
        return ''
      }
    }
  },
  data () {
    return {
      columns,
      list: []
    }
  },
  methods: {
    /** 查询供应商预约详情列表 */
    async loadTableList () {
      if (this.reserveNo === '' || this.reserveNo === null) {
        this.list = []
        return
      }
      try {
        this.tableLoading = true

        const { data } = await this.$store.dispatch('supplierReserve/getDetailsList', this.reserveNo)
        this.list = data
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async loadData () {
      this.loadTableList()
    }
  },
  mounted () {
    this.loadData()
  },
  watch: {
    isVisible (val) {
      if (val) {
        this.loadData()
      }
    }
  }
}
</script>

<style lang="less" scoped>
.not-danger-color{
  color: #aaa
}
</style>
