import layouts from './ViewerMap'

export default {
  name: 'FileViewerLayout',
  components: {
    ...layouts
  },
  props: {
    fileHandle: {
      type: Object | Array,
      require: true
    },
    showType: {
      type: String,
      default: ''
    }
  },
  render () {
    const { showType, fileHandle } = this
    const Layout = layouts[showType]

    if (!showType) {
      return (
        <div>"{showType}" component is not found</div>
      )
    }

    if (!Layout) {
      return (
        <div>"{showType}" component is not found</div>
      )
    }

    return (
      <Layout fileHandle={fileHandle}></Layout>
    )
  }
}
