<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form-model layout="inline">
        <a-form-model-item label="时间">
          <a-range-picker
            format="YYYY-MM-DD"
            v-model="queryForm.date"
          />
        </a-form-model-item>
        <a-form-model-item label="操作账号">
          <a-input v-model="queryForm.createBy" placeholder="操作账号" allow-clear/>
        </a-form-model-item>
        <a-form-model-item label="操作人">
          <a-input v-model="queryForm.nickName" placeholder="操作人" allow-clear/>
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
        rowKey="createBy"
        :pagination="false"
        size="middle"
       
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
    title: '操作账号',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 80
  },
{
    title: '操作人',
    key: 'nickName',
    dataIndex: 'nickName',
    width: 80
  },


  {
    title: '上架',
    key: 'binIn',
    dataIndex: 'binIn',
    width: 80
  },
  {
    title: '拣配下架',
    key: 'binOut',
    dataIndex: 'binOut',
    width: 80
  },
  {
    title: '其他下架',
    key: 'binOutOther',
    dataIndex: 'binOutOther',
    width: 80
  },
    {
    title: '转储',
    key: 'manualTrans',
    dataIndex: 'manualTrans',
    width: 80
  },

]

const queryFormAttr = () => {
  return {
    date:[],
    createBy:'',
    nickName: ''
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

        const res  = await this.$store.dispatch('dashboard/exportWorkloadPro')

        download(res,'员工实际工作量（成品）-标准单位托盘导出')
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
        } = await this.$store.dispatch('dashboard/getWorkloadPro', options)
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
