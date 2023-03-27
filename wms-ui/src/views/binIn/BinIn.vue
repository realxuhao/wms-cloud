<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="工厂编码">
              <a-input v-model="queryForm.plantNb" placeholder="工厂编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="仓库编码">
              <a-input v-model="queryForm.wareCode" placeholder="仓库编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="SSCC码">
              <a-input v-model="queryForm.ssccNumber" placeholder="SSCC码" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="批次号">
              <a-input v-model="queryForm.batchNb" placeholder="批次号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="状态">
              <a-select
                allow-clear
                v-model="queryForm.status"
              >
                <a-select-option v-for="item in status" :key="item.value" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <template v-if="advanced">
            <a-col :span="4">
              <a-form-model-item label="物料编码">
                <a-input v-model="queryForm.materialNb" placeholder="物料编码" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="托盘编码">
                <a-input v-model="queryForm.palletCode" placeholder="托盘编码" allow-clear/>
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
        rowKey="ssccNumber"
        :pagination="false"
        size="middle"
        :scroll="tableScroll"
      >
        <template slot="status" slot-scope="text">
          <div >
            <a-tag color="orange" v-if="text===0">
              待上架
            </a-tag>
            <a-tag color="#87d068" v-if="text===1">
              已上架
            </a-tag>
            <a-tag color="#666666" v-else>
              入库待上架
            </a-tag>
          </div>
        </template>
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a-popconfirm title="确认要删除吗?" ok-text="确认" cancel-text="取消" @confirm="handleDelete(record)">
              <a class="danger-color" :disabled="!record.status||record.status===1"><a-icon class="m-r-4" type="delete" />删除</a>
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

  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'

const columns = [
  {
    title: '工厂编码',
    key: 'plantNb',
    dataIndex: 'plantNb',
    width: 80
  },
  {
    title: '仓库编码',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 80
  },
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 160
  },
  {
    title: '物料编码',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 120
  },
  {
    title: '物料名称',
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
    title: '托盘编码',
    key: 'palletCode',
    dataIndex: 'palletCode',
    width: 90
  },
  {
    title: '托盘类型',
    key: 'palletType',
    dataIndex: 'palletType',
    width: 70
  },
  {
    title: '数量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 80
  },
  {
    title: '推荐库位编码',
    key: 'recommendBinCode',
    dataIndex: 'recommendBinCode',
    width: 120
  },
  {
    title: '实际库位编码',
    key: 'actualBinCode',
    dataIndex: 'actualBinCode',
    width: 120
  },
  {
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    width: 120,
    scopedSlots: { customRender: 'status' }
  },
  {
    title: '入库人',
    key: 'receiveBy',
    dataIndex: 'receiveBy',
    width: 80
  },
  {
    title: '上架人',
    key: 'updateBy',
    dataIndex: 'updateBy',
    width: 80
  },
  {
    title: '上架时间',
    key: 'updateTime',
    dataIndex: 'updateTime',
    width: 80
  },
  {
    title: '操作',
    key: 'action',
    width: 120,
    fixed: 'right',
    scopedSlots: { customRender: 'action' }
  }
]

const queryFormAttr = () => {
  return {
    plantNb: '',
    wareCode: '',
    ssccNumber: '',
    materialNb: '',
    batchNb: '',
    status: ''
  }
}
const status = [
  {
    text: '待上架',
    value: 0
  },
  {
    text: '已上架',
    value: 1
  },
  {
    text: '入库待上架',
    value: 3
  }
]

export default {
  name: 'BinIn',
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
  computed: {
    status: () => status
  },

  methods: {
    async handleDelete (row) {
      try {
        await this.$store.dispatch('binIn/delete', row.ssccNumber)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const {
          data: { rows, total }
        } = await this.$store.dispatch('binIn/getPaginationList', this.queryForm)
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
