<template>
  <div class="wrapper">

    <!-- table -->
    <div class="table-content">
      <a-form layout="inline" class="search-content">
        <a-row :gutter="16">
          <a-col :span="4">
            <a-form-model-item label="物料名称">
              <a-input v-model="queryForm.name" placeholder="物料名称" allow-clear/>
            </a-form-model-item>

          </a-col>
          <a-col :span="4">
            <a-form-model-item label="物料编码">
              <a-input v-model="queryForm.code" placeholder="物料编码" allow-clear/>
            </a-form-model-item>
          </a-col>
          <a-col :span="4">
            <a-form-model-item label="物料类型">
              <a-select
                allowClear
                show-search
                :filter-option="filterOption"
                option-filter-prop="children"
                style="width:100%"
                placeholder="物料类型"
                v-model="queryForm.materialTypeId"
                :loading="materialTypeListLoading">
                <a-select-option
                  :value="item.id"
                  v-for="item in materialTypeList"
                  :key="item.id">
                  {{ item.code }}</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <template v-if="advanced">

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
        :scroll="{ x: 1300 }"
      >
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

        <template slot="bindPallet" slot-scope="text">
          <a-tag v-if="!text" color="#f50">否</a-tag>
          <a-tag v-if="text===1" color="#87d068">是</a-tag>
        </template>

        <template slot="hasPallet" slot-scope="text">
          <a-tag v-if="!text" color="#f50">否</a-tag>
          <a-tag v-if="text===1" color="#87d068">是</a-tag>
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
          @change="changePagination" />
      </div>

    </div>

    <UpdateDrawer
      v-model="visible"
      :updateType="updateType"
      :id="currentUpdateId"
      @on-ok="loadTableList"
    ></UpdateDrawer>

  </div>
</template>

<script>
import UpdateDrawer from './UpdateDrawer'

import { mixinTableList } from '@/utils/mixin/index'

const columns = [
  {
    title: '物料名称',
    dataIndex: 'name',
    key: 'name',
    width: 100,
    fixed: 'left'

  },
  {
    title: '物料编码',
    dataIndex: 'code',
    key: 'code',
    width: 100
  },
  {
    title: '物料类型',
    dataIndex: 'materialType',
    key: 'materialType',
    width: 100
  },
  {
    title: '托盘类型',
    dataIndex: 'palletType',
    key: 'palletType',
    width: 80
  },
  // {
  //   title: '包装重量',
  //   dataIndex: 'packageWeight',
  //   key: 'packageWeight',
  //   width: 100
  // },
  {
    title: '托盘重量',
    dataIndex: 'palletWeight',
    key: 'palletWeight',
    width: 100

  },
  // {
  //   title: '是否绑定托盘',
  //   key: 'bindPallet',
  //   dataIndex: 'bindPallet',
  //   width: 200,
  //   scopedSlots: { customRender: 'bindPallet' }
  // },
  // {
  //   title: '是否带托盘',
  //   key: 'hasPallet',
  //   dataIndex: 'hasPallet',
  //   width: 200,
  //   scopedSlots: { customRender: 'hasPallet' }

  // },
  {
    title: '来料总重量',
    key: 'totalWeight',
    dataIndex: 'totalWeight',
    width: 100
  },
  {
    title: '物料防错方式',
    key: 'errorProofingMethod',
    dataIndex: 'errorProofingMethod',
    width: 100

  },
  // {
  //   title: '允许负偏差比例（绝对值）',
  //   key: 'lessDeviationRatio',
  //   dataIndex: 'lessDeviationRatio',
  //   width: 200
  // },
  {
    title: '最小包装毛重',
    key: 'packageWeight',
    dataIndex: 'packageWeight',
    width: 200
  },
  {
    title: '最小包装重量（净重）',
    key: 'minPackageNetWeight',
    dataIndex: 'minPackageNetWeight',
    width: 200
  },
  {
    title: '最小包装数量',
    key: 'minPackageNumber',
    dataIndex: 'minPackageNumber',
    width: 140
  },
  {
    title: '单位',
    key: 'unit',
    dataIndex: 'unit',
    width: 100
  },
  {
    title: '最小包装量允许最小值',
    key: 'lessDeviationRatio',
    dataIndex: 'lessDeviationRatio',
    width: 200
  },
  {
    title: '最小包装量允许最大值',
    key: 'moreDeviationRatio',
    dataIndex: 'moreDeviationRatio',
    width: 200
  },
  {
    title: '标准计数单位[L,Kg,m,㎡]对应的重量值KG[只针对称重物料]',
    key: 'transferWeightRatio',
    dataIndex: 'transferWeightRatio',
    width: 240
  },
  {
    title: '备注',
    key: 'remark',
    dataIndex: 'remark',
    width: 100
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 200
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
    code: '',
    name: '',
    materialTypeId: ''
  }
}

export default {
  name: 'Material',
  mixins: [mixinTableList],
  components: {
    UpdateDrawer
  },
  data () {
    return {
      dispatchRuleVisible: false,

      materialTypeListLoading: false,
      materialTypeList: [],

      columns,
      list: [],
      queryForm: {
        pageSize: 20,
        pageNum: 1,
        ...queryFormAttr()
      },
      uploadLoading: false
    }
  },
  methods: {
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    async handleDownloadTemplate () {
      try {
        this.$store.dispatch('file/downloadByFilename', '物料.xlsx')
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async uploadBatchUpdate (formdata) {
      try {
        await this.$store.dispatch('material/uploadBatchUpdate', formdata)
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
        await this.$store.dispatch('material/upload', formdata)

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
        await this.$store.dispatch('material/destroy', record.id)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
    },

    handleOpenDispathRule (record) {
      this.dispatchRuleVisible = true
      this.currentUpdateId = record.id
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const { data: { rows, total } } = await this.$store.dispatch('material/getPaginationList', this.queryForm)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },

    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      )
    },

    async loadMaterialTypeList () {
      try {
        this.materialTypeListLoading = true

        const { data: { rows } } = await this.$store.dispatch('materialType/getList', { pageSize: 0 })
        this.materialTypeList = rows
      } catch (error) {
        this.$message.error('获取物料类型失败，请联系管理员！')
      } finally {
        this.materialTypeListLoading = false
      }
    },
    async loadData () {
      this.loadTableList()
      this.loadMaterialTypeList()
    }
  },
  mounted () {
    this.loadData()
  }
}
</script>

<style lang="less" scoped>

</style>
