<template>
  <div class="wrapper">
    <div class="table-content">
      <div class="action-content">
        <a-form layout="inline" class="search-content">
          <a-row :gutter="16">
            <a-col :span="4">
              <a-form-model-item label="仓库编码">
                <a-select show-search v-model="queryForm.wareId" style="width: 100%" placeholder="仓库编码">
                  <a-select-option v-for="item in wareOptionList" :key="item.id" :value="item.id">
                    {{ item.code }}</a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>

            <a-col span="4">
              <span class="table-page-search-submitButtons">
                <a-button type="primary" @click="handleSearch" :loading="searchLoading"
                  ><a-icon type="search" />查询</a-button
                >
                <a-button style="margin-left: 8px" type="primary" @click="handleSave" :loading="saveLoading"
                  ><a-icon type="save" />保存</a-button
                >
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <TimeWindowTable 
        :wareId="queryForm.wareId"
        :allDockNum="queryForm.wareId == null ? null : wareOptionList.find(x => x.id == queryForm.wareId).dockNum"
        :isSave="saveLoading"
        @on-ok="childernSave"
      ></TimeWindowTable>
    </div>
  </div>
</template>

<script>
import TimeWindowTable from './TimeWindowTable'

const columns = [
  {
    title: '时间窗口起',
    key: 'startTime',
    dataIndex: 'startTime',
    width: 200,
  },
  {
    title: '时间窗口止',
    key: 'endTime',
    dataIndex: 'endTime',
    width: 200,
  },
  {
    title: '道口',
    key: 'windowCode',
    dataIndex: 'windowCode',
    width: 200,
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
    width: 200,
  },
  {
    title: '操作',
    key: 'action',
    width: 200,
    scopedSlots: { customRender: 'action' },
  },
]

const queryFormAttr = () => {
  return {
    wareId: null,
  }
}

export default {
  name: 'TimeWindow',
  components: {
    TimeWindowTable,
  },
  data() {
    return {
      visible: false,
      updateType: 'add', // edit、add
      currentUpdateId: 0,

      searchLoading: false,
      saveLoading: false,
      queryForm: {
        ...queryFormAttr(),
        pageSize: 20,
        pageNum: 1,
      },
      paginationTotal: 0,

      tableLoading: false,
      columns,
      list: [],
      wareOptionList: [
        { id: 1, code: 'W01', dockNum: 5 },
        { id: 2, code: 'W01', dockNum: 6 },
      ],
    }
  },
  methods: {
    handleResetQuery() {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    onShowSizeChange() {
      this.queryForm.pageNum = 1
      this.loadTableList()
    },
    changePagination(page) {
      this.queryForm.pageNum = page
      this.loadTableList()
    },

    async handleSearch() {
      this.searchLoading = true
      await this.loadTableList()
      this.searchLoading = false
    },
    /** 通知子页面保存time window数据 */
    handleSave() {
      if(this.queryForm.wareId == null){
        this.$message.error('请选择要保存的仓库！')
        return        
      }
      this.saveLoading = true
    },
    /** 接收Time window数据保存返回值 */
    childernSave(val){
      if(val == true){
        this.loadTableList();
      }
    },
    handleEdit(record) {
      this.updateType = 'edit'
      this.visible = true
      this.currentUpdateId = record.id
    },
    async handleDelete(record) {
      try {
        await this.$store.dispatch('timeWindow/destroy', record.id)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
    },

    handleAdd() {
      this.updateType = 'add'
      this.visible = true
      this.currentUpdateId = null
    },

    async loadTableList() {
      try {
        this.tableLoading = true
        this.saveLoading = false;

        const { rows, total } = await this.$store.dispatch('timeWindow/getList', this.queryForm)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    /** 获取仓库List */
    async getWareOptionList () {
      try {
        const data = await this.$store.dispatch('timeWindow/getWareOptionList')
        this.wareOptionList = data
        if(this.wareOptionList.length > 0){
          this.queryForm.wareId = this.wareOptionList[0].id;
        }
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async loadData() {
      this.loadTableList()
    },
  },
  mounted() {
    this.loadData()
  },
}
</script>

<style lang="less" scoped>
</style>
