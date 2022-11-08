<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form-model class="search-content" layout="inline" :model="queryForm">
        <a-form-model-item label="批次号">
          <a-input v-model="queryForm.batchNb" placeholder="批次号" />
        </a-form-model-item>
        <a-form-model-item label="物料编码">
          <a-input v-model="queryForm.materialNb" placeholder="物料编码" />
        </a-form-model-item>
        <a-form-model-item>
          <a-button type="primary" icon="search" @click="handleSearch" :loading="searchLoading"> 搜索 </a-button>
        </a-form-model-item>
      </a-form-model>

      <div class="action-content">

      </div>
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        size="middle"
        :scroll="{ x: 1300 }"
      >
      </a-table>

      <div class="pagination-con">
        <a-pagination
          show-size-changer
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
    title: '仓库编码',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 120
  },
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 200
  },
  {
    title: '物料编码',
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
    title: '托盘编码',
    key: 'palletCode',
    dataIndex: 'palletCode',
    width: 120
  },
  {
    title: '托盘类型',
    key: 'palletType',
    dataIndex: 'palletType',
    width: 120
  },
  {
    title: '数量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 80
  },
  {
    title: '推荐库位编码',
    key: 'recommendBinCode',
    dataIndex: 'recommendBinCode',
    width: 120
  },
  {
    title: '实际库位编码',
    key: 'actualBinCode',
    dataIndex: 'actualBinCode',
    width: 120
  }
]

export default {
  name: 'BinIn',
  mixins: [mixinTableList],
  data () {
    return {
      tableLoading: false,
      uploadLoading: false,
      columns,
      list: []
    }
  },
  methods: {
    async loadTableList () {
      try {
        this.tableLoading = true

        const {
          data: { rows, total }
        } = await this.$store.dispatch('binIn/getPaginationList', this.queryForm)
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
