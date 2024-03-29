<template>
  <div class="wrapper">
    <div class="table-content">
      <div class="action-content">
        <a-button type="primary" icon="plus" @click="handleAdd">
          新建
        </a-button>
      </div>
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        size="middle"
        :scroll="{ x: 1300 }"
      >
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a class="warning-color" @click="handleEdit(record)"><a-icon class="m-r-4" type="edit" />编辑</a>
            <a-divider type="vertical" />
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

    <UpdateDrawer
      v-model="visible"
      :updateType="updateType"
      :id="currentUpdateId"
      @on-ok="loadTableList"
    ></UpdateDrawer>
  </div>
</template>

<script>
import UpdateDrawer from './UpdateDrawer'

const columns = [
  {
    title: '时间窗口起',
    key: 'startTime',
    dataIndex: 'startTime',
    width: 200
  },
  {
    title: '时间窗口止',
    key: 'endTime',
    dataIndex: 'endTime',
    width: 200
  },
  {
    title: '道口',
    key: 'windowCode',
    dataIndex: 'windowCode',
    width: 200
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
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

export default {
  name: 'TimeWindow',
  components: {
    UpdateDrawer
  },
  data () {
    return {
      visible: false,
      updateType: 'add', // edit、add
      currentUpdateId: 0,

      searchLoading: false,
      queryForm: {
        name: '',
        code: '',
        pageSize: 20,
        pageNum: 1
      },
      paginationTotal: 0,

      tableLoading: false,
      columns,
      list: []
    }
  },
  methods: {
    onShowSizeChange () {
      this.queryForm.pageNum = 1
      this.loadTableList()
    },
    changePagination (page) {
      this.queryForm.pageNum = page
      this.loadTableList()
    },

    async handleSearch () {
      this.searchLoading = true
      await this.loadTableList()
      this.searchLoading = false
    },

    handleEdit (record) {
      this.updateType = 'edit'
      this.visible = true
      this.currentUpdateId = record.id
    },
    async handleDelete (record) {
      try {
        await this.$store.dispatch('timeWindow/destroy', record.id)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
    },

    handleAdd () {
      this.updateType = 'add'
      this.visible = true
      this.currentUpdateId = null
    },

    async loadTableList () {
      try {
        this.tableLoading = true

        const { rows, total } = await this.$store.dispatch('timeWindow/getList', this.queryForm)
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
