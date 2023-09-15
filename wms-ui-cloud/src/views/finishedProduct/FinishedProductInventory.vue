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
            <a-form-item label="FromProdOrder">
              <a-input v-model="queryForm.fromProdOrder" placeholder="FromProdOrder" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="创建人">
              <a-input v-model="queryForm.createBy" placeholder="创建人" allow-clear/>
            </a-form-item>
          </a-col>
          <!-- <a-col :span="8">
            <a-form-item label="创建时间" >
              <a-range-picker
                format="YYYY-MM-DD HH:mm"
                :show-time="{ format: 'HH:mm' }"
                v-model="queryForm.date"
              />
            </a-form-item>
          </a-col> -->
          <a-col span="4">
            <span class="table-page-search-submitButtons" >
              <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
              <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo" />重置</a-button>
              
            </span>
          </a-col>
        </a-row>
      </a-form>
      <div class="action-content">
        <a-button type="primary" class="m-r-8" icon="plus" @click="createTransfer" :disabled="this.selectedRowKeys.length <= 0"> 新建移库 </a-button>
        <a-button type="primary" style="margin-left: 8px" :loading="exportLoading" @click="handleDownload"><a-icon type="download" />导出结果</a-button>
        <h3 style="margin-left: 20px">当前页总库存量(TR)：{{ totalStock }}</h3>
        <h3 style="margin-left: 20px">当前页总库存量(PCS)：{{ totalPCSStock }}</h3>
        <h3 style="margin-left: 20px">当前页可用库存量(PCS)：{{ totalAvailablePCSStock }}</h3>

      </div>
     
      <a-table
        table-layout="fixed"
        :row-selection="{
          selectedRowKeys: selectedRowKeys, onChange: onSelectChange ,
          getCheckboxProps:record => ({
            props: {
              disabled: record.freezeStock > 0 
            },
          }),}"
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
    title: 'FromProdOrder',
    key: 'fromProdOrder',
    dataIndex: 'fromProdOrder',
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
    title: '过期日期',
    key: 'expireDate',
    dataIndex: 'expireDate',
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
    title: '批次总托数',
    key: 'sameProdOrderCount',
    dataIndex: 'sameProdOrderCount',
    width: 120
  },
  {
    title: '批次总库存',
    key: 'totalStockSum',
    dataIndex: 'totalStockSum',
    width: 120,
    sorter: true
  },
  {
    title: '总库存',
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
    title: 'Unit',
    key: 'unit',
    dataIndex: 'unit',
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
    title: '入库人',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 120,
    sorter: true
  },
  {
    title: '入库时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 200,
    sorter: true
  },
  {
    title: '质检状态',
    key: 'qualityStatus',
    dataIndex: 'qualityStatus',
    width: 120,
    scopedSlots: { customRender: 'qualityStatusSlot' },
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
    materialType: ''
  }
}

export default {
  name: 'FinishedProductInventory',
  mixins: [mixinTableList],
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
      submitLoading: false,
      confirmMaterialLoading: false,
      selectedRowKeys: [],
      columns,
      list: [],
      departmentList: [],

      currentOrderNb: '',
      currentCell: '',
      currentMaterialNb: '',
      stockListVisible: false,
      notQuantity: 0
    }
  },
  computed: {
    status: () => status,
    colorMap: () => colorMap,
    qualityStatus: () => qualityStatus,
    hasSelected () {
      return this.selectedRowKeys.length > 0
    },
    totalStock(){
     return _.sumBy(this.list,'totalStock')
    },
    totalPCSStock(){
     return _.sumBy(this.list,'pcsTotalStock')
    },
    totalAvailablePCSStock(){
     return _.sumBy(this.list,'pcsAvailableStock')
    },
  },
  methods: {
    async pageChange(page, filters, sorter){
        this.queryForm.isAsc= sorter.order === 'ascend' ? 'asc' : 'desc'
        this.queryForm.orderByColumn= sorter.columnKey
        this.loadTableList()
    },
    async handleDownload () {
      try {
        this.exportLoading = true
        // this.queryForm.pageSize = 0
        const blobData = await this.$store.dispatch('finishedProductInventory/productStockExport', this.queryForm)
        download(blobData, '成品工厂库存.xlsx')
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      } finally {
        this.exportLoading = false
      }
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
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
        } = await this.$store.dispatch('finishedProductInventory/getInventoryList', options)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async createTransfer () {
      try {
        await this.$store.dispatch('finishedProductTransfer/createTransfer', this.selectedRowKeys)
        this.$message.success('创建成功！')
        this.loadData()
        this.selectedRowKeys = []
      } catch (error) {
        this.$message.error(error.message)
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
