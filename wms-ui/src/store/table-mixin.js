import { mapState } from 'vuex'

const tableMixin = {
  data () {
    return {
      tableSize1: '',
      lastName: '',
      advanced: false,
      // 非多个禁用
      loading: false,
      total: 0
    }
  },
  computed: {
    ...mapState({
      tableBordered: state => state.app.tableBordered
    }),
    tableSize: {
      get () { return this.tableSize1 || this.$store.state.app.tableSize },
      set (value) {
        this.tableSize1 = value
      }
    }
  },
  methods: {
    loadData () {
      this.getList()
      console.log('需要实现loadData方法')
    },
    getList () {
      console.log('需要实现getList方法')
    },
    onShowSizeChange (current, pageSize) {
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    changeSize (current, pageSize) {
      this.queryParam.pageNum = current
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParam.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.queryParam = {
        // eslint-disable-next-line no-undef
        ...queryParamAttr(),
        pageNum: 1,
        pageSize: 10
      }
      this.handleQuery()
    }
  },
  mounted () {
    this.loadData()
  }
}

export {
  tableMixin
}
