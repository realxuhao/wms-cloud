<template>
  <div class="wrapper">
    <div class="table-content">
      <a-tabs defaultActiveKey="1" @change="callback">
        <a-tab-pane key="1" tab="今日已签到列表">
          <a-form layout="inline" class="search-content">
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-model-item label="仓库编码">
                  <a-select show-search allow-clear v-model="queryForm.wareId" style="width: 100%" placeholder="仓库编码">
                    <a-select-option v-for="item in wareOptionList" :key="item.id" :value="item.id">
                      {{ item.code }}</a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col span="4">
                <span class="table-page-search-submitButtons">
                  <a-button
                    type="primary"
                    @click="handleSearch"
                    :loading="searchLoading"
                  ><a-icon type="search" />查询</a-button>
                </span>
              </a-col>
            </a-row>
          </a-form>
          <a-table
            class="sign"
            :columns="signColumns"
            :data-source="signList"
            :loading="tableLoading"
            rowKey="dispatchId"
            :pagination="false"
            size="middle"
            :scroll="{ x: 1300 }"
          >
            <template slot="driverTypeDes" slot-scope="text, record">
              <div>
                <a-tag color="#2db7f5" v-if="record.driverType===0">
                  {{ text }}
                </a-tag>
                <a-tag color="#87d068" v-if="record.driverType===1">
                  {{ text }}
                </a-tag>
              </div>
            </template>
            <template slot="reserveTypeDes" slot-scope="text, record">
              <div>
                <a-tag color="orange" v-if="record.reserveType===0">
                  {{ text }}
                </a-tag>
                <a-tag color="green" v-else-if="record.reserveType===1">
                  {{ text }}
                </a-tag>
                <a-tag color="blue" v-else>
                  {{ text }}
                </a-tag>
              </div>
            </template>
            <template slot="lateDes" slot-scope="text, record">
              <div>
                <a-tag color="green" v-if="record.late===0">
                  {{ text }}
                </a-tag>
                <a-tag color="red" v-else-if="record.late===1">
                  {{ text }}
                </a-tag>
                <a-tag color="blue" v-else>
                  {{ text }}
                </a-tag>
              </div>
            </template>
            <template slot="statusDes" slot-scope="text, record">
              <div>
                <a-tag color="orange" v-if="record.status===0">
                  {{ text }}
                </a-tag>
                <a-tag color="green" v-if="record.status===1">
                  {{ text }}
                </a-tag>
                <a-tag color="blue" v-if="record.status===2">
                  {{ text }}
                </a-tag>
                <a-tag color="red" v-if="record.status===3">
                  {{ text }}
                </a-tag>
              </div>
            </template>
            <template slot="dockCode" slot-scope="text">
              <span v-if="text != null">道口 {{ text }}</span>
            </template>
            <template slot="action" slot-scope="text, record">
              <div class="action-con">
                <a v-hasPermi="['warehouse:dispatch:setdock']" v-if="record.status < 1" class="warning-color" @click="record.status < 1 && handleSetDock(record)"><a-icon class="m-r-4" type="edit" />分配道口</a>
                <a v-hasPermi="['warehouse:dispatch:setdock']" v-else class="not-danger-color" ><a-icon class="m-r-4" type="edit" />分配道口</a>

                <a-divider type="vertical" />
                <a-popconfirm
                  v-if="record.status < 1"
                  title="确认要车辆入厂吗? 请确认此道口为空"
                  ok-text="确认"
                  cancel-text="取消"
                  @confirm="record.status < 1 && handleEnter(record)"
                >
                  <a v-hasPermi="['warehouse:dispatch:enter']" style="color: green"><a-icon class="m-r-4" type="login" />入厂</a>
                </a-popconfirm>
                <a v-hasPermi="['warehouse:dispatch:enter']" v-else class="not-danger-color" ><a-icon class="m-r-4" type="login" />入厂</a>
                <a-divider type="vertical" />

                <a-popconfirm
                  v-if="record.status != 2"
                  title="确认要取消吗?"
                  ok-text="确认"
                  cancel-text="取消"
                  @confirm="record.status != 2 && handleCancel(record)"
                >
                  <a class="primary-color"><a-icon class="m-r-4" type="check-circle" />取消</a>
                </a-popconfirm>
                <a v-else class="not-danger-color" ><a-icon class="m-r-4" type="check-circle" />取消</a>
                <a-divider type="vertical" />

                <a-popconfirm
                  v-if="record.status == 1"
                  title="确认要完成吗?"
                  ok-text="确认"
                  cancel-text="取消"
                  @confirm="record.status == 1 && handleComplete(record)"
                >
                  <a v-hasPermi="['warehouse:dispatch:complete']" class="primary-color"><a-icon class="m-r-4" type="check-circle" />完成</a>
                </a-popconfirm>
                <a v-hasPermi="['warehouse:dispatch:complete']" v-else class="not-danger-color" ><a-icon class="m-r-4" type="check-circle" />完成</a>
              </div>
            </template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="2" tab="未签到列表">
          <a-form layout="inline" class="search-content">
            <a-row :gutter="16">
              <a-col span="8">
                <a-form-model-item label="预约时间" style="display: flex;">
                  <a-date-picker
                    v-model="searchNotSignDate"
                    format="YYYY-MM-DD"
                  />
                </a-form-model-item>
              </a-col>
              <a-col span="4">
                <span class="table-page-search-submitButtons">
                  <a-button
                    type="primary"
                    @click="handleSearchNot"
                    :loading="searchLoading"
                  ><a-icon type="search" />查询</a-button>
                </span>
              </a-col>
            </a-row>
          </a-form>
          <a-table
            :columns="notSignColumns"
            :data-source="notSignList"
            :loading="tableLoading"
            :rowKey="(record,index)=>{return index}"
            :pagination="false"
            size="middle"
            :scroll="{ x: 1300 }"
          >
            <template slot="driverTypeDes" slot-scope="text, record">
              <div>
                <a-tag color="#2db7f5" v-if="record.driverType===0">
                  {{ text }}
                </a-tag>
                <a-tag color="#87d068" v-if="record.driverType===1">
                  {{ text }}
                </a-tag>
              </div>
            </template>
            <template slot="lateDes" slot-scope="text, record">
              <div>
                <a-tag color="green" v-if="record.late===0">
                  {{ text }}
                </a-tag>
                <a-tag color="red" v-if="record.late===1">
                  {{ text }}
                </a-tag>
              </div>
            </template>
            <template slot="reserveTypeDes" slot-scope="text, record">
              <div>
                <a-tag color="orange" v-if="record.reserveType===0">
                  {{ text }}
                </a-tag>
                <a-tag color="green" v-else-if="record.reserveType===1">
                  {{ text }}
                </a-tag>
                <a-tag color="blue" v-else>
                  {{ text }}
                </a-tag>
              </div>
            </template>
          </a-table>
        </a-tab-pane>
      </a-tabs>
    </div>

    <SetDockDrawer
      v-model="visible"
      :updateType="updateType"
      :data="JSON.stringify(signData)"
      @on-ok="loadSignTableList"
    ></SetDockDrawer>
  </div>
