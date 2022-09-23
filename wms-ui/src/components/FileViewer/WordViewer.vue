<template>
  <div>
    <a :href="fileHandle.url">{{ fileHandle.name }}</a>
  </div>
</template>

<script>
export default {
  name: 'WordViewer',
  props: {
    fileHandle: {
      type: Object,
      required: true
    }
  }
}
</script>
