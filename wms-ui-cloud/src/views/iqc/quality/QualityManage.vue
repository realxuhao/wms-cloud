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
              <a-form-model-item label="cell部门">
                <a-select
                  placeholder="请选择cell部门"
                  allow-clear
                  v-model="queryForm.cell"
                >
                  <a-select-option v-for="item in cellList" :key="item.id" :value="item.name">
                    {{ item.name }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
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
        <a-upload
          :file-list="[]"
          name="file"
          :multiple="true"
          :before-upload="()=>false"
          @change="handleUpload"
        >
          <a-button
            type="primary"
            :loading="uploadLoading"
            icon="upload"
            v-hasPermi="['iqc:list:batchEdit']"
          >批量修改质检状态</a-button>
        </a-upload>
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
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a v-hasPermi="['iqc:list:updateBatch']" :disabled="record.freezeStock > 0" @click="handleUpdate('同批次', record)"><a-icon class="m-r-4" type="right-circle" theme="twoTone" />同批次</a>
            <a-divider type="vertical" />
            <a v-hasPermi="['iqc:list:updatePallet']" :disabled="record.freezeStock > 0" @click="handleUpdate('此托', record)"><a-icon class="m-r-4" type="right-circle" theme="twoTone" />此托</a>
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
      <!--      <a-form layout="inline" class="search-content">
        <a-form-model-item label="质量状态">
          <a-select
            style="width: 180px"
            placeholder="请选择质量状态"
            allow-clear
            v-model="queryForm.qualityStatus"
          >
            <a-select-option v-for="item in qualityStatus" :key="item.id" :value="item.text">
              {{ item.text }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
      </a-form>-->
      <a-form>
        <a-form-item label="起始质量状态" v-show="qualityType==='1'">
          <a-checkbox-group v-model="fromStatusList">
            <a-checkbox v-for="item in qualityStatus" :key="item.id" :value="item.text">
              {{ item.text }}
            </a-checkbox>
          </a-checkbox-group>
        </a-form-item>
        <a-form-item label="请选择质量状态">
          <!-- <a-radio-group v-model="fromStatusList">
            <a-radio v-for="item in qualityStatus" :key="item.id" :value="item.text">
              {{ item.text }}
            </a-radio>
          </a-radio-group> -->
          <a-radio-group v-model="radioStatus" @change="radioChange">
            <a-radio v-for="item in qualityStatus" :key="item.id" :value="item.text">
              {{ item.text }}
            </a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal
      v-model="editSSCCVisible"
      :width="800"
      title="确认质检状态"
      :ok-button-props="{ props: { disabled: confirmDisable } }"
      @cancel="handleCancel"
      @ok="handleSubmit"
      :confirm-loading="confirmLoading">
      <a-table
        :columns="editSSCCColumns"
        :data-source="editSSCCList"
        rowKey="ssccNumber"
        :pagination="false"
        size="middle"
        :scroll="{ y: 500 }"
      >
        <template slot="index" slot-scope="text">
          {{ text }}
        </template>
        <template slot="status" slot-scope="text">
          <a-tag :color="statusColroMap[text]" >
            {{ statusTextMap[text] }}
          </a-tag>
        </template>
      </a-table>
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
  },
  {
    text: 'Q',
    value: 2
  }
]

const colorMap = {
  '待检': 'orange',
  '已检': 'green',
  'U': 'green',
  'B': 'red',
  'Q': 'blue',
  '0': 'orange',
  '1': 'green'
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
    title: 'cell部门',
    key: 'cell',
    dataIndex: 'cell',
    width: 90
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

const editSSCCColumns = [
  {
    title: '序号',
    key: 'index',
    dataIndex: 'index'
  },
  {
    title: 'SSCC码',
    key: 'ssccnumber',
    dataIndex: 'ssccnumber'
  },
  {
    title: '质检状态',
    key: 'finalSAPStatus',
    dataIndex: 'finalSAPStatus'
  },
  {
    title: '更新状态',
    key: 'status',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' }
  }
]

const statusTextMap = {
  '0': '成功',
  '1': '失败',
  '2': '待确认'
}

const statusColroMap = {
  '0': 'green',
  '1': 'red',
  '2': 'orange'
}

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
    cell: undefined,
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
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      columns,
      list: [],
      submitVisible: false,
      modalTitle: '',
      modalRecord: undefined,
      radioStatus: undefined,
      qualityType: undefined,
      fromStatusList: [],

      uploadLoading: false,
      editSSCCVisible: false,
      editSSCCList: [],
      confirmLoading: false,
      confirmDisable: false,
      cellList: []
    }
  },
  computed: {
    editSSCCColumns: () => editSSCCColumns,
    qualityStatus: () => qualityStatus,
    changeStatusMap: () => changeStatusMap,
    colorMap: () => colorMap,
    statusTextMap: () => statusTextMap,
    statusColroMap: () => statusColroMap
  },
  methods: {
    async handleUpload (e) {
      const { file } = e

      const formdata = new FormData()

      try {
        formdata.append('file', file)

        this.uploadLoading = true
        const { data } = await this.$store.dispatch('iqcManagement/uploadEditSSCCStatusFile', formdata)

        this.editSSCCList = _.map(data, (x, index) => ({ ...x, status: 2, index: index + 1 }))
        this.editSSCCVisible = true
        this.confirmDisable = false
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.uploadLoading = false
      }
    },
    async loadCellList () {
      const { data } = await this.$store.dispatch('department/getList')
      this.cellList = data
    },
    async handleSubmit () {
      try {
        this.confirmLoading = true
        const { data } = await this.$store.dispatch('iqcManagement/saveEditSSCCStatusList', this.editSSCCList)
        this.editSSCCList = _.map(data, (x, index) => ({ ...x, index: index + 1 }))
        this.$message.success('提交成功')
        this.confirmDisable = true
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.confirmLoading = false
      }
    },
    handleCancel () {
      this.loadTableList()
    },
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
        // await this.$store.dispatch('iqcManagement/updateIqcQuality', record)
        const options = { ...this.modalRecord, 'type': this.qualityType, fromStatusList: this.fromStatusList }
        await this.$store.dispatch('iqcManagement/updateIqcQuality', options)
        this.$message.success('修改质检状态成功！')
        this.submitVisible = false
        this.fromStatusList = []
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleUpdate (clickMethod, record) {
      if (clickMethod === '同批次') {
        this.modalTitle = '物料编码:' + record.materialNb + ',批次号:' + record.batchNb
        this.qualityType = '1'
      }
      if (clickMethod === '此托') {
        this.modalTitle = 'SSCC:' + record.ssccNumber + ',物料编码:' + record.materialNb
        this.qualityType = '0'
      }
      console.log(record.changeStatus)
      if (record.changeStatus === 1) {
        // 检验过
        this.notQuality()
      } else {
        // 没有检验过
        this.submitVisible = true
      }
      this.modalRecord = record
      this.radioStatus = record.qualityStatus
    },
    notQuality () {
      this.$confirm({
        title: '此SSCC做过质检，是否确认再次变更质量状态?',
        onOk: () => {
          this.submitVisible = true
        }
      })
    },
    radioChange () {
      this.modalRecord.qualityStatus = this.radioStatus
    },
    async loadData () {
      this.loadTableList()
    }
  },
  mounted () {
    this.loadData()
    this.loadCellList()
  }
}
</script>

<style lang="less" scoped>
</style>
