<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="物料编码">
              <a-input v-model="queryForm.materialNb" placeholder="物料编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="状态">
              <a-select
                allow-clear
                v-model="queryForm.status"
              >
                <a-select-option v-for="item in status" :key="item.value" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <template v-if="advanced">
            <a-col :span="9">
              <a-form-item label="创建时间" >
                <a-range-picker
                  format="YYYY-MM-DD HH:mm"
                  :show-time="{ format: 'HH:mm' }"
                  v-model="queryForm.date"
                />
              </a-form-item>
            </a-col>
            <a-col :span="9">
              <a-form-item label="更新时间" >
                <a-range-picker
                  format="YYYY-MM-DD HH:mm"
                  :show-time="{ format: 'HH:mm' }"
                  v-model="queryForm.updateDate"
                />
              </a-form-item>
            </a-col>
          </template>
          <a-col span="4">
            <span class="table-page-search-submitButtons" >
              <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
              <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo" />重置</a-button>
              <a @click="toggleAdvanced" style="margin-left: 8px">
                {{ advanced ? '收起' : '展开' }}
                <a-icon :type="advanced ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
      <div class="action-content">
        <a-button
          class="m-r-8"
          type="primary"
          :loading="submitLoading"
          :disabled="!hasSelected"
          @click="handleBatchIssueJob">批量下发任务</a-button>

        <a-button
          type="primary"
          :loading="confirmMaterialLoading"
          @click="handleAddTrans"
        >新增仓内转储单</a-button>
      </div>
      <a-table
        table-layout="fixed"
        :row-selection="{
          selectedRowKeys: selectedRowKeys, onChange: onSelectChange ,
          getCheckboxProps:record => ({
            props: {
              disabled: [-1,1,2,3].includes(record.status), // Column configuration not to be checked
              name: record.name,
            },
          }),}"
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="ssccNb"
        :pagination="false"
        :scroll="tableScroll"
        size="middle"
      >
        <template slot="status" slot-scope="text">
          <div >
            <a-tag :color="statusColorMap[text]">{{ statusMap[text] }}</a-tag>
          </div>
        </template>
        <template slot="type" slot-scope="text">
          <div >
            <a-tag :color="typeColorMap[text]">{{ typeMap[text] }}</a-tag>
          </div>
        </template>
        <template slot="moveType" slot-scope="text">
          <div >
            {{ moveTypeMap[text] }}
          </div>
        </template>
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a-popconfirm
              title="确认要取消该条任务吗?"
              ok-text="确认"
              cancel-text="取消"
              @confirm="handleCancel(record)"
            >
              <!-- <a class="danger-color m-r-4" :disabled="[-1,6,7].includes(record.status)">取消</a> -->
              <a class="danger-color" :disabled="[-1,1,2,3].includes(record.status)"><a-icon class="m-r-4" type="delete" />取消任务</a>
            </a-popconfirm>
          </div>
        </template>
        <!-- <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a class="warning-color" @click="handleEdit(record)"><a-icon class="m-r-4" type="edit" />编辑</a>
            <a-divider type="vertical" />
            <a-popconfirm title="确认要删除吗?" ok-text="确认" cancel-text="取消" @confirm="handleDelete(record)">
              <a class="danger-color"><a-icon class="m-r-4" type="delete" />删除</a>
            </a-popconfirm>
          </div>
        </template> -->
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

      <StockList
        v-model="stockListVisible"
        :orderNb="currentOrderNb"
        :cell="currentCell"
        :materialNb="currentMaterialNb"
        :notQuantity="notQuantity"
        @on-ok="loadTableList"
      ></StockList>

    </div>

  </div>
</template>

<script>
import _ from 'lodash'
import { mixinTableList } from '@/utils/mixin/index'
import StockList from './StockList'

import EditTableCell from '@/components/EditTableCell'

