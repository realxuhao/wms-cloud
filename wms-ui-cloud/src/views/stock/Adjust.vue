<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="Cell">
              <a-select allow-clear v-model="queryForm.cell">
                <a-select-option v-for="item in departmentList" :key="item.id" :value="item.code">
                  {{ item.code }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="工厂编码">
              <a-input v-model="queryForm.plantNb" placeholder="工厂编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="仓库编码">
              <a-input v-model="queryForm.wareCode" placeholder="仓库编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="存储区编码">
              <a-input v-model="queryForm.areaCode" placeholder="仓库编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="库位编码">
              <a-input v-model="queryForm.binCode" placeholder="库位编码" allow-clear/>
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
          <template v-if="advanced">
            <!-- <a-col :span="4">
              <a-form-item label="托盘编码">
                <a-input v-model="queryForm.palletCode" placeholder="托盘编码" allow-clear/>
              </a-form-item>
            </a-col> -->
            <a-col :span="4">
              <a-form-item label="SSCC码">
                <a-input v-model="queryForm.ssccNumber" placeholder="SSCC码" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="物料编码" >
                <a-input v-model="queryForm.materialNb" placeholder="物料编码" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-model-item label="批次号">
                <a-input v-model="queryForm.batchNb" placeholder="批次号" allow-clear/>
              </a-form-model-item>
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
              <a-form-item label="操作人">
                <a-input v-model="queryForm.operateUser" placeholder="操作人" allow-clear/>
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
        @change="pageChange"
      >
        <template slot="adjustType" slot-scope="text">
          <div >
            <a-tag color="orange" v-if="text===0">
              领用
            </a-tag>
            <a-tag color="blue" v-if="text===1">
              报废
            </a-tag>
            <a-tag color="#87d068" v-if="text===2">
              其它
            </a-tag>
            <a-tag color="#895256" v-if="text===3">
              玻璃瓶配送到产线
            </a-tag>
          </div>
        </template>
      </a-table>

      <div class="pagination-con">
        <a-pagination
          :show-total="total =>`总共${total}条`"
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
import _ from 'lodash'
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
    text: '领用',
    value: 0
  },
  {
    text: '报废',
    value: 1
  },
  {
    text: '其它',
    value: 2
  },
  {
    text: '玻璃瓶配送到产线',
    value: 3
  }
]
const columns = [
  {
    title: '工厂编码',
    key: 'plantNb',
    dataIndex: 'plantNb',
    width: 120,
    sorter: true
  },
  {
    title: 'Cell',
    key: 'cell',
    dataIndex: 'cell',
    width: 100,
    sorter: true
  },
  {
    title: '仓库编码',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 120,
    sorter: true
  },
  {
    title: '存储区编码',
    key: 'areaCode',
    dataIndex: 'areaCode',
    width: 120,
    sorter: true
  },
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 140
  },
  {
    title: '跨编码',
    key: 'frameCode',
    dataIndex: 'frameCode',
    width: 140
  },
  {
    title: '库位编码',
    key: 'binCode',
    dataIndex: 'binCode',
    width: 140,
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
    title: '批次号',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 120,
    sorter: true
  },
  {
    title: '质检状态',
    key: 'qualityStatus',
    dataIndex: 'qualityStatus',
    width: 80,
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
    width: 120
  },
  {
    title: '冻结库存',
    key: 'freezeStock',
    dataIndex: 'freezeStock',
    width: 120
  },
  {
    title: '可用库存',
    key: 'availableStock',
    dataIndex: 'availableStock',
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
    title: '类型',
    key: 'type',
    dataIndex: 'type',
    scopedSlots: { customRender: 'adjustType' },
    width: 80
  },
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
    qualityStatus: '',
    operateUser:'',
    adjustType:''
  }
}

export default {
  name: 'Adjust',
  mixins: [mixinTableList],
  data () {
    return {
      tableLoading: false,
      uploadLoading: false,
      exportLoading: false,
      departmentList: [],
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: []
    }
  },
  computed: {
    qualityStatus: () => qualityStatus,
    adjustTypes:()=>adjustTypes
  },
  methods: {
    async pageChange(page, filters, sorter){
        this.queryForm.isAsc= sorter.order === 'ascend' ? 'asc' : 'desc'
        this.queryForm.orderByColumn= sorter.columnKey
        this.loadTableList()
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async loadDepartmentList () {
      const departmentList = await this.$store.dispatch('materialFeeding/getDepartmentList')
      this.departmentList = departmentList
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const {
          data: { rows, total }
        } = await this.$store.dispatch('stock/getPaginationAdjustList', this.queryForm)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async loadData () {
      this.loadDepartmentList()
      this.loadTableList()
    },
    async handleDownload () {
      try {
        this.exportLoading = true
        // this.queryForm.pageSize = 0
        const blobData = await this.$store.dispatch('stock/exportAdjustExcel', this.queryForm)
        download(blobData, '库存调整记录.xlsx')
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      } finally {
        this.exportLoading = false
      }
    },
  },
  mounted () {
    this.loadData()
  }
}
</script>

<style lang="less" scoped>
</style>