</template>

<script>
import Sortable from 'sortablejs'
import SetDockDrawer from './SetDockDrawer'
import moment from 'moment'

import { mixinTableList } from '@/utils/mixin/index'
const signColumns = [

  {
    title: '操作',
    key: 'action',
    width: 280,
    scopedSlots: { customRender: 'action' }
  },
  {
    title: '预约类型',
    key: 'driverTypeDes',
    dataIndex: 'driverTypeDes',
    scopedSlots: { customRender: 'driverTypeDes' },
    width: 75
  },
  {
    title: '车牌号',
    key: 'carNum',
    dataIndex: 'carNum',
    width: 90
  },
  {
    title: '是否预约',
    key: 'reserveTypeDes',
    dataIndex: 'reserveTypeDes',
    scopedSlots: { customRender: 'reserveTypeDes' },
    width: 80
  },
  {
    title: '预约时间',
    key: 'reserveDate',
    dataIndex: 'reserveDate',
    width: 180
  },
  {
    title: '签到时间',
    key: 'signinDate',
    dataIndex: 'signinDate',
    width: 150
  },
  {
    title: '是否迟到',
    key: 'lateDes',
    dataIndex: 'lateDes',
    scopedSlots: { customRender: 'lateDes' },
    width: 90
  },
  {
    title: '等待时间(分钟)',
    key: 'waitTime',
    dataIndex: 'waitTime',
    width: 120
  },
  {
    title: 'Cell',
    key: 'cell',
    dataIndex: 'cell',
    width: 80
  },
  {
    title: '仓库编码',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 120
  },
  {
    title: '道口',
    key: 'dockCode',
    dataIndex: 'dockCode',
    scopedSlots: { customRender: 'dockCode' },
    width: 80
  },
  {
    title: '状态',
    key: 'statusDes',
    dataIndex: 'statusDes',
    scopedSlots: { customRender: 'statusDes' },
    width: 120
  },
  {
    title: '供应商名称',
    key: 'supplierCode',
    dataIndex: 'supplierCode',
    width: 120
  },
  {
    title: '司机姓名',
    key: 'driverName',
    dataIndex: 'driverName',
    width: 100
  },
  {
    title: '司机联系方式',
    key: 'driverPhone',
    dataIndex: 'driverPhone',
    width: 120
  }
]
const notSignColumns = [
  {
    title: '预约类型',
    key: 'driverTypeDes',
    dataIndex: 'driverTypeDes',
    scopedSlots: { customRender: 'driverTypeDes' },
    width: 150
  },
  {
    title: '是否预约',
    key: 'reserveTypeDes',
    dataIndex: 'reserveTypeDes',
    scopedSlots: { customRender: 'reserveTypeDes' },
    width: 150
  },
  {
    title: '是否迟到',
    key: 'lateDes',
    dataIndex: 'lateDes',
    scopedSlots: { customRender: 'lateDes' },
    width: 100
  },
  {
    title: '预约时间',
    key: 'reserveDate',
    dataIndex: 'reserveDate',
    width: 180
  },
  {
    title: '供应商预约单号',
    key: 'reserveNo',
    dataIndex: 'reserveNo',
    width: 180
  },
  {
    title: '供应商名称',
    key: 'supplierCode',
    dataIndex: 'supplierCode',
    width: 150
  },
  {
    title: '司机姓名',
    key: 'driverName',
    dataIndex: 'driverName',
    width: 150
  },
  {
    title: '司机联系方式',
    key: 'driverPhone',
    dataIndex: 'driverPhone',
    width: 150
  },
  {
    title: '车牌号',
    key: 'carNum',
    dataIndex: 'carNum',
    width: 150
  }
]

