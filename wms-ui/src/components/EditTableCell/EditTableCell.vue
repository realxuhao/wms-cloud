<template>
  <div class="editable-cell">
    <div v-if="editable" class="editable-cell-input-wrapper">
      <a-input :value="value" @change="handleChange" @pressEnter="check" />
      <a-icon
        type="check"
        class="icon"
        @click="check"
      />

    </div>
    <div v-else class="editable-cell-text-wrapper">
      <span>{{ value || ' ' }}</span>
      <a-icon type="edit" class="icon" @click="edit" />
    </div>
  </div>
</template>
<script>
export default {
  props: {
    text: {
      type: [String, Number],
      default: ''
    }
  },
  data () {
    return {
      value: this.text,
      editable: false
    }
  },
  methods: {
    handleChange (e) {
      const value = e.target.value
      this.value = value
    },
    check () {
      this.editable = false
      this.$emit('change', this.value)
    },
    edit () {
      this.editable = true
    }
  }
}
</script>

<style lang="less" scoped>
.editable-cell{
  width: 100%;
}
.editable-cell-text-wrapper{
  display: flex;
  justify-content: space-between;
  align-items: center;

}

.editable-cell-input-wrapper{
  display: flex;
  align-items: center;
}

.icon{
  cursor: pointer;
  padding: 2px;
  box-sizing: border-box;
  margin-left: 4px;
  &:hover{
    color: #108ee9;
  }
}

</style>
