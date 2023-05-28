<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">

      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="订单PO号">
              <a-input v-model="queryForm.poNo" placeholder="订单PO号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="订单行号">
              <a-input v-model="queryForm.poItem" placeholder="订单行号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="供应商名称">
              <a-input v-model="queryForm.supplier" placeholder="供应商名称" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="料号">
              <a-input v-model="queryForm.sapCode" placeholder="料号" allow-clear/>
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
      <div class="sync-div">
        <a-button type="primary" class="m-r-8" @click="handleSync" :loading="syncLoading" v-hasPermi="['purchase:order:sync']">
          <a-icon type="redo"></a-icon>
          同步数据
        </a-button>
        <span>共 {{ paginationTotal }} 条</span>
      </div>
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="purchaseId"
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
        <template slot="remark" slot-scope="reText">
          <a-tooltip color="'purple'" :title="reText">
            <span>{{ reText ? reText.length > 10 ? reText.substring(0,10) + "..." : reText : "" }}</span>
          </a-tooltip>
        </template>
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a v-hasPermi="['purchase:order:details']" class="primary-color" @click="handleDetails(record.purchaseId)"><a-icon class="m-r-4" type="table" />跟踪</a>
            <a-divider type="vertical" />
            <a-popconfirm
              v-if="record.status == 0"
              title="确认要完成吗?"
              ok-text="确认"
              cancel-text="取消"
              @confirm="record.status == 0 && handleComplete(record)"
            >
              <a v-hasPermi="['purchase:order:complete']" style="color: green"><a-icon class="m-r-4" type="check-circle" />完成</a>
            </a-popconfirm>
            <a v-hasPermi="['purchase:order:complete']" v-else class="not-danger-color" ><a-icon class="m-r-4" type="check-circle" />完成</a>
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
          @change="changePagination" />
      </div>

    </div>

    <a-modal
      v-drag-modal
      v-model="isVisibleDetails"
      title="订单预约信息跟踪"
      :width="1200"
      @cancel="closeDetails">
      <DetailsTable
        :isVisible="isVisibleDetails"
        :purchaseId="purchaseId"
      />
      <template slot="footer">
        <a-button @click="closeDetails">关闭</a-button>
      </template>
    </a-modal>
  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'
import DetailsTable from './DetailsTable.vue'

const columns = [
  {
    title: '订单PO号',
    key: 'poNo',
    dataIndex: 'poNo',
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
    key: 'supplier',
    dataIndex: 'supplier',
    width: 200
  },
  {
    title: '料号',
    key: 'sapCode',
    dataIndex: 'sapCode',
    width: 150
  },
  {
    title: '物料名称',
    key: 'sapName',
    dataIndex: 'sapName',
    width: 200
  },
  {
    title: '需求数量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 130
  },
  {
    title: '单位',
    key: 'unit',
    dataIndex: 'unit',
    width: 80
  },
  {
    title: '预计到货日期',
    key: 'deliveryDate',
    dataIndex: 'deliveryDate',
    width: 110
  },
  {
    title: '需求放行日期',
    key: 'releaseDate',
    dataIndex: 'releaseDate',
    width: 110
  },
  {
    title: '首批变更号',
    key: 'firstBatchChangeNo',
    dataIndex: 'firstBatchChangeNo',
    width: 100
  },
  {
    title: '海关台帐号',
    key: 'cmsNumber',
    dataIndex: 'cmsNumber',
    width: 120
  },
  {
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    width: 100
  },
  {
    title: '备注',
    key: 'remark',
    dataIndex: 'remark',
    width: 200,
    scopedSlots: { customRender: 'remark' }
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 120
  },
  {
    title: '修改时间',
    key: 'updateTime',
    dataIndex: 'updateTime',
    width: 120
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    width: 150,
    scopedSlots: { customRender: 'action' }
  }
]

const queryFormAttr = () => {
  return {
    poNo: '',
    poItem: '',
    supplier: '',
    sapCode: ''
  }
}

export default {
  name: 'VrPurchase',
  mixins: [mixinTableList],
  components: {
    DetailsTable
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
      /** 预约单详情弹窗是否显示 */
      isVisibleDetails: false,
      syncLoading: false,
      purchaseId: 0

    }
  },
  methods: {
    /** 查看该预约单详情(查看对应po信息) */
    handleDetails (purchaseId) {
      this.isVisibleDetails = true
      this.purchaseId = purchaseId
    },
    /** 关闭详情页弹窗 */
    closeDetails () {
      this.isVisibleDetails = false
    },
    /** 完成动作 */
    async handleComplete (record) {
      try {
        await this.$store.dispatch('purchase/close', record.purchaseId)
        this.$message.success('已完成！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('完成失败，请联系系统管理员！')
      }
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    /** 从物料管理系统中同步数据 */
    async handleSync () {
      this.syncLoading = true
      const data = await this.$store.dispatch('purchase/syncdata')
      if (data.code === 200) {
        this.$message.success('同步完成')
      } else {
        this.$message.error('同步失败，请联系系统管理员！')
      }
      this.syncLoading = false
    },
    /** 查询采购订单列表 */
    async loadTableList () {
      try {
        this.tableLoading = true

        const { data: { rows, total } } = await this.$store.dispatch('purchase/getList', this.queryForm)
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
.not-danger-color{
  color: #aaa
}
.sync-div{
  margin-bottom: 10px;
}
</style>
