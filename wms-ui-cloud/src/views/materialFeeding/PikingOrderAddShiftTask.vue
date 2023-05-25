<template>
  <a-drawer
    width="80%"
    title="下发拣配任务"
    placement="right"

    :visible="visible"
    @close="onClose"
  >
    <a-steps style="display:none" :current="currentStep" @change="handleStepChange" class="m-b-24">
      <a-step title="下发任务" >
      </a-step>
      <a-step title="新增移库任务" />
    </a-steps>
    <a-row>
      <a-col :span="24" v-show="(currentStep===1)">
        <a-form layout="inline" class="search-content">
          <a-row :gutter="16">
            <a-col :span="4">
              <a-form-model-item label="批次号">
                <a-input v-model="queryForm.batchNb" placeholder="批次号" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="SSCC码">
                <a-input v-model="queryForm.ssccNumber" placeholder="SSCC码" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col span="4">
              <span class="table-page-search-submitButtons" >
                <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
                <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo" />重置</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>

        <div class="table-content">
          <div class="m-b-8">
            <a-button
              class="m-r-8"
              type="primary"
              :disabled="!hasSelected"
              @click="handleSubmit"
              :loading="submitLoading">下发任务</a-button>
            <span class="m-r-8">
              <template v-if="hasSelected">
                {{ `已选择 ${selectedRowKeys.length} 条` }}
              </template>
            </span>
            <a-button class="m-r-8" icon="rollback" @click="(currentStep = 0)">返回</a-button>

          </div>
          <a-table
            :row-selection="{
              selectedRowKeys: selectedRowKeys,
              onChange: onSelectChange ,
              getCheckboxProps:record => ({
                props: {
                  disabled:record.freezeStock>0,
                  name: record.name,
                },
              })}"

            :scroll="tableScroll"
            :columns="columns"
            :data-source="list"
            :loading="tableLoading"
            rowKey="ssccNumber"
            :pagination="false"
            size="middle"
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
      </a-col>

      <a-col v-show="(currentStep===0)">
        <div class="m-b-8">
          <a-button
            class="m-r-8"
            type="primary"
            :disabled="!hasStockSelected"
            @click="handleStockSubmit"
            :loading="submitLoading">下发拣配任务</a-button>
          <a-button icon="plus" class="m-r-8" @click="currentStep = 1">新增移库任务</a-button>

        </div>
        <a-table
          :row-selection="{
            selectedRowKeys: selectedStockRowKeys,
            onChange: onSelectStockChange ,

          }"
          :scroll="{ x: 1300 }"
          :columns="stockInfoColumns"
          :data-source="stockInfo"
          :loading="tableLoading"
          rowKey="ssccNumber"
          :pagination="false"
          size="middle"
        >
        </a-table>
      </a-col>
    </a-row>

  </a-drawer>
</template>

<script>
import _ from 'lodash'
import { mixinTableList } from '@/utils/mixin/index'

const columns = [
  {
    title: '工厂编码',
    key: 'plantNb',
    dataIndex: 'plantNb',
    width: 120
  },
  {
    title: '仓库编码',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 120
  },
  {
    title: '存储区编码',
    key: 'areaCode',
    dataIndex: 'areaCode',
    width: 120
  },
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 140
  },
  {
    title: '库位编码',
    key: 'binCode',
    dataIndex: 'binCode',
    width: 140
  },
  // {
  //   title: '托盘编码',
  //   key: 'palletCode',
  //   dataIndex: 'palletCode',
  //   width: 140
  // },
  {
    title: '物料编码',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 120
  },
  {
    title: '批次号',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 120
  },
  {
    title: '质检状态',
    key: 'qualityStatus',
    dataIndex: 'qualityStatus',
    width: 80
  },
  {
    title: '库存量',
    key: 'totalStock',
    dataIndex: 'totalStock',
    width: 120
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
    width: 120
  }
]

const stockInfoColumns = [
  ...columns
]

const queryFormAttr = () => {
  return {
    ssccNumber: '',
    batchNb: ''
  }
}

export default {
  name: 'PikingOrderAddShiftTask',
  mixins: [mixinTableList],
  props: {
  },
  data () {
    return {
      currentStep: 0,
      visible: false,
      submitLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },

      selectedRowKeys: [],
      selectedStockRowKeys: [],
      list: [],
      stockInfo: []
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
    hasStockSelected () {
      return this.selectedStockRowKeys.length > 0
    },
    columns: () => columns,
    stockInfoColumns: () => stockInfoColumns
  },
  methods: {
    handleStepChange (current) {
      this.currentStep = current
    },
    async handleOpen (record) {
      this.visible = true
      this.record = record

      this.loadData()
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    onClose () {
      this.visible = false
      this.selectedRowKeys = []
      this.currentStep = 0
      this.$emit('on-ok')
    },
    hasDisabled (record) {
      return !!_.includes(this.stockInfo, x => x.ssccNumber === record.ssccNumber)
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    onSelectStockChange (selectedRowKeys) {
      this.selectedStockRowKeys = selectedRowKeys
    },
    async handleSubmit () {
      try {
        this.submitLoading = true
        await this.$store.dispatch('materialFeeding/addShiftTask', { ssccNbList: this.selectedRowKeys })
        this.selectedRowKeys = []
        this.loadTableList()
        this.$message.success('提交成功')
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.submitLoading = false
      }
    },
    async handleStockSubmit () {
      try {
        this.submitLoading = true

        await this.$store.dispatch('materialFeeding/batchAddJob', { ids: this.selectedStockRowKeys })
        this.getStockInfo()
        this.$message.success('提交成功')
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.submitLoading = false
      }
    },
    async getStockInfo () {
      try {
        this.tableLoading = true

        const options = {
          materialNb: this.record.materialCode,
          wareCode: this.record.wareCode
        }
        const { data } = await this.$store.dispatch('materialFeeding/getStockInfo', options)
        this.stockInfo = data
        this.selectedStockRowKeys = _.map(data, x => x.ssccNumber)
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const options = { wareCode: this.record.wareCode, materialNb: this.record.materialCode }
        const {
          data: { rows, total }
        } = await this.$store.dispatch('stock/getPaginationList', { ...options, ...this.queryForm })
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async loadData () {
      this.getStockInfo()
    }
  },
  watch: {
    currentStep (val) {
      if (val === 1) {
        this.loadTableList()
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
