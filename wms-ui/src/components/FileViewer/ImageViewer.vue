<template>
  <div>
    <img :src="fileHandle.url" alt="preview" />
  </div>
</template>

<script>
export default {
  name: 'ImageViewer',
  components: {},
  // 使用该组件需要传入两个数据，一个是图片的源Blob数据，一个是图片的mimeType
  props: {
    fileHandle: {
      type: Object,
      required: true
    }
  }
}
</script>

<style lang="less" scoped>
img {
  max-width: 100%;
}
</style>
