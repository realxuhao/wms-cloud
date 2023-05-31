<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="编号">
              <a-input v-model="queryForm.code" placeholder="编号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="盘点cell">
              <a-input v-model="queryForm.cell" placeholder="盘点cell" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="盘点仓库">
              <a-input v-model="queryForm.wareCode" placeholder="盘点仓库" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="盘点存储区">
              <a-input v-model="queryForm.areaCode" placeholder="盘点存储区" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="盘点物料类型">
              <a-select
                allow-clear
                v-model="queryForm.takeMaterialType"
              >
                <a-select-option v-for="item in takeMaterialType" :key="item.value" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>

          <template v-if="advanced">
            <a-col :span="4">
              <a-form-model-item label="盘点类型">
                <a-select
                  allow-clear
                  v-model="queryForm.type"
                >
                  <a-select-option v-for="item in type" :key="item.value" :value="item.value">
                    {{ item.text }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-model-item label="盘点方式">
                <a-select
                  allow-clear
                  v-model="queryForm.method"
                >
                  <a-select-option v-for="item in method" :key="item.value" :value="item.value">
                    {{ item.text }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-model-item label="总下发量">
                <a-input v-model="queryForm.totalIssueQuantity" placeholder="总下发量" allow-clear/>
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
          </template>
          <a-col span="4">
            <span class="table-page-search-submitButtons" >
              <a-button v-hasPermi="['take:plan:query']" type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
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
        <a-button v-hasPermi="['take:plan:add']" type="primary" class="m-r-8" icon="plus" @click="handleAdd"> 新建 </a-button>
        <a-button v-hasPermi="['take:plan:export']" :loading="exportLoading" @click="handleDownload"><a-icon type="download" />导出结果</a-button>
      </div>
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="ssccNumber"
        :pagination="false"
        size="middle"
        :scroll="tableScroll"
      >
        <template slot="status" slot-scope="text">
          <div >
            <a-tag color="orange" v-if="text===0">
              已创建
            </a-tag>
            <a-tag color="#87d068" v-if="text===1">
              进行中
            </a-tag>
            <a-tag color="#666666" v-if="text===2">
              完成
            </a-tag>
          </div>
        </template>
        <template slot="type" slot-scope="text">
          <div >
            <a-tag color="orange" v-if="text===0">
              明盘
            </a-tag>
            <a-tag color="#87d068" v-if="text===1">
              盲盘
            </a-tag>
          </div>
        </template>
        <template slot="method" slot-scope="text">
          <div >
            <a-tag color="orange" v-if="text===0">
              普通盘点
            </a-tag>
            <a-tag color="#87d068" v-if="text===1">
              循环盘点
            </a-tag>

          </div>
        </template>
        <template slot="takeMaterialType" slot-scope="text">
          <div >
            <a-tag color="orange" v-if="text===0">
              原材料
            </a-tag>
            <a-tag color="#87d068" v-if="text===1">
              成品
            </a-tag>

          </div>
        </template>

        <template slot="action" slot-scope="text, record">
          <div class="action-con">

            <a-divider type="vertical" />
            <a-popconfirm title="确认要删除吗?" ok-text="确认" cancel-text="取消" @confirm="handleDelete(record)">
              <a v-hasPermi="['take:plan:delete']" class="danger-color" :disabled="record.status!==0"><a-icon class="m-r-4" type="delete" />删除</a>
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
    </div>

    <UpdateDrawer
      v-model="visible"
      :updateType="updateType"
      :id="currentUpdateId"
      @on-ok="loadTableList"
    ></UpdateDrawer>
  </div>
</template>

<script>
import UpdateDrawer from './UpdateDrawer'
import { mixinTableList } from '@/utils/mixin/index'
import { download } from '@/utils/file'

const columns = [

  {
    title: '盘点计划编码',
    key: 'code',
    dataIndex: 'code',
    width: 150
  },
  {
    title: '盘点cell',
    key: 'cell',
    dataIndex: 'cell',
    width: 120
  },
  {
    title: '盘点仓库',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 120
  },
  {
    title: '盘点区域',
    key: 'areaCode',
    dataIndex: 'areaCode',
    width: 120
  },
  {
    title: '盘点类型',
    key: 'type',
    dataIndex: 'type',
    width: 100,
    scopedSlots: { customRender: 'type' }
  },
  {
    title: '盘点方式',
    key: 'method',
    dataIndex: 'method',
    width: 100,
    scopedSlots: { customRender: 'method' }
  },
  {
    title: '创建时物料总数',
    key: 'createdMaterialQuantity',
    dataIndex: 'createdMaterialQuantity',
    width: 150
  },
  {
    title: '总下发物料总数',
    key: 'totalIssueQuantity',
    dataIndex: 'totalIssueQuantity',
    width: 150
  },
  {
    title: '1st月下发时物料总数',
    key: 'firstIssueMaterialQuantity',
    dataIndex: 'firstIssueMaterialQuantity',
    width: 180
  },
  {
    title: '2nd月下发时物料总数',
    key: 'secondIssueMaterialQuantity',
    dataIndex: 'secondIssueMaterialQuantity',
    width: 180
  },
  {
    title: '3rd月下发时物料总数',
    key: 'thirdIssueMaterialQuantity',
    dataIndex: 'thirdIssueMaterialQuantity',
    width: 180
  },

  {
    title: '盘点库位总数',
    key: 'takeBinQuantity',
    dataIndex: 'takeBinQuantity',
    width: 120
  },
  {
    title: '差异库位数',
    key: 'diffBinQuantity',
    dataIndex: 'diffBinQuantity',
    width: 120
  },
  {
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    width: 100,
    scopedSlots: { customRender: 'status' }
  },
  {
    title: '物料类型',
    key: 'takeMaterialType',
    dataIndex: 'takeMaterialType',
    width: 120,
    scopedSlots: { customRender: 'takeMaterialType' }
  },
  {
    title: '循环盘点月份',
    key: 'circleTakeMonth',
    dataIndex: 'circleTakeMonth',
    width: 150
  },
  {
    title: '操作',
    key: 'action',
    width: 120,
    fixed: 'right',
    scopedSlots: { customRender: 'action' }
  }
]

const queryFormAttr = () => {
  return {
    code: '',
    cell: '',
    wareCode: '',
    areaCode: '',
    takeMaterialType: '',
    type: '',
    method: '',
    totalIssueQuantity: '',
    status: ''
  }
}
const status = [
  {
    text: '已创建',
    value: 0
  },
  {
    text: '进行中',
    value: 1
  },
  {
    text: '完成',
    value: 2
  }
]
const type = [
  {
    text: '明盘',
    value: 0
  },
  {
    text: '盲盘',
    value: 1
  }
]
const method = [
  {
    text: '普通盘点',
    value: 0
  },
  {
    text: '循环盘点',
    value: 1
  }
]

const takeMaterialType = [
  {
    text: '原材料',
    value: 0
  },
  {
    text: '成品',
    value: 1
  }
]
export default {
  name: 'StockTakePlan',
  mixins: [mixinTableList],
  components: {
    UpdateDrawer
  },
  data () {
    return {
      tableLoading: false,
      uploadLoading: false,
      columns,
      list: [],
      exportLoading:false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      }
    }
  },
  computed: {
    status: () => status,
    type: () => type,
    method: () => method,
    takeMaterialType: () => takeMaterialType
  },

  methods: {
    async handleDownload () {
      try {
        this.exportLoading = true
        const blobData = await this.$store.dispatch('stockTake/exportList', this.queryForm)
        console.log(blobData)
        download(blobData, '盘点计划')
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      } finally {
        this.exportLoading = false
      }
    },
    async handleDelete (row) {
      try {
        await this.$store.dispatch('stockTake/delete', row.id)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleAdd () {
      this.updateType = 'add'
      this.visible = true
      this.currentUpdateId = null
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
        } = await this.$store.dispatch('stockTake/getPaginationList', this.queryForm)
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
