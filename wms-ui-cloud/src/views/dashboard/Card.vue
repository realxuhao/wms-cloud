<template>
  <div class="card">
    <div class="card-header">
      <div class="card-header-title">
        {{ title }}
      </div>
      <div class="card-header-search">
        <slot name="header"></slot>
      </div>
    </div>

    <div class="card-main">
      <slot name="main"></slot>
    </div>
  </div>
</template>

<script>
export default{
  props:{
    title:{
      type:String,
      default:''
    }
  }
}

</script>

<style lang="less" scoped>
.card{
  padding: 16px 12px;
  box-sizing: border-box;
  color: #000000d9;
  font-weight: 500;
  font-size: 16px;
  background: transparent;
  border-radius:5px;
  background: #fff;

  .card-header{
    height: 40px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    .card-header-title{
      position: relative;
      margin-left: 8px;
      &::before{
        content: '';
        display: inline-block;
        width: 3px;
        height: 10px;
        background: @primary-color;
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        left: -8px;
        
      }
    }

    .card-header-search{
      margin-left: 16px;
      display: flex;
      flex: 1;
      justify-content: flex-start;
      align-items: center;
    }
  }

  .card-main{
    margin-top: 16px;
    min-height: 200px;
    // div{
    //   width: 100%;
    //   height: 300px;
    // }
  }
}
</style>