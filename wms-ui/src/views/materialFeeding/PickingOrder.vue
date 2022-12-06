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
          style="margin-right:8px"
          type="primary"
          :loading="submitLoading"
          :disabled="!hasSelected"
          @click="handleBatchAddJob">批量下发</a-button>
      </div>
      <a-table
        :row-selection="{
          selectedRowKeys: selectedRowKeys, onChange: onSelectChange ,
          getCheckboxProps:record => ({
            props: {
              disabled: record.status === 0, // Column configuration not to be checked
              name: record.name,
            },
          }),}"
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        :scroll="{ x: 1300 }"
        size="middle"
      >
        <template slot="status" slot-scope="text">
          <div >
            {{ statusMap[text] }}
          </div>
        </template>
        <template slot="type" slot-scope="text">
          <div >
            {{ typeMap[text] }}
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
              <a class="danger-color m-r-4" :disabled="record.status===-1">取消</a>
            </a-popconfirm>
            <a-divider type="vertical" />
            <a
              class="m-r-4"
              :disabled="record.factoryCode!=='7752'"
              @click="$refs.pikingOrderAddShiftTask.handleOpen(record)">新增移库任务</a>
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

    </div>

    <PikingOrderAddShiftTask ref="pikingOrderAddShiftTask"></PikingOrderAddShiftTask>
  </div>
</template>

<script>
// import _ from 'lodash'
import { mixinTableList } from '@/utils/mixin/index'
import EditTableCell from '@/components/EditTableCell'
import PikingOrderAddShiftTask from './PikingOrderAddShiftTask'

const columns = [
  {
    title: 'cell',
    key: 'cell',
    dataIndex: 'cell',
    width: 120
  },
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
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
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    width: 120
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
    text: '未下发',
    value: 0
  },
  {
    text: '已下发',
    value: 1
  }
]

const statusMap = {
  '-1': '已取消',
  1: '已下发',
  0: '未下发'
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

const queryFormAttr = () => {
  return {
    cell: '',
    materialNb: '',
    status: '',
    createBy: ''
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
    moveTypeMap: () => moveTypeMap,
    typeMap: () => typeMap,
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

        this.$message.success('提交成功')
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.submitLoading = false
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

        const {
          data: { rows, total }
        } = await this.$store.dispatch('materialFeeding/getPickingOrderList', this.queryForm)
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
    async handleCancel (record) {
      try {
        await this.$store.dispatch('materialFeeding/cancelPickingOrder', record)
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
