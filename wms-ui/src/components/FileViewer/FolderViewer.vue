<template>
  <a-tabs class="full-height">
    <a-tab-pane
      v-for="file in fileHandle"
      :key="file.name"
      :tab="file.name"
      class="full-height">
      <FileViewerLayout :fileHandle="file" />
    </a-tab-pane>
  </a-tabs>
</template>

<script>
export default {
  name: 'FolderViewer',
  props: {
    fileHandle: {
      type: Array,
      default: () => {}
    }
  },
  beforeCreate: function () {
    this.$options.components.FileViewerLayout = () => import('./FileViewerLayout')
  }
}
</script>
