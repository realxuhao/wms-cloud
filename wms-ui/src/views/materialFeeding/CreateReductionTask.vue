<template>
  <a-drawer
    width="70%"
    title="创建物料需求"
    placement="right"
    :closable="false"
    :visible="createReductionTaskVisible"
    @close="onClose"
  >
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="批次号">
              <a-input v-model="queryForm.batchNb" placeholder="批次号" allow-clear/>
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
        <a-button
          type="primary"
          :disabled="!hasSelected"
          @click="handleSubmit"
          :loading="submitLoading">提交并生成拣配任务 </a-button>
        <div class="number-content">
          <span class="m-r-8"><span class="primary-color">未下发量：</span>{{ notQuantity }}</span>
          <span> <span class="primary-color">已选择量：</span>{{ stockNumber }}</span>
        </div>
      </div>
      <a-table
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        size="middle"
        @change="handleChangeTab"
      >
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
  </a-drawer>
</template>

<script>
// import _ from 'lodash'
import { mixinTableList } from '@/utils/mixin/index'

const queryFormAttr = () => {
  return {
    'batchNb': ''
  }
}

export default {
  mixins: [mixinTableList],
  props: {
    materialNb: {
      type: String,
      default () {
        return ''
      }
    },
    notQuantity: {
      type: Number,
      required: true
    },
    createReductionTaskVisible: {
      type: Boolean,
      default () {
        return false
      }
    }
  },
  data () {
    return {
      submitLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        sortMap: {
          availableStock: 'ascend',
          batchNb: 'ascend'
        },
        ...queryFormAttr()
      },

      selectedRowKeys: [],
      list: [],

      stockNumber: 0
    }
  },
  model: {
    prop: 'createReductionTaskVisible',
    event: 'change'
  },
  computed: {
    hasSelected () {
      return this.selectedRowKeys.length > 0
    },
    columns () {
      const { sortMap } = this.queryForm
      const columns = [
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
          width: 160
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
          width: 120,
          sorter: true,
          sortDirections: ['descend', 'ascend'],
          sortOrder: sortMap.batchNb
        },
        {
          title: '可用库存',
          key: 'availableStock',
          dataIndex: 'availableStock',
          width: 120,
          sorter: true,
          sortDirections: ['descend', 'ascend'],
          sortOrder: sortMap.availableStock
        },
        {
          title: '冻结库存',
          key: 'freezeStock',
          dataIndex: 'freezeStock',
          width: 120
        },
        {
          title: '库存量',
          key: 'totalStock',
          dataIndex: 'totalStock',
          width: 120
        }
      ]
      return columns
    }
  },
  methods: {
    onClose () {
      this.$emit('change', false)
      this.selectedRowKeys = []
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    onSelectChange (selectedRowKeys) {
      console.log(selectedRowKeys)
      this.selectedRowKeys = selectedRowKeys
    },
    async handleSubmit () {
      try {
        this.submitLoading = true
        const options = { callIds: this.selectedRowKeys }
        const data = await this.$store.dispatch('materialFeeding/checkStock', options)
        console.lg(data)
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.submitLoading = false
      }
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const {
          data: { rows, total }
        } = await this.$store.dispatch('materialFeeding/getRuleList', this.queryForm)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    loadData () {
      this.loadTableList()
    },
    handleChangeTab (p, f, { field, order }) {
      this.queryForm.sortMap[field] = this.queryForm.sortMap[field] === 'descend' ? 'ascend' : 'descend'
    }
  },
  watch: {
    materialNb: function (val) {
      if (val) {
        this.queryForm.materialNb = val
        this.loadData()
      }
    }
  }
}
</script>

<style lang="less" scoped>
.action{
  position: absolute;
  right: 0;
  bottom: 0;
  width: 100%;
  border-top: 1px solid #e9e9e9;
  padding: 10px 16px;
  background: #fff;
  text-align: right;
  z-index: 1;
}
/deep/.ant-drawer-body{
  // overflow-y: auto;
  padding-bottom: 60px;
}

/deep/.ant-input-number{
  width: 100%;
}

.number-content{
  display: inline-block;
  margin-left: 20px;
}
</style>
