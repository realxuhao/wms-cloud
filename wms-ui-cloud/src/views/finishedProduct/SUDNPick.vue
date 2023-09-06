<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
         
          <a-col :span="4">
            <a-form-model-item label="Delivery">
              <a-input v-model="queryForm.delivery" placeholder="Delivery" allow-clear/>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="4">
            <a-form-model-item label="Plant">
              <a-input v-model="queryForm.plant" placeholder="Plant" allow-clear/>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="4">
            <a-form-item label="Material">
              <a-input v-model="queryForm.material" placeholder="Material" allow-clear/>
            </a-form-item>
          </a-col>
          <!-- <a-col :span="4">
            <a-form-item label="Material Name">
              <a-input v-model="queryForm.materialName" placeholder="Material Name" allow-clear/>
            </a-form-item>
          </a-col> -->
          <a-col :span="4">
            <a-form-item label="SSCC">
              <a-input v-model="queryForm.ssccNb" placeholder="SSCC" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="Batch">
              <a-input v-model="queryForm.batch" placeholder="Batch" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="Production batch">
              <a-input v-model="queryForm.productionBatch" placeholder="Production batch" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="创建时间" >
              <a-range-picker
                v-model="queryForm.date"
                format="YYYY-MM-DD"
            
              />
            </a-form-item>
          </a-col>
          <!-- <a-col :span="8">
            <a-form-item label="Ship_Date">
              <a-range-picker
                format="YYYY-MM-DD"

                v-model="queryForm.shipDate"
              />
            </a-form-item>
          </a-col> -->
          <!-- <a-col :span="4">
            <a-form-item label="Ship-To Party">
              <a-input v-model="queryForm.shipToParty" placeholder="Ship-To Party" allow-clear/>
            </a-form-item>
          </a-col> -->
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
            <span class="table-page-search-submitButtons" >
              <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
              <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo" />重置</a-button>
              <a @click="toggleAdvanced" style="margin-left: 8px">
                {{ advanced ? '收起' : '展开' }}
                <a-icon :type="advanced ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
          <template v-if="advanced">
          
          </template>
        </a-row>
      </a-form>
      <div class="action-content">
     
        <!-- <a-button
          class="m-r-8"
          type="primary"
          icon="plus"
          :loading="generateLoading"
          @click="handleGenerate"
          :disabled="!selectedRowKeys.length">
          生成拣配
        </a-button> -->

        <a-button
          type="primary"
          icon="plus"
          :loading="dispatchLoading"
          @click="handleDispatch"
          :disabled="!selectedRowKeys.length">
          批量下发
        </a-button>
        <a-button style="margin-left: 8px" :loading="exportLoading" @click="handleDownload"><a-icon type="download" />导出结果</a-button>
        <a-button style="margin-left: 8px" :loading="exportLoading" @click="handleBatchCancel"><a-icon type="download" />批量删除</a-button>
      </div>

      
      <a-table
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange,
                          getCheckboxProps:record => ({
                            props: {
                              
                            },
                          }), }"
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        size="middle"
        :scroll="tableScroll"
      >

        <template slot="status" slot-scope="text">
          <a-tag :color="statusColorMap[text]"> {{ statusMap[text] }}</a-tag>
        </template>
        <template slot="binDownQuantity" slot-scope="text,record">
          <EditTableCell v-if="record.status === 2" :text="record.binDownQuantity" @change="(val)=>handleQuantityChange(record,val)" />
          <span v-else>{{ text }}</span>
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
  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'
import { colorMap } from '@/utils/color'
import { download } from '@/utils/file'
import EditTableCell from '@/components/EditTableCell'

import _ from 'lodash'

