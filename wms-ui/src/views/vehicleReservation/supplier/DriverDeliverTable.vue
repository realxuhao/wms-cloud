<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="司机姓名">
              <a-input v-model="queryForm.driverName" placeholder="司机姓名" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col span="4">
            <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
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
        :scroll="{ x: 1300 }"
      >
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a-popconfirm
              title="确认要删除吗?"
              ok-text="确认"
              cancel-text="取消"
              @confirm="handleDelete(record)"
            >
              <a class="danger-color"><a-icon class="m-r-4" type="delete" />删除</a>
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
          @change="changePagination" />
      </div>

    </div>
  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'

const columns = [
  {
    title: '司机姓名',
    key: 'driverName',
    dataIndex: 'driverName',
    width: 200
  },
  {
    title: '是否加入黑名单',
    key: 'status',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
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
  },
  {
    title: '创建人',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 200
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
    driverName: ''
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
    async handleDelete (record) {
      // try {
      //   await this.$store.dispatch('blackDriver/destroy', record.driverId)
      //   this.$message.success('删除成功！')

      //   this.loadTableList()
      // } catch (error) {
      //   console.log(error)
      //   this.$message.error('删除失败，请联系系统管理员！')
      // }
    },
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
</style>
