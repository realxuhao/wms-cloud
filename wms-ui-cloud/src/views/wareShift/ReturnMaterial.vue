<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="仓库编码">
              <a-input v-model="queryForm.wareCode" placeholder="仓库编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="SSCC码">
              <a-input v-model="queryForm.ssccNumber" placeholder="SSCC码" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="物料编码" >
              <a-input v-model="queryForm.materialNb" placeholder="物料编码" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="状态">
              <a-select
                show-search
                placeholder="状态"
                style="width:100%"
                v-model="queryForm.status"
              >
                <a-select-option v-for="(item,key) in statusMap" :key="key" :value="key">
                  {{ item.text }}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="advanced">
            <a-col :span="4">
              <a-form-model-item label="批次号">
                <a-input v-model="queryForm.batchNb" placeholder="批次号" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="退库类型">
                <a-select
                  show-search
                  placeholder="退库类型"
                  style="width:100%"
                  v-model="queryForm.type"
                >
                  <a-select-option v-for="(item,key) in typeMap" :key="key" :value="key">
                    {{ item.text }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="创建人">
                <a-input v-model="queryForm.createBy" placeholder="创建人" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="创建时间">
                <a-range-picker
                  format="YYYY-MM-DD HH:mm"
                  :show-time="{ format: 'HH:mm' }"
                  v-model="queryForm.createTime"
                />
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="更新人">
                <a-input v-model="queryForm.updateBy" placeholder="更新人" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="更新时间">
                <a-range-picker
                  format="YYYY-MM-DD HH:mm"
                  :show-time="{ format: 'HH:mm' }"
                  v-model="queryForm.updateTime"
                />
              </a-form-item>
            </a-col>
          </template>
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
        </a-row>
      </a-form>
      <div class="action-content">
        <a-button style="margin-left: 8px" :loading="exportLoading" @click="handleDownload"><a-icon type="download" />导出结果</a-button>
      </div>
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        size="middle"
        :scroll="tableScroll"
      >
        <template slot="moveType" slot-scope="text">
          <span>{{ moveTypeMap[text]&&moveTypeMap[text].text }}</span>
        </template>
        <template slot="type" slot-scope="text">
          <span>{{ typeMap[text]&&typeMap[text].text }}</span>
        </template>
        <template slot="status" slot-scope="text">
          <a-tag :color="colorMap[text]">{{ statusMap[text]&&statusMap[text].text }}</a-tag>
        </template>

      </a-table>

      <div class="pagination-con">
        <a-pagination
          show-size-changer
          :page-size-options="pageSizeOptions||[10,20,30,40,100,150]"
          show-less-items
          :current="queryForm.pageNum"
          :page-size.sync="queryForm.pageSize"
          :total="paginationTotal"
          @showSizeChange="loadTableList"
          @change="changePagination"
        />
      </div>
    </div>

  </div>
</template>

<script>
import _ from 'lodash'
import { download } from '@/utils/file'
import { mixinTableList } from '@/utils/mixin/index'

const columns = [
  {
    title: '仓库编码',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 120
  },
  {
    title: 'cell',
    key: 'cell',
    dataIndex: 'cell',
    width: 120
  },
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 200
  },
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 200
  },
  {
    title: '物料编码',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 200
  },
  {
    title: '批次号',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 200
  },
  {
    title: '数量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 200
  },
  {
    title: '移动类型',
    key: 'moveType',
    dataIndex: 'moveType',
    scopedSlots: { customRender: 'moveType' },
    width: 200
  },
  {
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    width: 200
  },
  {
    title: '退库类型',
    key: 'type',
    dataIndex: 'type',
    scopedSlots: { customRender: 'type' },
    width: 200
  },

  {
    title: '创建人',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 200
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 200
  },
  {
    title: '更新人',
    key: 'updateBy',
    dataIndex: 'updateBy',
    width: 200
  },
  {
    title: '更新时间',
    key: 'updateTime',
    dataIndex: 'updateTime',
    width: 200
  }
]

const moveTypeMap = {
  0: {
    text: '收货'
  },
  1: {
    text: '入库'
  },
  2: {
    text: '上架'
  },
  3: {
    text: '生产叫料'
  },
  4: {
    text: '移库'
  },
  5: {
    text: '拆托上架'
  },
  6: {
    text: '仓库内移动'
  },
  7: {
    text: '生产退料'
  }
}

const typeMap = {
  0: {
    text: '正常退库'
  },
  1: {
    text: '异常退库'
  }
}

const statusMap = {
  '-1': {
    text: '取消任务'
  },
  0: {
    text: '待确认'
  },
  1: {
    text: '待上架'
  },
  2: {
    text: '完成'
  }
}

const colorMap = {
  '-1': undefined,
  0: 'orange',
  1: 'orange',
  2: 'green'
}

const queryFormAttr = () => {
  return {
    wareCode: '',
    ssccNumber: '',
    materialNb: '',
    batchNb: '',
    type: undefined,
    status: undefined,
    createBy: '',
    updateBy: '',
    createTime: [],
    updateTime: []

  }
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  data () {
    return {
      tableLoading: false,
      uploadLoading: false,
      exportLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: []

    }
  },
  computed: {
    moveTypeMap: () => moveTypeMap,
    typeMap: () => typeMap,
    statusMap: () => statusMap,
    colorMap: () => colorMap
  },
  methods: {
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async handleDownload () {
      try {
        this.exportLoading = true
        this.queryForm.pageSize = 0
        const blobData = await this.$store.dispatch('wareShift/exportReturnExcel', this.queryForm)
        download(blobData, '退库列表')
      } catch (error) {
        console.log(error)
        this.$message.error(error.message)
      } finally {
        this.exportLoading = false
      }
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const { createTime = [], updateTime = [] } = this.queryForm
        const createTimeStart = createTime.length > 0 ? createTime[0].format(this.startDateFormat) : undefined
        const createTimeEnd = createTime.length > 0 ? createTime[1].format(this.endDateFormat) : undefined
        const updateTimeStart = updateTime.length > 0 ? updateTime[0].format(this.startDateFormat) : undefined
        const updateTimeEnd = updateTime.length > 0 ? updateTime[1].format(this.endDateFormat) : undefined

        const options = { ..._.omit(this.queryForm, ['createTime', 'updateTime']), createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd }

        const {
          data: { rows, total }
        } = await this.$store.dispatch('wareShift/getReturnMaterialList', options)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async loadData () {
      this.loadTableList()
    }
  },
  mounted () {
    this.loadData()
  }
}
</script>

<style lang="less" scoped>
</style>
