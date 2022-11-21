<template>
  <!-- <a-drawer
    width="800px"
    title="设置分配规则"
    placement="right"
    :visible="visible"
    @close="onClose"
  >
    <div v-show="!formVisible">
      <a-button
        class="m-b-8 m-r-8"
        type="primary"
        icon="plus"
        @click="handleAdd">
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

    <a-form v-show="formVisible" class="search-content m-b-8" layout="inline" :form="form">
      <a-form-item label="跨类型">
        <a-select
          show-search
          class="width180"
          v-decorator="[
            'frameTypeCode',
            { rules: [{ required: true, message: '请选择跨类型!' },] }
          ]">
          <a-select-option v-for="item in frameTypeList" :key="item" :value="item">{{ item }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="优先级">
        <a-input
          placeholder="优先级"
          v-decorator="[
            'priorityLevel',
            { rules: [{ required: true, message: '请输入优先级!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="">
        <a-button class="m-r-8" type="primary" @click="handleSubmit" :loading="submitLoading">确认</a-button>
        <a-button @click="handleCancel" :loading="submitLoading">取消</a-button>
      </a-form-item>
    </a-form>

  </a-drawer> -->
  <div class="wrapper">
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="物料名称">
              <a-input v-model="queryForm.materialName" placeholder="物料名称" allow-clear/>
            </a-form-model-item>

          </a-col>
          <a-col :span="4">
            <a-form-model-item label="物料编码">
              <a-input v-model="queryForm.materialCode" placeholder="物料编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="跨类型">
              <a-select
                show-search
                v-model="queryForm.frameTypeCode"
                style="width:100%"
              >
                <a-select-option v-for="item in frameTypeList" :key="item" :value="item">{{ item }}</a-select-option>
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
      >
        <template slot="action" slot-scope="text, record">
          <div class="action-con">
            <a class="warning-color" @click="handleEdit(record)"><a-icon class="m-r-4" type="edit" />编辑</a>
            <a-divider type="vertical" />
            <a-popconfirm title="确认要删除吗?" ok-text="确认" cancel-text="取消" @confirm="handleDelete(record)">
              <a class="danger-color"><a-icon class="m-r-4" type="delete" />删除</a>
            </a-popconfirm>
          </div>
        </template>
      </a-table>
    </div>

    <UpdateDrawer
      v-model="visible"
      :updateType="updateType"
      :id="currentUpdateId"
      @on-ok="loadTableList"
      :frameTypeList="frameTypeList"
      :materialList="materialList"
    ></UpdateDrawer>
  </div>
</template>

<script>
import { mixinTableList } from '@/utils/mixin/index'
import UpdateDrawer from './DispatchRuleUpdateDrawer'

const labelCol = {
  span: 5
}
const wrapperCol = {
  span: 19
}

const columns = [
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 200
  },
  {
    title: '跨类型',
    key: 'frameTypeCode',
    dataIndex: 'frameTypeCode',
    width: 200
  },
  {
    title: '优先级',
    key: 'priorityLevel',
    dataIndex: 'priorityLevel',
    width: 200
  },
  {
    title: '操作',
    key: 'action',
    width: 60,
    scopedSlots: { customRender: 'action' }
  }
]

const queryFormAttr = () => {
  return {
    code: '',
    name: '',
    frameType: ''
  }
}

export default {
  name: 'MaterialDispatchRule',
  mixins: [mixinTableList],
  components: {
    UpdateDrawer
  },
  data () {
    return {
      form: this.$form.createForm(this),
      submitLoading: false,

      columns,
      list: [],
      tableLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },

      frameTypeList: [],
      materialList: [],
      formVisible: false,
      uploadLoading: false
    }
  },
  computed: {
    labelCol () {
      return labelCol
    },
    wrapperCol () {
      return wrapperCol
    }
  },
  methods: {
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async handleDownloadTemplate () {
      try {
        this.$store.dispatch('file/downloadByFilename', '物料跨对应关系表.xlsx')
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async uploadBatchUpdate (formdata) {
      try {
        await this.$store.dispatch('material/uploadMaterialBinBatchUpdate', formdata)
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
        await this.$store.dispatch('material/uploadMaterialBin', formdata)

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
          this.uploadLoading = false
        }
      }
    },
    async handleDelete (record) {
      try {
        await this.$store.dispatch('material/destroyDispatchBin', record.id)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
    },

    async loadTableList () {
      try {
        this.tableLoading = true

        const { data: { rows } } = await this.$store.dispatch('material/getDispatchFrameTypeList', this.queryForm)
        this.list = rows
      } catch (error) {
        this.$message.error(error)
      } finally {
        this.tableLoading = false
      }
    },
    async getFrameTypeList () {
      try {
        const data = await this.$store.dispatch('frame/getFrameTypeList')
        this.frameTypeList = data
      } catch (error) {
        this.$message.error(error)
      }
    },
    async getMaterialList () {
      try {
        const rows = await this.$store.dispatch('material/getList')
        this.materialList = rows
      } catch (error) {
        console.log(error)
        this.$message.error('获取物料列表失败，请联系管理员！')
      }
    },
    async loadData () {
      await this.loadTableList()
      this.getFrameTypeList()
      this.getMaterialList()
    }
  },
  mounted () {
    this.loadData()
  }
}
</script>

<style lang="less" scoped>

</style>
