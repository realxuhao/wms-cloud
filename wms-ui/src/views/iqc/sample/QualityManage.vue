<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="工厂编码">
              <a-input v-model="queryForm.plantNb" placeholder="工厂编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="仓库编码">
              <a-input v-model="queryForm.wareCode" placeholder="仓库编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
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
            <a-form-item label="状态">
              <a-input v-model="queryForm.changeStatus" placeholder="状态" allow-clear/>
            </a-form-item>
          </a-col>

          <template v-if="advanced">
            <a-col :span="4">
              <a-form-item label="托盘编码">
                <a-input v-model="queryForm.palletCode" placeholder="托盘编码" allow-clear/>
              </a-form-item>
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
              <a-form-model-item label="批次号">
                <a-input v-model="queryForm.batchNb" placeholder="批次号" allow-clear/>
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="操作人">
                <a-input v-model="queryForm.operateUser" placeholder="操作人" allow-clear/>
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

      </div>
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        size="middle"
        :scroll="{ x: 1300 }"
      >
        <template slot="qualityStatusSlot" slot-scope="text">
          <a-tag :color="colorMap[text]">
            {{ text }}
          </a-tag>
        </template>
        <template slot="changeStatusSlot" slot-scope="text">
          <a-tag :color="colorMap[text]">
            {{ text }}
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
            <a @click="handleUpdate(record)"><a-icon class="m-r-4" type="edit" />同批次</a>
            <a @click="handleUpdate(record)"><a-icon class="m-r-4" type="form" />此托</a>
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

    <a-modal v-model="submitVisible" title="Modal" ok-text="确认" cancel-text="取消" @ok="onSubmit">
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
import { mixinTableList } from '@/utils/mixin/index'

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

const columns = [
  {
    title: '工厂编码',
    key: 'plantNb',
    dataIndex: 'plantNb',
    width: 120
  },
  {
    title: '仓库编码',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 120
  },
  {
    title: '存储区编码',
    key: 'areaCode',
    dataIndex: 'areaCode',
    width: 120
  },
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 140
  },
  {
    title: '库位编码',
    key: 'binCode',
    dataIndex: 'binCode',
    width: 140
  },
  {
    title: '托盘编码',
    key: 'palletCode',
    dataIndex: 'palletCode',
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
    title: '操作人',
    key: 'createBy',
    dataIndex: 'createBy',
    width: 120
  },
  {
    title: '操作时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 200
  },
  {
    title: '操作',
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
    changeStatus: undefined
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
      submitVisible: false
    }
  },
  computed: {
    qualityStatus: () => qualityStatus,
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

        const {
          data: { rows, total }
        } = await this.$store.dispatch('stock/getPaginationList', this.queryForm)
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
        await this.$store.dispatch('iqcSample/updateIqcQuality', record)
        this.$message.success('修改质检状态成功！')
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleUpdate (record) {
      // todo
      const flag = true
      if (flag) {
      // if (record.status) {
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
