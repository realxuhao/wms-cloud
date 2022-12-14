<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="源库位编码">
              <a-input v-model="queryForm.sourceWareCode" placeholder="源库位编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="目的库位编码">
              <a-input v-model="queryForm.targetWareCode" placeholder="目的库位编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="状态">
              <a-input v-model="queryForm.status" placeholder="状态" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col span="4">
            <span class="table-page-search-submitButtons" >
              <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
              <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo" />重置</a-button>
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
        :scroll="{ x: 1300 }"
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
        <template slot="moveType" slot-scope="text">
          <div >
            {{ moveTypeMap[text] }}
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
    title: '源工厂',
    key: 'sourcePlantNb',
    dataIndex: 'sourcePlantNb',
    width: 120
  },
  {
    title: '源仓库',
    key: 'sourceWareCode',
    dataIndex: 'sourceWareCode',
    width: 120
  },
  {
    title: '源存储区',
    key: 'sourceAreaCode',
    dataIndex: 'sourceAreaCode',
    width: 120
  },
  {
    title: '源库位',
    key: 'sourceBinCode',
    dataIndex: 'sourceBinCode',
    width: 120
  },
  {
    title: '移动类型',
    key: 'moveType',
    dataIndex: 'moveType',
    scopedSlots: { customRender: 'moveType' },
    width: 120
  },
  {
    title: 'sscc码',
    key: 'ssccNb',
    dataIndex: 'ssccNb',
    width: 120
  },
  {
    title: '物料号',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 120
  },
  {
    title: '物料名',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 120
  },
  {
    title: '批次号',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 120
  },
  {
    title: 'bbd过期时间',
    key: 'expireDate',
    dataIndex: 'expireDate',
    width: 120
  },
  {
    title: '目的工厂',
    key: 'targetPlant',
    dataIndex: 'targetPlant',
    width: 120
  },
  {
    title: '目的仓库',
    key: 'targetWareCode',
    dataIndex: 'targetWareCode',
    width: 120
  },
  {
    title: '目的存储区code',
    key: 'targetAreaCode',
    dataIndex: 'targetAreaCode',
    width: 120
  },
  {
    title: '目的库位code',
    key: 'targetBinCode',
    dataIndex: 'targetBinCode',
    width: 120
  },
  {
    title: '推荐库位',
    key: 'recommendBinCode',
    dataIndex: 'recommendBinCode',
    width: 120
  },
  {
    title: '操作人',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 120
  },
  {
    title: '操作时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 200
  }
]

const moveTypeMap = {
  0: '收货',
  1: '入库',
  2: '上架',
  3: '生产叫料',
  4: '移库',
  5: '拆托上架'
}

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
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: []
    }
  },
  computed: {
    moveTypeMap: () => moveTypeMap
  },
  methods: {
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async loadTableList () {
      try {
        this.tableLoading = true
        console.log('1ww1')
        const {
          data: { rows, total }
        } = await this.$store.dispatch('wareShift/getPaginationList', this.queryForm)
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
