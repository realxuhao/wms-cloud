<template>
  <vue-json-pretty :data="fileHandle.jsonData"> </vue-json-pretty>
</template>

<script>
import VueJsonPretty from 'vue-json-pretty'

export default {
  name: 'JsonViewer',
  components: {
    VueJsonPretty
  },
  props: {
    fileHandle: {
      type: [Object, Array],
      required: true
    }
  }
}
</script>
