<template>
  <iframe height="100%" width="100%" :src="pdfUrl" ></iframe>
</template>

<script>
import qs from 'qs'
import config from '@/config/api.config'
/**
 * 组件名称： pdfjs
 * 功能： pdf 的预览，搜索，放大瘦小，下载，旋转等
 * 使用： 调用组件的时候，请传入 pdf 的 Blob 数据
 * 大小： 想要调整 pdf 的宽度和高度，可以在外层调用组件的时候改变组件的size
 */
export default {
  name: 'PdfJsViewer',
  props: {
    fileHandle: {
      type: Object,
      required: true
    }
  },
  computed: {
    pdfUrl () {
      const pdfQueryString = qs.stringify({ file: this.fileHandle.url })
      return `${config.approvalHost}/public/pdfjs/web/viewer.html?${pdfQueryString}`
    }
  }
}
</script>
