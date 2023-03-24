<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="Shipping Mark">
              <a-input v-model="queryForm.shippingMark" placeholder="Shipping Mark" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="ETO PO">
              <a-input v-model="queryForm.etoPo" placeholder="ETO PO" allow-clear/>
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
        <template slot="checkType" slot-scope="text">
          <div >
            <a-tag color="orange" v-if="text===0">
              称重
            </a-tag>
            <a-tag color="blue" v-if="text===1">
              数数
            </a-tag>
            <a-tag color="#87d068" v-if="text===2">
              免检
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
    title: '移库日期',
    key: 'stockMovement',
    dataIndex: 'stockMovement',
    width: 140
  },
  {
    title: 'ETO PO',
    key: 'ETOPO',
    dataIndex: 'ETOPO',
    width: 120
  },
  {
    title: 'Shipping Mark',
    key: 'shippingMark',
    dataIndex: 'shippingMark',
    width: 120
  },

  // {
  //   title: '成品料号',
  //   key: 'ETOPLANT',
  //   dataIndex: 'ETOPLANT',
  //   width: 120
  // },

  {
    title: '原托数',
    key: 'country',
    dataIndex: 'country',
    width: 140
  },
  {
    title: '组托数',
    key: 'prodOrder',
    dataIndex: 'prodOrder',
    width: 140
  },
  {
    title: '进度',
    key: 'Qty',
    dataIndex: 'Qty',
    width: 120
  },
  {
    title: '状态',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 120
  }

]

const queryFormAttr = () => {
  return {
    plantNb: '',
    wareCode: '',
    ssccNumber: '',
    materialNb: '',
    batchNb: '',
    areaCode: '',
    binCode: '',
    palletCode: ''
  }
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  data () {
    return {
      tableLoading: false,
      uploadLoading: false,
      genTaskLoading: false,
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
    async handleDownloadTemplate () {
      try {
        this.$store.dispatch('file/downloadByFilename', '打包计划.xlsx')
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async handleGenTask () {
      try {
        this.genTaskLoading = true
        await this.$store.dispatch('finishedProduct/genTask')

        this.queryForm.pageNum = 1
        this.loadTableList()

        this.$message.success('打包任务生成成功')
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.genTaskLoading = false
      }
    },
    async handleUpload (e) {
      const { file } = e

      const formdata = new FormData()

      try {
        formdata.append('file', file)

        this.uploadLoading = true
        await this.$store.dispatch('finishedProduct/planImport', formdata)

        this.queryForm.pageNum = 1
        this.loadTableList()

        this.$message.success('导入成功！')
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.uploadLoading = false
      }
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const {
          data: { rows, total }
        } = await this.$store.dispatch('finishedProduct/getTaskList', this.queryForm)
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
