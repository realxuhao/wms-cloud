<script>
export default {
  name: 'MSpin',
  props: {
    spinning: {
      type: Boolean,
      required: true,
      default: true
    }
  },
  render () {
    const props = { ...this.$props, ...this.$attrs }
    const { spinning } = props
    return (
      <div class="m-spin-container">
        <div class={{ 'm-spin-wrapper': spinning }}>
          <a-spin class="m-spin" props={props}/>
        </div>
        {this.$slots.default}
      </div>
    )
  }
}
</script>

<style lang="less" scoped>
.m-spin-container {
  position: relative;
  .m-spin {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
  .m-spin-wrapper {
    background: #f1f2f5;
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    z-index: 10000;
  }
}
</style>
