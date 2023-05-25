import { mapState } from 'vuex'

const tableMixin = {
  data () {
    return {
      tableSize1: '',
      lastName: ''
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
  }
}

export {
  tableMixin
}
