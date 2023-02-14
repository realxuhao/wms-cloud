<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="订单PO号">
              <a-input v-model="queryForm.poCode" placeholder="订单PO号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="订单行号">
              <a-input v-model="queryForm.poItem" placeholder="订单行号" allow-clear/>
            </a-form-model-item>
          </a-col>
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

      <!-- <a-table
        :columns="columns"
        :data-source="list"
        :row-selection="rowSelection"
        :loading="tableLoading"
        rowKey="driverId"
        :pagination="false"
        size="middle"
        :scroll="{ x: 1300 }"
      >
        <template slot="status" slot-scope="text">
          <div >
            <a-tag color="green" v-if="text===0">
              未完成
            </a-tag>
            <a-tag color="red" v-if="text===1">
              已完成
            </a-tag>
          </div>
        </template>
      </a-table> -->

      <div class="pagination-con">
        <a-pagination
          show-size-changer
          show-less-items
          :current="queryForm.pageNum"
          :page-size.sync="queryForm.pageSize"
          :total="paginationTotal"
          @showSizeChange="onShowSizeChange"
          @change="changePagination" />
      </div>
    </div>
  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'

const columns = [
  {
    title: '订单PO号',
    key: 'poCode',
    dataIndex: 'poCode',
    width: 150,
    fixed: 'left'
  },
  {
    title: '订单行号',
    key: 'poItem',
    dataIndex: 'poItem',
    width: 100,
    fixed: 'left'
  },
  {
    title: '供应商名称',
    key: 'supplierName',
    dataIndex: 'supplierName',
    width: 200
  },
  {
    title: '料号',
    key: 'materialCode',
    dataIndex: 'materialCode',
    width: 150
  },
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 150
  },
  {
    title: '需求数量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 100
  },
  {
    title: '单位',
    key: 'unit',
    dataIndex: 'unit',
    width: 100
  },
  {
    title: '预计到货日期',
    key: 'deliveryDate',
    dataIndex: 'deliveryDate',
    width: 180
  },
  {
    title: '需求放行日期',
    key: 'releaseDate',
    dataIndex: 'releaseDate',
    width: 180
  },
  {
    title: '首批变更号',
    key: 'firstBatchChangeNo',
    dataIndex: 'firstBatchChangeNo',
    width: 150
  },
  {
    title: '海关台帐号',
    key: 'cmsNumber',
    dataIndex: 'cmsNumber',
    width: 200
  },
  {
    title: '备注',
    key: 'remark',
    dataIndex: 'remark',
    width: 200
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 200
  }
]

const queryFormAttr = () => {
  return {
    poCode: '',
    poItem: ''
  }
}

export default {
  name: 'vr-supplier-purchase-table',
  mixins: [mixinTableList],
  components: {
    
  },
  props: {
  },
  data () {
    return {
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: [],
      selectedRowKeys: []

    }
  },
  methods: {
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    /** 从物料管理系统中同步数据 */
    handleSync(){

    },
    //#region 点击行事件
    
    //#endregion
    /** 查询采购订单列表 */
    async loadTableList () {
      try {
        this.tableLoading = true

        const { data: { rows, total } } = await this.$store.dispatch('blackDriver/getList', this.queryForm)
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
