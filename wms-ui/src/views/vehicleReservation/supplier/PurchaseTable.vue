<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="订单PO号">
              <a-input v-model="queryForm.poCode" placeholder="订单PO号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="订单行号">
              <a-input v-model="queryForm.poItem" placeholder="订单行号" allow-clear/>
            </a-form-model-item>
          </a-col>
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
          <a-col span="4" offset="8">
            <span class="table-page-search-submitButtons" >
              <a-button style="float: right;" type="primary" @click="handleSumbitPo" :loading="submitPoLoading"><a-icon type="check" />提交</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>

      <a-table
        :columns="columns"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :data-source="list"
        :loading="tableLoading"
        rowKey="purchaseId"
        :pagination="false"
        size="middle"
        :scroll="{ x: 1300 }"
      >
        <template slot="arriveQuantity" slot-scope="text, record">
          <div>
            <a-input-number style="width: 85%;" :v-model="text == null ? '' : text" @change="e => onArriveNumChange(e,record.purchaseId)" />
          </div>
        </template>
        <template slot="remark" slot-scope="reText">
          <a-tooltip color="'purple'" :title="reText">
            <span>{{ reText.length > 10 ? reText.substring(0,10) + "..." : reText }}</span>
          </a-tooltip>
        </template>
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

      <!-- 选择时间窗口弹窗 -->
      <a-modal
        v-drag-modal
        v-model="isVisibleTimeWindow"
        title="预约时间窗口"
        :width="980"
        :closable="false"
        cancelText="关闭"
        @cancel="handleClose"
        @ok="handleSubmitePorder"
        :confirmLoading="confirmLoading">
        <div>
          <a-row :gutter="16">
            <a-col :span="8">
              <a-form-model-item label="仓库联系人" style="display: flex;">
                <a-select
                  show-search
                  v-model="supplierReserveDTO.wareId"
                  style="width: 100%"
                  placeholder="仓库联系人"
                  @change="onWareIdChange">
                  <a-select-option v-for="item in wareOptionList" :key="item.id" :value="item.id">
                    {{ item.wareUser }}</a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="送货日期" style="display: flex;">
                <a-date-picker
                  v-model="supplierReserveDTO.reserveDate"
                  @change="onDateChange"
                  format="YYYY-MM-DD"
                  :disabled-date="disabledDate"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <p class="select-tw">{{ '已选择的时间段: ' + supplierReserveDTO.timeWindow }}</p>
            </a-col>
          </a-row>
          <a-row :gutter="[16, 16]" v-show="isShowTimeWindowData">
            <a-col :key="index" v-for="(item, index) in timeWindowList" :span="4">
              <a-button @click="onClickTimeWindow(item)" class="hour-div" :style="{'--background-image': item.color}">
                {{ item.startTime + '-' + item.endTime }}
              </a-button>
            </a-col>
          </a-row>
          <a-row v-show="!isShowTimeWindowData">
            <div class="none-tw-div">暂无时间窗口数据</div>
          </a-row>
          <div class="avatar-div" v-show="isShowTimeWindowData">
            <a-avatar shape="square" size="small" style=" background-image:linear-gradient(to bottom right, rgb(0 0 0 / 39%), rgb(0 0 0 / 15%));"></a-avatar>
            <span>不可预约</span>
            <a-avatar shape="square" size="small" style=" background-image:linear-gradient(to bottom right, #2f508d40, #34b8abbd);"></a-avatar>
            <span>可预约</span>
          </div>
        </div>
      </a-modal>
    </div>
  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'
import moment from 'moment'

const columns = [
  {
    title: '订单PO号',
    key: 'poCode',
    dataIndex: 'poCode',
    width: 150
  },
  {
    title: '订单行号',
    key: 'poItem',
    dataIndex: 'poItem',
    width: 100
  },
  {
    title: '供应商名称',
    key: 'supplierName',
    dataIndex: 'supplierName',
    width: 200
  },
  {
    title: '料号',
    key: 'materialCode',
    dataIndex: 'materialCode',
    width: 150
  },
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 200
  },
  {
    title: '需求数量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 130
  },
  {
    title: '送货数量',
    key: 'arriveQuantity',
    dataIndex: 'arriveQuantity',
    scopedSlots: { customRender: 'arriveQuantity' },
    width: 150
  },
  {
    title: '单位',
    key: 'unit',
    dataIndex: 'unit',
    width: 100
  },
  {
    title: '预计到货日期',
    key: 'deliveryDate',
    dataIndex: 'deliveryDate',
    width: 120
  },
  {
    title: '需求放行日期',
    key: 'releaseDate',
    dataIndex: 'releaseDate',
    width: 120
  },
  {
    title: '首批变更号',
    key: 'firstBatchChangeNo',
    dataIndex: 'firstBatchChangeNo',
    width: 150
  },
  {
    title: '海关台帐号',
    key: 'cmsNumber',
    dataIndex: 'cmsNumber',
    width: 150
  },
  {
    title: '备注',
    key: 'remark',
    dataIndex: 'remark',
    width: 200,
    scopedSlots: { customRender: 'remark' }
  }
]

