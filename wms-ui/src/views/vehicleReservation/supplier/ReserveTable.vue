<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="采购订单号">
              <a-input v-model="queryForm.reserveNo" placeholder="采购订单号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="预约状态">
              <a-select
                allow-clear
                show-search
                v-model="queryForm.status"
                style="width: 100%"
                placeholder="预约状态">
                <a-select-option v-for="item in statusList" :key="item.value" :value="item.value">
                  {{ item.label }}</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col span="4">
            <span class="table-page-search-submitButtons" >
              <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>

      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="reserveId"
        :pagination="false"
        size="middle"
        :scroll="tableScroll"
      >
        <template slot="statusDes" slot-scope="text, record">
          <div >
            <a-tag color="blue" v-if="record.status===0">
              {{ text }}
            </a-tag>
            <a-tag color="orange" v-if="record.status===1">
              {{ text }}
            </a-tag>
            <a-tag color="cyan" v-if="record.status===2">
              {{ text }}
            </a-tag>
            <a-tag color="green" v-if="record.status===3">
              {{ text }}
            </a-tag>
          </div>
        </template>
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a class="primary-color" @click="handleDetails(record.reserveNo)"><a-icon class="m-r-4" type="table" />详情</a>
            <a-divider type="vertical" />
            <a-popconfirm
              v-if="record.status == 0"
              title="确认要删除吗?"
              ok-text="确认"
              cancel-text="取消"
              @confirm="record.status == 0 && handleDelete(record)"
            >
              <a class="danger-color"><a-icon class="m-r-4" type="delete" />删除</a>
            </a-popconfirm>
            <!-- 状态不为0(即该供应商预约单未被司机预约或签到)时，不能删除，按钮灰化 -->
            <a v-if="record.status != 0" class="not-danger-color"><a-icon class="m-r-4" type="delete" />删除</a>
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
      title="预约单详情"
      :width="1200"
      @cancel="closeDetails">
      <ReserveDetailsTable
        :isVisible="isVisibleDetails"
        :reserveNo="reserveNo"
      />
      <template slot="footer">
        <a-button @click="closeDetails">关闭</a-button>
      </template>
    </a-modal>
  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'
import ReserveDetailsTable from './ReserveDetailsTable.vue'

const columns = [
  {
    title: '预约单号',
    key: 'reserveNo',
    dataIndex: 'reserveNo',
    width: 150,
    fixed: 'left'
  },
  {
    title: '仓库名称',
    key: 'wareName',
    dataIndex: 'wareName',
    width: 150
  },
  {
    title: '仓库联系人',
    key: 'wareUser',
    dataIndex: 'wareUser',
    width: 150
  },
  {
    title: '联系人电话',
    key: 'wareUserPhone',
    dataIndex: 'wareUserPhone',
    width: 150
  },
  {
    title: '仓库地址',
    key: 'wareLocation',
    dataIndex: 'wareLocation',
    width: 200
  },
  {
    title: '预约日期',
    key: 'reserveDate',
    dataIndex: 'reserveDate',
    width: 120
  },
  {
    title: '预约时间',
    key: 'timeWindow',
    dataIndex: 'timeWindow',
    width: 120
  },
  {
    title: '预约状态',
    key: 'statusDes',
    dataIndex: 'statusDes',
    scopedSlots: { customRender: 'statusDes' },
    width: 150
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 150
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
    reserveNo: '',
    status: null
  }
}

export default {
  name: 'VrSupplierReserve',
  mixins: [mixinTableList],
  components: {
    ReserveDetailsTable
  },
  props: {
    isVisible: {
      type: Boolean,
      default () {
        return false
      }
    },
    supplierName: {
      type: String,
      default () {
        return ''
      }
    },
    reloadReserve: {
      type: Boolean,
      default () {
        return false
      }
    }
  },
  data () {
    return {
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      statusList: [
        { value: 0, label: '已预约' },
        { value: 1, label: '在途（司机已预约）' },
        { value: 2, label: '已到货（司机现场签到）' },
        { value: 3, label: '已完成' }
      ],
      columns,
      list: [],
      detailsList: [],
      /** 预约单详情弹窗是否显示 */
      isVisibleDetails: false,
      reserveNo: ''

    }
  },
  methods: {
    /** 查看该预约单详情(查看对应po信息) */
    handleDetails (reserveNo) {
      this.isVisibleDetails = true
      this.reserveNo = reserveNo
    },
    /** 关闭详情页弹窗 */
    closeDetails () {
      this.isVisibleDetails = false
    },
    /** 删除供应商预约订单 */
    async handleDelete (record) {
      try {
        await this.$store.dispatch('supplierReserve/destroy', record.reserveId)
        this.$message.success('删除成功！')
        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
    },
    /** 查询采购订单列表 */
    async loadTableList () {
      if (this.supplierName === '' || this.supplierName === null) {
        return
      }
      try {
        this.tableLoading = true
        const { data: { rows, total } } = await this.$store.dispatch('supplierReserve/getList', { ...{ supplierCode: this.supplierName }, ...this.queryForm })
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
  },
  watch: {
    isVisible (val) {
      if (val && this.supplierName) {
        this.loadData()
      }
    },
    reloadReserve (val) {
      if (this.supplierName) {
        this.loadData()
      }
    }
  }
}
</script>

<style lang="less" scoped>
.not-danger-color{
  color: #aaa
}
</style>
