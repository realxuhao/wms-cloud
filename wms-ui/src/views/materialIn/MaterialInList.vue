<template>
  <div class="wrapper">
    <!-- table -->
    <div class="table-content">
      <a-form-model class="search-content" layout="inline" :model="queryForm">
        <a-form-model-item label="批次号">
          <a-input v-model="queryForm.batchNb" placeholder="批次号" />
        </a-form-model-item>
        <a-form-model-item label="物料名">
          <a-input v-model="queryForm.materialName" placeholder="物料名" />
        </a-form-model-item>
        <a-form-model-item>
          <a-button type="primary" icon="search" @click="handleSearch" :loading="searchLoading"> 搜索 </a-button>
        </a-form-model-item>
      </a-form-model>

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
              手动
            </a-tag>
            <!-- <a-tag color="#87d068" v-else>
              自动
            </a-tag>
            <a-tag color="#87d068" v-else>
              免检
            </a-tag> -->
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
    key: 'originPalletQuantity',
    dataIndex: 'originPalletQuantity',
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
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 200
  }
]

export default {
  name: 'Area',
  mixins: [mixinTableList],
  data () {
    return {
      tableLoading: false,
      uploadLoading: false,
      columns,
      list: []
    }
  },
  methods: {
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
