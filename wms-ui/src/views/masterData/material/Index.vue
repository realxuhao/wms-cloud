<template>
  <div class="wrapper">

    <!-- table -->
    <div class="table-content">
      <a-form-model class="search-content" layout="inline" :model="queryForm">
        <a-form-model-item label="物料名称">
          <a-input v-model="queryForm.name" placeholder="物料名称" />
        </a-form-model-item>
        <a-form-model-item label="物料代码">
          <a-input v-model="queryForm.code" placeholder="物料代码" />
        </a-form-model-item>
        <a-form-model-item label="物料类型">
          <a-select
            allowClear
            show-search
            :filter-option="filterOption"
            option-filter-prop="children"
            class="width180"
            placeholder="物料类型"
            v-model="queryForm.materialTypeId"
            :loading="materialTypeListLoading">
            <a-select-option
              :value="item.id"
              v-for="item in materialTypeList"
              :key="item.id">
              {{ item.description }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item >
          <a-button type="primary" icon="search" @click="handleSearch" :loading="searchLoading">
            搜索
          </a-button>
        </a-form-model-item>
      </a-form-model>

      <div class="action-content">
        <a-button type="primary" icon="plus" @click="handleAdd">
          新建
        </a-button>
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
            <a-divider type="vertical" />
            <a class="primary-color" @click="handleOpenDispathRule(record)"><a-icon class="m-r-4" type="setting" />分配库位</a>
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
          :current="currentPage"
          :page-size.sync="pageSize"
          :total="paginationTotal"
          @showSizeChange="loadTableList"
          @change="changePagination" />
      </div>

    </div>

    <UpdateDrawer
      v-model="visible"
      :updateType="updateType"
      :id="currentUpdateId"
      @on-ok="loadTableList"
    ></UpdateDrawer>

    <DispatchBin
      v-model="dispatchRuleVisible"
      :id="currentUpdateId"
    ></DispatchBin>
  </div>
</template>

<script>
import UpdateDrawer from './UpdateDrawer'
import DispatchBin from './DispatchBin'

const columns = [
  {
    title: '物料名称',
    dataIndex: 'name',
    key: 'name',
    width: 200,
    fixed: 'left'

  },
  {
    title: '物料代码',
    dataIndex: 'code',
    key: 'code',
    width: 200
  },
  {
    title: '物料类型',
    dataIndex: 'materialType',
    key: 'materialType',
    width: 200
  },
  {
    title: '包装重量',
    dataIndex: 'packageWeight',
    key: 'packageWeight',
    width: 200
  },
  {
    title: '托盘重量',
    dataIndex: 'palletWeight',
    key: 'palletWeight',
    width: 200

  },
  {
    title: '是否绑定托盘',
    key: 'bindPallet',
    dataIndex: 'bindPallet',
    width: 200,
    scopedSlots: { customRender: 'bindPallet' }
  },
  {
    title: '是否带托盘',
    key: 'hasPallet',
    dataIndex: 'hasPallet',
    width: 200,
    scopedSlots: { customRender: 'hasPallet' }

  },
  {
    title: '来料总重量',
    key: 'totalWeight',
    dataIndex: 'totalWeight',
    width: 200
  },
  {
    title: '物料防错方式',
    key: 'errorProofingMethod',
    dataIndex: 'errorProofingMethod',
    width: 200

  },
  {
    title: '允许负偏差比例（绝对值）',
    key: 'lessDeviationRatio',
    dataIndex: 'lessDeviationRatio',
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
    width: 200
  },
  {
    title: '单位',
    key: 'unit',
    dataIndex: 'unit',
    width: 100
  },
  {
    title: '备注',
    key: 'remark',
    dataIndex: 'remark',
    width: 200
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
    width: 240,
    scopedSlots: { customRender: 'action' }
  }
]

export default {
  name: 'Material',
  components: {
    UpdateDrawer,
    DispatchBin
  },
  data () {
    return {
      visible: false,
      updateType: 'add', // edit、add
      currentUpdateId: undefined,

      dispatchRuleVisible: false,

      materialTypeListLoading: false,
      materialTypeList: [],

      searchLoading: false,
      queryForm: {
        name: '',
        code: '',
        materialTypeId: undefined
      },
      pageSize: 20,
      currentPage: 1,
      paginationTotal: 0,
      tableLoading: false,
      columns,
      list: []
    }
  },
  methods: {
    changeTab () {
      this.currentPage = 1
      this.loadData()
    },
    changePagination (page) {
      this.currentPage = page
      this.loadData()
    },

    async handleSearch () {
      this.searchLoading = true
      await this.loadTableList()
      this.searchLoading = false
    },

    handleEdit (record) {
      this.updateType = 'edit'
      this.visible = true
      this.currentUpdateId = record.id
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

    handleAdd () {
      this.updateType = 'add'
      this.visible = true
      this.currentUpdateId = null
    },

    handleOpenDispathRule (record) {
      this.dispatchRuleVisible = true
      this.currentUpdateId = record.id
    },
    async loadTableList () {
      try {
        this.tableLoading = true

        const { data: { rows, total } } = await this.$store.dispatch('material/getList', this.queryForm)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error)
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
