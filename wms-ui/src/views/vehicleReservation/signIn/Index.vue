<template>
  <div class="wrapper">
    <div class="table-content">

      <div class="action-content">
        <a-form layout="inline" class="search-content">
          <a-row :gutter="16">
            <a-col :span="4">
              <a-form-model-item label="仓库编码">
                <a-select show-search allow-clear v-model="queryForm.wareId" style="width: 100%" placeholder="仓库编码">
                  <a-select-option v-for="item in wareOptionList" :key="item.id" :value="item.id">
                    {{ item.code }}</a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col span="4">
              <a-form-model-item label="签到时间" style="display: flex;">
                <a-date-picker
                  v-model="queryForm.signinDate"
                  format="YYYY-MM-DD"
                />
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
      </div>
      <a-table
        class="sign"
        :columns="signColumns"
        :data-source="list"
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
          </div>
        </template>
        <template slot="dockCode" slot-scope="text">
          <span v-if="text != null">道口 {{ text }}</span>
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
    </div>

  </div>
</template>

<script>

import { mixinTableList } from '@/utils/mixin/index'
import moment from 'moment'
const signColumns = [
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

const queryFormAttr = () => {
  return {
    wareId: '',
    signinDate: null
  }
}
export default {
  name: 'VrSignInIndex',
  mixins: [mixinTableList],
  components: {
  },
  props: {},
  data () {
    return {
      /** 已签到页面字段 */
      signColumns,
      /** 已签到页面数据 */
      list: [],
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
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
    async handleSearch () {
      this.searchLoading = true
      await this.loadTableList()
      this.searchLoading = false
    },
    /** 查询已签到数据 */
    async loadTableList () {
      try {
        this.tableLoading = true
        this.queryForm.signinDate = this.queryForm.signinDate == null ? null : moment(new Date(this.queryForm.signinDate)).format('YYYY-MM-DD')
        const { data: { rows, total } } = await this.$store.dispatch('driverDispatch/getList', this.queryForm)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async loadData () {
      this.getWareOptionList()
      this.loadTableList()
    }
  },
  mounted () {
    this.loadData()
  }
}
</script>

<style lang="less" scoped>
.not-danger-color{
  color: #aaa
}
</style>
