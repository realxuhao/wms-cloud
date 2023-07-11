<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="源工厂">
              <a-input v-model="queryForm.sourcePlantNb" placeholder="源工厂" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="源仓库">
              <a-input v-model="queryForm.sourceWareCode" placeholder="源仓库" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="转运单号">
              <a-input v-model="queryForm.orderNb" placeholder="转运单号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="状态">
              <a-select
                allow-clear
                v-model="queryForm.status"
              >
                <a-select-option v-for="(item,key) in statusMap" :key="item" :value="key">
                  {{ item }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="SSCC码">
              <a-input v-model="queryForm.ssccNb" placeholder="SSCC码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <template v-if="advanced">
            <a-col :span="4">
              <a-form-model-item label="车牌号">
                <a-input v-model="queryForm.carNb" placeholder="车牌号" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-model-item label="目的库仓库">
                <a-input v-model="queryForm.targetWareCode" placeholder="目的库仓库" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="移库人">
                <a-input v-model="queryForm.createBy" placeholder="移库人" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="移库时间" >
                <a-range-picker
                  format="YYYY-MM-DD HH:mm"
                  :show-time="{ format: 'HH:mm' }"
                  v-model="queryForm.date"
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
        <a-button type="primary" style="margin-left: 8px" :loading="exportLoading" @click="handleDownload"><a-icon type="download" />导出结果</a-button>
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
        <template slot="moveType" slot-scope="text">
          <div >
            {{ moveTypeMap[text] }}
          </div>
        </template>
        <template slot="status" slot-scope="text">
          <a-tag :color="statusColorMap[text]"> {{ statusMap[text] }}</a-tag>
        </template>
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a-popconfirm
              title="确认要取消该条任务吗?"
              ok-text="确认"
              cancel-text="取消"
              @confirm="handleCancel(record)"
            >
              <a class="danger-color" :disabled="record.status !== 0"><a-icon class="m-r-4" type="delete" />取消</a>
            </a-popconfirm>
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

  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'
import { colorMap } from '@/utils/color'
import _ from 'lodash'
import { download } from '@/utils/file'

const columns = [
  {
    title: '转运单号',
    key: 'orderNb',
    dataIndex: 'orderNb',
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
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    width: 120
  },
  {
    title: '移库数量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 120
  },
  // {
  //   title: '移动类型',
  //   key: 'moveType',
  //   dataIndex: 'moveType',
  //   scopedSlots: { customRender: 'moveType' },
  //   width: 120
  // },
  {
    title: '车牌号',
    key: 'carNb',
    dataIndex: 'carNb',
    width: 120
  },
  {
    title: 'sscc码',
    key: 'ssccNb',
    dataIndex: 'ssccNb',
    width: 120
  },
  {
    title: '物料号',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 120
  },
  {
    title: '物料名',
    key: 'materialName',
    dataIndex: 'materialName',
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
  // {
  //   title: '目的工厂',
  //   key: 'targetPlant',
  //   dataIndex: 'targetPlant',
  //   width: 120
  // },
  {
    title: '目的仓库',
    key: 'targetWareCode',
    dataIndex: 'targetWareCode',
    width: 120
  },
  // {
  //   title: '目的存储区',
  //   key: 'targetAreaCode',
  //   dataIndex: 'targetAreaCode',
  //   width: 120
  // },
  // {
  //   title: '目的库位',
  //   key: 'targetBinCode',
  //   dataIndex: 'targetBinCode',
  //   width: 120
  // },

  // {
  //   title: '推荐库位',
  //   key: 'recommendBinCode',
  //   dataIndex: 'recommendBinCode',
  //   width: 120
  // },
  {
    title: '操作人',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 120
  },
  {
    title: '操作时间',
    key: 'createTime',
    dataIndex: 'createTime',
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

const moveTypeMap = {
 '-1': '取消',
  0: '待发运',
  1: '待收货',
  2: '待上架',
  3: '完成'
}
const statusMap = {
  '-1': '取消',
  0: '待发运',
  1: '待收货',
  2: '待上架',
  3: '完成'
}

const statusColorMap = {
  '-1': colorMap['cancel'],
  0: colorMap['warning'],
  1: colorMap['warning'],
  2: colorMap['warning'],
  3: colorMap['success']
  
}

const queryFormAttr = () => {
  return {
    sourcePlantNb: '',
    sourceWareCode: '',
    orderNb: '',
    status: '',
    ssccNb: '',
    carNb: '',
    targetWareCode: '',
    date: [],
    createBy: ''
  }
}

export default {
  name: 'FinishedProductTransfer',
  mixins: [mixinTableList],
  components: {
  },
  data () {
    return {
      tableLoading: false,
      uploadLoading: false,
      exportLoading:false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: [],
      stockListVisible: false
    }
  },
  computed: {
    moveTypeMap: () => moveTypeMap,
    statusMap: () => statusMap,
    statusColorMap: () => statusColorMap
  },
  methods: {
    async handleDownload () {
      try {
        this.exportLoading = true
        // this.queryForm.pageSize = 0
        const blobData = await this.$store.dispatch('finishedProduct/productShiftexport', this.queryForm)
        download(blobData, '成品移库.xlsx')
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      } finally {
        this.exportLoading = false
      }
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async handleCancel (record) {
      try {
        await this.$store.dispatch('finishedProductTransfer/cancelTransfer', record.id)
        this.$message.success('取消成功')
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async loadTableList () {
      try {
        this.tableLoading = true
        const { date = [] } = this.queryForm
        const startCreateTime = date.length > 0 ? date[0].format(this.startDateFormat) : undefined
        const endCreateTime = date.length > 0 ? date[1].format(this.endDateFormat) : undefined

        const options = { ..._.omit(this.queryForm, ['date']), startCreateTime, endCreateTime }

        const {
          data: { rows, total }
        } = await this.$store.dispatch('finishedProductTransfer/getTransferList', options)
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

<style scoped>

</style>
