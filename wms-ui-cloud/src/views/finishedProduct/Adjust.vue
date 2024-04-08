<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="plantNb">
              <a-input v-model="queryForm.plantNb" placeholder="plantNb" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="SSCC码">
              <a-input v-model="queryForm.ssccNumber" placeholder="SSCC码" allow-clear/>
            </a-form-item>
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
            <a-form-item label="仓库编码">
              <a-input v-model="queryForm.wareCode" placeholder="仓库编码" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="质检状态" >
              <a-select
                allow-clear
                v-model="queryForm.qualityStatus"
              >
                <a-select-option v-for="item in qualityStatus" :key="item.value" :value="item.text">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="物料号">
              <a-input v-model="queryForm.materialNb" placeholder="物料号" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="物料类型">
              <a-input v-model="queryForm.materialType" placeholder="物料类型" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="批次号">
              <a-input v-model="queryForm.batchNb" placeholder="批次号" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="批次号">
              <a-input v-model="queryForm.batchNb" placeholder="批次号" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="存储区">
              <a-input v-model="queryForm.areaCode" placeholder="存储区" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="库位">
              <a-input v-model="queryForm.binCode" placeholder="库位" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="类型" >
              <a-select allow-clear v-model="queryForm.adjustType">
                <a-select-option v-for="item in adjustTypes" :key="item.value" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="领用理由">
              <a-select placeholder="请选择领用理由" allow-clear v-model="queryForm.useReason">
                <a-select-option v-for="dict in dict.type['sys_use_reason']" :key="dict.value" :value="dict.value">
                  {{ dict.label }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="创建人">
              <a-input v-model="queryForm.createBy" placeholder="创建人" allow-clear/>
            </a-form-item>
          </a-col>         
          <a-col span="4">
            <span class="table-page-search-submitButtons" >
              <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
              <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo" />重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
      <div class="action-content">
        <a-button type="primary" style="margin-left: 8px" :loading="exportLoading" @click="handleDownload"><a-icon type="download" />导出结果</a-button>
      </div>
      
      <a-table
        table-layout="fixed"        
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        :scroll="tableScroll"
        size="middle"
        @change="pageChange"
      >
        <template slot="qualityStatusSlot" slot-scope="text">
          <a-tag :color="colorMap[text]">
            {{ text }}
          </a-tag>
        </template>
        <template slot="adjustType" slot-scope="text">
          <div >
            <a-tag color="orange" v-if="text===0">
              质检取样
            </a-tag>
            <a-tag color="blue" v-if="text===1">
              取样
            </a-tag>
            <a-tag color="#87d068" v-if="text===2">
              报废
            </a-tag>
            <a-tag color="#f95256" v-if="text===3">
              整托出库
            </a-tag>
            <a-tag color="#ddc256" v-if="text===4">
              其它
            </a-tag>
            <a-tag color="#895256" v-if="text===5">
              库存恢复
            </a-tag>
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
import _ from 'lodash'
import { mixinTableList } from '@/utils/mixin/index'
import { download } from '@/utils/file'
const qualityStatus = [
  {
    text: 'U',
    value: 0
  },
  {
    text: 'B',
    value: 1
  },
  {
    text: 'Q',
    value: 2
  }
]
const adjustTypes=[
  {
    text: '质检取样',
    value: 0
  },
  {
    text: '取样',
    value: 1
  },
  {
    text: '报废',
    value: 2
  },
  {
    text: '整托出库',
    value: 3
  },
  {
    text: '其它',
    value: 4
  },
  {
    text: '库存恢复',
    value: 5
  }
]
const columns = [
  {
    title: 'cell',
    key: 'cell',
    dataIndex: 'cell',
    width: 100,
    sorter: true
  },
  {
    title: 'plantNb',
    key: 'plantNb',
    dataIndex: 'plantNb',
    width: 100,
    sorter: true
  },
  {
    title: 'ssccNumber',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 180,
    sorter: true
  },
  {
    title: '物料编码',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 120,
    sorter: true
  },
  {
    title: '物料类型',
    key: 'materialType',
    dataIndex: 'materialType',
    width: 120,
    sorter: true
  },
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 120,
    sorter: true
  }, 
  {
    title: '仓库',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 120,
    sorter: true
  },
  {
    title: '存储区',
    key: 'areaCode',
    dataIndex: 'areaCode',
    width: 120,
    sorter: true
  },  
  {
    title: '跨编码',
    key: 'frameCode',
    dataIndex: 'frameCode',
    width: 120,
    sorter: true
  },
  {
    title: '库位',
    key: 'binCode',
    dataIndex: 'binCode',
    width: 120,
    sorter: true
  },
  {
    title: '保质/有效期',
    key: 'expireDate',
    dataIndex: 'expireDate',
    width: 120,
    sorter: true
  },  
  {
    title: '库存量',
    key: 'totalStock',
    dataIndex: 'totalStock',
    width: 120,
    sorter: true
  },
  {
    title: '冻结库存',
    key: 'freezeStock',
    dataIndex: 'freezeStock',
    width: 120,
    sorter: true
  },
  {
    title: '可用库存',
    key: 'availableStock',
    dataIndex: 'availableStock',
    width: 120,
    sorter: true
  },
  {
    title: '包装规格',
    key: 'boxSpecification',
    dataIndex: 'boxSpecification',
    width: 120
  },
   {
    title: 'PCS总库存',
    key: 'pcsTotalStock',
    dataIndex: 'pcsTotalStock',
    width: 120
  },
  {
    title: 'PCS冻结库存',
    key: 'pcsFreezeStock',
    dataIndex: 'pcsFreezeStock',
    width: 120
  },
  {
    title: 'PCS可用库存',
    key: 'pcsAvailableStock',
    dataIndex: 'pcsAvailableStock',
    width: 120
  },
  {
    title: '调整后总库存',
    key: 'adjustTotalStock',
    dataIndex: 'adjustTotalStock',
    width: 120
  },
  {
    title: '调整后冻结库存',
    key: 'adjustFreezeStock',
    dataIndex: 'adjustFreezeStock',
    width: 120
  },
  {
    title: '调整后可用库存',
    key: 'adjustAvailableStock',
    dataIndex: 'adjustAvailableStock',
    width: 120
  },
  {
    title: '质检状态',
    key: 'qualityStatus',
    dataIndex: 'qualityStatus',
    width: 120,
    scopedSlots: { customRender: 'qualityStatusSlot' },
    sorter: true
  },
  {
    title: '类型',
    key: 'type',
    dataIndex: 'type',
    scopedSlots: { customRender: 'adjustType' },
    width: 80
  },
  {
    title: '领用理由',
    key: 'useReason',
    dataIndex: 'useReason',
    width: 160
  },
  {
    title: '创建者',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 120,
    sorter: true
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 200,
    sorter: true
  }
]

