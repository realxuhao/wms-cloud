<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        :rowKey="(record,index)=>{return index}"
        :pagination="false"
        size="small"
        :scroll="tableScroll"
      >
        <template slot="statusDes" slot-scope="text, record">
          <div >
            <a-tag color="blue" v-if="record.status===0">
              {{ text }}
            </a-tag>
            <a-tag color="orange" v-if="record.status===1">
              {{ text }}
            </a-tag>
            <a-tag color="cyan" v-if="record.status===2">
              {{ text }}
            </a-tag>
            <a-tag color="green" v-if="record.status===3">
              {{ text }}
            </a-tag>
          </div>
        </template>
        <template slot="reserveDate" slot-scope="text, record">
          {{ record.timeWindow ? record.reserveDate + " " + record.timeWindow : '' }}
        </template>
      </a-table>

    </div>

  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'

const columns = [
  {
    title: '供应商名称',
    key: 'supplierCode',
    dataIndex: 'supplierCode',
    width: 150
  },
  {
    title: '预约单号',
    key: 'reserveNo',
    dataIndex: 'reserveNo',
    width: 100
  },
  {
    title: '预约时间',
    key: 'reserveDate',
    dataIndex: 'reserveDate',
    scopedSlots: { customRender: 'reserveDate' },
    width: 130
  },
  {
    title: '状态',
    key: 'statusDes',
    dataIndex: 'statusDes',
    scopedSlots: { customRender: 'statusDes' },
    width: 110
  },
  {
    title: '送货数量',
    key: 'arriveQuantity',
    dataIndex: 'arriveQuantity',
    width: 90
  },
  {
    title: '剩余送货数量',
    key: 'surplusQuantity',
    dataIndex: 'surplusQuantity',
    width: 90
  },
  {
    title: '签到时间',
    key: 'signinDate',
    dataIndex: 'signinDate',
    width: 120
  }
]

export default {
  name: 'VrReserveDetails',
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
    purchaseId: {
      type: Number,
      default () {
        return 0
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
      if (this.purchaseId === 0 || this.purchaseId === null) {
        this.list = []
        return
      }
      try {
        this.tableLoading = true

        const { data } = await this.$store.dispatch('purchase/detail', this.purchaseId)
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
