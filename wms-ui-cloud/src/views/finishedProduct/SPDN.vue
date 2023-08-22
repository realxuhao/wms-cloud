<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="Plant">
              <a-input v-model="queryForm.plant" placeholder="Plant" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="DeliveryNb">
              <a-input v-model="queryForm.deliveryNb" placeholder="DeliveryNb" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="ShipTo">
              <a-input v-model="queryForm.shipTo" placeholder="ShipTo" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="Vendor Code">
              <a-input v-model="queryForm.vendorCode" placeholder="Vendor Code" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="状态">
              <a-select
                placeholder="状态"
                allow-clear
                v-model="queryForm.status"
              >
                <a-select-option v-for="(value,key) in statusMap" :key="key" :value="key">
                  {{ value }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="Ship_Date">
              <a-range-picker
                format="YYYY-MM-DD"

                v-model="queryForm.shipDate"
              />
            </a-form-item>
          </a-col>
          <template v-if="advanced">
            <a-col :span="4">
              <a-form-item label="SSCCNumber">
                <a-input v-model="queryForm.ssccNumber" placeholder="SSCCNumber" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="Material">
                <a-input v-model="queryForm.material" placeholder="Material" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="Qty" >
                <a-input v-model="queryForm.qty" placeholder="Qty" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-model-item label="Batch">
                <a-input v-model="queryForm.batch" placeholder="Batch" allow-clear/>
              </a-form-model-item>
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
        <a-tooltip placement="left">
          <!-- <template slot="title">
            <a style="color:#fff" @click="handleDownloadTemplate"><a-icon type="arrow-down" />下载模板</a>
          </template> -->
          <a-upload
            :file-list="[]"
            name="file"
            :multiple="true"
            :before-upload="()=>false"
            @change="handleUpload"
          >
            <a-button :loading="uploadLoading" type="primary" icon="upload" class="m-r-8">
              导入模板
            </a-button>
          </a-upload>
        </a-tooltip>


        <a-button
          type="primary"
          icon="plus"
          class="m-r-8"
          :loading="approvalNo7761Loading"
          @click="handleApprovalNo7761"
          :disabled="!selectedRowKeys.length">
          非7761审批
        </a-button>

        <a-button
          type="primary"
          class="m-r-8"
          icon="plus"
          :loading="shipLoading"
          @click="handleBatchShip"
          :disabled="!selectedRowKeys.length">
          7761发运
        </a-button>


        

        <a-button
          type="primary"
          class="m-r-8"
          icon="plus"
          :loading="approvalLoading"
          @click="handleApproval"
          :disabled="!selectedRowKeys.length">
          7761审批
        </a-button>




        <h3>总库存量：{{ totalQty }}</h3>
      </div>
      <a-table
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange,
        }"
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        size="middle"
        :scroll="tableScroll"
      >

        <template slot="status" slot-scope="text">
          <a-tag :color="colorMap[text]">
            {{ statusMap[text] }}
          </a-tag>
        </template>
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a-popconfirm
              title="确认要删除吗?"
              ok-text="确认"
              cancel-text="取消"
              @confirm="handleDelete(record)"
            >
              <a :disabled="record.status ===1" class="danger-color"><a-icon class="m-r-4" type="delete" />删除</a>
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
          @showSizeChange="onShowSizeChange"
          @change="changePagination"
        />
      </div>
    </div>

    <!-- <a-modal
      title="请筛选移库日期范围"
      :visible="visible"
      :confirm-loading="genTaskLoading"
      @ok="handleGenTask"
      width="600px"
      @cancel="visible = false"
    >
      <a-form layout="inline" class="search-content">
        <a-form-item label="移库日期">
          <a-range-picker
            format="YYYY-MM-DD"
            v-model="moveDate"
          />
        </a-form-item>
      </a-form>
    </a-modal> -->
  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'

import _ from 'lodash'

const columns = [

  {
    title: 'External Ref',
    key: 'externalRef',
    dataIndex: 'externalRef',
    width: 120
  },
  {
    title: 'Plant',
    key: 'plant',
    dataIndex: 'plant',
    width: 120
  },
  {
    title: 'DeliveryNb',
    key: 'deliveryNb',
    dataIndex: 'deliveryNb',
    width: 120
  },
  {
    title: 'Ship_To',
    key: 'shipTo',
    dataIndex: 'shipTo',
    width: 140
  },
  {
    title: 'Ship_Date',
    key: 'Ship_Date',
    dataIndex: 'shipDate',
    width: 120
  },
  {
    title: 'Vendor Code',
    key: 'VendorCode',
    dataIndex: 'vendorCode',
    width: 100
  },
  {
    title: 'SSCCNumber',
    key: 'SSCCNumber',
    dataIndex: 'ssccNumber',
    width: 120
  },
  {
    title: 'Delivery_Item',
    key: 'Delivery_Item',
    dataIndex: 'deliveryItem',
    width: 120
  },
  {
    title: 'Material',
    key: 'Material',
    dataIndex: 'material',
    width: 70
  },
  {
    title: 'Batch',
    key: 'Batch',
    dataIndex: 'batch',
    width: 80
  },
  {
    title: 'Qty',
    key: 'Qty',
    dataIndex: 'qty',
    width: 80
  },
 
  {
    title: 'UoM',
    key: 'UoM',
    dataIndex: 'uom',
    width: 100
  },
  {
    title: 'StorageLocation',
    key: 'StorageLocation',
    dataIndex: 'storageLocation',
    width: 120
  },
  {
    title: 'ProdBatch',
    key: 'ProdBatch',
    dataIndex: 'prodBatch',
    width: 100
  },
  {
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    width: 80,
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
    width: 180
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    width: 160,
    scopedSlots: { customRender: 'action' }
  }
]

