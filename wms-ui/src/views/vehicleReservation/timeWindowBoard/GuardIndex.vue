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
                <a-button
                  type="primary"
                  @click="handleSearch"
                  :loading="searchLoading"
                ><a-icon type="search" />查询</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'VrGuardTimeWindowBoardIndex',
  props: {
  },
  data () {
    return {
      searchLoading: false,
      queryForm: {
        wareId: null,
        statusList: [1]
      },
      wareOptionList: [],
      list: []

    }
  },
  methods: {
    /** 获取仓库List */
    async getWareOptionList () {
      this.wareOptionList = []
      try {
        const data = await this.$store.dispatch('ware/getOptionList')
        this.wareOptionList = data.data
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async handleSearch () {
      const that = this
      if (that.queryForm.wareId != null) {
        this.searchLoading = true
        await this.loadTableList()
        this.searchLoading = false
      }
    },
    async loadTableList () {
      try {
        this.list = []
        const { data } = await this.$store.dispatch('driverDispatch/getTodaySignlist', this.queryForm)
        this.list = data
      } catch (error) {
        this.$message.error(error.message)
      } finally {
      }
    },
    loadData () {
      this.getWareOptionList()
    }
  },
  mounted () {
    this.loadData()
  },
  watch: {
  }
}
</script>

<style lang="less" scoped>
</style>
