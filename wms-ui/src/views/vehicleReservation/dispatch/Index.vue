<template>
  <div class="wrapper">
    <div class="table-content">
      <a-tabs defaultActiveKey="1" @change="callback">
        <a-tab-pane key="1" tab="今日已签到列表">
          <a-table
            :columns="signColumns"
            :data-source="signList"
            :loading="tableLoading"
            rowKey="dispatchId"
            :pagination="false"
            size="middle"
            :scroll="{ x: 1300 }"
          >
          </a-table></a-tab-pane>
        <a-tab-pane key="2" tab="今日未签到列表">

        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>

<script>

import { mixinTableList } from '@/utils/mixin/index'
const signColumns = [
  {
    title: '预约类型',
    key: 'driverTypeDes',
    dataIndex: 'driverTypeDes',
    width: 100
  },
  {
    title: '车牌号',
    key: 'carNum',
    dataIndex: 'carNum',
    width: 100
  },
  {
    title: '是否预约',
    key: 'reserveTypeDes',
    dataIndex: 'reserveTypeDes',
    scopedSlots: { customRender: 'reserveTypeDes' },
    width: 100
  },
  {
    title: '预约时间',
    key: 'reserveDate',
    dataIndex: 'reserveDate',
    width: 150
  },
  {
    title: '签到时间',
    key: 'signinDate',
    dataIndex: 'signinDate',
    width: 150
  },
  {
    title: '仓库',
    key: 'wareName',
    dataIndex: 'wareName',
    width: 120
  },
  {
    title: 'Cell',
    key: 'cell',
    dataIndex: 'cell',
    width: 120
  },
  {
    title: '道口',
    key: 'dockCode',
    dataIndex: 'dockCode',
    scopedSlots: { customRender: 'dockCode' },
    width: 200
  },
  {
    title: '是否迟到',
    key: 'lateDes',
    dataIndex: 'lateDes',
    scopedSlots: { customRender: 'lateDes' },
    width: 200
  },
  {
    title: '操作',
    key: 'action',
    width: 200,
    scopedSlots: { customRender: 'action'
    }
  }
]
const notSignColumns = []

export default {
  name: 'VrDispatchIndex',
  mixins: [mixinTableList],
  components: {
  },
  props: {},
  data () {
    return {
      /** 选中标签页 */
      activeKey: 1,
      signColumns,
      signList: [],
      notSignColumns,
      notSignList: [],
      queryForm: {
      }
    }
  },
  model: {},
  computed: {},
  methods: {
    callback (key) {
      if (key === '1') {
        this.loadSignTableList()
      }
      if (key === '2') {
        this.loadNotSignTableList()
      }
    },
    async loadSignTableList () {
      try {
        this.tableLoading = true

        const { data } = await this.$store.dispatch('driverDispatch/getTodaySignlist', this.queryForm)
        this.signList = data
        console.info(data)
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async loadNotSignTableList () {
      try {
        this.tableLoading = true

        const { data } = await this.$store.dispatch('driverDispatch/getTodayNoSignList')
        this.notSignList = data
        console.info(data)
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async loadData () {
      this.loadSignTableList()
    }
  },
  mounted () {
    this.loadData()
  }
}
</script>

<style lang="less" scoped>
</style>