const queryFormAttr = () => {
  return {
    plant: '',
    deliveryNb: '',
    shipTo: '',
    material: '',
    shipDate: [],
    vendorCode: '',
    ssccNumber: '',
    material: '',
    batch: '',
    qty:'',

  }
}
// const status = [
//   {
//     text: '已上传',
//     value: 0
//   },
//   {
//     text: '已发运',
//     value: 1
//   },
//   {
//     text: '已审批',
//     value: 2
//   }
// ]

const statusMap = {
  0:'已上传',
  1:'已审批',
  2:'已发运'
}

const colorMap = {
  '0': 'orange',
  '1': 'orange',
  '2': 'blue',
}
export default {
  name: 'Area',
  mixins: [mixinTableList],
  data () {
    return {
      tableLoading: false,
      uploadLoading: false,
      shipLoading:false,
      genTaskLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: [],

      visible: false,
      moveDate: [],

      selectedRowKeys: [],
      approvalLoading:false,
      approvalNo7761Loading: false
    }
  },
  computed: {
    totalQty(){
     return _.sumBy(this.list,'qty')
    },
    colorMap: () => colorMap,
    statusMap:()=>statusMap
  },
  methods: {
    async handleBatchShip () {
      try {
        this.shipLoading = true
        await this.$store.dispatch('finishedProduct/supnBatchShip', this.selectedRowKeys)
        this.$message.success('提交成功')
        this.selectedRowKeys = []
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.shipLoading = false
      }
    },
    async handleApproval () {
      try {
        this.approvalLoading = true
        await this.$store.dispatch('finishedProduct/approveSpdnList', this.selectedRowKeys)
        this.$message.success('提交成功')
        this.selectedRowKeys = []
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.approvalLoading = false
      }
    },
    async handleApprovalNo7761 () {
      try {
        this.approvalNo7761Loading = true
        await this.$store.dispatch('finishedProduct/approveNo7761SpdnList', this.selectedRowKeys)
        this.$message.success('提交成功')
        this.selectedRowKeys = []
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.approvalNo7761Loading = false
      }
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async handleDelete (record) {
      try {
        await this.$store.dispatch('finishedProduct/deleteSpdn', record.id)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
    },
    // async handleDownloadTemplate () {
    //   try {
    //     this.$store.dispatch('file/downloadByFilename', '打包计划.xlsx')
    //   } catch (error) {
    //     this.$message.error(error.message)
    //   }
    // },
    // async handleGenTask () {
    //   try {
    //     const date = this.moveDate
    //     const stockMovementDateStart = date.length > 0 ? date[0].format('YYYY-MM-DD 00:00:00') : undefined
    //     const stockMovementDateEnd = date.length > 0 ? date[1].format('YYYY-MM-DD 23:59:59') : undefined

    //     this.genTaskLoading = true
    //     await this.$store.dispatch('finishedProduct/genTask', { stockMovementDateStart, stockMovementDateEnd })

    //     this.queryForm.pageNum = 1
    //     this.loadTableList()

    //     this.$message.success('打包任务生成成功')
    //     this.visible = false
    //   } catch (error) {
    //     this.$message.error(error.message)
    //   } finally {
    //     this.genTaskLoading = false
    //   }
    // },
    async handleUpload (e) {
      const { file } = e

      const formdata = new FormData()

      try {
        formdata.append('file', file)

        this.uploadLoading = true
        await this.$store.dispatch('finishedProduct/importSPDN', formdata)

        this.queryForm.pageNum = 1
        this.loadTableList()

        this.$message.success('导入成功！')
      } catch (error) {
        // if (error.code === 400) {
        //   this.$confirm({
        //     title: '导入出错',
        //     content: error.message + '，是否全部删除',
        //     onOk: () => {
        //       this.uploadBatchUpdate(formdata)
        //     },
        //     onCancel () {
        //     }
        //   })
        // } else {
        //   this.$message.error(error.message)
        //   this.uploadLoading = false
        // }
          this.$message.error(error.message)

      } finally {
        this.uploadLoading = false
      }
    },
    async loadTableList () {
      try {
        this.tableLoading = true
        const { shipDate = [] } = this.queryForm
        const shipDateStart = shipDate.length > 0 ? shipDate[0].format('YYYY-MM-DD 00:00:00') : undefined
        const shipDateEnd = shipDate.length > 0 ? shipDate[1].format('YYYY-MM-DD 23:59:59') : undefined
        const options = { ..._.omit(this.queryForm, ['shipDate']), shipDateStart, shipDateEnd }
        const {
          data: { rows, total }
        } = await this.$store.dispatch('finishedProduct/spdnList', options)
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
