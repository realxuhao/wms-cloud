<template>
  <div class="wrapper zx">
    <h3 class="title">

    </h3>
    <div class="content">
      <!-- <img src="@/assets/images/home.png" alt="">
      <div class="border">

      </div>
      <div class="desc">
        供应商管理平台
      </div> -->
    </div>
  </div>
</template>

<script>

import { mapGetters } from 'vuex'
import { PageHeaderWrapper } from '@/components/ProLayout'
import ruoyiLogo from '@/assets/projects/ruoyi.png'
import antdvLogo from '@/assets/projects/antdv.png'
import { getUserProfile } from '@/api/system/user'

export default {
  name: 'Index',
  components: {
  },
  data () {
    return {
    }
  },
  computed: {

  },
  created () {
    // this.getUser()
  },
  mounted () {
  },
  methods: {
  }
}
</script>

<style lang="less" scoped>
.wrapper{
  height: calc(100vh - 200px);
  background: #fff;
  padding: 16px;
  border-radius: 4px;
}

.title{
  position: absolute;
  right: 24px;
  top:20px;
  font-size:20px;
  color: #037f3e;
}
.content{
  display: flex;
  height: 100%;
  display: flex;
  align-items: center;
  img{
    max-height: 100%;
  }
  .border{
    background: #037f3e;
    width: 20px;
    height: 200px;
    border-radius: 2px;
    margin-left: 40px;
  }

  .desc{
    margin-left: 20px;
    font-size: 64px;
    color: #000;
    font-weight: bold;
  }
}


</style>
