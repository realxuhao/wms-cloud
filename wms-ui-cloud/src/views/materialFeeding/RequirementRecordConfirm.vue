<template>
  <a-modal
    title="需求计算"
    :visible="visible"
    :confirm-loading="submitLoading"
    :footer="false"
    width="80%"
    @cancel="onClose">
    <a-form layout="inline" class="search-content">
      <a-row :gutter="16">
        <a-col :span="6">
          <a-form-model-item label="主库是否满足">
            <a-select placeholder="请选择" allow-clear v-model="queryForm.enough">
              <a-select-option v-for="item in list" :key="item.value" :value="item.value">
                {{ item.label }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col span="4">
          <span class="table-page-search-submitButtons">
            <a-button
              v-hasPermi="['requirement:list:query']"
              type="primary"
              @click="handleSearch"
            ><a-icon type="search" />查询</a-button>
            <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo" />重置</a-button>
           
          </span>
        </a-col>
      </a-row>
    </a-form>
      
    <div class="action-content">
      <a-button
        type="primary"
        :loading="createMoveTaskLoading"
        :disabled="!hasSelected"
        style="margin-right:8px"
        @click="handleCreateMoveTask">
        创建移库
      </a-button>
      <a-button
        style="margin-right:8px"
        type="primary"
        :loading="runRequirementLoading"
        :disabled="!hasSelected"
        @click="handleIssue">下发</a-button>
    </div>
    <a-table
      :row-selection="{
        selectedRowKeys,
        onChange: this.onSelectChange,
      }"
      :columns="columns"
      :data-source="filterList"
      rowKey="callId"
      :pagination="false"
      size="middle">
      <template slot="recommendShiftQuantity" slot-scope="text,record">
        <a-input v-model="record.recommendShiftQuantity"></a-input>
      </template>
      <template slot="enough" slot-scope="text">
        {{ text ? '满足' : '不满足' }}
      </template>
      <template slot="shiftFlag" slot-scope="text">
        {{ text ? '是' : '否' }}
      </template>
      <template slot="action" slot-scope="text, record">
        <div class="action-con">
          <a-popconfirm title="确认要取消该条任务吗?" ok-text="确认" cancel-text="取消" @confirm="handleCancel(record)">
            <a class="danger-color" :disabled="record.status !== 0">取消</a>
          </a-popconfirm>
          <!-- <a-divider type="vertical" />
            <a
              class="primary-color"
              :disabled="[2].includes(record.status)"
              @click="handleCreateReductionTask(record)"><a-icon class="m-r-4" type="add" />人工创建拣配任务</a> -->
        </div>

      </template>
    </a-table>
  </a-modal>
</template>

<script>
import _ from 'lodash'
import { mixinTableList } from '@/utils/mixin/index'

const list = [
  {
    label: '满足',
    value: 1,
  },
  {
    label: '不满足',
    value: 0,
  },
]



const columns = [
  {
    title: '生产需求号',
    key: 'orderNb',
    dataIndex: 'orderNb',
    width: 80
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
    width: 120
  },
  {
    title: '需求量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 80
  },
  {
    title: '已下发捡配量',
    key: 'issuedQuantity',
    dataIndex: 'issuedQuantity',
    width: 80
  },
  {
    title: '主库可用库存量',
    key: 'mainStock',
    dataIndex: 'mainStock',
    width: 80
  },
  {
    title: '主库暂不可用库存量',
    key: 'noAvailableStockCount',
    dataIndex: 'noAvailableStockCount',
    width: 80
  },
  {
    title: '外库库存量',
    key: 'outStock',
    dataIndex: 'outStock',
    width: 80
  },
  {
    title: '推荐移库量',
    key: 'recommendShiftQuantity',
    dataIndex: 'recommendShiftQuantity',
    scopedSlots: { customRender: 'recommendShiftQuantity' },
    width: 80
  },
  {
    title: '在途量',
    key: 'inTransStock',
    dataIndex: 'inTransStock',
    width: 60
  },
  {
    title: '是否创建过移库',
    key: 'shiftFlag',
    // filters: [
    //   {
    //     text: '满足',
    //     value: 1,
    //   },
    //   {
    //     text: '不满足',
    //     value: 0,
    //   },
    // ],
    // onFilter: (value, record) => record.enough == value,
    scopedSlots: { customRender: 'shiftFlag' },
    dataIndex: 'shiftFlag',
    width: 100
  },
  {
    title: '主库是否满足',
    key: 'enough',
    scopedSlots: { customRender: 'enough' },
    dataIndex: 'enough',
    width: 100
  }
]

const queryFormAttr = () => {
  return {
    enough: undefined,
    shiftFlag: undefined
  }
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  components: {

  },
  props: {

  },
  data () {
    return {
      tableList: [],
      filterList:[],
      visible: false,
      submitLoading: false,
      selectedRowKeys: [],
      columns,
      selectedRows: [],
      createMoveTaskLoading: false,
      runRequirementLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      // createReductionTaskVisible: false,
    }
  },
  computed: {
    hasSelected () {
      return this.selectedRowKeys.length > 0
    },
    list(){
      return list
    },

  },
  methods: {
    onClose () {
      this.visible = false
      this.selectedRowKeys = []
      this.selectedRows = []
      this.handleResetQuery()
      this.$emit('successCallback')
    },
    onOpen (list) {
      this.tableList = list
      this.filterList = list
      this.visible = true
    },
    handleSearch(){
      this.selectedRowKeys = []
      this.selectedRows = []

      if(this.queryForm.enough === undefined){
        this.filterList = this.tableList
        return
      }

      this.filterList = _.filter(this.tableList,x=>x.enough === Boolean(this.queryForm.enough))
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },

    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    async handleIssue () {
      try {
        this.runRequirementLoading = true
        // const options = { ids:  }
        await this.$store.dispatch('materialFeeding/issueCall', _.join(this.selectedRowKeys, ','))

        this.onClose()
        // this.$emit('successCallback')
        this.$message.success('提交成功')
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.runRequirementLoading = false
      }
    },
    async handleCreateMoveTask () {
      try {
        this.createMoveTaskLoading = true
        const dtos = _.map(this.selectedRows, x => {
          return {
            callId: x.callId,
            shiftQuality: x.recommendShiftQuantity
          }
        })

        // const options = { ids:  }
        await this.$store.dispatch('wareShift/generateWareShiftByCall', dtos)
        this.onClose()

        // this.$emit('successCallback')
        this.$message.success('提交成功')
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.createMoveTaskLoading = false
      }
    }
  },
  mounted () {
  }
}
</script>

<style lang="less" scoped>
/deep/.ant-table-filter-selected.anticon {
  color: #fff !important;
}
</style>
