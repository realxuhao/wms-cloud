<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="Shipping Mark">
              <a-input v-model="queryForm.shippingMark" placeholder="Shipping Mark" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="ETO PO">
              <a-input v-model="queryForm.etoPo" placeholder="ETO PO" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="移库日期">
              <a-range-picker
                format="YYYY-MM-DD"
                v-model="queryForm.date"
              />
            </a-form-item>
          </a-col>
          <template v-if="advanced">

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
        <template slot="status" slot-scope="text">
          <a-tag :color="statusColorMap[text]"> {{ status[text] }}</a-tag>
        </template>

        <template slot="rateOfProgress" slot-scope="text">
          <div class="progress-box">
            <a-progress :percent="text"/>
          </div>

        </template>

        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a-popconfirm v-if="record.status!=2" title="确认要强制完成任务吗?" ok-text="确认" cancel-text="取消" @confirm="onSubmit(record)">
              <a class="warning-color"><a-icon class="m-r-4" type="complete" />完成</a>
            </a-popconfirm>

            <a v-if="record.status==2" class="warning-color"><a-icon class="m-r-4" type="complete" />已完成</a>

            <a-divider type="vertical" />
            <a-popconfirm title="确认要删除吗?" ok-text="确认" cancel-text="取消" @confirm="handleDelete(record)">
              <a class="danger-color"><a-icon class="m-r-4" type="delete" />删除</a>
            </a-popconfirm>
          </div>
        </template>
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
      <a-modal v-model="submitVisible" :title="modalTitle" ok-text="确认" cancel-text="取消" @ok="onSubmit">
      </a-modal>
    </div>

  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'
import { colorMap } from '@/utils/color'
import _ from 'lodash'

const columns = [
  {
    title: 'Shipping Mark',
    key: 'shippingMark',
    dataIndex: 'shippingMark',
    width: 120
  },
  {
    title: 'ETO PO',
    key: 'etoPo',
    dataIndex: 'etoPo',
    width: 120
  },
  {
    title: 'ETO PLANT',
    key: 'etoPlant',
    dataIndex: 'etoPlant',
    width: 120
  },
  {
    title: '移库日期',
    key: 'stockMovementDate',
    dataIndex: 'stockMovementDate',
    width: 120
  },
  {
    title: 'Country',
    key: 'country',
    dataIndex: 'country',
    width: 80
  },
  {
    title: 'Prod-order',
    key: 'prodOrder',
    dataIndex: 'prodOrder',
    width: 100
  },
  {
    title: 'SAP Code',
    key: 'sapCode',
    dataIndex: 'sapCode',
    width: 100
  },
  {
    title: 'Pallet Quantity',
    key: 'palletQuantity',
    dataIndex: 'palletQuantity',
    width: 120
  },
  {
    title: 'after packing',
    key: 'afterPacking',
    dataIndex: 'afterPacking',
    width: 100
  },
  {
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    width: 80,
    scopedSlots: { customRender: 'status' }
  },
  {
    title: '进度',
    key: 'rateOfProgress',
    dataIndex: 'rateOfProgress',
    scopedSlots: { customRender: 'rateOfProgress' },
    width: 200
  },
  {
    title: '创建者',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 120
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 120
  },
  {
    title: '更新者',
    key: 'updateBy',
    dataIndex: 'updateBy',
    width: 120
  },
  {
    title: '更新时间',
    key: 'updateTime',
    dataIndex: 'updateTime',
    width: 120
  },
  {
    title: '操作',
    key: 'action',
    width: 160,
    scopedSlots: { customRender: 'action' }
  }
]
const queryFormAttr = () => {
  return {
    shippingMark: '',
    etoPo: '',
    date: []
  }
}
const status = {
  0: '未执行',
  1: '执行中',
  2: '已执行'
}

const statusColorMap = {
  0: colorMap['error'],
  1: colorMap['warning'],
  2: colorMap['success']
}
export default {
  name: 'Area',
  mixins: [mixinTableList],
  data () {
    return {
      submitVisible: false,
      modalTitle: '',
      tableLoading: false,
      uploadLoading: false,
      genTaskLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: []
    }
  },
  computed: {
    status: () => status,
    statusColorMap: () => statusColorMap
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
        const stockMovementDateStart = date.length > 0 ? date[0].format('YYYY-MM-DD 00:00:00') : undefined
        const stockMovementDateEnd = date.length > 0 ? date[1].format('YYYY-MM-DD 23:59:59') : undefined
        const options = { ..._.omit(this.queryForm, ['date']), stockMovementDateStart, stockMovementDateEnd }

        const {
          data: { rows, total }
        } = await this.$store.dispatch('finishedProduct/getDashboard', options)
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
    async handleDelete (record) {
      try {
        await this.$store.dispatch('finishedProduct/deleteTask', record.id)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
    },
    async handleEdit (record) {
      this.submitVisible = true
    },
    async onSubmit (record) {
      // 调用API
      try {
        await this.$store.dispatch('finishedProduct/completeTask', record.id)
        this.$message.success('成功！')
        this.submitVisible = false
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
.progress-box{
  padding-right: 12px ;
  box-sizing: border-box;
}
</style>
