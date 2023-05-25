<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="Shipping Mark">
              <a-input v-model="queryForm.shippingMark" placeholder="Shipping Mark" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="ETO PO">
              <a-input v-model="queryForm.etoPo" placeholder="ETO PO" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="ETO PLANT">
              <a-input v-model="queryForm.etoPlant" placeholder="ETO PLANT" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="移库日期">
              <a-range-picker
                format="YYYY-MM-DD"

                v-model="queryForm.date"
              />
            </a-form-item>
          </a-col>
          <template v-if="advanced">
            <a-col :span="4">
              <a-form-item label="Country">
                <a-input v-model="queryForm.country" placeholder="Country" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="Prod-order">
                <a-input v-model="queryForm.prodOrder" placeholder="Prod-order" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="Qty" >
                <a-input v-model="queryForm.qty" placeholder="Qty" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-model-item label="是否拆托">
                <a-input v-model="queryForm.isDisassembled" placeholder="是否拆托" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="TR">
                <a-input v-model="queryForm.tr" placeholder="TR" allow-clear/>
              </a-form-item>
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
          <template slot="title">
            <a style="color:#fff" @click="handleDownloadTemplate"><a-icon type="arrow-down" />下载模板</a>
          </template>
          <a-upload
            :file-list="[]"
            name="file"
            :multiple="true"
            :before-upload="()=>false"
            @change="handleUpload"
          >
            <a-button :loading="uploadLoading" type="primary" icon="upload" class="m-r-8">
              导入打包计划
            </a-button>
          </a-upload>
        </a-tooltip>
        <a-button type="primary" icon="logout" @click="visible = true" :loading="genTaskLoading">
          生成打包计划
        </a-button>
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

        <template slot="status" slot-scope="text">
          <div >
            <a-tag color="orange" v-if="text===0">
              未生成
            </a-tag>
            <a-tag color="#87d068" v-if="text===1">
              已生成
            </a-tag>
          </div>
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
          show-less-items
          :current="queryForm.pageNum"
          :page-size.sync="queryForm.pageSize"
          :total="paginationTotal"
          @showSizeChange="onShowSizeChange"
          @change="changePagination"
        />
      </div>
    </div>

    <a-modal
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
    </a-modal>
  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'

import _ from 'lodash'

const columns = [
  {
    title: 'Shipping Mark',
    key: 'shippingMark',
    dataIndex: 'shippingMark',
    width: 120
  },
  {
    title: 'ETO PO',
    key: 'etoPo',
    dataIndex: 'etoPo',
    width: 120
  },
  {
    title: 'ETO PLANT',
    key: 'etoPlant',
    dataIndex: 'etoPlant',
    width: 120
  },
  {
    title: '移库日期',
    key: 'stockMovementDate',
    dataIndex: 'stockMovementDate',
    width: 140
  },
  {
    title: 'Pallet Quantity',
    key: 'palletQuantity',
    dataIndex: 'palletQuantity',
    width: 120
  },
  {
    title: 'after packing',
    key: 'afterPacking',
    dataIndex: 'afterPacking',
    width: 100
  },
  {
    title: 'Country',
    key: 'country',
    dataIndex: 'country',
    width: 80
  },
  {
    title: 'Prod-order',
    key: 'prodOrder',
    dataIndex: 'prodOrder',
    width: 100
  },
  {
    title: 'Qty',
    key: 'qty',
    dataIndex: 'qty',
    width: 70
  },
  {
    title: '是否拆托',
    key: 'isDisassembled',
    dataIndex: 'isDisassembled',
    width: 80
  },
  {
    title: 'TR',
    key: 'tr',
    dataIndex: 'tr',
    width: 80
  },
  {
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    width: 80,
    scopedSlots: { customRender: 'status' }
  },
  {
    title: 'SAP Code',
    key: 'sapCode',
    dataIndex: 'sapCode',
    width: 100
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
    plantNb: '',
    wareCode: '',
    ssccNumber: '',
    materialNb: '',
    batchNb: '',
    areaCode: '',
    binCode: '',
    palletCode: '',
    status: ''
  }
}
const status = [
  {
    text: '未生成',
    value: 0
  },
  {
    text: '已生成',
    value: 1
  }
]
export default {
  name: 'Area',
  mixins: [mixinTableList],
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
      moveDate: []
    }
  },
  computed: {
    status: () => status
  },
  methods: {
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async uploadBatchUpdate (formdata) {
      try {
        await this.$store.dispatch('finishedProduct/uploadBatchUpdate', formdata)
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.uploadLoading = false
      }
    },
    async handleDelete (record) {
      try {
        await this.$store.dispatch('finishedProduct/destroyPlan', record.id)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
    },
    async handleDownloadTemplate () {
      try {
        this.$store.dispatch('file/downloadByFilename', '打包计划.xlsx')
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async handleGenTask () {
      try {
        const date = this.moveDate
        const stockMovementDateStart = date.length > 0 ? date[0].format('YYYY-MM-DD 00:00:00') : undefined
        const stockMovementDateEnd = date.length > 0 ? date[1].format('YYYY-MM-DD 23:59:59') : undefined

        this.genTaskLoading = true
        await this.$store.dispatch('finishedProduct/genTask', { stockMovementDateStart, stockMovementDateEnd })

        this.queryForm.pageNum = 1
        this.loadTableList()

        this.$message.success('打包任务生成成功')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.genTaskLoading = false
      }
    },
    async handleUpload (e) {
      const { file } = e

      const formdata = new FormData()

      try {
        formdata.append('file', file)

        this.uploadLoading = true
        await this.$store.dispatch('finishedProduct/planImport', formdata)

        this.queryForm.pageNum = 1
        this.loadTableList()

        this.$message.success('导入成功！')
      } catch (error) {
        if (error.code === 400) {
          this.$confirm({
            title: '导入出错',
            content: error.message + '，是否全部删除',
            onOk: () => {
              this.uploadBatchUpdate(formdata)
            },
            onCancel () {
            }
          })
        } else {
          this.$message.error(error.message)
          this.uploadLoading = false
        }
      } finally {
        this.uploadLoading = false
      }
    },
    async loadTableList () {
      try {
        this.tableLoading = true
        const { date = [] } = this.queryForm
        const stockMovementDateStart = date.length > 0 ? date[0].format('YYYY-MM-DD 00:00:00') : undefined
        const stockMovementDateEnd = date.length > 0 ? date[1].format('YYYY-MM-DD 23:59:59') : undefined
        const options = { ..._.omit(this.queryForm, ['date']), stockMovementDateStart, stockMovementDateEnd }
        const {
          data: { rows, total }
        } = await this.$store.dispatch('finishedProduct/getAllPlanList', options)
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
