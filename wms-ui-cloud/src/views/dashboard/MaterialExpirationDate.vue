<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <div class="action-content">
        <a-button type="primary" class="m-r-8" @click="loadTableList" >
          <a-icon type="redo" />刷新
        </a-button>
        <a-button type="primary" :loading="downloadLoading" @click="handleExport" >
          <a-icon type="download" />导出
        </a-button>
      </div>
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        size="middle"
        :scroll="tableScroll"
        @change="pageChange"
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
import { download } from '@/utils/file'

const columns = [

  {
    title: '工厂',
    key: 'plantNb',
    dataIndex: 'plantNb',
    width: 80
  },
  {
    title: '仓库编码',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 80
  },
  {
    title: '区域编码',
    key: 'areaCode',
    dataIndex: 'areaCode',
    width: 80
  },
  {
    title: '跨编码',
    key: 'frameCode',
    dataIndex: 'frameCode',
    width: 80
  },
  {
    title: '库位编码',
    key: 'binCode',
    dataIndex: 'binCode',
    width: 80
  },
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 120
  },
  {
    title: '物料号',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 120
  },
  {
    title: '批次号',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 120
  },
  {
    title: '过期时间',
    key: 'expireDate',
    dataIndex: 'expireDate',
    width: 120,
    sorter: true
  },
  {
    title: '总库存',
    key: 'totalStock',
    dataIndex: 'totalStock',
    width: 80
  },
  {
    title: '冻结库存',
    key: 'freezeStock',
    dataIndex: 'freezeStock',
    width: 80
  },
  {
    title: '可用库存',
    key: 'availableStock',
    dataIndex: 'availableStock',
    width: 80
  },
  {
    title: 'PO号',
    key: 'fromPurchaseOrder',
    dataIndex: 'fromPurchaseOrder',
    width: 120
  },
  {
    title: '质检状态',
    key: 'qualityStatus',
    dataIndex: 'qualityStatus',
    width: 80
  }
]

const queryFormAttr = () => {
  return {
  
  }
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  data () {
    return {
      tableLoading: false,
      downloadLoading: false,
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
    pageChange(page, filters, sorter){
        this.queryForm.isAsc= sorter.order === 'ascend' ? 'asc' : 'desc'
        this.queryForm.orderByColumn= sorter.columnKey
        this.loadTableList()
      // try {
      //   this.queryForm.isAsc= sorter.order === 'ascend' ? 'asc' : 'desc',
      //     this.queryForm.orderByColumn= sorter.columnKey,
      //     this.tableLoading = true
      //   const {
      //     data: { rows, total }
      //   } = await this.$store.dispatch('dashboard/getExpiredMaterial', {...this.queryForm})
      //
      //   this.list = rows
      //   this.paginationTotal = total
      // } catch (error) {
      //   this.$message.error(error.message)
      // } finally {
      //   this.tableLoading = false
      // }
    },
    async handleExport(){
      try {
        this.downloadLoading = true

        const res  = await this.$store.dispatch('dashboard/exportExpiredMaterial')
        download(res,'30天到期物料.xlsx')
      } catch (error) {
        this.$message.error(error.message)
      }finally{
        this.downloadLoading = false
      }
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const {
          rows, total
        } = await this.$store.dispatch('dashboard/getExpiredMaterial', this.queryForm)
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
