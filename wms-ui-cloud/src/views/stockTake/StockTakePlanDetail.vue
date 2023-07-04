<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="3">
            <a-form-model-item label="计划编码">
              <a-input v-model="queryForm.code" placeholder="计划编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="3">
            <a-form-model-item label="ssccNb">
              <a-input v-model="queryForm.ssccNb" placeholder="ssccNb" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="3">
            <a-form-model-item label="批次号">
              <a-input v-model="queryForm.batchNb" placeholder="批次号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="盘点方式">
              <a-select
                v-model="queryForm.method"
              >
                <a-select-option v-for="item in method" :key="item.value" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="3">
            <a-form-model-item label="盘点cell">
              <a-input v-model="queryForm.cell" placeholder="盘点cell" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="3">
            <a-form-item label="盘点仓库">
              <a-input v-model="queryForm.wareCode" placeholder="盘点仓库" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="3">
            <a-form-model-item label="盘点存储区">
              <a-input v-model="queryForm.areaCode" placeholder="盘点存储区" allow-clear/>
            </a-form-model-item>
          </a-col>
          <template v-if="advanced">
            <a-col :span="3">
              <a-form-model-item label="物料编码">
                <a-input v-model="queryForm.materialCode" placeholder="物料编码" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="3">
              <a-form-model-item label="循环盘点月份">
                <a-input v-model="queryForm.circleTakeMonth" placeholder="循环盘点月份" allow-clear/>
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
            <span class="table-page-search-submitButtons">
              <a-button v-hasPermi="['take:detail:query']" type="primary" @click="handleSearch" :loading="searchLoading"><a-icon
                type="search"/>查询</a-button>
              <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo"/>重置</a-button>
              <a @click="toggleAdvanced" style="margin-left: 8px">
                {{ advanced ? '收起' : '展开' }}
                <a-icon :type="advanced ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
      <div class="action-content">
        <a-button v-hasPermi="['take:detail:distribute']" type="primary" class="m-r-8" @click="issue" v-if="queryForm.method===0"> 下发</a-button>
        <!-- <a-button v-hasPermi="['take:detail:distribute']" type="primary" class="m-r-8" @click="issue" > 下发</a-button> -->
        <a-button v-hasPermi="['take:detail:confirm']" type="primary" class="m-r-8" @click="confirm"> 确认</a-button>
        <a-button v-hasPermi="['take:detail:export']" :loading="exportLoading" @click="handleDownload"><a-icon type="download" />导出结果</a-button>
      </div>
      <a-table
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange ,
          getCheckboxProps:record => ({
            props: {
              disabled: ![0,2].includes(record.status)
            },
          })}"
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        size="middle"
        :scroll="tableScroll"
      >

        <template slot="status" slot-scope="text">
          <div>
            <a-tag color="orange" v-if="text===0">
              待下发
            </a-tag>
            <a-tag color="#87d068" v-else-if="text===1">
              待盘点
            </a-tag>
            <a-tag color="#87d068" v-else-if="text===2">
              待确认
            </a-tag>
            <a-tag color="#666666" v-else>
              完成
            </a-tag>
          </div>
        </template>
        <template slot="type" slot-scope="text">
          <div>
            <a-tag color="orange" v-if="text===0">
              明盘
            </a-tag>
            <a-tag color="#87d068" v-if="text===1">
              盲盘
            </a-tag>
          </div>
        </template>
        <template slot="method" slot-scope="text">
          <div>
            <a-tag color="orange" v-if="text===0">
              普通盘点
            </a-tag>
            <a-tag color="#87d068" v-if="text===1">
              循环盘点
            </a-tag>

          </div>
        </template>
        <template slot="isDiff" slot-scope="text">
          <div>
            <!-- <a-tag color="orange" v-if="text===0">
              普通盘点
            </a-tag>
            <a-tag color="#87d068" v-if="text===1">
              循环盘点
            </a-tag> -->

            {{ isDiffMap[text] }}

          </div>
        </template>
        <template slot="takeMaterialType" slot-scope="text">
          <div>
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
            <a v-hasPermi="['take:detail:edit']" :disabled="record.status !==2" class="warning-color" @click="edit(record)">
              <a-icon class="m-r-4" type="edit"/>
              修改盘点数量</a>

            <span v-show="record.method ===1&&record.status ===0">
              <a-divider type="vertical" />
              <a
                v-hasPermi="['take:detail:distribute']"
                class="warning-color"
                @click="issue({
                  planCode:record.planCode,
                  issueType:1,
                  circleTakeMonth:record.circleTakeMonth
                })">
                下发</a>
            </span>

          </div>
        </template>
      </a-table>
      <template>
        <div>
          <a-modal
            title="请输入数量"
            :visible="editvisible"
            :confirm-loading="confirmLoading"
            @ok="handleOk"
            @cancel="handleCancel"
          >
            <a-input style="width: 80px" v-model="pdaTakeQuantity" placeholder="盘点数量"></a-input>
          </a-modal>
        </div>
      </template>
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
    title: '计划编码',
    key: 'planCode',
    dataIndex: 'planCode',
    width: 80
  },
    {
    title: '任务编码',
    key: 'taskNo',
    dataIndex: 'taskNo',
    width: 160
  },
  {
    title: '物料编码',
    key: 'materialCode',
    dataIndex: 'materialCode',
    width: 80
  },
  {
    title: 'sscc',
    key: 'ssccNb',
    dataIndex: 'ssccNb',
    width: 80
  },
  {
    title: '批次号',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 80
  },
  {
    title: 'cell',
    key: 'cell',
    dataIndex: 'cell',
    width: 80
  },
  {
    title: '物料类型',
    key: 'takeMaterialType',
    dataIndex: 'takeMaterialType',
    width: 80,
    scopedSlots: { customRender: 'takeMaterialType' }
  },
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 140
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
    title: '盘点数量',
    key: 'takeQuantity',
    dataIndex: 'takeQuantity',
    width: 120
  },
  {
    title: '是否一致',
    key: 'isDiff',
    dataIndex: 'isDiff',
     width: 80,
    scopedSlots: { customRender: 'isDiff' }
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
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    width: 80,
    scopedSlots: { customRender: 'status' }
  },
  {
    title: '循环盘点月份',
    key: 'circleTakeMonth',
    dataIndex: 'circleTakeMonth',
    width: 80
  },
  {
    title: '操作',
    key: 'action',
    width: 200,
    fixed: 'right',
    scopedSlots: { customRender: 'action' }
  }
]

