<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">          
          <a-col :span="4">
            <a-form-model-item label="生产需求号">
              <a-input v-model="queryForm.orderNumber" placeholder="生产需求号" allow-clear />
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="物料编码">
              <a-input v-model="queryForm.materialCode" placeholder="物料编码" allow-clear />
            </a-form-model-item>
          </a-col>
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
          <template v-if="advanced">           
            <a-col :span="8">
              <a-form-item label="仓库拣配日期">
                <a-range-picker format="YYYY-MM-DD HH:mm" :show-time="{ format: 'HH:mm' }" v-model="queryForm.date" />
              </a-form-item>
            </a-col>
          </template>
          <a-col span="4">
            <span class="table-page-search-submitButtons">
              <a-button
                v-hasPermi="['requirement:list:query']"
                type="primary"
                @click="handleSearch"
                :loading="searchLoading"><a-icon type="search" />查询</a-button>
              <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo" />重置</a-button>
              <a-button
                v-hasPermi="['requirement:list:export']"
                style="margin-left: 8px"
                :loading="exportLoading"
                @click="handleDownload"><a-icon type="download" />导出结果</a-button>
              <a @click="toggleAdvanced" style="margin-left: 8px">
                {{ advanced ? '收起' : '展开' }}
                <a-icon :type="advanced ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
      <div class="action-content">
        <a-button
          v-hasPermi="['requirement:list:add']"
          type="primary"
          icon="upload"
          style="margin-right:8px"
          @click="handleOpenUpload">
          导入BOM信息
        </a-button>
      </div>
      <a-table        
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        :scroll="tableScroll"
        size="middle"
        bordered>
        
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
          @change="changePagination" />
      </div>

      <RegisterBatchUpload v-model="visible" @on-ok="loadTableList"></RegisterBatchUpload>
      
    </div>

  </div>
</template>

<script>
import _ from 'lodash'
import { mixinTableList } from '@/utils/mixin/index'
import RegisterBatchUpload from './RegisterBatchUpload'
import { download } from '@/utils/file'

const columns = [ 
  {
    title: '生产需求号',
    key: 'orderNumber',
    dataIndex: 'orderNumber',
    width: 120
  },
  {
    title: '物料编码',
    key: 'materialCode',
    dataIndex: 'materialCode',
    width: 120
  },
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 160
  },
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 180
  },
  {
    title: '批次号',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 120
  },
  {
    title: '需求量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 120,
    customRender:(value, row, index) => {
      const obj = {
        children: value,
        attrs: {},
      }      
      if (row.rowSpan != null) {
        obj.attrs.rowSpan = row.rowSpan
      }
      return obj
    }
  },
  {
    title: '仓库拣配量',
    key: 'pickQuantity',
    dataIndex: 'pickQuantity',
    width: 120
  },
  {
    title: '仓库拣配日期',
    key: 'pickingTime',
    dataIndex: 'pickingTime',
    width: 150
  },
  {
    title: '车间接收数量',
    key: 'receivedQuantity',
    dataIndex: 'receivedQuantity',
    width: 120
  },
  {
    title: '车间接收日期',
    key: 'receivedTime',
    dataIndex: 'receivedTime',
    width: 150
  },
  {
    title: '车间退库数量',
    key: 'returnQuantity',
    dataIndex: 'returnQuantity',
    width: 120
  },
  {
    title: '车间退库日期',
    key: 'returnTime',
    dataIndex: 'returnTime',
    width: 150
  },
  {
    title: '仓库接收数量',
    key: 'returnReceivedQuantity',
    dataIndex: 'returnReceivedQuantity',
    width: 120
  },
  {
    title: '仓库接收日期',
    key: 'returnReceivedTime',
    dataIndex: 'returnReceivedTime',
    width: 150
  },
  {
    title: '车间消耗数量',
    key: 'productQuantity',
    dataIndex: 'productQuantity',
    width: 120
  },
  {
    title: '标准配方量',
    key: 'componentQuantity',
    dataIndex: 'componentQuantity',
    width: 120,
    customRender:(value, row, index) => {
      const obj = {
        children: value,
        attrs: {},
      }
      console.log(index,row.rowSpan)
      if (row.rowSpan != null) {
        obj.attrs.rowSpan = row.rowSpan
      }
      return obj
    }
  },
  {
    title: '消耗与配方差异量',
    key: 'gapQuantity',
    dataIndex: 'gapQuantity',
    width: 120,
    customRender:(value, row, index) => {
      const obj = {
        children: value,
        attrs: {},
      }
      console.log(index,row.rowSpan)
      if (row.rowSpan != null) {
        obj.attrs.rowSpan = row.rowSpan
      }
      return obj
    }
  }  
]


const queryFormAttr = () => {
  return {
    ssccNumber: '',
    materialCode: '',
    orderNumber: '',
    batchNb: '',
    date: [],
    updateDate: [],
    createBy: ''
  }
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  components: {
    RegisterBatchUpload
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

      
      notQuantity: 0
    }
  },
  computed: {
   
  },
  methods: {
    async handleDownload () {
      try {
        this.exportLoading = true
        //this.queryForm.pageSize = 0
        const blobData = await this.$store.dispatch('materialFeeding/exportRegisterBatchExcel', this.queryForm)
        console.log(blobData)
        download(blobData, '注册批记录')
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
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const { date = [], updateDate = [] } = this.queryForm
        const createTimeStart = date.length > 0 ? date[0].format(this.startDateFormat) : undefined
        const createTimeEnd = date.length > 0 ? date[1].format(this.endDateFormat) : undefined        

        const options = { ..._.omit(this.queryForm, ['date', 'updateDate']), createTimeStart, createTimeEnd }

        const {
          data: { rows, total }
        } = await this.$store.dispatch('materialFeeding/getPaginationRegisterList', options)
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

<style lang="less" scoped></style>
