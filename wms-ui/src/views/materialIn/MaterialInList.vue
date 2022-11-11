<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="工厂编码">
              <a-input v-model="queryForm.plantNb" placeholder="工厂编码" />
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="仓库编码">
              <a-input v-model="queryForm.wareCode" placeholder="仓库编码" />
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

          <template v-if="advanced">
            <a-col :span="4">
              <a-form-model-item label="批次号">
                <a-input v-model="queryForm.batchNb" placeholder="批次号" />
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="检验方式">
                <a-select
                  show-search
                  placeholder="检验方式"
                  style="width:100%"
                  v-model="queryForm.checkType"
                >
                  <a-select-option v-for="item in checkTypeList" :key="item.value" :value="item.value">{{ item.text }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="操作人">
                <a-input v-model="queryForm.operateUser" placeholder="操作人" />
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

  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'

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
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
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
    title: '检验方式',
    key: 'checkType',
    dataIndex: 'checkType',
    scopedSlots: { customRender: 'checkType' },
    width: 200
  },
  {
    title: '应检查数量',
    key: 'checkQuantity',
    dataIndex: 'checkQuantity',
    width: 200
  },
  {
    title: '实际数量',
    key: 'actualQuantity',
    dataIndex: 'actualQuantity',
    width: 200
  },
  {
    title: '结果',
    key: 'actualResult',
    dataIndex: 'actualResult',
    width: 200
  },
  {
    title: '实际平均结果',
    key: 'averageResult',
    dataIndex: 'averageResult',
    width: 200
  },
  {
    title: '最小标准',
    key: 'minStandard',
    dataIndex: 'minStandard',
    width: 200
  },
  {
    title: '最大标准',
    key: 'maxStandard',
    dataIndex: 'maxStandard',
    width: 200
  },
  {
    title: '原托数',
    key: 'originalPalletQuantity',
    dataIndex: 'originalPalletQuantity',
    width: 200
  },
  {
    title: '操作人',
    key: 'operateUser',
    dataIndex: 'operateUser',
    width: 200
  },
  {
    title: '操作时间',
    key: 'operateTime',
    dataIndex: 'operateTime',
    width: 200
  }
]

const checkTypeList = [
  {
    value: 0,
    text: '称重'
  },
  {
    value: 1,
    text: '数数'
  },
  {
    value: 2,
    text: '免检'
  }
]

const queryFormAttr = () => {
  return {
    plantNb: '',
    wareCode: '',
    ssccNumber: '',
    materialNb: '',
    batchNb: '',
    checkType: '',
    operateUser: ''
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
      checkTypeList
    }
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
        } = await this.$store.dispatch('materialInList/getPaginationList', this.queryForm)
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
