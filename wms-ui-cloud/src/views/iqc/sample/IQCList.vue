<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <div class="table-content">
        <a-form layout="inline" class="search-content">
          <a-row :gutter="16">
            <a-col :span="4">
              <a-form-model-item label="仓库">
                <a-input v-model="queryForm.wareCode" placeholder="仓库" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-model-item label="SSCC码">
                <a-input v-model="queryForm.ssccNb" placeholder="sscc码" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-model-item label="cell部门">
                <a-select
                  placeholder="请选择cell部门"
                  allow-clear
                  v-model="queryForm.cell"
                >
                  <a-select-option v-for="item in cellList" :key="item.id" :value="item.name">
                    {{ item.name }}
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
                  placeholder="请选择状态"
                  mode="multiple"
                  allow-clear
                  v-model="queryForm.statusList"
                >
                  <a-select-option v-for="(item, key) in statusTextMap" :key="item" :value="key">
                    {{ item }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <template v-if="advanced">
              <a-col :span="4">
                <a-form-model-item label="下架人">
                  <a-input v-model="queryForm.binDownUser" placeholder="下架人" allow-clear/>
                </a-form-model-item>
              </a-col>
              <a-col :span="4">
                <a-form-model-item label="质检人">
                  <a-input v-model="queryForm.sampleUser" placeholder="质检人" allow-clear/>
                </a-form-model-item>
              </a-col>
              <a-col :span="4">
                <a-form-model-item label="下架时间">
                  <a-range-picker
                    format="YYYY-MM-DD HH:mm"
                    :show-time="{ format: 'HH:mm' }"
                    v-model="queryForm.binDownTime"
                  />
                </a-form-model-item>
              </a-col>
              <a-col :span="4">
                <a-form-model-item label="质检时间">
                  <a-range-picker
                    format="YYYY-MM-DD HH:mm"
                    :show-time="{ format: 'HH:mm' }"
                    v-model="queryForm.sampleTime"
                  />
                </a-form-model-item>
              </a-col>
            </template>
            <a-col span="4">
              <span class="table-page-search-submitButtons" >
                <a-button v-hasPermi="['iqc:sample:query']" type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
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
            :loading="exportLoading"
            class="m-r-8"
            @click="handleDownload"
            v-hasPermi="['iqc:list:export']"><a-icon type="download" />导出结果</a-button>
          <a-button
            v-hasPermi="['iqc:list:add']"
            type="primary"
            class="m-r-8"
            icon="plus"
            @click="handleAddIqcSample">
            新建抽样计划
          </a-button>
          <a-button
            type="primary"
            v-hasPermi="['iqc:list:batchIssue']"
            icon="plus"
            :loading="batchIssueLoading"
            @click="handleBatchIssue"
            :disabled="!selectedRowKeys.length">
            批量下发
          </a-button>
        </div>
        <a-table
          :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
          :columns="columns"
          :data-source="list"
          :loading="tableLoading"
          rowKey="id"
          :pagination="false"
          size="middle"
          :scroll="tableScroll"
        >
          <template slot="statusSlot" slot-scope="text">
            <a-tag :color="statusColorMap[text]">
              {{ statusTextMap[text] }}
            </a-tag>
          </template>
          <template slot="action" slot-scope="text, record">
            <div class="action-con">
              <a
                v-hasPermi="['iqc:list:edit']"
                :disabled="![0,5].includes(record.status)"
                class="warning-color"
                @click="$refs.editSample.onOpen(record)">
                <a-icon class="m-r-4" type="edit" /> 编辑
              </a>
              <a-divider type="vertical" />

              <a
                v-hasPermi="['iqc:list:editSampleQuantity']"
                :disabled="![0,5].includes(record.status)"
                class="warning-color"
                @click="handleEditRecommendSampleQuantity(record)">
                <a-icon class="m-r-4" type="edit" /> 编辑推荐抽样量
              </a>
              <a-divider type="vertical" />
              <a-popconfirm
                title="确认要取消吗?"
                ok-text="确认"
                cancel-text="取消"
                @confirm="handleDelete(record)"
              >
                <a
                  v-hasPermi="['iqc:list:delete']"
                  :disabled="![0,5].includes(record.status)"
                  class="danger-color">
                  <a-icon class="m-r-4" type="delete" />取消
                </a>
              </a-popconfirm>
              <a-divider type="vertical" />
              <a-popconfirm
                class="custom-pop"
                ok-text="确认"
                cancel-text="取消"
                placement="left"
                @confirm="handleCancelShift(record)"
              >
                <template slot="title">
                  <p>确认提交吗？</p>
                </template>
                <a :disabled="!(record.status ===4 && record.plantNb === '7752')" ><a-icon class="m-r-4" type="to-top" />此库抽样</a>
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

        <IqcSample
          v-model="iqcSampleVisible"
          @on-ok="loadTableList"
        >
        </IqcSample>

        <EditSample ref="editSample" @on-ok="loadTableList"></EditSample>

      </div>
    </div>

    <a-modal
      v-model="editVisible"
      title="修改推荐抽样数量"
      :confirm-loading="confirmLoading"
      ok-text="确认"
      cancel-text="取消"
      @ok="onSubmit">

      <a-form :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-form-item label="数量">
          <a-input-number style="width:200px" :min="0" v-model="editForm.quantity"></a-input-number>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin'
import { download } from '@/utils/file'
import IqcSample from '@/views/iqc/sample/IqcSample.vue'
import EditSample from '@/views/iqc/sample/EditIQCSample.vue'
import _ from 'lodash'

// import _ from 'lodash'

const labelCol = {
  span: 3
}
const wrapperCol = {
  span: 19
}

const statusTextMap = {
  '-1': '已取消',
  0: '待下架',
  1: '待抽样',
  2: '待上架',
  3: '完成',
  4: '移库中',
  5: '待下发'
}

const statusColorMap = {
  '-1': '#ccc',
  0: 'orange',
  1: 'orange',
  2: 'orange',
  3: 'green',
  4: 'orange',
  5: 'orange'
}

const columns = [
  {
    title: '工厂',
    key: 'plantNb',
    dataIndex: 'plantNb',
    width: 60
  },
  {
    title: '仓库',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 80
  },
  {
    title: 'SSCC码',
    key: 'ssccNb',
    dataIndex: 'ssccNb',
    width: 160
  },
  {
    title: 'cell部门',
    key: 'cell',
    dataIndex: 'cell',
    width: 90
  },
  {
    title: '物料编码',
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
    title: '有效期',
    key: 'expireDate',
    dataIndex: 'expireDate',
    width: 120
  },
  {
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    width: 120,
    scopedSlots: { customRender: 'statusSlot' }
  },
  {
    title: '数量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 90
  },
  {
    title: '推荐抽样量',
    key: 'recommendSampleQuantity',
    dataIndex: 'recommendSampleQuantity',
    width: 90
  },
  {
    title: '单位',
    key: 'unit',
    dataIndex: 'unit',
    width: 120
  },
  {
    title: '下架库位',
    key: 'binDownCode',
    dataIndex: 'binDownCode',
    width: 120
  },
  {
    title: '下架人',
    key: 'binDownUser',
    dataIndex: 'binDownUser',
    width: 120
  },
  {
    title: '下架时间',
    key: 'binDownTime',
    dataIndex: 'binDownTime',
    width: 120
  },

  {
    title: '抽样量',
    key: 'sampleQuantity',
    dataIndex: 'sampleQuantity',
    width: 90
  },
  {
    title: '抽样人',
    key: 'sampleUser',
    dataIndex: 'sampleUser',
    width: 120
  },
  {
    title: '抽样时间',
    key: 'sampleTime',
    dataIndex: 'sampleTime',
    width: 120
  },
  {
    title: '上架人',
    key: 'binInUser',
    dataIndex: 'binInUser',
    width: 120
  },
  {
    title: '上架时间',
    key: 'binInTime',
    dataIndex: 'binInTime',
    width: 120
  },
  {
    title: '上架库位',
    key: 'binInCode',
    dataIndex: 'binInCode',
    width: 120
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    width: 370,
    scopedSlots: { customRender: 'action' }
  }
]

const queryFormAttr = () => {
  return {
    materialNb: '',
    statusList: [],
    binDownUser: '',
    binDownTime: [],
    sampleUser: '',
    sampleTime: [],
    wareCode: '',
    cellList: [],
    cell: ''
  }
}
export default {
  name: 'IQCList',
  mixins: [mixinTableList],
  components: {
    IqcSample,
    EditSample
  },
  data () {
    return {
      tableLoading: false,
      uploadLoading: false,
      exportLoading: false,
      cellList: [],
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: [],
      wareList: [],
      iqcSampleVisible: false,
      editVisible: false,
      editForm: {
        quantity: 0
      },
      confirmLoading: false,
      selectedRowKeys: [],
      batchIssueLoading: false
    }
  },
  computed: {
    statusTextMap: () => statusTextMap,
    statusColorMap: () => statusColorMap,
    labelCol () {
      return labelCol
    },
    wrapperCol () {
      return wrapperCol
    }
  },
  methods: {
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    async handleBatchIssue () {
      try {
        this.batchIssueLoading = true
        await this.$store.dispatch('iqcManagement/issueJob', this.selectedRowKeys)
        this.$message.success('修改成功')
        this.selectedRowKeys = []
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.batchIssueLoading = false
      }
    },
    async handleDownload () {
      try {
        this.exportLoading = true
        this.queryForm.pageSize = 0
        const blobData = await this.$store.dispatch('iqcSample/exportExcel', this.queryForm)
        download(blobData, '抽样计划列表')
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      } finally {
        this.exportLoading = false
      }
    },
    handleEditRecommendSampleQuantity (record) {
      console.log(record)
      this.editForm.ssccNb = record.ssccNb
      this.editForm.quantity = record.recommendSampleQuantity
      this.editVisible = true
    },
    async onSubmit () {
      try {
        this.confirmLoading = true
        await this.$store.dispatch('iqcManagement/modifyQuantity', this.editForm)
        this.$message.success('修改成功')
        this.editVisible = false
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.confirmLoading = false
      }
    },
    async handleCancelShift (record) {
      // if (!record.targetWareCode) {
      //   this.$message.error('请先选择目标库位')
      //   return
      // }

      try {
        // const options = { sscc: record.ssccNb, targetWareCode: record.targetWareCode }

        await this.$store.dispatch('iqcSample/cancelShift', record)
        this.$message.success('修改成功')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      }
    },
    async getWareList () {
      try {
        const data = await this.$store.dispatch('ware/getList', {})
        this.wareList = data
      } catch (error) {
        this.$message.error('获取仓库数据失败！')
      }
    },
    async handleCancel (record) {
      try {
        await this.$store.dispatch('iqcSample/cancelIqc', record)
        this.$message.success('取消成功')
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async loadTableList () {
      try {
        this.tableLoading = true
        const { binDownTime = [], sampleTime = [] } = this.queryForm
        const startBinDownTime = binDownTime.length > 0 ? binDownTime[0].format(this.startDateFormat) : undefined
        const endBinDownTime = binDownTime.length > 0 ? binDownTime[1].format(this.endDateFormat) : undefined
        const startSampleTime = sampleTime.length > 0 ? sampleTime[0].format(this.startDateFormat) : undefined
        const endSampleTime = sampleTime.length > 0 ? sampleTime[1].format(this.endDateFormat) : undefined

        const options = {
          ..._.omit(this.queryForm, ['binDownTime', 'sampleTime']),
          startBinDownTime,
          endBinDownTime,
          startSampleTime,
          endSampleTime
        }
        const {
          data: { rows, total }
        } = await this.$store.dispatch('iqcSample/getList', options)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    handleAddIqcSample () {
      this.iqcSampleVisible = true
    },
    async handleDelete (record) {
      try {
        await this.$store.dispatch('iqcSample/cancelIqc', record)
        this.$message.success('取消成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      }
    },
    async uploadBatchUpdate (formdata) {
      try {
        await this.$store.dispatch('iqcSample/uploadBatchUpdate', formdata)
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.uploadLoading = false
      }
    },
    async loadCellList () {
      const { data } = await this.$store.dispatch('department/getList')
      this.cellList = data
    },
    async loadData () {
      this.loadTableList()
      this.loadCellList()
      this.getWareList()
    }
  },
  mounted () {
    this.loadData()
  }
}
</script>

<style scoped>

</style>
