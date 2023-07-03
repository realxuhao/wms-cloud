<template xmlns="http://www.w3.org/1999/html">
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="3">
            <a-form-model-item label="Cell">
              <a-select
                allow-clear
                v-model="queryForm.cell"
                @change="handleCellChange"
              >
                <a-select-option v-for="item in departmentList" :key="item.id" :value="item.code">
                  {{ item.code }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="生产需求号">
              <a-input v-model="queryForm.orderNumber" placeholder="生产需求号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="5">
            <a-form-model-item label="物料编码">
              <a-input v-model="queryForm.materialCode" placeholder="物料编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="5">
            <a-form-model-item label="物料类型">
              <a-select
                mode="multiple"
                style="width: 100%"
                placeholder="请先选择Cell"
                optionLabelProp="value"
                v-model="queryForm.materialTypeList"
              >
                <a-select-option class="white-pre-w" v-for="item in materialTypeList" :key="item.code" :value="item.code">
                  <span class="m-r-4">{{ item.code }}</span>{{ item.description }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="5">
            <a-form-model-item label="状态">
              <a-select
                allow-clear
                v-model="queryForm.status"
              >
                <a-select-option v-for="(item,key) in statusMap" :key="key" :value="key">
                  {{ item }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <template v-if="advanced">
            <a-col :span="4">
              <a-form-item label="SSCC">
                <a-input v-model="queryForm.ssccNumber" placeholder="SSCC" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="批次号">
                <a-input v-model="queryForm.batchNb" placeholder="batchNb" allow-clear/>
              </a-form-item>
            </a-col>
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
              <a-button v-hasPermi="['kanban:list:query']" type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>

              <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo" />重置</a-button>

              <a-button v-hasPermi="['kanban:list:export']" style="margin-left: 8px" :loading="exportLoading" @click="handleDownload"><a-icon type="download" />导出结果</a-button>

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
          v-hasPermi="['kanban:list:task']"
          class="m-r-8"
          type="primary"
          :loading="submitLoading"
          :disabled="!hasSelected"
          @click="handleBatchAddJob">批量下发任务</a-button>

        <a-button
          v-hasPermi="['kanban:list:confirm']"
          type="primary"
          :loading="confirmMaterialLoading"
          :disabled="!hasSelected"
          @click="handleConfirmMaterial"
        >产线收货确认</a-button>

      </div>
      <a-table
        table-layout="fixed"
        :row-selection="{
          selectedRowKeys: selectedRowKeys, onChange: onSelectChange ,
          getCheckboxProps:record => ({
            props: {
              disabled: [-1,1,6,7].includes(record.status), // Column configuration not to be checked
              name: record.name,
            },
          }),}"
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
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
            <a-popconfirm v-show="record.status===0" title="确认要删除吗?" ok-text="确认" cancel-text="取消" @confirm="handleDelete(record)">
              <a v-hasPermi="['kanban:list:delete']" class="danger-color"><a-icon class="m-r-4" type="delete" />删除</a>
            </a-popconfirm>
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
              <a v-hasPermi="['kanban:list:cancel']" class="danger-color" :disabled="[-1,6,7].includes(record.status)">取消</a>
            </a-popconfirm>
            <!-- <a-divider type="vertical" /> -->
            <!-- <a
              class="m-r-4"
              :disabled="(record.factoryCode!=='7752' || record.status>0)"
              @click="$refs.pikingOrderAddShiftTask.handleOpen(record)">新增移库任务</a> -->
          </div>
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

    <PikingOrderAddShiftTask ref="pikingOrderAddShiftTask" @on-ok="loadTableList"></PikingOrderAddShiftTask>
  </div>
</template>

<script>
import _ from 'lodash'
import { mixinTableList } from '@/utils/mixin/index'
import EditTableCell from '@/components/EditTableCell'
import PikingOrderAddShiftTask from './PikingOrderAddShiftTask'
import { download } from '@/utils/file'

const columns = [
  {
    title: 'cell',
    key: 'cell',
    dataIndex: 'cell',
    width: 70
  },
  {
    title: '生产需求号',
    key: 'orderNumber',
    dataIndex: 'orderNumber',
    width: 120
  },
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
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
    title: '需求量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 120
  },
  {
    title: '源工厂编码',
    key: 'factoryCode',
    dataIndex: 'factoryCode',
    width: 120
  },
  {
    title: '源仓库',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 120
  },
  {
    title: '源存储区',
    key: 'areaCode',
    dataIndex: 'areaCode',
    width: 120
  },
  {
    title: '源库位',
    key: 'binCode',
    dataIndex: 'binCode',
    width: 120
  },
  {
    title: '动作类型',
    key: 'type',
    dataIndex: 'type',
    scopedSlots: { customRender: 'type' },
    width: 140
  },
  {
    title: '移动类型',
    key: 'moveType',
    dataIndex: 'moveType',
    scopedSlots: { customRender: 'moveType' },
    width: 140
  },
  {
    title: '物料编码',
    key: 'materialCode',
    dataIndex: 'materialCode',
    width: 160
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
    width: 160
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
    width: 120,
    scopedSlots: { customRender: 'action' }
  }
]

const status = [
  {
    text: '已取消',
    value: '-1'
  },
  {
    text: '未下发',
    value: 0
  },
  {
    text: '已下发',
    value: 1
  }
]

const statusMap = {
  '-1': '取消任务',
  0: '待下发',
  1: '待下架',
  2: '外库待发运',
  3: '产线待收货',
  4: '主库待收货',
  5: '待上架',
  6: '产线已收货',
  7: '完成'
}

const statusColorMap = {
  '-1': '#8f939c',
  0: '#f3a73f',
  1: '#f3a73f',
  2: '#f3a73f',
  3: '#f3a73f',
  4: '#f3a73f',
  5: '#f3a73f',
  6: '#87d068',
  7: '#87d068'
}

const moveTypeMap = {
  0: '收货',
  1: '入库',
  2: '上架',
  3: '生产叫料'
}

const typeMap = {
  0: '整托下架',
  1: '拆托下架'
}

const typeColorMap = {
  0: '#18bc37',
  1: '#f3a73f'
}

const queryFormAttr = () => {
  return {
    cell: '',
    materialCode: '',
    orderNumber: '',
    status: undefined,
    createBy: '',
    date: [],
    updateDate: [],
    materialTypeList: [],
    ssccNumber: '',
    batchNb: '',
  }
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  components: {
    EditTableCell,
    PikingOrderAddShiftTask
  },
  data () {
    return {
      tableLoading: false,
      exportLoading: false,
      queryForm: {
        pageSize: 300,
        pageNum: 1,
        ...queryFormAttr()
      },

      submitLoading: false,
      confirmMaterialLoading: false,
      selectedRowKeys: [],
      columns,
      list: [],
      departmentList: [],
      materialTypeList: [],

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
    moveTypeMap: () => moveTypeMap,
    typeMap: () => typeMap,
    typeColorMap: () => typeColorMap,
    statusColorMap: () => statusColorMap,
    hasSelected () {
      return this.selectedRowKeys.length > 0
    }
  },
  methods: {
    async handleBatchAddJob () {
      try {
        this.submitLoading = true
        const options = { ids: this.selectedRowKeys }
        await this.$store.dispatch('materialFeeding/batchAddJob', options)

        this.$message.success('下发成功')
        this.selectedRowKeys = []

        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.submitLoading = false
      }
    },
    async handleConfirmMaterial () {
      // this.$confirm({
      //   title: '以下物料可用库存不足，是否进行部分拣配？',
      //   content: h => {
      //     return (
      //       _.map(this.selectedRowKeys, item => {
      //         return <p>物料号：{item.materialNb}, 可用库存量：{item.avaliableQuantity}</p>
      //       })
      //     )
      //   },
      //   onOk: () => this.submitCreateReductionTask(options),
      //   onCancel: () => {
      //     this.selectedRowKeys = []
      //   }
      // })
      try {
        this.confirmMaterialLoading = true
        await this.$store.dispatch('materialFeeding/confirmMaterial', this.selectedRowKeys)
        this.$message.success('收货确认成功')
        this.selectedRowKeys = []
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.confirmMaterialLoading = false
      }
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
        const createTimeStart = date.length > 0 ? date[0].format(this.startDateFormat) : undefined
        const createTimeEnd = date.length > 0 ? date[1].format(this.endDateFormat) : undefined
        const updateTimeStart = updateDate.length > 0 ? updateDate[0].format(this.startDateFormat) : undefined
        const updateTimeEnd = updateDate.length > 0 ? updateDate[1].format(this.endDateFormat) : undefined

        const options = { ..._.omit(this.queryForm, ['date', 'updateDate']), createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd }

        const {
          data: { rows, total }
        } = await this.$store.dispatch('materialFeeding/getPickingOrderList', options)
        this.list = rows
        this.paginationTotal = total
        this.selectedRowKeys = []
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

    async handleCellChange (value) {
      if (!value) {
        this.materialTypeList = []
        return
      }
      const materialTypeList = await this.$store.dispatch('materialType/getByCell', value)
      this.materialTypeList = materialTypeList.data
    },
    async loadData () {
      this.loadDepartmentList()
      this.loadTableList()
    },
    async handleCancel (record) {
      try {
        await this.$store.dispatch('materialFeeding/cancelPickingOrder', record)
        this.$message.success('取消成功')
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async handleDownload () {
      try {
        this.exportLoading = true
        this.queryForm.pageSize = 0
        const blobData = await this.$store.dispatch('materialFeeding/exportCallExcel', this.queryForm)
        console.log(blobData)
        download(blobData, '拣配记录')
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      } finally {
        this.exportLoading = false
      }
    }
  },
  mounted () {
    this.loadData()
  }
}
</script>

<style lang="less" scoped>

/deep/.ant-select-dropdown-menu-item{
  white-space: pre-wrap !important;
}
</style>
