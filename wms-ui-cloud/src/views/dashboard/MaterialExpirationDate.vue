<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <div class="action-content">
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
    title: '工厂编码',
    key: 'plantNb',
    dataIndex: 'plantNb',
    width: 120
  },
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
    width: 200
  },
  {
    title: '批次号',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 200
  },
  {
    title: '检验方式',
    key: 'checkType',
    dataIndex: 'checkType',
    scopedSlots: { customRender: 'checkType' },
    width: 200
  },
  {
    title: '应检查数量',
    key: 'checkQuantity',
    dataIndex: 'checkQuantity',
    width: 200
  },
  {
    title: '实际数量',
    key: 'actualQuantity',
    dataIndex: 'actualQuantity',
    width: 200
  },
  {
    title: '结果',
    key: 'actualResult',
    dataIndex: 'actualResult',
    width: 200
  },
  {
    title: '实际平均结果',
    key: 'averageResult',
    dataIndex: 'averageResult',
    width: 200
  },
  {
    title: '最小标准',
    key: 'minStandard',
    dataIndex: 'minStandard',
    width: 200
  },
  {
    title: '最大标准',
    key: 'maxStandard',
    dataIndex: 'maxStandard',
    width: 200
  },
  {
    title: '原托数',
    key: 'originalPalletQuantity',
    dataIndex: 'originalPalletQuantity',
    width: 200
  },
  {
    title: '操作人',
    key: 'operateUser',
    dataIndex: 'operateUser',
    width: 200
  },
  {
    title: '操作时间',
    key: 'operateTime',
    dataIndex: 'operateTime',
    width: 200
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
    async handleExport(){
      try {
        this.downloadLoading = true

        const res  = await this.$store.dispatch('dashboard/exportOldMaterial')
        download(res.data,'在库时间最长物料.xlsx')
      } catch (error) {
        
      }finally{
        this.downloadLoading = false
      }
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const {
          data: { rows, total }
        } = await this.$store.dispatch('dashboard/getOldMaterialSummary', this.queryForm)
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