const queryFormAttr = () => {
  return {
    plantNb: '',
    wareCode: '',
    materialNb: '',
    batchNb: '',
    status: '',
    planCode: '',
    materialCode: '',
    method: 0,
    type: '',
    areaCode: '',
    ssccNb: '',
    circleTakeMonth: undefined
  }
}

const status = [
  {
    text: '待下发',
    value: 0
  },
  {
    text: '待盘点',
    value: 1
  },
  {
    text: '待确认',
    value: 2
  },
  {
    text: '完成',
    value: 3
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
const isDiff = [
  {
    text: '是',
    value: 0
  },
  {
    text: '否',
    value: 1
  }
]

const isDiffMap = {
  0:'是',
  1:'否'
}
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
  name: 'StockTakePlanDetail',
  mixins: [mixinTableList],
  data () {
    return {
      editvisible: false,
      tableLoading: false,
      uploadLoading: false,
      confirmLoading: false,
      editId: undefined,
      pdaTakeQuantity: undefined,
      columns,
      list: [],
      selectedRowKeys: [], // Check here to configure the default column
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      exportLoading:false
    }
  },
  computed: {
    status: () => status,
    type: () => type,
    method: () => method,
    isDiff: ()=> isDiff,
    isDiffMap:()=>isDiffMap,
    takeMaterialType: () => takeMaterialType,
    hasSelected () {
      return this.selectedRowKeys.length > 0
    }
  },

  methods: {
    async handleDownload () {
      try {
        this.exportLoading = true
        const blobData = await this.$store.dispatch('stockTake/exportList', this.queryForm)
        console.log(blobData)
        download(blobData, '盘点明细')
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      } finally {
        this.exportLoading = false
      }
    },
    async handleOk (e) {
      try {
        this.confirmLoading = true
        const options = {
          detailId: this.editId,
          pdaTakeQuantity: this.pdaTakeQuantity
        }
        await this.$store.dispatch('stockTakeDetail/editTakeQuantity', options)
        this.editvisible = false
        this.loadTableList()
        this.$message.success('提交成功')
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.editvisible = false
        this.confirmLoading = false
      }
    },
    handleCancel (e) {
      console.log('Clicked cancel button')
      this.editvisible = false
    },
    async edit (record) {
      try {
        this.editvisible = true
        this.editId = record.id
        console.log(record)
      } catch (error) {
        this.$message.error(error.message)
      }
    },

    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    async issue (form) {
      try {
        this.submitLoading = true
        const options = { ids: this.selectedRowKeys, ...this.queryForm,...form }

        await this.$store.dispatch('stockTakeDetail/issue', options)

        this.selectedRowKeys = []
        this.loadTableList()

        this.$message.success('提交成功')
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.submitLoading = false
      }
    },
    async confirm () {
      try {
        this.submitLoading = true
        const options = {
          ids: this.selectedRowKeys

        }
        await this.$store.dispatch('stockTakeDetail/confirm', options)

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

        const {
          data: { rows, total }
        } = await this.$store.dispatch('stockTakeDetail/getList', this.queryForm)
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
