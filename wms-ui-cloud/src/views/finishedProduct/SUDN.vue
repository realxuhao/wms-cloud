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
          <a-col :span="4">
            <a-form-model-item label="Plant">
              <a-input v-model="queryForm.plant" placeholder="Plant" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="Material">
              <a-input v-model="queryForm.material" placeholder="Material" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="Material Name">
              <a-input v-model="queryForm.materialName" placeholder="Material Name" allow-clear/>
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
          <a-col :span="4">
            <a-form-item label="Ship-To Party">
              <a-input v-model="queryForm.shipToParty" placeholder="Ship-To Party" allow-clear/>
            </a-form-item>
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
            <!-- <a-col :span="4">
              <a-form-item label="Ship-To Party">
                <a-input v-model="queryForm.shipToParty" placeholder="Ship-To Party" allow-clear/>
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
            </a-col> -->
          </template>
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
          :loading="generateLoading"
          @click="handleGenerate"
          :disabled="!selectedRowKeys.length">
          生成拣配
        </a-button>
      </div>
      <a-table
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange,
                          getCheckboxProps:record => ({
                            props: {
                              disabled: record.status ===1
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
        <template slot="deliveryQuantity" slot-scope="text,record">
          <EditTableCell v-if="record.status === 0" :text="record.deliveryQuantity" @change="(val)=>handleQuantityChange(record,val)" />
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
import { colorMap } from '@/utils/color'
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
    scopedSlots: { customRender: 'deliveryQuantity' },
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
    title: 'status',
    key: 'status',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    width: 100
  },
  
  {
    title: '更新者',
    key: 'updateBy',
    dataIndex: 'updateBy',
    width: 120
  },
  {
    title: '更新时间',
    key: 'updateTime',
    dataIndex: 'updateTime',
    width: 180
  },
  {
    title: '创建者',
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
    delivery: '',
    shipToParty: '',
    material: '',
    materialName: '',
    
    status:undefined
  }
}

const statusMap = {
  '-1': '取消',
  0: '待生成',
  1: '已生成',
}

const statusColorMap = {
  '-1': colorMap['cancel'],
  0: colorMap['warning'],
  1: colorMap['success'],
  
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
      uploadLoading: false,
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
      generateLoading:false
    }
  },
  computed: {
    statusMap: () => statusMap,
    statusColorMap:()=>statusColorMap
  },
  methods: {
    async handleGenerate () {
      try {
        this.generateLoading = true
        await this.$store.dispatch('finishedProduct/sudnGenerate', this.selectedRowKeys)
        this.$message.success('成功')
        this.selectedRowKeys = []
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.generateLoading = false
      }
    },
    async handleQuantityChange (record, value) {
      try {
        await this.$store.dispatch('finishedProduct/sudnUpdateQuantity', { id: record.id, newQuantity: value })
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
        await this.$store.dispatch('finishedProduct/sudnDelete', record.id)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
    },
  
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
        } = await this.$store.dispatch('finishedProduct/sudnList', options)
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
