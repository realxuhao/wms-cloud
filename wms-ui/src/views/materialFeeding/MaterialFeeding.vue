<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="Cell">
              <a-select
                allow-clear
                v-model="queryForm.cell"
              >
                <a-select-option v-for="item in departmentList" :key="item.id" :value="item.code">
                  {{ item.code }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="生产订单号">
              <a-input v-model="queryForm.orderNb" placeholder="生产订单号" allow-clear/>
            </a-form-model-item>
          </a-col>
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
            <a-col :span="4">
              <a-form-item label="创建人">
                <a-input v-model="queryForm.createBy" placeholder="创建人" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="创建时间" >
                <a-range-picker
                  format="YYYY-MM-DD HH:mm"
                  :show-time="{ format: 'HH:mm' }"
                  v-model="queryForm.date"
                />
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="修改时间" >
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
          type="primary"
          icon="upload"
          style="margin-right:8px"
          @click="handleOpenUpload">
          创建物料需求
        </a-button>
        <a-button
          style="margin-right:8px"
          type="primary"
          :loading="submitLoading"
          :disabled="!hasSelected"
          @click="handleCheckCreateReductionTask">系统创建拣配任务</a-button>
        <a-button
          type="primary"
          :loading="exportLoading"
          icon="download"
          @click="handleDownload">
          导出
        </a-button>
      </div>
      <a-table
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        :scroll="{ x: 1300 }"
        size="middle"
      >
        <template slot="status" slot-scope="text">
          <a-tag :color="statusColorMap[text]">{{ statusMap[text] }}</a-tag>
        </template>
        <template slot="quantity" slot-scope="text,record">
          <EditTableCell v-if="record.status === 0" :text="text" @change="(val)=>handleQuantityChange(record,val)" />
          <span v-else>{{ text }}</span>
        </template>
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a
              class="primary-color"
              :disabled="[2].includes(record.status)"
              @click="handleCreateReductionTask(record)"><a-icon class="m-r-4" type="add" />人工创建拣配任务</a>
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

      <MaterialFeedingUpload
        v-model="visible"
        @on-ok="loadTableList"
      ></MaterialFeedingUpload>

      <CreateReductionTask
        v-model="createReductionTaskVisible"
        :orderNb="currentOrderNb"
        :cell="currentCell"
        :materialNb="currentMaterialNb"
        :notQuantity="notQuantity"
        @on-ok="loadTableList"
      ></CreateReductionTask>
    </div>

  </div>
</template>

<script>
import _ from 'lodash'
import { mixinTableList } from '@/utils/mixin/index'
import MaterialFeedingUpload from './MaterialFeedingUpload'
import CreateReductionTask from './CreateReductionTask'
import EditTableCell from '@/components/EditTableCell'
import { download } from '@/utils/file'
import { colorMap } from '@/utils/color'

const columns = [
  {
    title: 'cell',
    key: 'cell',
    dataIndex: 'cell',
    width: 120
  },
  {
    title: '生产订单号',
    key: 'orderNb',
    dataIndex: 'orderNb',
    width: 120
  },
  {
    title: '物料编码',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 160
  },
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 120
  },
  {
    title: '需求量',
    key: 'quantity',
    dataIndex: 'quantity',
    scopedSlots: { customRender: 'quantity' },
    width: 120
  },
  {
    title: '已下发量',
    key: 'issuedQuantity',
    dataIndex: 'issuedQuantity',
    width: 120
  },
  {
    title: '单位',
    key: 'unit',
    dataIndex: 'unit',
    width: 140
  },

  {
    title: '需求状态',
    key: 'status',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    width: 120
  },
  {
    title: '备注',
    key: 'remark',
    dataIndex: 'remark',
    width: 80
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
    width: 140,
    scopedSlots: { customRender: 'action' }
  }
]

const statusColorMap = {
  '-1': colorMap['cancel'],
  0: colorMap['error'],
  1: colorMap['warning'],
  2: colorMap['success']
}

