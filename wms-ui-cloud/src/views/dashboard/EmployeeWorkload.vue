<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <div class="action-content">
        <a-form-model layout="inline">
          <a-form-model-item label="时间">
            <a-range-picker
              format="YYYY-MM-DD"
              v-model="queryForm.date"
            />
          </a-form-model-item>
          <a-form-model-item >
            <span class="table-page-search-submitButtons" >
              <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
              <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo" />重置</a-button>
            </span>
          </a-form-model-item>
        </a-form-model>
      </div>
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        size="middle"
        :scroll="tableScroll"
      >
    
      </a-table>

      <div class="pagination-con">
        <a-pagination
          show-size-changer
          :page-size-options="pageSizeOptions||[10,20,30,40,100,150]"
          show-less-items
          :current="queryForm.pageNum"
          :page-size.sync="queryForm.pageSize"
          :total="paginationTotal"
          @showSizeChange="loadTableList"
          @change="changePagination"
        />
      </div>
    </div>

  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'

const columns = [

  {
    title: '移库单号',
    key: 'orderNb',
    dataIndex: 'orderNb',
    width: 120
  },
  {
    title: '车牌号',
    key: 'carNb',
    dataIndex: 'carNb',
    width: 80
  },
  {
    title: 'sscc',
    key: 'ssccNb',
    dataIndex: 'ssccNb',
    width: 120
  },
  {
    title: '物料号',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 120
  },
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 120
  },
  {
    title: '批次号',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 120
  },
  {
    title: '移库数量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 80
  },
  {
    title: '拆托数量',
    key: 'splitQuality',
    dataIndex: 'splitQuality',
    width: 80
  },
  {
    title: '源工厂',
    key: 'sourcePlantNb',
    dataIndex: 'sourcePlantNb',
    width: 80
  },
  {
    title: '源仓库',
    key: 'sourceWareCode',
    dataIndex: 'sourceWareCode',
    width: 80
  },
  {
    title: '源存储区',
    key: 'sourceAreaCode',
    dataIndex: 'sourceAreaCode',
    width: 80
  },
  {
    title: '源库位',
    key: 'sourceBinCode',
    dataIndex: 'sourceBinCode',
    width: 80
  },
  {
    title: 'bbd过期时间',
    key: 'expireDate',
    dataIndex: 'expireDate',
    width: 120
  },
  {
    title: '目的工厂',
    key: 'targetPlant',
    dataIndex: 'targetPlant',
    width: 80
  },
  {
    title: '目的仓库',
    key: 'targetWareCode',
    dataIndex: 'targetWareCode',
    width: 80
  },
  {
    title: '目的存储区',
    key: 'targetAreaCode',
    dataIndex: 'targetAreaCode',
    width: 80
  },
  {
    title: '推荐库位',
    key: 'recommendBinCode',
    dataIndex: 'recommendBinCode',
    width: 80
  },
  {
    title: '目的库位',
    key: 'targetBinCode',
    dataIndex: 'targetBinCode',
    width: 100
  },
  {
    title: '所属需求订单号',
    key: 'orderNumber',
    dataIndex: 'orderNumber',
    width: 140
  }
]

const queryFormAttr = () => {
  return {
    date:[]
  }
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  data () {
    return {
      tableLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: [],
    }
  },
  methods: {
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const { date = [] } = this.queryForm
        const createTimeStart = date.length > 0 ? date[0].format('YYYY-MM-DD 00:00:00') : undefined
        const createTimeEnd = date.length > 0 ? date[1].format('YYYY-MM-DD 23:59:59') : undefined
        const options = { ..._.omit(this.queryForm, ['date']), createTimeStart, createTimeEnd }

        const {
           rows, total
        } = await this.$store.dispatch('dashboard/getWareShiftList', options)
        this.list = rows
        this.paginationTotal = total
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
  }
}
</script>

<style lang="less" scoped>
</style>
