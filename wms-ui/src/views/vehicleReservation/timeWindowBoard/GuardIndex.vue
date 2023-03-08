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
          :columns="columns"
          :data-source="list"
          :loading="tableLoading"
          rowKey="dispatchId"
          :pagination="false"
          size="middle"
        >
          <template slot="dockCode" slot-scope="text">
            <span v-if="text != null">道口 {{ text }}</span>
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
        </a-table>
      </div>
    </div>
  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'

const columns = [
  {
    title: '仓库编号',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 200
  },
  {
    title: '送货道口',
    key: 'dockCode',
    dataIndex: 'dockCode',
    scopedSlots: { customRender: 'dockCode' },
    width: 150
  },
  {
    title: '车牌号',
    key: 'carNum',
    dataIndex: 'carNum',
    width: 200
  },
  {
    title: '是否预约',
    key: 'reserveTypeDes',
    dataIndex: 'reserveTypeDes',
    scopedSlots: { customRender: 'reserveTypeDes' },
    width: 150
  },
  {
    title: '供应商名称',
    key: 'supplierCode',
    dataIndex: 'supplierCode',
    width: 200
  },
  {
    title: '当前状态',
    key: 'statusDes',
    dataIndex: 'statusDes',
    scopedSlots: { customRender: 'statusDes' },
    width: 200
  }
]

export default {
  name: 'VrGuardTimeWindowBoardIndex',
  mixins: [mixinTableList],
  props: {
  },
  data () {
    return {
      searchLoading: false,
      queryForm: {
        wareId: null,
        statusList: [0, 1]
      },
      wareOptionList: [],
      columns,
      list: [],
      timer: null

    }
  },
  beforeDestroy () {
    clearInterval(this.timer)
    this.timer = null
  },
  methods: {
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
    async loadTableList () {
      try {
        this.tableLoading = true
        this.list = []
        const { data } = await this.$store.dispatch('driverDispatch/getTodaySignlist', this.queryForm)
        this.list = data
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    loadData () {
      this.getWareOptionList()
      this.loadTableList()
      this.timer = setInterval(() => {
        this.loadTableList()
      }, 2 * 1000 * 1000)
    }
  },
  mounted () {
    this.loadData()
  },
  watch: {
  }
}
</script>

<style lang="less" scoped>
</style>
