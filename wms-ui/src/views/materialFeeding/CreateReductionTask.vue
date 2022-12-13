<template>
  <a-drawer
    width="70%"
    title="创建物料需求"
    placement="right"

    :visible="createReductionTaskVisible"
    @close="onClose"
  >
    <div class="table-content">

      <a-steps :current="currentStep" @change="handleStepChange" class="m-b-24">
        <a-step title="选择下发需求" >
        </a-step>
        <a-step title="确认下发需求" />
      </a-steps>
      <a-row>
        <a-col :span="24" v-show="currentStep===0">
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
          <div class="m-b-8">
            <a-button icon="step-forward" class="m-r-8" @click="currentStep = 1">下一步</a-button>
            <span class="m-r-8"><span class="primary-color">未下发量：</span>{{ notQuantity }}</span>
            <span> <span class="primary-color">已选择量：</span>{{ stockNumber }}</span>
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
        </a-col>

        <a-col :span="24" v-show="currentStep === 1">
          <div class="action-content">
            <a-button class="m-r-8" icon="step-backward" @click="currentStep = 0">上一步</a-button>
            <a-button
              type="primary"
              :disabled="!hasSelected"
              @click="handleSubmit"
              :loading="submitLoading">提交并生成拣配任务 </a-button>
          </div>
          <a-table
            :columns="selectColumns"
            :data-source="hasSelectedList"
            rowKey="id"
            :pagination="false"
            size="middle"
          >
            <template slot="quantity" slot-scope="text,record">
              <a-input-number
                :min="0"
                :max="record.availableStock"
                v-model="record.quantity" ></a-input-number>
            </template>
          </a-table>
        </a-col>
      </a-row>

    </div>
  </a-drawer>
</template>

<script>
import _ from 'lodash'
import { mixinTableList } from '@/utils/mixin/index'

const queryFormAttr = () => {
  return {
    'batchNb': ''
  }
}

const selectColumns = [
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
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
    width: 90
  },
  {
    title: '批次号',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 120
  },
  {
    title: '可用库存',
    key: 'availableStock',
    dataIndex: 'availableStock',
    width: 120
  },
  {
    title: '下发量',
    key: 'quantity',
    dataIndex: 'quantity',
    scopedSlots: { customRender: 'quantity' },
    width: 120
  },
  {
    title: '库存量',
    key: 'totalStock',
    dataIndex: 'totalStock',
    width: 80
  },
  {
    title: '冻结库存',
    key: 'freezeStock',
    dataIndex: 'freezeStock',
    width: 120
  },
  {
    title: '有效期',
    key: 'expireDate',
    dataIndex: 'expireDate',
    width: 140
  }
]

export default {
  name: 'CreateReductionTask',
  mixins: [mixinTableList],
  props: {
    orderNb: {
      type: String,
      required: true
    },
    cell: {
      type: String,
      required: true
    },
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
      currentStep: 0,
      submitLoading: false,
      queryForm: {
        pageSize: 40,
        pageNum: 1,
        sortMap: {
          availableStock: 'ascend',
          batchNb: 'ascend'
        },
        ...queryFormAttr()
      },

      selectedRowKeys: [],
      list: [],
      hasSelectedList: []
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
    selectColumns () {
      return selectColumns
    },
    columns () {
      const { sortMap } = this.queryForm
      const columns = [
        {
          title: 'SSCC码',
          key: 'ssccNumber',
          dataIndex: 'ssccNumber',
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
          width: 90
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
          width: 80
        },
        {
          title: '有效期',
          key: 'expireDate',
          dataIndex: 'expireDate',
          width: 140
        }
      ]
      return columns
    },
    stockNumber () {
      const result = _.sumBy(this.hasSelectedList, 'totalStock')
      return result
    }
  },
  methods: {
    handleStepChange (current) {
      this.currentStep = current
    },
    onClose () {
      this.$emit('change', false)
      this.hasSelectedList = []
      this.selectedRowKeys = []
      this.currentStep = 0
      this.$emit('on-ok')
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    onSelectChange (selectedRowKeys) {
      // 删除
      if (selectedRowKeys.length < this.selectedRowKeys.length) {
        const [id] = _.difference(this.selectedRowKeys, selectedRowKeys)

        this.hasSelectedList = _.remove(this.hasSelectedList, x => x.id === id)
      } else {
      // 新增
        const lastKey = _.last(selectedRowKeys)
        const row = _.find(this.list, ['id', lastKey])

        this.hasSelectedList.push({ ...row, quantity: row.totalStock })
      }

      this.selectedRowKeys = selectedRowKeys
    },
    async handleSubmit () {
      try {
        const postDataMap = _.map(this.hasSelectedList, item => {
          return {
            ...item,
            cell: this.cell,
            orderNumber: this.orderNb,
            materialCode: item.materialNb
          }
        })

        this.submitLoading = true
        const options = postDataMap
        await this.$store.dispatch('materialFeeding/callPersonStock', options)
        this.$message.success('提交成功')

        this.onClose()
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
    createReductionTaskVisible: function (val) {
      if (val) {
        this.queryForm.materialNb = this.materialNb
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

</style>
