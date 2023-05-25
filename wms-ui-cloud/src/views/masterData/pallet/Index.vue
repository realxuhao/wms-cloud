<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="托盘前缀编码">
              <a-input v-model="queryForm.virtualPrefixCode" placeholder="托盘前缀编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <template v-if="advanced">

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
        :scroll="tableScroll"
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
        <template slot="isVirtual" slot-scope="text">
          <span>{{ !text?'实物':'虚拟' }}</span>
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
import { mixinTableList } from '@/utils/mixin/index'

const columns = [
  {
    title: '托盘长度[cm]',
    key: 'length',
    dataIndex: 'length',
    width: 200
  },
  {
    title: '托盘宽度[cm]',
    key: 'width',
    dataIndex: 'width',
    width: 200
  },
  {
    title: '托盘高度[cm]',
    key: 'height',
    dataIndex: 'height',
    width: 200
  },
  {
    title: '托盘类型',
    key: 'type',
    dataIndex: 'type',
    width: 200
  },
  {
    title: '是否有托盘码',
    key: 'isVirtual',
    dataIndex: 'isVirtual',
    width: 140,
    scopedSlots: { customRender: 'isVirtual'
    }
  },
  {
    title: '托盘前缀编码',
    key: 'virtualPrefixCode',
    dataIndex: 'virtualPrefixCode',
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

const queryFormAttr = () => {
  return {
    code: '',
    name: '',
    type: ''
  }
}

export default {
  name: 'Pallet',
  mixins: [mixinTableList],
  components: {
    UpdateDrawer
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
    async handleDelete (record) {
      try {
        await this.$store.dispatch('pallet/destroy', record.id)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
    },

    async loadTableList () {
      try {
        this.tableLoading = true

        const { data: { rows, total } } = await this.$store.dispatch('pallet/getPaginationList', this.queryForm)
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
