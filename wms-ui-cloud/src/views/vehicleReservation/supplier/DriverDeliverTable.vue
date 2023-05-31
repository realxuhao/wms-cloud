<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="预约单号">
              <a-input v-model="queryForm.reserveNo" placeholder="预约单号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="司机姓名">
              <a-input v-model="queryForm.driverName" placeholder="司机姓名" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col span="4">
            <a-form-model-item label="预约日期" style="display: flex;">
              <a-date-picker
                v-model="queryForm.reserveDate"
                format="YYYY-MM-DD"
              />
            </a-form-model-item>
          </a-col>
          <a-col span="4">
            <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
            <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo" />重置</a-button>
          </a-col>
        </a-row>
      </a-form>

      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="deliverId"
        :pagination="false"
        size="middle"
        :scroll="tableScroll"
      >

        <template slot="statusDes" slot-scope="text, record">
          <div >
            <a-tag color="orange" v-if="record.status===0">
              {{ text }}
            </a-tag>
            <a-tag color="green" v-if="record.status===1">
              {{ text }}
            </a-tag>
          </div>
        </template>
        <template slot="lateDes" slot-scope="text, record">
          <div>
            <a-tag color="green" v-if="record.late===0">
              {{ text }}
            </a-tag>
            <a-tag color="red" v-if="record.late===1">
              {{ text }}
            </a-tag>
          </div>
        </template>
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a-popconfirm
              title="确认要取消吗?"
              ok-text="确认"
              cancel-text="取消"
              @confirm="handleDelete(record)"
            >
              <a class="danger-color"><a-icon class="m-r-4" type="delete" />取消</a>
            </a-popconfirm>
            <!-- 状态不为0(即该供应商预约单未被司机预约或签到)时，不能删除，按钮灰化 -->
            <!-- <a class="not-danger-color"><a-icon class="m-r-4" type="delete" />取消</a> -->
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
  </div>
</template>

<script>
import moment from 'moment'
import { mixinTableList } from '@/utils/mixin/index'

const columns = [
  {
    title: '预约单号',
    key: 'reserveNo',
    dataIndex: 'reserveNo',
    width: 150
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
    title: '司机姓名',
    key: 'driverName',
    dataIndex: 'driverName',
    width: 120
  },
  {
    title: '司机联系方式',
    key: 'driverPhone',
    dataIndex: 'driverPhone',
    width: 120
  },
  {
    title: '车牌号',
    key: 'carNum',
    dataIndex: 'carNum',
    width: 120
  },
  {
    title: '是否签到',
    key: 'statusDes',
    dataIndex: 'statusDes',
    scopedSlots: { customRender: 'statusDes' },
    width: 100
  },
  {
    title: '是否迟到',
    key: 'lateDes',
    dataIndex: 'lateDes',
    scopedSlots: { customRender: 'lateDes' },
    width: 100
  },
  {
    title: '签到时间',
    key: 'signinDate',
    dataIndex: 'signinDate',
    width: 150
  },
  {
    title: '操作',
    key: 'action',
    width: 200,
    scopedSlots: { customRender: 'action'
    }
  }
]

const queryFormAttr = () => {
  return {
    reserveNo: '',
    driverName: '',
    reserveDate: null
  }
}

export default {
  name: 'VrDriverDeliverTable',
  mixins: [mixinTableList],
  components: {
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
    reloadDeliver: {
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
      columns,
      list: []

    }
  },
  methods: {
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    moment,
    async handleDelete (record) {
      try {
        await this.$store.dispatch('driverDeliver/destroy', record.deliverId)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      }
    },
    async loadTableList () {
      try {
        this.tableLoading = true
        this.queryForm.reserveDate = this.queryForm.reserveDate == null ? null : moment(new Date(this.queryForm.reserveDate)).format('YYYY-MM-DD')
        this.queryForm = { ...this.queryForm, ...{ supplierName: this.supplierName } }
        const { data: { rows, total } } = await this.$store.dispatch('driverDeliver/getList', this.queryForm)
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
    reloadDeliver (val) {
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