const colorMap = {
  '待检': 'orange',
  '已检': 'green',
  'U': 'green',
  'B': 'red',
  'Q': 'blue',
  '0': 'orange',
  '1': 'green'
}

const queryFormAttr = () => {
  return {
    plantNb: '',
    ssccNumber: '',
    wareCode: '',
    areaCode: '',
    binCode: '',
    fromProdOrder: '',
    createBy: '',
    qualityStatus: '',
    adjustType:'',
    useReason:'',
    materialType: ''
  }
}

export default {
  name: 'FinishedProductAdjust',
  mixins: [mixinTableList],
  dicts: ['sys_use_reason'],
  components: {
  },
  data () {
    return {
      tableLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      exportLoading:false,
      columns,
      list: [],
      cellList: [],
    }
  },
  computed: {
    colorMap: () => colorMap,
    qualityStatus: () => qualityStatus,
    adjustTypes:()=>adjustTypes
  },
  methods: {
    async loadCellList () {
      const { data } = await this.$store.dispatch('department/getList')
      this.cellList = data
    },
    async pageChange(page, filters, sorter){
        this.queryForm.isAsc= sorter.order === 'ascend' ? 'asc' : 'desc'
        this.queryForm.orderByColumn= sorter.columnKey
        this.loadTableList()
    },
    async handleDownload () {
      try {
        this.exportLoading = true
        // this.queryForm.pageSize = 0
        const blobData = await this.$store.dispatch('finishedProductInventory/productStockAdjustExport', this.queryForm)
        download(blobData, '成品库存调整记录.xlsx')
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
    async loadTableList () {
      try {
        this.tableLoading = true

        const { date = [] } = this.queryForm
        const startCreateTime = date.length > 0 ? date[0].format(this.startDateFormat) : undefined
        const endCreateTime = date.length > 0 ? date[1].format(this.endDateFormat) : undefined
        const options = { ..._.omit(this.queryForm, ['date']), startCreateTime, endCreateTime }

        const {
          data: { rows, total }
        } = await this.$store.dispatch('finishedProductInventory/getAdjustList', options)
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
    this.loadCellList()
  }
}
</script>

<style scoped>

</style>