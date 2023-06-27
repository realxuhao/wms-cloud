<template>
  <div class="main">
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="Tab 1">
        <MaterialOld></MaterialOld>
      </a-tab-pane>
      <a-tab-pane key="2" tab="Tab 2" >
        Content of Tab Pane 2
      </a-tab-pane>
    </a-tabs>
  </div>
  
</template>

<script>
import MaterialOld from './MaterialOld'
export default {
  components:{
    MaterialOld
  }
}
</script>

<style lang="less" scoped>
.main{
  background: #fff;
}
</style>