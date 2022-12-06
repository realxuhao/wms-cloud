<template>
  <a-drawer
    width="70%"
    title="新增移库任务"
    placement="right"
    :closable="false"
    :visible="visible"
    @close="onClose"
  >
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
          type="primary"
          :disabled="!hasSelected"
          @click="handleSubmit"
          :loading="submitLoading">提交并生成拣配任务 </a-button>
        <span style="margin-left: 8px">
          <template v-if="hasSelected">
            {{ `已选择 ${selectedRowKeys.length} 条` }}
          </template>
        </span>
      </div>
      <a-table

        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange ,
          getCheckboxProps:record => ({
            props: {
              disabled: hasDisabled(record.ssccNumber),
              name: record.name,
            },
          })}"
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
  {
    title: '托盘编码',
    key: 'palletCode',
    dataIndex: 'palletCode',
    width: 140
  },
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
  }
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
      visible: false,
      submitLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },

      selectedRowKeys: [],
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
    columns: () => columns
  },
  methods: {
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
    },
    hasDisabled (record) {
      return !!_.includes(this.stockInfo, x => x.ssccNumber === record.ssccNumber)
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    async handleSubmit () {
      try {
        this.submitLoading = true
        const options = {
          ssccNbList: this.selectedRowKeys
        }
        await this.$store.dispatch('materialFeeding/callPersonStock', options)
        this.$message.success('提交成功')

        this.onClose()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.submitLoading = false
      }
    },

    async getStockInfo () {
      const options = {
        materialNb: this.record.materialCode,
        wareCode: this.record.wareCode
      }
      const { data } = await this.$store.dispatch('materialFeeding/getStockInfo', options)
      this.stockInfo = data
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
      await this.getStockInfo()
      this.loadTableList()
    }
  },
  watch: {
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
