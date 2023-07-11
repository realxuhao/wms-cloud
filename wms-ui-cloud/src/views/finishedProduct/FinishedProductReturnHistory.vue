<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="工厂编号">
              <a-input v-model="queryForm.plantNb" placeholder="工厂编号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="SSCC码">
              <a-input v-model="queryForm.ssccNumber" placeholder="SSCC码" allow-clear/>
            </a-form-item>
          </a-col>
         
          <a-col :span="4">
            <a-form-item label="物料号" >
              <a-input v-model="queryForm.productNb" placeholder="物料号" allow-clear/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="退货类型">
              <a-select
                show-search
                placeholder="退货类型"
                style="width:100%"
                v-model="queryForm.type"
              >
                <a-select-option v-for="(item,key) in typeMap" :key="key" :value="key">
                  {{ item.text }}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="advanced">
            <a-col :span="4">
              <a-form-item label="批次号" >
                <a-input v-model="queryForm.batchNb" placeholder="批次号" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="退货类型">
                <a-select
                  show-search
                  placeholder="退货类型"
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
        <a-button type="primary" style="margin-left: 8px" :loading="exportLoading" @click="handleDownload"><a-icon type="download" />导出结果</a-button>
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
      
        <template slot="type" slot-scope="text">
          <span>{{ typeMap[text]&&typeMap[text].text }}</span>
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
    title: '工厂编号',
    key: 'plantNb',
    dataIndex: 'plantNb',
    width: 120
  },
 {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 200
  },
  {
    title: '物料号',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 200
  },
  {
    title: '退货数量',
    key: 'quantity',
    dataIndex: 'quantity',
    width: 200
  },
  
  {
    title: '批次号',
    key: 'batchNb',
    dataIndex: 'batchNb',
    width: 200
  },
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 200
  },
  {
    title: '退货类型',
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


const typeMap = {
  0: {
    text: '经销商退货'
  },
  1: {
    text: '退货到工厂'
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
    plantNb: '',
    productNb: '',
    ssccNumber: '',
    ssccNumber: '',
    type: undefined,
    materialNb:'',
    batchNb:'',
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
    typeMap: () => typeMap,
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
        // this.queryForm.pageSize = 0
        const blobData = await this.$store.dispatch('finishedProduct/exportProductReturnList', this.queryForm)
        download(blobData, '成品退货历史记录.xlsx')
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
        } = await this.$store.dispatch('finishedProduct/productReturnList', options)
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
