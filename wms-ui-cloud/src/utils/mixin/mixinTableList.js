const mixinTableList = {
  data () {
    return {
      visible: false,
      updateType: 'add', // edit、add
      currentUpdateId: 0,

      // 高级搜索 展开/关闭
      advanced: false,

      searchLoading: false,
      queryForm: {
        pageSize: 20,
        pageNum: 1
      },
      paginationTotal: 0,

      tableLoading: false
    }
  },
  computed: {
    tableScroll: () => ({ x: 1300, y: 700 }),
    startDateFormat () {
      return 'YYYY-MM-DD HH:MM:00'
    },
    endDateFormat () {
      return 'YYYY-MM-DD HH:MM:00'
    }
  },
  methods: {
    onShowSizeChange () {
      this.queryForm.pageNum = 1
      this.loadTableList()
    },
    changePagination (page) {
      this.queryForm.pageNum = page
      this.loadTableList()
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
    handleAdd () {
      this.updateType = 'add'
      this.visible = true
      this.currentUpdateId = null
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    }
  }
}

export { mixinTableList }
