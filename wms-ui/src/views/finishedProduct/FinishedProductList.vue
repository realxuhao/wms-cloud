<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
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

          <template v-if="advanced">
            <a-col :span="4">
              <a-form-item label="托盘编码">
                <a-input v-model="queryForm.palletCode" placeholder="托盘编码" allow-clear/>
              </a-form-item>
            </a-col>
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
        <template slot="checkType" slot-scope="text">
          <div >
            <a-tag color="orange" v-if="text===0">
              称重
            </a-tag>
            <a-tag color="blue" v-if="text===1">
              数数
            </a-tag>
            <a-tag color="#87d068" v-if="text===2">
              免检
            </a-tag>
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
          @showSizeChange="loadTableList"
          @change="changePagination"
        />
      </div>
    </div>

  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'

const columns = [
  {
    title: 'Shipping Mark',
    key: 'shippingMark',
    dataIndex: 'shippingMark',
    width: 120
  },
  {
    title: 'ETO PO',
    key: 'ETOPO',
    dataIndex: 'ETOPO',
    width: 120
  },
  {
    title: 'ETO PLANT',
    key: 'ETOPLANT',
    dataIndex: 'ETOPLANT',
    width: 120
  },
  {
    title: 'stock movement 移库日期',
    key: 'stockMovement',
    dataIndex: 'stockMovement',
    width: 140
  },
  {
    title: 'Country',
    key: 'country',
    dataIndex: 'country',
    width: 140
  },
  {
    title: 'Prod-order',
    key: 'prodOrder',
    dataIndex: 'prodOrder',
    width: 140
  },
  {
    title: 'Qty',
    key: 'Qty',
    dataIndex: 'Qty',
    width: 120
  },
  {
    title: '是否拆托',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 120
  },
  {
    title: 'TR',
    key: 'TR',
    dataIndex: 'TR',
    width: 80
  },
  {
    title: 'SAP Code',
    key: 'SAPCode',
    dataIndex: 'SAPCode',
    width: 120
  },
  {
    title: 'Pallet Quantity',
    key: 'PalletQuantity',
    dataIndex: 'PalletQuantity',
    width: 120
  },
  {
    title: 'after packing',
    key: 'afterPacking',
    dataIndex: 'afterPacking',
    width: 120
  },
  {
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    width: 120
  },
  {
    title: '进度',
    key: 'process',
    dataIndex: 'process',
    width: 120
  },
  {
    title: '打包人',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 120
  },
  {
    title: '打包完成时间',
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
    palletCode: ''
  }
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  data () {
    return {
      tableLoading: false,
      uploadLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: []
    }
  },
  methods: {
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const {
          data: { rows, total }
        } = await this.$store.dispatch('stock/getPaginationList', this.queryForm)
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
