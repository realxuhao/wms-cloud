<template>
  <a-modal
    title="需求计算"
    :visible="visible"
    :confirm-loading="submitLoading"
    width="70%"
    :footer="false"
    @cancel="onClose">
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
        selectedRowKeys: selectedRowKeys, onChange: onSelectChange,
      }"
      :columns="columns"
      :data-source="tableList"
      rowKey="callId"
      :pagination="false"
      size="middle">
      <template slot="recommendShiftQuantity" slot-scope="text,record">
        <a-input v-model="record.recommendShiftQuantity"></a-input>
      </template>
      <template slot="enough" slot-scope="text">
        {{ text ? '满足' : '不满足' }}
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
    title: '主库库存量',
    key: 'mainStock',
    dataIndex: 'mainStock',
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
    title: '主库是否满足',
    key: 'enough',
    scopedSlots: { customRender: 'enough' },
    dataIndex: 'enough',
    width: 80
  }
]

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
      visible: false,
      submitLoading: false,
      selectedRowKeys: [],
      columns,
      selectedRows: [],
      createMoveTaskLoading: false,
      runRequirementLoading: false
      // createReductionTaskVisible: false,
    }
  },
  computed: {
    hasSelected () {
      return this.selectedRowKeys.length > 0
    }
  },
  methods: {
    onClose () {
      this.visible = false
      this.selectedRowKeys = []
      this.selectedRows = []
      this.$emit('successCallback')
    },
    onOpen (list) {
      this.tableList = list
      this.visible = true
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

<style lang="less" scoped></style>
