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
          <template v-if="advanced">

          </template>
          <a-col span="4">
            <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
          </a-col>
        </a-row>
      </a-form>

      <div class="action-content">
        <a-button type="primary" class="m-r-8" icon="plus" @click="handleAdd">
          新建
        </a-button>
      </div>
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="driverId"
        :pagination="false"
        size="middle"
        :scroll="{ x: 1300 }"
      >
        <template slot="status" slot-scope="text">
          <div >
            <a-tag color="green" v-if="text===0">
              否
            </a-tag>
            <a-tag color="red" v-if="text===1">
              是
            </a-tag>
          </div>
        </template>
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
      :driverId="currentUpdateId"
      @on-ok="loadTableList"
    ></UpdateDrawer>
  </div>
</template>

<script>
import UpdateDrawer from './UpdateDrawer'
import Sortable from 'sortablejs'

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
  name: 'VrBlackDriver',
  mixins: [mixinTableList],
  components: {
    UpdateDrawer
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
      list: []

    }
  },
  methods: {
    rowDrop () {
      const tbody = document.querySelector('.ant-table-tbody') // 元素选择器名称根据实际内容替换
      const _this = this
      Sortable.create(tbody, {
        // 官网上的配置项,加到这里面来,可以实现各种效果和功能
        animation: 150,
        ghostClass: 'blue-background-class',
        onEnd ({ newIndex, oldIndex }) {
          const currRow = _this.list.splice(oldIndex, 1)[0]
          _this.list.splice(newIndex, 0, currRow)
        }
      })
    },
    async handleDelete (record) {
      try {
        await this.$store.dispatch('blackDriver/destroy', record.driverId)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
    },
    handleEdit (record) {
      this.updateType = 'edit'
      this.visible = true
      this.currentUpdateId = record.driverId
    },
    async loadTableList () {
      console.info(this.list)
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
    // this.rowDrop()
  }
}
</script>

<style lang="less" scoped>
</style>
