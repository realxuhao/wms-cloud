<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form-model class="search-content" layout="inline" :model="queryForm">
        <a-form-model-item label="编码">
          <a-input v-model="queryForm.code" placeholder="编码" />
        </a-form-model-item>
        <a-form-model-item label="名称">
          <a-input v-model="queryForm.name" placeholder="名称" />
        </a-form-model-item>
        <a-form-model-item>
          <a-button type="primary" icon="search" @click="handleSearch" :loading="searchLoading"> 搜索 </a-button>
        </a-form-model-item>
      </a-form-model>

      <div class="action-content">
        <a-button type="primary" class="m-r-8" icon="plus" @click="handleAdd"> 新建 </a-button>
        <a-tooltip placement="right">
          <template slot="title">
            <a style="color:#fff" @click="handleDownloadTemplate"><a-icon type="arrow-down" />下载模板</a>
          </template>
          <a-upload
            :file-list="[]"
            name="file"
            :multiple="true"
            :before-upload="()=>false"
            @change="handleUpload"
          >
            <a-button :loading="uploadLoading" type="primary" icon="upload" >
              导入
            </a-button>
          </a-upload>
        </a-tooltip>
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
            <a-popconfirm title="确认要删除吗?" ok-text="确认" cancel-text="取消" @confirm="handleDelete(record)">
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
          @showSizeChange="loadTableList"
          @change="changePagination"
        />
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
    title: '编码',
    key: 'code',
    dataIndex: 'code',
    width: 200
  },
  {
    title: '名称',
    key: 'name',
    dataIndex: 'name',
    width: 200
  },
  {
    title: '跨',
    key: 'frameName',
    dataIndex: 'frameName',
    width: 200
  },
  {
    title: '存储区名称',
    key: 'areaName',
    dataIndex: 'areaName',
    width: 200
  },
  {
    title: '仓库名称',
    key: 'wareName',
    dataIndex: 'wareName',
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
    fixed: 'right',
    scopedSlots: { customRender: 'action' }
  }
]

export default {
  name: 'Bin',
  mixins: [mixinTableList],
  components: {
    UpdateDrawer
  },
  data () {
    return {
      columns,
      list: [],

      uploadLoading: false
    }
  },
  methods: {
    async handleDownloadTemplate () {
      try {
        this.$store.dispatch('file/downloadByFilename', '库位.xlsx')
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async uploadBatchUpdate (formdata) {
      try {
        await this.$store.dispatch('bin/uploadBatchUpdate', formdata)
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.uploadLoading = false
      }
    },
    async handleUpload (e) {
      const { file } = e

      const formdata = new FormData()

      try {
        formdata.append('file', file)

        this.uploadLoading = true
        await this.$store.dispatch('bin/upload', formdata)

        this.currentPage = 1
        this.loadTableList()

        this.uploadLoading = false

        this.$message.success('导入成功！')
      } catch (error) {
        if (error.code === 400) {
          this.$confirm({
            title: '是否更新？',
            content: '存在重复数据',
            onOk: () => {
              this.uploadBatchUpdate(formdata)
            },
            onCancel () {
            }
          })
        } else {
          this.$message.error(error.message)
          this.uploadLoading = false
        }
      }
    },

    async handleDelete (record) {
      try {
        await this.$store.dispatch('bin/destroy', record.id)
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

        const {
          data: { rows, total }
        } = await this.$store.dispatch('bin/getPaginationList', this.queryForm)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error)
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