export default {
  name: 'VrDispatchIndex',
  mixins: [mixinTableList],
  components: {
    SetDockDrawer
  },
  props: {},
  beforeDestroy () {
    clearInterval(this.signTableTimer)
    this.signTableTimer = null
  },
  data () {
    return {
      /** 选中标签页 */
      activeKey: 1,
      // #region 已签到页面参数
      /** 已签到页面定时器 */
      signTableTimer: null,
      /** 已签到页面字段 */
      signColumns,
      /** 已签到页面数据 */
      signList: [],
      /** 已签到数据分配道口 */
      signData: null,
      firstSortNo: null,
      // #endregion
      // #region 未签到页面参数
      /** 未签到页面字段 */
      notSignColumns,
      /** 未签到页面数据 */
      notSignList: [],
      searchNotSignDate: null,
      // #endregion
      queryForm: {
        wareId: null
      },
      /** 仓库list */
      wareOptionList: []
    }
  },
  model: {},
  computed: {},
  methods: {
    moment,
    /** 获取仓库List */
    async getWareOptionList () {
      this.wareOptionList = []
      try {
        const data = await this.$store.dispatch('ware/getOptionList')
        this.wareOptionList = data.data
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    /** table拖动 */
    rowDrop () {
      const tbody = document.querySelector('.sign .ant-table-tbody') // 元素选择器名称根据实际内容替换
      const _this = this
      Sortable.create(tbody, {
        // 官网上的配置项,加到这里面来,可以实现各种效果和功能
        animation: 150,
        ghostClass: 'blue-background-class',
        async onEnd ({ newIndex, oldIndex }) {
          const currRow = _this.signList.splice(oldIndex, 1)[0]
          _this.signList.splice(newIndex, 0, currRow)
          try {
            if (newIndex !== oldIndex) {
              _this.tableLoading = true
              let newSort = 0
              if (newIndex > oldIndex) {
                newSort = Number(newIndex) + _this.firstSortNo + 1
              } else {
                newSort = Number(newIndex) + _this.firstSortNo
              }
              const param = { dispatchId: currRow.dispatchId, newSortNo: newSort }
              await _this.$store.dispatch('driverDispatch/sort', param)
              _this.handleSearch()
            }
          } catch (error) {
            _this.$message.error(error.message)
          } finally {
            _this.tableLoading = false
          }
        }
      })
    },
    /** 切换tab */
    callback (key) {
      if (key === '1') {
        this.loadSignTableList()
      }
      if (key === '2') {
        this.loadNotSignTableList()
      }
    },
    async handleSearch () {
      this.searchLoading = true
      await this.loadSignTableList()
      this.searchLoading = false
    },
    async handleSearchNot () {
      this.searchLoading = true
      await this.loadNotSignTableList()
      this.searchLoading = false
    },
    /** 分配仓库及道口 */
    handleSetDock (record) {
      this.updateType = 'edit'
      this.visible = true
      this.signData = record
    },
    /** 入厂动作 */
    async handleEnter (record) {
      // 判断是否分配仓库和道口，没有分配不能入厂
      if (record.wareId === null || record.dockCode === null) {
        this.$message.error('请先分配仓库和道口！')
        return
      }
      try {
        await this.$store.dispatch('driverDispatch/enter', record.dispatchId)
        this.$message.success('入厂成功！')

        this.loadSignTableList()
        this.sendMessageToWx(record)
      } catch (error) {
        console.log(error)
        this.$message.error('入厂失败，请联系系统管理员！')
      }
    },
    /** 完成动作 */
    async handleComplete (record) {
      try {
        await this.$store.dispatch('driverDispatch/complete', record.dispatchId)
        this.$message.success('已完成！')

        this.loadSignTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('完成失败，请联系系统管理员！')
      }
    },
    /** 完成动作 */
    async handleCancel (record) {
      try {
        await this.$store.dispatch('driverDispatch/cancel', record.dispatchId)
        this.$message.success('已取消')

        this.loadSignTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('取消失败，请联系系统管理员！')
      }
    },
    /** 发送微信推送 */
    async sendMessageToWx (record) {
      // 获取微信token
      const { msg } = await this.$store.dispatch('driverDispatch/getWxToken')
      if (msg) {
        record = { ...record, ...{ wxToken: msg } }
        await this.$store.dispatch('driverDispatch/sendMsgToWx', record)
      }
    },
    /** 查询已签到数据 */
    async loadSignTableList () {
      try {
        this.tableLoading = true

        const { data } = await this.$store.dispatch('driverDispatch/getTodaySignlist', this.queryForm)
        this.signList = data
        if (this.signList.length > 0) {
          this.firstSortNo = Number(this.signList[0].sortNo)
        }
        this.signList.forEach(item => {
          const second = new Date().getTime() / 1000 - new Date(item.signinDate).getTime() / 1000
          this.$set(item, 'waitTime', parseInt(second / 60))
        })
        if (this.signTableTimer == null) {
          this.signTableTimer = setInterval(() => {
            this.signList.forEach(item => {
              const second = new Date().getTime() / 1000 - new Date(item.signinDate).getTime() / 1000
              this.$set(item, 'waitTime', parseInt(second / 60))
            })
          }, 5 * 1000 * 1000)
        }
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    /** 查询已预约未签到数据 */
    async loadNotSignTableList () {
      try {
        this.tableLoading = true
        const { data } = await this.$store.dispatch('driverDispatch/getTodayNoSignList', { signinDate: this.searchNotSignDate == null ? null : moment(new Date(this.searchNotSignDate)).format('YYYY-MM-DD') })
        this.notSignList = data
        this.notSignList.forEach(x => {
          if(x.reserveType == 1 && x.late == null && x.reserveDate != null){
            if(x.driverType == 0){
              const date = x.reserveDate.split(' ')
              const timeList = date[1].split('-')
              const reserveDate = new Date(new Date(date[0]).getFullYear(),new Date(date[0]).getMonth(), new Date(date[0]).getDate(), timeList.length == 2 ? timeList[1].split(':')[0] : 0, 0, 0)
              if(reserveDate.getTime() < new Date().getTime()){
                x.late = 1
                x.lateDes = '迟到'
              }
            }
            if(x.driverType == 1){
              const date = x.reserveDate.split(' ')
              const reserveDate = new Date(new Date(date[0]).getFullYear(),new Date(date[0]).getMonth(), new Date(date[0]).getDate() + 1, 0, 0, 0)
              if(reserveDate.getTime() < new Date().getTime()){
                x.late = 1
                x.lateDes = '迟到'
              }
            }

          }
        })
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async loadData () {
      this.getWareOptionList()
      this.loadSignTableList()
    }
  },
  mounted () {
    this.loadData()
    // 加载拖动列表事件
    this.rowDrop()
  }
}
</script>

<style lang="less" scoped>
.not-danger-color{
  color: #aaa
}
</style>
