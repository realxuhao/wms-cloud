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
            <a-form-model-item label="物料号">
              <a-input v-model="queryForm.materialNb" placeholder="物料号" allow-clear/>
            </a-form-model-item>
          </a-col>

          <a-col :span="4">
            <a-form-model-item label="批次号">
              <a-input v-model="queryForm.batch" placeholder="批次号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="状态">
              <a-select
                allow-clear
                v-model="queryForm.status"
              >
                <a-select-option v-for="item in status" :key="item.value" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <template v-if="advanced">
            <a-col :span="4">
              <a-form-model-item label="物料编码">
                <a-input v-model="queryForm.sapMaterialCode" placeholder="物料编码" allow-clear/>
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
        <a-button type="primary" :disabled="!hasSelected" class="m-r-8" icon="plus" @click="changeStatus"> 调整</a-button>
        <a-tooltip placement="right">

          <template slot="title">
            <a style="color:#fff" @click="handleDownloadTemplate"><a-icon type="arrow-down" />下载模板</a>
          </template>
          <a-upload
            :file-list="[]"
            name="file"
            :multiple="true"
            :before-upload="()=>false"
            @change="handleUpload"
          >
            <a-button :loading="uploadLoading" type="primary" icon="upload" >
              导入
            </a-button>
          </a-upload>
        </a-tooltip>
      </div>
      <a-table
        :row-selection="{
        selectedRowKeys: selectedRowKeys,
        onChange: onSelectChange ,
        getCheckboxProps:record => ({
            props: {
              disabled: record.status !== 0
            },
          })}"
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        size="middle"
        :scroll="tableScroll"
      >
        <template slot="status" slot-scope="text">
          <div >
            <a-tag color="#99CCFF" v-if="text===0">
              不同
            </a-tag>
            <a-tag color="#87d068" v-if="text===1">
              相同
            </a-tag>
            <a-tag color="#ef7427de" v-if="text===2">
              已调整
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
    width: 80
  },
  {
    title: 'materialDescription',
    key: 'materialDescription',
    dataIndex: 'materialDescription',
    width: 80
  },
  {
    title: 'material',
    key: 'materialNb',
    dataIndex: 'materialNb',
    width: 80
  },
  {
    title: 'Unrestricted',
    key: 'unrestricted',
    dataIndex: 'unrestricted',
    width: 80
  },
  {
    title: 'Base Unit of Measure',
    key: 'unit',
    dataIndex: 'unit',
    width: 80
  },
  {
    title: 'Storage Location',
    key: 'storageLocation',
    dataIndex: 'storageLocation',
    width: 80
  },
  {
    title: 'Batch',
    key: 'batch',
    dataIndex: 'batch',
    width: 80
  },
  {
    title: 'In Quality Insp.',
    key: 'inQualityInsp',
    dataIndex: 'inQualityInsp',
    width: 80
  },
  {
    title: 'Restricted-Use Stock',
    key: 'restrictedUseStock',
    dataIndex: 'restrictedUseStock',
    width: 80
  },
  {
    title: 'Blocked',
    key: 'blocked',
    dataIndex: 'blocked',
    width: 80
  },
  {
    title: 'Returns',
    key: 'returns',
    dataIndex: 'returns',
    width: 80
  },
  {
    title: 'Stock in Transit',
    key: 'stockInTransit',
    dataIndex: 'stockInTransit',
    width: 80
  },
  {
    title: 'In transfer (plant)',
    key: 'inTransfer',
    dataIndex: 'inTransfer',
    width: 80
  },
  {
    title: 'TEKDAN',
    children: [
      {
        title: 'MaterialCode',
        dataIndex: 'sapMaterialCode',
        key: 'sapMaterialCode',
        width: 85,
      },

    ],
  },
  {
    title: 'WMS',
    children: [
      {
        title: 'stockQuantity',
        dataIndex: 'stockQuantity',
        key: 'stockQuantity',
        width: 85,
      }
    ],
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 80
  },
  {
    title: 'status',
    key: 'status',
    dataIndex: 'status',
    width: 80,
    scopedSlots: { customRender: 'status' }
  },
]

const queryFormAttr = () => {
  return {
    plantNb: '',
    id: '',
    sapMaterialCode: '',
    batch: '',
    status: '',
  }
}
const status = [
  {
    text: '不同',
    value: 0
  },
  {
    text: '相同',
    value: 1
  }
  ,
  {
    text: '已调整',
    value: 2
  }
]

export default {
  name: 'BinIn',
  mixins: [mixinTableList],
  data () {
    return {
      tableLoading: false,
      uploadLoading: false,
      columns,
      selectedRowKeys: [], // Check here to configure the default column
      list: [],
      queryForm: {
        pageSize: 10,
        pageNum: 1,
        ...queryFormAttr()
      }
    }
  },
  computed: {
    status: () => status,
    hasSelected() {
      return this.selectedRowKeys.length > 0
    }
  },

  methods: {
    async changeStatus() {
      try {
        this.submitLoading = true
        const options = {ssccList: this.selectedRowKeys}

        console.log(options)
        const {data: checkResult} = await this.$store.dispatch('comparison/updateBySsccList', options)

        this.selectedRowKeys = []
        this.loadTableList()

        this.$message.success('提交成功')
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.submitLoading = false
      }
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    handleDownloadTemplate(){
      try {
        this.$store.dispatch('file/downloadByFilename', 'FG_SAP 库存对比模板.xlsx')
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async handleUpload (e) {
      const { file } = e

      const formdata = new FormData()

      try {
        formdata.append('file', file)

        this.uploadLoading = true
        await this.$store.dispatch('comparison/uploadPro', formdata)

        this.queryForm.pageNum = 1
        this.loadTableList()

        this.uploadLoading = false

        this.$message.success('导入成功！')
      } catch (error) {
        if (error.code === 400) {
          this.$confirm({
            title: '是否更新？',
            content: '存在重复数据',
            onOk: () => {
              this.uploadBatchUpdate(formdata)
            },
            onCancel () {
            }
          })
        } else {
          this.$message.error(error.message)

        }
      }finally {
        this.uploadLoading = false
      }
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const {
          data: { rows, total }
        } = await this.$store.dispatch('comparison/getPaginationProList', this.queryForm)
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
