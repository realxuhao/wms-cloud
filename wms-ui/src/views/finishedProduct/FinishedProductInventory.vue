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
            <a-form-item label="库位">
              <a-input v-model="queryForm.binCode" placeholder="库位" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="FromProdOrder">
              <a-input v-model="queryForm.fromProdOrder" placeholder="FromProdOrder" allow-clear/>
            </a-form-item>
          </a-col>
          <template v-if="advanced">
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
            <a-col :span="8">
              <a-form-item label="创建时间" >
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
        <a-button type="primary" class="m-r-8" icon="plus" @click="createTransfer" :disabled="this.selectedRowKeys.length <= 0"> 新建移库 </a-button>
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
    title: '入库人',
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
    title: '质检状态',
    key: 'qualityStatus',
    dataIndex: 'qualityStatus',
    width: 120,
    scopedSlots: { customRender: 'qualityStatusSlot' }
  },
  {
    title: 'cell',
    key: 'cell',
    dataIndex: 'cell',
    width: 200
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
    status: () => status,
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
