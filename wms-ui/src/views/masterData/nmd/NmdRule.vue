<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="料号">
              <a-input v-model="queryForm.materialCode" placeholder="料号" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="分类">
              <a-select
                allow-clear
                v-model="queryForm.classification"
              >
                <a-select-option v-for="item in category" :key="item.value" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="检验水平级别">
              <a-select
                allow-clear
                v-model="queryForm.level"
              >
                <a-select-option v-for="item in checkLevel" :key="item.value" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="严格程度">
              <a-select
                allow-clear
                v-model="queryForm.plan"
              >
                <a-select-option v-for="item in strictLevel" :key="item.value" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
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
        <a-button type="primary" class="m-r-8" icon="plus" @click="handleAdd">
          新建
        </a-button>
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
        :columns="columns"
        :data-source="list"
        :loading="tableLoading"
        rowKey="id"
        :pagination="false"
        size="middle"
        :scroll="tableScroll"
      >
        <template slot="updateBy" slot-scope="text, record">
          {{ record.updateBy?record.updateBy:record.createBy }}
        </template>
        <template slot="updateTime" slot-scope="text, record">
          {{ record.updateTime?record.updateTime:record.createTime }}
        </template>
        <template slot="classification" slot-scope="text">
          {{ classificationTextMap[text] }}
        </template>
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a class="warning-color" @click="handleEdit(record)"><a-icon class="m-r-4" type="edit" />编辑</a>
            <a-divider type="vertical" />
            <a-popconfirm
              title="确认要删除吗?"
              ok-text="确认"
              cancel-text="取消"
              @confirm="handleDelete(record)"
            >
              <a class="danger-color"><a-icon class="m-r-4" type="delete" />删除</a>
            </a-popconfirm>
            <!-- <a
              class="primary-color"
              @click="handleOpenDispathRule(record)"><a-icon class="m-r-4" type="setting" />分配跨类型</a> -->
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
    <UpdateDrawer
      v-model="visible"
      :updateType="updateType"
      :id="currentUpdateId"
      :category="category"
      :strictLevel="strictLevel"
      :checkLevel="checkLevel"
      @on-ok="loadTableList"
    ></UpdateDrawer>

  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin'
import UpdateDrawer from './UpdateDrawer'

const category = [
  {
    text: 'Components',
    value: 0
  },
  {
    text: 'Package',
    value: 1
  }
]

const checkLevel = [
  {
    text: 'S-1',
    value: 'S-1'
  },
  {
    text: 'S-2',
    value: 'S-2'
  },
  {
    text: 'S-3',
    value: 'S-3'
  },
  {
    text: 'S-4',
    value: 'S-4'
  },
  {
    text: 'Ⅰ',
    value: 'Ⅰ'
  },
  {
    text: 'Ⅱ',
    value: 'Ⅱ'
  },
  {
    text: 'Ⅲ',
    value: 'Ⅲ'
  }
]

const strictLevel = [
  {
    text: '正常',
    value: 1
  },
  {
    text: '严格',
    value: 2
  },
  {
    text: '放宽',
    value: 3
  }
]

const classificationTextMap = {
  0: 'Components',
  1: 'Package'
}

const columns = [
  {
    title: '料号',
    key: 'materialCode',
    dataIndex: 'materialCode',
    width: 120
  },
  {
    title: '分类',
    key: 'classification',
    dataIndex: 'classification',
    width: 120,
    scopedSlots: { customRender: 'classification' }
  },
  {
    title: '校验水平级别',
    key: 'level',
    dataIndex: 'level',
    width: 120
  },
  {
    title: '正常[1]/加严[2]/放宽[3]',
    key: 'plan',
    dataIndex: 'plan',
    width: 140
  },
  {
    title: '操作人',
    key: 'updateBy',
    dataIndex: 'updateBy',
    width: 120,
    scopedSlots: { customRender: 'updateBy' }
  },
  {
    title: '操作时间',
    key: 'updateTime',
    dataIndex: 'updateTime',
    width: 200,
    scopedSlots: { customRender: 'updateTime' }
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    width: 160,
    scopedSlots: { customRender: 'action' }
  }
]

const queryFormAttr = () => {
  return {
    materialCode: '',
    classification: '',
    level: '',
    plan: ''
  }
}

export default {
  name: 'NmdRule',
  components: {
    UpdateDrawer
  },
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
      list: []
    }
  },
  computed: {
    category: () => category,
    strictLevel: () => strictLevel,
    checkLevel: () => checkLevel,
    classificationTextMap: () => classificationTextMap
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
        } = await this.$store.dispatch('nmdRule/getList', this.queryForm)
        console.log('rows')
        console.log(rows)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    handleAdd () {
      this.updateType = 'add'
      this.visible = true
      this.currentUpdateId = null
    },
    async handleDelete (record) {
      try {
        await this.$store.dispatch('nmdRule/destroy', record.id)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
    },
    async handleDownloadTemplate () {
      try {
        this.$store.dispatch('file/downloadByFilename', 'NMD.xlsx')
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async uploadBatchUpdate (formdata) {
      try {
        await this.$store.dispatch('nmdRule/uploadBatchUpdate', formdata)
        this.loadTableList()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.uploadLoading = false
      }
    },
    async handleUpload (e) {
      const { file } = e

      const formdata = new FormData()

      try {
        formdata.append('file', file)

        this.uploadLoading = true
        await this.$store.dispatch('nmdRule/upload', formdata)

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
            onCancel: () => {
              this.uploadLoading = false
            }
          })
        } else {
          this.$message.error(error.message)
          this.uploadLoading = false
        }
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

<style scoped>

</style>
