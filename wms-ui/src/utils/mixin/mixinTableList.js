const mixinTableList = {
  data () {
    return {
      visible: false,
      updateType: 'add', // edit、add
      currentUpdateId: 0,

      searchLoading: false,
      queryForm: {
        name: '',
        code: '',
        pageSize: 20,
        pageNum: 1
      },
      paginationTotal: 0,

      tableLoading: false
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
    }
  }
}

export { mixinTableList }