const queryFormAttr = () => {
  return {
    poCode: '',
    poItem: ''
  }
}

const setSupplierReserveDTO = () => {
  return {
    wareId: null,
    reserveDate: null,
    timeWindow: ''
  }
}

export default {
  name: 'VrSupplierPurchaseTable',
  mixins: [mixinTableList],
  components: {

  },
  props: {
    isVisible: {
      type: Boolean,
      default () {
        return false
      }
    },
    supplierName: {
      type: String,
      default () {
        return ''
      }
    },
    reloadPurchase: {
      type: Boolean,
      default () {
        return false
      }
    }
  },
  data () {
    return {
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: [],
      /** 仓库list:value:wareId,label: wareUser */
      wareOptionList: [],
      /** time window数据list */
      timeWindowList: [],
      /** 选中的行id list */
      selectedRowKeys: [],
      /** 选中的行数据list */
      selectedRowList: [],
      /** 供应商预约单关联的采购信息list */
      supplierPorderDTOSList: [],
      /** 启用时间段背景色 */
      openStatusColor: 'linear-gradient(to bottom right, #2f508d40, #34b8abbd)',
      /** 禁用时间段背景色 */
      closeStatusColor: 'linear-gradient(to bottom right, rgb(0 0 0 / 39%), rgb(0 0 0 / 15%))',
      /** 供应商预约单 */
      supplierReserveDTO: { ...setSupplierReserveDTO() },
      /** 提交按钮loading */
      submitPoLoading: false,
      /** timewindow弹窗状态 */
      isVisibleTimeWindow: false,
      /** 是否显示仓库timewindow列表 */
      isShowTimeWindowData: false,
      /** 提交按钮loading */
      confirmLoading: false
    }
  },
  methods: {
    moment,
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    // #region 提交选中的po和时间窗口信息
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    /** 实际送货数量值变化回调函数 */
    onArriveNumChange (value, purchaseId) {
      this.list.find(x => x.purchaseId === purchaseId).arriveQuantity = value == null ? null : Number(value)
    },
    /** 仓库变化，重新查询时间窗口 */
    onWareIdChange () {
      this.supplierReserveDTO.timeWindow = ''
      if (this.supplierReserveDTO.wareId != null && this.supplierReserveDTO.reserveDate != null) {
        this.getTimeWindowList()
      } else {
        this.isShowTimeWindowData = false
      }
    },
    /** 选择日期变化回调，重新查询当天时间窗口情况 */
    onDateChange () {
      this.supplierReserveDTO.timeWindow = ''
      if (this.supplierReserveDTO.wareId != null && this.supplierReserveDTO.reserveDate != null) {
        this.getTimeWindowList()
      } else {
        this.isShowTimeWindowData = false
      }
    },
    async getTimeWindowList () {
      const param = {
        wareId: this.supplierReserveDTO.wareId,
        reserveDate: this.supplierReserveDTO.reserveDate == null ? null : moment(new Date(this.supplierReserveDTO.reserveDate)).format('YYYY-MM-DD')
      }
      const { data } = await this.$store.dispatch('supplierReserve/getListByParam', param)
      this.timeWindowList = data
      if (this.timeWindowList.length > 0) {
        this.timeWindowList.forEach(item => {
          item['color'] = item.enable ? this.openStatusColor : this.closeStatusColor
        })
        this.isShowTimeWindowData = true
      } else {
        this.isShowTimeWindowData = false
      }
    },
    /** 点击时间窗口button回调函数 */
    onClickTimeWindow (item) {
      if (item.enable) {
        this.supplierReserveDTO.timeWindow = item.startTime + '-' + item.endTime
      } else {
        this.supplierReserveDTO.timeWindow = ''
      }
    },
    /** 提交选中的PO单，判断后弹出时间窗口 */
    handleSumbitPo () {
      this.selectedRowList = this.list.filter(item => {
        return this.selectedRowKeys.find(x => x === item.purchaseId)
      })
      if (this.selectedRowList.length === 0) {
        this.$message.error('请至少选择一条订单！')
        return
      }
      for (let i = 0; i < this.selectedRowList.length; i++) {
        if (this.selectedRowList[i].arriveQuantity === null || this.selectedRowList[i].arriveQuantity === undefined || this.selectedRowList[i].arriveQuantity <= 0 || this.selectedRowList[i].arriveQuantity === '') {
          this.$message.error('请填写完整已选择订单的送货数量！')
          return
        }
      }
      if (!(this.supplierReserveDTO.wareId == null || this.supplierReserveDTO.reserveDate == null)) {
        this.getTimeWindowList()
      }
      this.isVisibleTimeWindow = true
    },
    /** 提交供应商预约信息 */
    async handleSubmitePorder () {
      if (this.supplierReserveDTO.wareId == null) {
        this.$message.error('请选择预约联系人!')
        return
      }
      if (this.supplierReserveDTO.reserveDate == null) {
        this.$message.error('请选择要预约的日期!')
        return
      }
      if (this.supplierReserveDTO.timeWindow === '') {
        this.$message.error('请选择要预约的时间段!')
        return
      }
      try {
        this.confirmLoading = true
        this.supplierReserveDTO.reserveDate = this.supplierReserveDTO.reserveDate == null ? null : moment(new Date(this.supplierReserveDTO.reserveDate)).format('YYYY-MM-DD')
        let supplierPorderDTOS = []
        for (let i = 0; i < this.selectedRowList.length; i++) {
          const row = {
            purchaseId: this.selectedRowList[i].purchaseId,
            arriveQuantity: this.selectedRowList[i].arriveQuantity
          }
          supplierPorderDTOS = [...supplierPorderDTOS, row]
        }
        const param = {
          supplierName: this.supplierName,
          supplierPorderDTOS: supplierPorderDTOS,
          supplierReserveDTO: this.supplierReserveDTO
        }
        await this.$store.dispatch('supplierReserve/addList', param)
        this.$message.success('保存成功!')
        this.isVisibleTimeWindow = false
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.confirmLoading = false
      }
    },
    /** 关闭选择时间窗口弹窗 */
    handleClose () {
      this.isVisibleTimeWindow = false
    },
    /** 设置禁用时间 */
    disabledDate (current) {
      return current && (current < moment().startOf('day') || current > moment().add(2, 'weeks'))
    },
    // #endregion
    /** 如果供应商精确查询后有数据，则可以进行页面中的筛选查询 */
    async loadTableList () {
      if (this.paginationTotal > 0) {
        this.selectedRowKeys = []
        this.selectedRowList = []
        this.list = []
        try {
          this.tableLoading = true
          const { data: { rows, total } } = await this.$store.dispatch('purchase/getListBySupplierName', { name: this.supplierName, queryParams: { ...this.queryForm, ...{ status: 0 } } })
          this.list = rows
          this.list.forEach(item => {
            this.$set(item, 'arriveQuantity', undefined)
          })
          this.paginationTotal = total
        } catch (error) {
          this.$message.error(error.message)
        } finally {
          this.tableLoading = false
        }
      }
    },
    /** 获取了供应商名称后精确查询，采购订单列表 */
    async initTableList () {
      try {
        this.selectedRowKeys = []
        this.selectedRowList = []
        this.list = []
        this.tableLoading = true
        const { data: { rows, total } } = await this.$store.dispatch('purchase/getListBySupplierName', { name: this.supplierName, queryParams: this.queryForm })
        this.list = rows
        this.list.forEach(item => {
          this.$set(item, 'arriveQuantity', undefined)
        })
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async loadData () {
      this.initTableList()
    },
    /** 初始化Option */
    async preOptionList () {
      const data = await this.$store.dispatch('ware/getOptionList')
      this.wareOptionList = data.data
    }
  },
  mounted () {
    this.preOptionList()
  },
  watch: {
    isVisible (val) {
      if (val && this.supplierName) {
        this.loadData()
      }
    },
    reloadPurchase (val) {
      if (this.supplierName) {
        this.loadData()
      }
    }
  }
}
</script>

<style lang="less" scoped>
.hour-div {
  background-image: var(--background-image);
  height: 8vh;
  font-size: 18px;
  font-weight: 800;
}
.select-tw{
  line-height: 37px;
  font-size: 17px;
  font-weight: 800;
  color: #1890ff;
}
.none-tw-div{
  color: #aca8a8;
  font-size: 15px;
  font-weight: 600;
  position: absolute;
  left: 50%;
  top: 50%;
  padding-top: 6px;
  transform: translate(-50%, -40%);
}
.avatar-div{
  margin-top: 20px;
}
/deep/.ant-form-item-control-wrapper{
  width: 80%;
}
</style>
