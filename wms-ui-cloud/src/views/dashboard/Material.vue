<template>
  <div class="main">
    <a-tabs default-active-key="1" >
      <a-tab-pane key="1" tab="物料在库时间排序" :forceRender="true">
        <MaterialOld></MaterialOld>
      </a-tab-pane>
      <a-tab-pane key="2" tab="30天有效期到期物料排序" :forceRender="true">
        <MaterialExpirationDate></MaterialExpirationDate>
      </a-tab-pane>
    </a-tabs>
  </div>
  
</template>

<script>
import MaterialOld from './MaterialOld'
import MaterialExpirationDate from './MaterialExpirationDate'

export default {
  components:{
    MaterialOld,
    MaterialExpirationDate
  }
}
</script>

<style lang="less" scoped>
.main{
  background: #fff;
}
</style>