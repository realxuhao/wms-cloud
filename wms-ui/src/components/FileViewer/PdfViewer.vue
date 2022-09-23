<template>
  <object
    :data="pdfUrl"
    :height="height"
    :width="width"
    type="application/pdf"
  />
</template>

<script>
export default {
  name: 'PdfLayout',
  components: {},
  props: {
    fileHandle: {
      type: Object,
      required: true
    },
    height: {
      type: String,
      required: false,
      default: '100%'
    },
    width: {
      type: String,
      required: false,
      default: '100%'
    }
  },
  computed: {
    pdfUrl () {
      const blob = new Blob([this.fileHandle.blobData], { type: 'application/pdf' })
      return window.URL.createObjectURL(blob)
    }
  }
}
</script>
