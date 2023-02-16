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
            <a-form-item label="质检状态" >
              <a-select
                allow-clear
                v-model="queryForm.qualityStatus"
              >
                <a-select-option v-for="item in qualityStatus" :key="item.value" :value="item.text">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="状态">
              <a-select
                placeholder="请选择状态"
                allow-clear
                v-model="queryForm.changeStatus"
              >
                <a-select-option v-for="(item, key) in changeStatusMap" :key="item" :value="key">
                  {{ item }}
                </a-select-option>
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
              <a-form-item label="修改人">
                <a-input v-model="queryForm.operateUser" placeholder="修改人" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-model-item label="更新时间">
                <a-range-picker
                  format="YYYY-MM-DD HH:mm"
                  :show-time="{ format: 'HH:mm' }"
                  v-model="queryForm.updateTimeList"
                />
              </a-form-model-item>
            </a-col>
<!--            <a-col :span="4">
              <a-form-model-item label="存储区编码">
                <a-input v-model="queryForm.areaCode" placeholder="仓库编码" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="库位编码">
                <a-input v-model="queryForm.binCode" placeholder="库位编码" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-model-item label="工厂编码">
                <a-input v-model="queryForm.plantNb" placeholder="工厂编码" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="托盘编码">
                <a-input v-model="queryForm.palletCode" placeholder="托盘编码" allow-clear/>
              </a-form-item>
            </a-col>
            -->
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

      </div>
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        size="middle"
        :scroll="{ x: 1300, y: 1300 }"
      >
        <template slot="qualityStatusSlot" slot-scope="text">
          <a-tag :color="colorMap[text]">
            {{ text }}
          </a-tag>
        </template>
        <template slot="changeStatusSlot" slot-scope="text">
          <a-tag :color="colorMap[text]">
            {{ changeStatusMap[text] }}
          </a-tag>
        </template>
        <template slot="checkType" slot-scope="text">
          <div >
            <a-tag color="orange" v-if="text===0">
              称重
            </a-tag>
            <a-tag color="blue" v-if="text===1">
              数数
            </a-tag>
            <a-tag color="#87d068" v-if="text===2">
              免检
            </a-tag>
          </div>
        </template>
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a :disabled="record.freezeStock > 0" @click="handleUpdate('同批次', record)"><a-icon class="m-r-4" type="right-circle" theme="twoTone" />同批次</a>
            <a-divider type="vertical" />
            <a :disabled="record.freezeStock > 0" @click="handleUpdate('此托', record)"><a-icon class="m-r-4" type="right-circle" theme="twoTone" />此托</a>
          </div>
        </template>
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

    <a-modal v-model="submitVisible" :title="modalTitle" ok-text="确认" cancel-text="取消" @ok="onSubmit">
      <a-form layout="inline" class="search-content">
        <a-form-model-item label="质量状态">
          <a-select
            style="width: 180px"
            placeholder="请选择质量状态"
            allow-clear
            v-model="queryForm.cell"
          >
            <a-select-option v-for="item in qualityStatus" :key="item.id" :value="item.text">
              {{ item.text }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
      </a-form>
    </a-modal>

  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin'
import _ from 'lodash'

const qualityStatus = [
  {
    text: 'U',
    value: 0
  },
  {
    text: 'B',
    value: 1
  }
]

const colorMap = {
  '待检': 'orange',
  '已检': 'green',
  'U': 'green',
  'B': 'red',
  'Q': 'green'
}

const changeStatusMap = {
  0: '未变更',
  1: '已变更'
}

const columns = [
  {
    title: '工厂编码',
    key: 'plantNb',
    dataIndex: 'plantNb',
    width: 80
  },
  {
    title: '仓库编码',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 80
  },
  {
    title: '存储区编码',
    key: 'areaCode',
    dataIndex: 'areaCode',
    width: 90
  },
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 160,
    align: 'center'
  },
  {
    title: '库位编码',
    key: 'binCode',
    dataIndex: 'binCode',
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
    width: 80,
    scopedSlots: { customRender: 'qualityStatusSlot' }
  },
  {
    title: '状态',
    key: 'changeStatus',
    dataIndex: 'changeStatus',
    width: 80,
    scopedSlots: { customRender: 'changeStatusSlot' }
  },
  {
    title: '库存量',
    key: 'totalStock',
    dataIndex: 'totalStock',
    width: 120
  },
  {
    title: '单位',
    key: 'unit',
    dataIndex: 'unit',
    width: 80
  },
  {
    title: '冻结库存',
    key: 'freezeStock',
    dataIndex: 'freezeStock',
    width: 120
  },
  {
    title: '有效期',
    key: 'expireDate',
    dataIndex: 'expireDate',
    width: 120
  },
  {
    title: '修改人',
    key: 'updateBy',
    dataIndex: 'updateBy',
    width: 120
  },
  {
    title: '操作时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 200
  },
  {
    title: '质检操作',
    key: 'action',
    fixed: 'right',
    width: 180,
    scopedSlots: { customRender: 'action' }
  }
]

const queryFormAttr = () => {
  return {
    plantNb: '',
    wareCode: '',
    ssccNumber: '',
    materialNb: '',
    batchNb: '',
    areaCode: '',
    binCode: '',
    palletCode: '',
    qualityStatus: '',
    changeStatus: undefined,
    updateTimeList: []
  }
}

export default {
  name: 'Area',
  mixins: [mixinTableList],
  data () {
    return {
      tableLoading: false,
      uploadLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: [],
      submitVisible: false,
      modalTitle: ''
    }
  },
  computed: {
    qualityStatus: () => qualityStatus,
    changeStatusMap: () => changeStatusMap,
    colorMap: () => colorMap
  },
  methods: {
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async loadTableList () {
      try {
        this.tableLoading = true
        const { updateTimeList = [] } = this.queryForm
        const startUpdateTime = updateTimeList.length > 0 ? updateTimeList[0].format(this.startDateFormat) : undefined
        const endUpdateTime = updateTimeList.length > 0 ? updateTimeList[1].format(this.endDateFormat) : undefined
        const options = { ..._.omit(this.queryForm, ['updateTimeList']), startUpdateTime, endUpdateTime }

        const {
          data: { rows, total }
        // } = await this.$store.dispatch('stock/getPaginationList', this.queryForm)
        } = await this.$store.dispatch('iqcManagement/getIqcQualityList', options)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    async onSubmit (record) {
      // 调用API
      try {
        await this.$store.dispatch('iqcManagement/updateIqcQuality', record)
        this.$message.success('修改质检状态成功！')
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleUpdate (clickMethod, record) {
      if (clickMethod === '同批次') {
        this.modalTitle = '物料编码:' + record.materialNb + ',批次号:' + record.batchNb
      }
      if (clickMethod === '此托') {
        this.modalTitle = 'SSCC:' + record.ssccNumber + ',物料编码:' + record.materialNb
      }
      console.log(record.changeStatus)
      if (record.changeStatus === 1) {
        // 检验过
        this.notQuality()
      } else {
        // 没有检验过
        this.submitVisible = true
      }
    },
    notQuality () {
      this.$confirm({
        title: '此SSCC做过质检，是否确认再次变更质量状态?',
        onOk: () => {
          this.submitVisible = true
        }
      })
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
