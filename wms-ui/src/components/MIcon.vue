<template>
  <icon-font :type="type" />
</template>

<script>
import { Icon } from 'ant-design-vue'

const IconFont = Icon.createFromIconfontCN({
  scriptUrl: '/icon/iconfont.js' // 在 iconfont.cn 上生成
})

export default {
  name: 'MIcon',
  components: { IconFont },
  props: {
    type: {
      type: String,
      required: true
    }
  }
}
</script>
