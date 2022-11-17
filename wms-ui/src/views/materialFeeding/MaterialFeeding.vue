<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="Cell">
              <a-select
                allow-clear
                v-model="queryForm.cell"
              >
                <a-select-option v-for="item in departmentList" :key="item.id" :value="item.code">
                  {{ item.code }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="生产订单号">
              <a-input v-model="queryForm.orderNb" placeholder="生产订单号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="物料编码">
              <a-input v-model="queryForm.materialNb" placeholder="物料编码" allow-clear/>
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
              <a-form-item label="创建人">
                <a-input v-model="queryForm.createBy" placeholder="创建人" allow-clear/>
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

        <a-button type="primary" icon="add" @click="handleOpenUpload">
          创建物料需求
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
        <template slot="status" slot-scope="text">
          <div >
            <a-tag color="orange" v-if="text===0">
              未执行
            </a-tag>
            <a-tag color="#87d068" v-else>
              已执行
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

      <MaterialFeedingUpload
        v-model="visible"
        @on-ok="loadTableList"
      ></MaterialFeedingUpload>
    </div>

  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'
import MaterialFeedingUpload from './MaterialFeedingUpload'

const columns = [
  {
    title: 'cell',
    key: 'cell',
    dataIndex: 'cell',
    width: 120
  },
  {
    title: '生产订单号',
    key: 'orderNb',
    dataIndex: 'orderNb',
    width: 120
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
    title: '需求量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 120
  },
  {
    title: '配送量',
    key: 'deliveryQuantity',
    dataIndex: 'deliveryQuantity',
    width: 140
  },
  {
    title: '差值',
    key: 'diffQuantity',
    dataIndex: 'diffQuantity',
    width: 140
  },
  {
    title: '单位',
    key: 'unit',
    dataIndex: 'unit',
    width: 140
  },

  {
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    width: 120
  },
  {
    title: '备注',
    key: 'remark',
    dataIndex: 'remark',
    width: 80
  },
  {
    title: '创建人',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 120
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 200
  }
]

const status = [
  {
    text: '未执行',
    value: 0
  },
  {
    text: '已执行',
    value: 1
  }
]

const queryFormAttr = () => {
  return {
    cell: '',
    materialNb: '',
    orderNb: '',
    status: '',
    createBy: ''
  }
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  components: {
    MaterialFeedingUpload
  },
  data () {
    return {
      tableLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: [],
      departmentList: []
    }
  },
  computed: {
    status: () => status
  },
  methods: {
    handleOpenUpload () {
      this.visible = true
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
        } = await this.$store.dispatch('materialFeeding/getPaginationList', this.queryForm)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async loadDepartmentList () {
      const departmentList = await this.$store.dispatch('materialFeeding/getDepartmentList')
      this.departmentList = departmentList
    },
    async loadData () {
      this.loadDepartmentList()
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