const status = [
  {
    text: '未下发',
    value: 0
  },
  {
    text: '部分下发',
    value: 1
  },
  {
    text: '已全部下发',
    value: 2
  }
]

const statusMap = {
  0: '未下发',
  1: '部分下发',
  2: '已全部下发'
}

const queryFormAttr = () => {
  return {
    cell: '',
    materialNb: '',
    orderNb: '',
    status: '',
    date: [],
    updateDate: [],
    createBy: ''
  }
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  components: {
    MaterialFeedingUpload,
    CreateReductionTask,
    EditTableCell
  },
  data () {
    return {
      tableLoading: false,
      exportLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },

      submitLoading: false,
      selectedRowKeys: [],
      columns,
      list: [],
      departmentList: [],

      currentOrderNb: '',
      currentCell: '',
      currentMaterialNb: '',
      createReductionTaskVisible: false,
      notQuantity: 0
    }
  },
  computed: {
    status: () => status,
    statusMap: () => statusMap,
    statusColorMap: () => statusColorMap,
    hasSelected () {
      return this.selectedRowKeys.length > 0
    }
  },
  methods: {
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    async handleDownload () {
      try {
        this.exportLoading = true
        const blobData = await this.$store.dispatch('materialFeeding/exportExcel', this.queryForm)
        console.log(blobData)
        download(blobData, '叫料记录')
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      } finally {
        this.exportLoading = false
      }
    },
    handleOpenUpload () {
      this.visible = true
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
    async submitCreateReductionTask (options) {
      await this.$store.dispatch('materialFeeding/callSystemStock', options)
    },
    async handleCheckCreateReductionTask () {
      try {
        this.submitLoading = true
        const options = { callIds: this.selectedRowKeys }
        const { data: checkResult } = await this.$store.dispatch('materialFeeding/checkStock', options)
        if (!checkResult.checkFlag) {
          this.$confirm({
            title: '以下物料可用库存不足，是否进行部分拣配？',
            content: h => {
              return (
                _.map(checkResult.notEnoughStockList, item => {
                  return <p>物料号：{item.materialNb}, 可用库存量：{item.avaliableQuantity}</p>
                })
              )
            },
            onOk: () => this.submitCreateReductionTask(options),
            onCancel: () => {
              this.selectedRowKeys = []
            }
          })
          return
        }
        await this.submitCreateReductionTask()

        this.selectedRowKeys = []
        this.loadTableList()

        this.$message.success('提交成功')
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.submitLoading = false
      }
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const { date = [], updateDate = [] } = this.queryForm
        const startCreateTime = date.length > 0 ? date[0].format(this.startDateFormat) : undefined
        const endCreateTimeEnd = date.length > 0 ? date[1].format(this.endDateFormat) : undefined
        const startUpdateTime = updateDate.length > 0 ? updateDate[0].format(this.startDateFormat) : undefined
        const endUpdateTime = updateDate.length > 0 ? updateDate[1].format(this.endDateFormat) : undefined

        const options = { ..._.omit(this.queryForm, ['date', 'updateDate']), startCreateTime, endCreateTimeEnd, startUpdateTime, endUpdateTime }

        const {
          data: { rows, total }
        } = await this.$store.dispatch('materialFeeding/getPaginationList', options)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async loadDepartmentList () {
      const departmentList = await this.$store.dispatch('materialFeeding/getDepartmentList')
      this.departmentList = departmentList
    },
    async loadData () {
      this.loadDepartmentList()
      this.loadTableList()
    },
    handleCreateReductionTask (record) {
      this.currentMaterialNb = record.materialNb
      this.currentOrderNb = record.orderNb
      this.currentCell = record.cell
      const notQuantity = Number(record.quantity) - Number(record.issuedQuantity)
      this.notQuantity = notQuantity

      this.createReductionTaskVisible = true
    }
  },
  mounted () {
    this.loadData()
  }
}
</script>

<style lang="less" scoped>
</style>
