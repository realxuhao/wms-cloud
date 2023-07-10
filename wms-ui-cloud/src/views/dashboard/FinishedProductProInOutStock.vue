<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form-model layout="inline">
        <a-form-model-item label="批次">
          <a-input v-model="queryForm.batchNb" placeholder="批次" allow-clear/>
        </a-form-model-item>
  
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
      
        <template slot="operationType" slot-scope="text">
         
          <a-tag > {{ operationTypeMap[text] }}</a-tag>
        </template>
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
    title: '操作类型',
    key: 'operationType',
    dataIndex: 'operationType',
    width: 80,
    scopedSlots: { customRender: 'operationType' },

  },
  {
    title: '数量',
    key: 'operationStock',
    dataIndex: 'operationStock',
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
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 120
  },

]

const operationTypeMap = {
  '-1':'期初',
  '0':'入库',
  '1':'销售出库',
  '2':'其它出库'
}

const queryFormAttr = () => {
  return {
    sourceWareCode:'',
    targetWareCode:'',
    carNb:'',
    ssccNb:'',
    date:[]
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
      operationTypeMap
    }
  },
  methods: {
    async handleExport(){
      try {
        this.downloadLoading = true

        const res  = await this.$store.dispatch('dashboard/exportOldMaterial')

        download(res,'成品进销存')
      } catch (error) {
        this.$message.error(error.message)
      }finally{
        this.downloadLoading = false
      }
    },
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
        } = await this.$store.dispatch('dashboard/proInOutStock', options)
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
