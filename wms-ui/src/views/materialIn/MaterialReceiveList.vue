<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form-model class="search-content" layout="inline" :model="queryForm">
        <a-form-model-item label="批次号">
          <a-input v-model="queryForm.batchNb" placeholder="批次号" />
        </a-form-model-item>
        <a-form-model-item label="物料名">
          <a-input v-model="queryForm.materialName" placeholder="物料名" />
        </a-form-model-item>
        <a-form-model-item>
          <a-button type="primary" icon="search" @click="handleSearch" :loading="searchLoading"> 搜索 </a-button>
        </a-form-model-item>
      </a-form-model>

      <div class="action-content">
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
        <template slot="status" slot-scope="text">
          <div >
            <a-tag color="orange" v-if="text===0">
              未入库
            </a-tag>
            <a-tag color="#87d068" v-else>
              已入库
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
    title: '仓库',
    key: 'plantNb',
    dataIndex: 'plantNb',
    width: 90
  },
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 200
  },
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 100
  },
  {
    title: '物料编码',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 140
  },
  {
    title: '批次号',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 120
  },
  // {
  //   title: '供应商',
  //   key: 'supplierNb',
  //   dataIndex: 'storageLocation',
  //   width: 200
  // },
  // {
  //   title: '存储区域',
  //   key: 'storageLocation',
  //   dataIndex: 'storageLocation',
  //   width: 200
  // },
  {
    title: '过期时间',
    key: 'expireDate',
    dataIndex: 'expireDate',
    width: 120
  },
  {
    title: '数量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 60
  },
  {
    title: '单位',
    key: 'unit',
    dataIndex: 'unit',
    width: 60
  },
  // {
  //   title: '来源区域',
  //   key: 'fromArea',
  //   dataIndex: 'fromArea',
  //   width: 200
  // },
  {
    title: '来源PO号',
    key: 'fromPurchaseOrder',
    dataIndex: 'fromPurchaseOrder',
    width: 120
  },
  // {
  //   title: '目的分拣区域',
  //   key: 'toPickingArea',
  //   dataIndex: 'toPickingArea',
  //   width: 200
  // },
  {
    title: 'PO行号',
    key: 'poNumberItem',
    dataIndex: 'poNumberItem',
    width: 100
  },
  {
    title: '上传人',
    key: 'uploadUser',
    dataIndex: 'uploadUser',
    width: 120
  },
  {
    title: '上传时间',
    key: 'uploadTime',
    dataIndex: 'uploadTime',
    width: 200
  },
  {
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    width: 100
  }
  // {
  //   title: '来源区域',
  //   key: 'fromArea',
  //   dataIndex: 'fromArea',
  //   width: 200
  // },
  // {
  //   title: '创建时间',
  //   key: 'createTime',
  //   dataIndex: 'createTime',
  //   width: 200
  // }
]

export default {
  name: 'Area',
  mixins: [mixinTableList],
  data () {
    return {
      uploadLoading: false,
      columns,
      list: []
    }
  },
  methods: {
    async uploadBatchUpdate (formdata) {
      try {
        await this.$store.dispatch('materialInList/uploadBatchUpdate', formdata)
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
        await this.$store.dispatch('materialInList/upload', formdata)

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

    async loadTableList () {
      try {
        this.tableLoading = true

        const {
          data: { rows, total }
        } = await this.$store.dispatch('materialInList/getPaginationReceiveList', this.queryForm)
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