const columns = [
  {
    title: 'SSCC',
    key: 'ssccNb',
    dataIndex: 'ssccNb',
    width: 180
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
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    width: 120
  },
  {
    title: '源工厂',
    key: 'sourcePlantNb',
    dataIndex: 'sourcePlantNb',
    width: 120
  },
  {
    title: '源仓库',
    key: 'sourceWareCode',
    dataIndex: 'sourceWareCode',
    width: 120
  },
  {
    title: '源存储区',
    key: 'sourceAreaCode',
    dataIndex: 'sourceAreaCode',
    width: 120
  },
  {
    title: '源库位',
    key: 'sourceBinCode',
    dataIndex: 'sourceBinCode',
    width: 120
  },
  {
    title: '目的存储区',
    key: 'targetAreaCode',
    dataIndex: 'targetAreaCode',
    width: 120
  },
  {
    title: '目的库位',
    key: 'targetBinCode',
    dataIndex: 'targetBinCode',
    width: 120
  },
  {
    title: '移动类型',
    key: 'moveType',
    dataIndex: 'moveType',
    scopedSlots: { customRender: 'moveType' },
    width: 140
  },
  {
    title: '转储类型',
    key: 'type',
    dataIndex: 'type',
    scopedSlots: { customRender: 'type' },
    width: 140
  },
  {
    title: '创建人',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 120
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 200
  },
  {
    title: '修改人',
    key: 'updateBy',
    dataIndex: 'updateBy',
    width: 120
  },
  {
    title: '修改时间',
    key: 'updateTime',
    dataIndex: 'updateTime',
    width: 200
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    width: 200,
    scopedSlots: { customRender: 'action' }
  }
]

const status = [
  {
    text: '已取消',
    value: '-1'
  },
  {
    text: '待下发',
    value: 0
  },
  {
    text: '待下架',
    value: 1
  },
  {
    text: '待上架',
    value: 2
  },
  {
    text: '已完成',
    value: 3
  }
]

const statusMap = {
  '-1': '已取消',
  0: '待下发',
  1: '待下架',
  2: '待上架',
  3: '已完成'
}

const statusColorMap = {
  '-1': '#8f939c',
  0: '#f3a73f',
  1: '#f3a73f',
  2: '#f3a73f',
  3: '#87d068'
}

const moveTypeMap = {
  0: '收货',
  1: '入库',
  2: '上架',
  3: '生产叫料',
  6: '库内转储'
}

const typeMap = {
  0: '正常',
  1: '异常'
}

const typeColorMap = {
  0: '#18bc37',
  1: '#f3a73f'
}

const queryFormAttr = () => {
  return {
    cell: '',
    materialNb: '',
    status: '',
    createBy: '',
    date: [],
    updateDate: []
  }
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  components: {
    EditTableCell,
    StockList
  },
  data () {
    return {
      tableLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },

      submitLoading: false,
      confirmMaterialLoading: false,
      selectedRowKeys: [],
      columns,
      list: [],
      departmentList: [],

      currentOrderNb: '',
      currentCell: '',
      currentMaterialNb: '',
      stockListVisible: false,
      notQuantity: 0
    }
  },
  computed: {
    status: () => status,
    statusMap: () => statusMap,
    moveTypeMap: () => moveTypeMap,
    typeMap: () => typeMap,
    typeColorMap: () => typeColorMap,
    statusColorMap: () => statusColorMap,
    hasSelected () {
      return this.selectedRowKeys.length > 0
    }
  },
  methods: {
    async handleBatchIssueJob () {
      try {
        this.submitLoading = true
        const options = { ids: this.selectedRowKeys }
        await this.$store.dispatch('manualTrans/batchIssueJob', options)

        this.$message.success('下发成功')
        this.selectedRowKeys = []

        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.submitLoading = false
      }
    },
    async handleAddTrans () {
      this.stockListVisible = true
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    async handleQuantityChange (record, value) {
      try {
        await this.$store.dispatch('materialFeeding/updateQuantity', { id: record.id, quantity: value })
        this.$message.success('修改成功！')

        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const { date = [], updateDate } = this.queryForm
        const startCreateTime = date.length > 0 ? date[0].format(this.startDateFormat) : undefined
        const endCreateTime = date.length > 0 ? date[1].format(this.endDateFormat) : undefined
        const satrtUpdateTime = updateDate.length > 0 ? updateDate[0].format(this.startDateFormat) : undefined
        const endUpdateTime = updateDate.length > 0 ? updateDate[1].format(this.endDateFormat) : undefined

        const options = { ..._.omit(this.queryForm, ['date', 'updateDate']), startCreateTime, endCreateTime, satrtUpdateTime, endUpdateTime }

        const {
          data: { rows, total }
        } = await this.$store.dispatch('manualTrans/getPaginationList', options)
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
    },
    async handleCancel (record) {
      try {
        await this.$store.dispatch('manualTrans/cancelManualTrans', record)
        this.$message.success('取消成功')
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  },
  mounted () {
    this.loadData()
  }
}
</script>

<style lang="less" scoped>
</style>
