<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <div class="action-content">
        <a-form-model layout="inline">

          <a-form-model-item label="源仓库">
            <a-input v-model="queryForm.sourceWareCode" placeholder="源库位" allow-clear/>
          </a-form-model-item>

          <a-form-model-item label="目标仓库">
            <a-input v-model="queryForm.targetWareCode" placeholder="目标库位" allow-clear/>
          </a-form-model-item>
          <a-form-model-item label="车牌号">
            <a-input v-model="queryForm.carNb" placeholder="车牌号" allow-clear/>
          </a-form-model-item>
          <a-form-model-item label="移库单号">
            <a-input v-model="queryForm.orderNb" placeholder="orderNb" allow-clear/>
          </a-form-model-item>
          <a-form-model-item label="时间">
            <a-range-picker
              format="YYYY-MM-DD"
              v-model="queryForm.date"
            />
          </a-form-model-item>
          <a-form-model-item>
            <span class="table-page-search-submitButtons">
              <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon
                type="search"/>查询</a-button>
              <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo"/>重置</a-button>

              <a-button style="margin-left: 8px" type="primary" :loading="downloadLoading" @click="handleExport">
                导出
              </a-button>
            </span>
          </a-form-model-item>


        </a-form-model>

      </div>
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"

        :pagination="false"
        size="middle"
        :scroll="tableScroll"
      >

      </a-table>

      <div class="pagination-con">
      <a class="pagination-total">总车次数：{{paginationTotal}}</a>

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
import {mixinTableList} from '@/utils/mixin/index'
import moment from 'moment'
import _ from 'lodash'
import {download} from '@/utils/file'

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
    title: '运货托数',
    key: 'number',
    dataIndex: 'number',
    width: 80
  },
  {
    title: '源仓库',
    key: 'sourceWareCode',
    dataIndex: 'sourceWareCode',
    width: 80
  },

  {
    title: '目的仓库',
    key: 'targetWareCode',
    dataIndex: 'targetWareCode',
    width: 80
  },
  {
    title: '装车时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 80
  },
]

const queryFormAttr = () => {
  return {
    sourceWareCode: '',
    targetWareCode: '',
    carNb: '',
    orderNb: '',
    date: [],

  }
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  data() {
    return {
      tableLoading: false,
      downloadLoading: false,
      paginationTotal:'',//总条数
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
    async handleExport() {
      try {
        this.downloadLoading = true
        this.queryForm.pageSize = 0
        const {date = []} = this.queryForm
        const createTimeStart = date.length > 0 ? date[0].format('YYYY-MM-DD 00:00:00') : undefined
        const createTimeEnd = date.length > 0 ? date[1].format('YYYY-MM-DD 23:59:59') : undefined
        const options = {..._.omit(this.queryForm, ['date']), createTimeStart, createTimeEnd}

        const res = await this.$store.dispatch('dashboard/wareShiftExport', options)
        console.log(res)
        download(res, '移库列表')
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.downloadLoading = false
      }
    },
    handleResetQuery() {
      this.queryForm = {...this.queryForm, ...queryFormAttr()}
      this.handleSearch()
    },
    async loadTableList() {
      try {
        this.tableLoading = true
        const {date = []} = this.queryForm
        const createTimeStart = date.length > 0 ? date[0].format('YYYY-MM-DD 00:00:00') : undefined
        const createTimeEnd = date.length > 0 ? date[1].format('YYYY-MM-DD 23:59:59') : undefined
        const options = {..._.omit(this.queryForm, ['date']), createTimeStart, createTimeEnd}

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
    async loadData() {
      this.loadTableList()
    }
  },
  mounted() {
    const endDate = moment()
    const startDate = moment().subtract(1, 'months')
    this.queryForm.date = [startDate, endDate]
    this.loadData()
  }
}
</script>

<style lang="less" scoped>

.pagination-con{
  justify-content: space-between;
  align-items: center;
  .pagination-total{
    font-weight: bold;
    font-size: 16px;
    margin-left: 8px;
  }
}
</style>
