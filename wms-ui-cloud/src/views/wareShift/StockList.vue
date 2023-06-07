<template>
  <a-drawer width="80%" title="新增移库任务" placement="right" :visible="stockListVisible" @close="onClose">
    <div class="table-content">
      <a-steps :current="currentStep" @change="handleStepChange" class="m-b-24">
        <a-step title="选择库存信息"> </a-step>
        <a-step title="选择目的仓库" />
      </a-steps>
      <a-row>
        <a-col :span="24" v-show="currentStep === 0">
          <a-form layout="inline" class="search-content">
            <a-row :gutter="16">
              <a-col :span="4">
                <a-form-model-item label="仓库编码">
                  <a-input v-model="queryForm.wareCode" placeholder="仓库编码" allow-clear />
                </a-form-model-item>
              </a-col>
              <a-col :span="4">
                <a-form-model-item label="物料号">
                  <a-input v-model="queryForm.materialCode" placeholder="物料号" allow-clear />
                </a-form-model-item>
              </a-col>
              <a-col :span="4">
                <a-form-model-item label="批次号">
                  <a-input v-model="queryForm.batchNb" placeholder="批次号" allow-clear />
                </a-form-model-item>
              </a-col>
              <a-col :span="4">
                <a-form-model-item label="SSCC">
                  <a-input v-model="queryForm.ssccNumber" placeholder="SSCC" allow-clear />
                </a-form-model-item>
              </a-col>
              <a-col span="4">
                <span class="table-page-search-submitButtons">
                  <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
                  <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo" />重置</a-button>
                </span>
              </a-col>
            </a-row>
          </a-form>
          <div class="m-b-8">
            <a-button icon="step-forward" class="m-r-8" @click="checkSelectWareCode">下一步</a-button>
          </div>
          <a-table
            :row-selection="{ selectedRowKeys: selectedRowKeys,
                              onChange: onSelectChange,
                              getCheckboxProps: record => ({
                                props: {
                                  disabled: record.freezeStock > 0, // Column configuration not to be checked
                                },
                              }), }"
            :columns="columns"
            :data-source="list"
            :loading="tableLoading"
            rowKey="ssccNumber"
            :pagination="false"
            size="middle"
            :scroll="tableScroll"
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
              @change="changePagination"
            />
          </div>
        </a-col>

        <a-col :span="24" v-show="currentStep === 1">
          <div class="action-content">
            <a-button class="m-r-8" icon="step-backward" @click="previousStep">上一步</a-button>
            <a-button type="primary" :disabled="!hasSelected" @click="handleSubmit" :loading="submitLoading">提交并生成移库任务
            </a-button>
          </div>
          <a-form :form="form" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-form-item label="目的工厂" required>
              <a-radio-group @change="handleGetWareList" v-decorator="['selectedPlantNb', { rules: [{ required: true, message: '不能为空!' }] }]">
                <a-radio value="7751"> 7751 </a-radio>
                <a-radio value="7752"> 7752 </a-radio>
              </a-radio-group>
            </a-form-item>
            <a-form-item label="目的仓库" required>
              <a-select placeholder="请选择目的仓库" v-decorator="['targetWareCode', { rules: [{ required: true, message: '不能为空!' }] }]">
                <a-select-option v-for="item in wareList" :key="item.id" :value="item.code">
                  {{ item.code }}&emsp;{{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </div>
  </a-drawer>
</template>

<script>
import _ from 'lodash'
import { mixinTableList } from '@/utils/mixin/index'

const labelCol = {
  span: 5
}
const wrapperCol = {
  span: 19
}
const queryFormAttr = () => {
  return {
    'batchNb': '',
    'wareCode': '',
    'materialCode': '',
    'ssccNumber': '',
  }
}

export default {
  name: 'StockList',
  mixins: [mixinTableList],
  props: {
    materialNb: {
      type: String,
      default () {
        return ''
      }
    },
    stockListVisible: {
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
      form: this.$form.createForm(this),
      selectedRowKeys: [],
      list: [],
      hasSelectedList: [],
      wareCode: '',
      wareList: []
    }
  },
  model: {
    prop: 'stockListVisible',
    event: 'change'
  },
  computed: {
    wrapperCol () {
      return wrapperCol
    },
    labelCol () {
      return labelCol
    },
    hasSelected () {
      return this.selectedRowKeys.length > 0
    },
    columns () {
      const { sortMap } = this.queryForm
      const columns = [
        {
          title: '工厂',
          key: 'plantNb',
          dataIndex: 'plantNb',
          width: 100
        },
        {
          title: '仓库编码',
          key: 'wareCode',
          dataIndex: 'wareCode',
          width: 120
        },
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
    }

  },
  methods: {
    previousStep () {
      this.currentStep = 0
      this.form.resetFields()
    },
    checkSelectWareCode () {
      this.currentStep = 1
    },
    handleStepChange (current) {
      this.currentStep = current
    },
    onClose () {
      this.$emit('change', false)
      this.hasSelectedList = []
      this.selectedRowKeys = []
      this.currentStep = 0
      this.$emit('on-ok')
      this.form.resetFields()
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      const selectedRowsMap = _.map(selectedRows, x => {
        return {
          ...x,
          quantity: x.totalStock
        }
      })

      this.hasSelectedList = selectedRowsMap

      this.selectedRowKeys = selectedRowKeys
    },
    async handleGetWareList (e) {
      const selectedPlantNb = e.target.value

      this.form.setFieldsValue({
        targetWareCode: undefined
      })

      const options = { factoryCode: selectedPlantNb }
      try {
        const data = await this.$store.dispatch('ware/getList', options)
        this.wareList = data
        console.log(data)
      } catch (error) {
        this.$message.error('获取仓库数据失败！')
      } finally {
        this.areaLoading = false
      }
    },
    async handleSubmit (e) {
      e.preventDefault()

      this.form.validateFieldsAndScroll(async (err, values) => {
        if (err) {
          console.log('Received values of form: ', values)
          return
        }
        console.log('Received values of form: ', values)
        try {
          this.submitLoading = true
          const options = {
            'targetWareCode': values.targetWareCode,
            'targetPlantNb': values.selectedPlantNb,
            'ssccNbList': this.selectedRowKeys
          }
          await this.$store.dispatch('wareShift/addWareShift', options)
          this.$emit('on-ok')
          this.onClose()
        } catch (error) {
          this.$message.error(error.message)
        } finally {
          this.submitLoading = false
        }
      })
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const {
          data: { rows, total }
        } = await this.$store.dispatch('stock/getPaginationList', this.queryForm)

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
    stockListVisible: function (val) {
      if (val) {
        this.loadData()
      }
    }
  }
}
</script>

<style lang="less" scoped>
.action {
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
/deep/.ant-drawer-body {
  // overflow-y: auto;
  padding-bottom: 60px;
}

/deep/.ant-input-number {
  width: 100%;
}
</style>
