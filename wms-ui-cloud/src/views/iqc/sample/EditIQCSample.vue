<template>
  <a-drawer width="80%" placement="right" :visible="visible" @close="onClose">
    <template slot="title">
      物料编码：{{ record.materialNb }}，批次号：{{ record.batchNb }}
    </template>
    <div class="table-content">
      <a-row>
        <a-col :span="24" >
          <a-form layout="inline" class="search-content">
            <a-row :gutter="16">
              <a-col :span="4">
                <a-form-model-item label="仓库编码">
                  <a-input v-model="queryForm.wareCode" placeholder="仓库编码" allow-clear />
                </a-form-model-item>
              </a-col>
              <a-col span="4">
                <span class="table-page-search-submitButtons">
                  <a-button type="primary" @click="handleSearch" :loading="searchLoading"><a-icon type="search" />查询</a-button>
                  <a-button style="margin-left: 8px" @click="handleResetQuery"><a-icon type="redo" />重置</a-button>
                </span>
              </a-col>
            </a-row>
          </a-form>

          <a-table
            :columns="columns"
            :data-source="list"
            :loading="tableLoading"
            :pagination="false"
            size="middle"
            :scroll="tableScroll"
          >
            <template slot="action" slot-scope="text, row">
              <div class="action-con">
                <a-popconfirm
                  class="custom-pop"
                  ok-text="确认"
                  cancel-text="取消"
                  placement="left"
                  @confirm="handleModifySSCC(row)"
                >
                  <template slot="title">
                    <p>确认提交吗？</p>
                    <a-form layout="inline" class="search-content">
                      <a-form-model-item label="抽样数量">
                        <a-input-number style="width:120px" v-model="row.sampleQuantity" :min="0" :max="row.availableStock"></a-input-number>
                      </a-form-model-item>
                    </a-form>
                  </template>
                  <a :disabled="row.freezeStock > 0"> <a-icon class="m-r-4" type="plus" />生成抽样计划</a>
                </a-popconfirm>
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
              @showSizeChange="onShowSizeChange"
              @change="changePagination"
            />
          </div>
        </a-col>
      </a-row>
    </div>
  </a-drawer>
</template>

<script>
import _ from 'lodash'
import { mixinTableList } from '@/utils/mixin'

const labelCol = {
  span: 5
}
const wrapperCol = {
  span: 19
}

const queryFormAttr = () => {
  return {
    'wareCode': ''
  }
}

const columns = [
  {
    title: 'SSCC码',
    key: 'ssccNumber',
    dataIndex: 'ssccNumber',
    width: 120
  },
  {
    title: '仓库编码',
    key: 'wareCode',
    dataIndex: 'wareCode',
    width: 120
  },
  {
    title: '库位编码',
    key: 'binCode',
    dataIndex: 'binCode',
    width: 140
  },
  {
    title: '质检状态',
    key: 'qualityStatus',
    dataIndex: 'qualityStatus',
    width: 80
  },
  {
    title: '库存量',
    key: 'totalStock',
    dataIndex: 'totalStock',
    width: 120
  },
  {
    title: '可用库存',
    key: 'availableStock',
    dataIndex: 'availableStock',
    width: 120
  },
  {
    title: '冻结库存',
    key: 'freezeStock',
    dataIndex: 'freezeStock',
    width: 120
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    width: 180,
    scopedSlots: { customRender: 'action' }
  }
]
export default {
  name: 'IqcSample',
  mixins: [mixinTableList],
  props: {
    materialNb: {
      type: String,
      default () {
        return ''
      }
    },
    iqcSampleVisible: {
      type: Boolean,
      default () {
        return false
      }
    }
  },
  data () {
    return {
      submitLoading: false,
      queryForm: {
        pageSize: 40,
        pageNum: 1,
        ...queryFormAttr()
      },
      form: this.$form.createForm(this),
      list: [],

      record: {},
      visible: false
    }
  },

  computed: {
    wrapperCol () {
      return wrapperCol
    },
    labelCol () {
      return labelCol
    },
    columns: () => columns
  },
  methods: {
    onOpen (record) {
      this.record = record
      this.visible = true

      this.loadData()
    },
    onClose () {
      this.visible = false
      this.form.resetFields()
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async handleModifySSCC (record) {
      const options = {
        sampleQuantity: record.sampleQuantity,
        sourceSsccNb: this.record.ssccNb,
        ssccNb: record.ssccNumber
      }
      try {
        this.submitLoading = true
        await this.$store.dispatch('iqcSample/modifySscc', options)
        this.$message.success('修改抽样计划成功！')

        this.onClose()
        this.$emit('on-ok')
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.submitLoading = false
      }
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const { batchNb, materialNb } = this.record
        const {
          data: { rows, total }
        } = await this.$store.dispatch('stock/getPaginationList', { ...this.queryForm, batchNb, materialNb })

        const rowsMap = _.map(rows, item => ({ ...item, sampleQuantity: 0 }))
        this.list = rowsMap
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    loadData () {
      this.loadTableList()
    }
  },
  watch: {
  }
}
</script>

<style lang="less" scoped>
.action {
  position: absolute;
  right: 0;
  bottom: 0;
  width: 100%;
  border-top: 1px solid #e9e9e9;
  padding: 10px 16px;
  background: #fff;
  text-align: right;
  z-index: 1;
}
/deep/.ant-drawer-body {
// overflow-y: auto;
  padding-bottom: 60px;
}

///deep/.ant-input-number {
//  width: 100%;
//}

</style>