const columns = [

  {
    title: 'Delivery',
    key: 'delivery',
    dataIndex: 'delivery',
    width: 120
  },
  {
    title: 'Deliv. date(From/to)',
    key: 'deliveryDate',
    dataIndex: 'deliveryDate',
    width: 120
  },
  {
    title: 'Delivery Quantity',
    key: 'deliveryQuantity',
    dataIndex: 'deliveryQuantity',
    width: 140
  },
  {
    title: '下架数量',
    key: 'binDownQuantity',
    dataIndex: 'binDownQuantity',
    scopedSlots: { customRender: 'binDownQuantity' },
    width: 140
  },
  {
    title: 'Item',
    key: 'item',
    dataIndex: 'item',
    width: 140
  },
  {
    title: 'Material',
    key: 'material',
    dataIndex: 'material',
    width: 120
  },
  {
    title: 'Material Name',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 100
  },
  {
    title: 'Name of the ship-to party',
    key: 'nameOfShipToParty',
    dataIndex: 'nameOfShipToParty',
    width: 140
  },
  {
    title: 'Plant',
    key: 'plant',
    dataIndex: 'plant',
    width: 120
  },

  {
    title: 'Sales Unit',
    key: 'salesUnit',
    dataIndex: 'salesUnit',
    width: 80
  },
  {
    title: 'Ship-To Party',
    key: 'shipToParty',
    dataIndex: 'shipToParty',
    width: 80
  },
 
  {
    title: 'SSCC',
    key: 'SSCC',
    dataIndex: 'sscc',
    width: 80
  },
  {
    title: 'Batch',
    key: 'Batch',
    dataIndex: 'batch',
    width: 80
  },
  {
    title: 'Production batch',
    key: 'Production batch',
    dataIndex: 'productionBatch',
    width: 120
  },

  
  {
    title: 'status',
    key: 'status',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    width: 100
  },
  
  {
    title: '车牌号',
    key: 'carNb',
    dataIndex: 'carNb',
    width: 120
  },
  // {
  //   title: '下架人',
  //   key: 'updateBy',
  //   dataIndex: 'updateBy',
  //   width: 120
  // },
  // {
  //   title: '下架日期',
  //   key: 'updateTime',
  //   dataIndex: 'updateTime',
  //   width: 180
  // },
  // {
  //   title: '发运人',
  //   key: 'createBy',
  //   dataIndex: 'createBy',
  //   width: 120
  // },
  // {
  //   title: '发运时间',
  //   key: 'createTime',
  //   dataIndex: 'createTime',
  //   width: 180
  // },
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
    delivery: '',
    shipToParty: '',
    material: '',
    materialName: '',
    date: [],
    status:undefined
  }
}

const statusMap = {
  '-1': '已取消',
  0: '待下发',
  1: '待下架',
  2: '待发运',
  3: '完成'
}

const statusColorMap = {
  '-1': colorMap['cancel'],
  0: colorMap['warning'],
  1: colorMap['warning'],
  2: colorMap['warning'],
  3: colorMap['success'],
  
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  components:{
    EditTableCell
  },
  data () {
    return {
      tableLoading: false,
      genTaskLoading: false,
      dispatchLoading:false,
      exportLoading: false,
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
      generateLoading:false
    }
  },
  computed: {
    statusMap: () => statusMap,
    statusColorMap:()=>statusColorMap
  },
  methods: {
    async handleDownload () {
      try {
        this.exportLoading = true
        const { date = [] } = this.queryForm

        const startCreateTime = date.length > 0 ? date[0].format(this.startDateFormat) : undefined
        const endCreateTime = date.length > 0 ? date[1].format(this.endDateFormat) : undefined
        const options = { ..._.omit(this.queryForm, ['date']), startCreateTime, endCreateTime }

        const blobData = await this.$store.dispatch('finishedProduct/exportSUDNPickExcel', options)
        download(blobData, 'SUDN捡配列表')
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      } finally {
        this.exportLoading = false
      }
    },
    async handleGenerate () {
      try {
        this.generateLoading = true
        await this.$store.dispatch('finishedProduct/', this.selectedRowKeys)
        this.$message.success('成功')
        this.selectedRowKeys = []
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.generateLoading = false
      }
    },
    async handleDispatch () {
      try {
        this.dispatchLoading = true
        await this.$store.dispatch('finishedProduct/sudnPickBatchIssue', this.selectedRowKeys)
        this.$message.success('成功')
        this.selectedRowKeys = []
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.dispatchLoading = false
      }
    },
    
    async handleQuantityChange (record, value) {
      try {
        await this.$store.dispatch('finishedProduct/editBinDownQuantity', { pickId: record.id, newBinDownQuantity: value })
        this.$message.success('修改成功！')

        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
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
        await this.$store.dispatch('finishedProduct/sudnPickBatchDelete', record.id)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      }
    },
    async handleBatchCancel (record) {
      try {
        await this.$store.dispatch('finishedProduct/sudnPickBatchDelete', this.selectedRowKeys)
        this.$message.success('删除成功！')
this.selectedRowKeys = []
        this.loadTableList()
      } catch (error) {
        console.log(error)
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
        } = await this.$store.dispatch('finishedProduct/sudnPickList', options)
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
