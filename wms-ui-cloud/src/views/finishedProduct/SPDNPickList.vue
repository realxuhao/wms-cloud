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
            <a-form-item label="存储区">
              <a-input v-model="queryForm.areCode" placeholder="存储区" allow-clear/>
            </a-form-item>
          </a-col>
          
         
          <a-col :span="4">
            <a-form-model-item label="状态">
              <a-select
                placeholder="状态"
                allow-clear
                v-model="queryForm.status"
              >
                <a-select-option v-for="(value,key) in statusTextMap" :key="key" :value="value">
                  {{ value }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
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
      >
        <template slot="statusSlot" slot-scope="text">
          <a-tag :color="colorMap[text]">
            {{ statusTextMap[text] }}
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
const columns = [
  {
    title: 'plantNb',
    key: 'plantNb',
    dataIndex: 'plantNb',
    width: 120
  },
  {
    title: 'ssccNumber',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 180
  },
  {
    title: '物料编码',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 200
  },
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 200
  },
  {
    title: '仓库',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 120
  },
  {
    title: '存储区',
    key: 'areaCode',
    dataIndex: 'areaCode',
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
    title: '过期日期',
    key: 'expireDate',
    dataIndex: 'expireDate',
    width: 120
  },
  {
    title: '库位',
    key: 'binCode',
    dataIndex: 'binCode',
    width: 120
  },
  {
    title: '总库存',
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
    title: 'Unit',
    key: 'unit',
    dataIndex: 'unit',
    width: 120
  },
  {
    title: 'FromProdOrder',
    key: 'fromProdOrder',
    dataIndex: 'fromProdOrder',
    width: 120
  },
  {
    title: '创建人',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 120
  },
  {
    title: '入库时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 200
  },

  {
    title: 'cell',
    key: 'cell',
    dataIndex: 'cell',
    width: 200
  }
]

const colorMap = {
  '-1': '',
  '0': 'orange',
  '1': 'orange',
  '2': 'blue',
}

const statusTextMap = {
  '-1': '已取消',
  '0': '待下架',
  '1': '待发运',
  '2': '完成',
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
    status:undefined,
    date: []
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
    statusTextMap:()=>statusTextMap,
    colorMap: () => colorMap,
    hasSelected () {
      return this.selectedRowKeys.length > 0
    }
  },
  methods: {
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
        } = await this.$store.dispatch('finishedProduct/spdnPicklist', options)
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
