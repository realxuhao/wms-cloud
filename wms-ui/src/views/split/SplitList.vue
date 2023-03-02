<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="仓库编码">
              <a-input v-model="queryForm.wareCode" placeholder="仓库编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="SSCC">
              <a-input v-model="queryForm.ssccNb" placeholder="拆托SSCC" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="源SSCC">
              <a-input v-model="queryForm.sourceSscc" placeholder="源SSCC" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="物料编码">
              <a-input v-model="queryForm.materialNb" placeholder="物料编码" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="上架区域">
              <a-input v-model="queryForm.targetAreaCode" placeholder="上架区域" allow-clear/>
            </a-form-model-item>
          </a-col>

          <template v-if="advanced">
            <a-col :span="4">
              <a-form-model-item label="上架库位">
                <a-input v-model="queryForm.targetBinCode" placeholder="上架库位" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-model-item label="源区域">
                <a-input v-model="queryForm.sourceAreaCode" placeholder="源区域" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="源库位">
                <a-input v-model="queryForm.sourceBinCode" placeholder="源库位" allow-clear/>
              </a-form-item>
            </a-col>
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
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 50
  },
  {
    title: 'SSCC',
    key: 'ssccNb',
    dataIndex: 'ssccNb',
    width: 90
  },

  {
    title: '物料编码',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 80
  },
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 160
  },
  {
    title: '拆托数量',
    key: 'splitQuantity',
    dataIndex: 'splitQuantity',
    width: 60
  },
  {
    title: '单位',
    key: 'unit',
    dataIndex: 'unit',
    width: 60
  },
  {
    title: '上架区域',
    key: 'targetAreaCode',
    dataIndex: 'targetAreaCode',
    width: 70
  },
  {
    title: '上架库位',
    key: 'targetBinCode',
    dataIndex: 'targetBinCode',
    width: 70
  },
  {
    title: '源SSCC',
    key: 'sourceSscc',
    dataIndex: 'sourceSscc',
    width: 90
  },
  {
    title: '源总库存',
    key: 'sourceTotalStock',
    dataIndex: 'sourceTotalStock',
    width: 80
  },
  {
    title: '源库位',
    key: 'sourceBinCode',
    dataIndex: 'sourceBinCode',
    width: 80
  },
  {
    title: '源存储区',
    key: 'sourceAreaCode',
    dataIndex: 'sourceAreaCode',
    width: 80
  },

  {
    title: '拆托人',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 80
  },
  {
    title: '拆托时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 80
  }
]

const queryFormAttr = () => {
  return {
    wareCode: '',
    ssccNb: '',
    materialNb: '',
    sourceSscc: '',
    targetAreaCode: '',
    targetBinCode: '',
    sourceAreaCode: '',
    sourceBinCode: ''
  }
}

export default {
  name: 'SplitList',
  mixins: [mixinTableList],
  data () {
    return {
      tableLoading: false,
      uploadLoading: false,
      columns,
      list: [],
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      }
    }
  },

  methods: {

    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const {
          data: { rows, total }
        } = await this.$store.dispatch('split/list', this.queryForm)
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
