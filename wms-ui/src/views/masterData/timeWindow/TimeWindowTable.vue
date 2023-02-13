<template>
  <div class="content">
    <a-row :gutter="[16, 16]">
      <template v-for="(item, index) in timeWindowList">
        <a-col :key="index" :span="4">
          <div class="hour-div" :style="{'--background-image': item.enable ? openStatusColor : closeStatusColor}">
            {{ item.startTime + '-' + item.endTime }}
            <a-input
              oninput="value=value.replace(/[^\d]/g,'')"
              class="center-input"
              v-model="item.dockNum"
              :bordered="false"
              placeholder="可预约车辆数"
              @change="onDockNumChange(item)"
            />
            <a-switch class="center" v-model="item.enable" checked-children="启用" un-checked-children="禁用" />
          </div>
        </a-col>
      </template>
    </a-row>
    
    <a-modal 
      v-drag-modal
      v-model="isVisibleExceedAllDockNum" 
      title="提示"
      :width="420"
      @cancel="close"
      @ok="handleSubmit"
      :confirmLoading="confirmLoading">  
      <p class="exceed-docknun-p">您输入的可预约车辆数，在时间窗口内大于总道口数，请确认是否保存？</p>    
    </a-modal>
  </div>
</template>

<script>

export default {
  props: {
    visible: {
      type: Boolean,
      default() {
        return false
      },
    },
    wareId: {
      type: Number,
      default() {
        return 0
      },
    },
    allDockNum: {
      type: Number,
      default() {
        return 0
      },
    },
    isSave: {
      type: Boolean,
      default() {
        return false
      },
    },
  },
  data() {
    return {
      /** time window数据list */
      timeWindowList: [],
      /** 启用时间段背景色 */
      openStatusColor: 'linear-gradient(to bottom right, #2f508d40, #34b8abbd)',
      /** 禁用时间段背景色 */
      closeStatusColor: 'linear-gradient(to bottom right, rgb(0 0 0 / 39%), rgb(0 0 0 / 15%))',
      /** 预约道口数量超过总道口数量提醒弹框，超过总数量时提醒 */
      isVisibleExceedAllDockNum: false,
      /** 弹窗确认按钮loading */
      confirmLoading: false
    }
  },
  mounted() {
    this.loadData()
  },
  model: {
    prop: 'checked',
    event: 'change',
  },
  computed: {},
  methods: {
    /** 根据wareId查询数据 */
    async loadData() {
      this.isVisibleExceedAllDockNum = false;
      if (this.wareId != null) {
        const { data } = await this.$store.dispatch('timeWindow/getDataByWareId', this.wareId)
        if (data.length > 0) {
          this.timeWindowList = data
        } else {
          //如果该仓库没有配置time window，初始化time window页面
          this.initTimeWindowList()
        }
      } else {
        this.initTimeWindowList()
      }
    },
    /** 当数据库没有该仓库数据时，初始化timewindow数据(目前按照每个小时有一个的规则生成，共24个) */
    initTimeWindowList() {
      this.timeWindowList = []
      for (let i = 0; i < 24; i++) {
        const startTime = i < 10 ? '0' + i + ':' + '00' : i + ':' + '00'
        const endTime = i + 1 < 10 ? '0' + (i + 1) + ':' + '00' : i + 1 + ':' + '00'
        const row = {
          id: null,
          startTime: startTime,
          endTime: endTime,
          wareId: this.wareId,
          dockNum: null,
          status: 1,
          enable: true
        }
        this.timeWindowList = [...this.timeWindowList, row]
      }
    },
    /** 道口数量change方法，判断是否超过该仓库道口数 */
    onDockNumChange(item) {
      const num = Number(item.dockNum)
      if (this.wareId == 0 || this.wareId == null) {
        this.$message.error('请选择仓库！')
        item.dockNum = null
        return
      }
    },
    /** 整合time window数据，并进行数据判断 */
    handleSave() {
      if(this.wareId == null){
        this.$message.error('请选择仓库！')
        return;
      }
      //判断启用的时间的道口数量，启动时道口数不能为0
      for(let index in this.timeWindowList){
        if(this.timeWindowList[index].enable && (this.timeWindowList[index].dockNum == null || this.timeWindowList[index].dockNum == '' || this.timeWindowList[index].dockNum == 0)){
          this.$message.error('请确认启动的时间段，道口数量不为0！')
          this.$emit('on-ok', false)
          return;
        }
      }
      let openModal = false;
      //转换enable状态为status的值
      this.timeWindowList.forEach((item) => {
        if (item.enable == true) {
          item.status = 1
        } else {
          item.status = 0
        }
        if (item.dockNum == '') {
          item.dockNum = 0
        }
        if(item.dockNum > this.allDockNum && item.enable){
          openModal = true;
        }
      })
      //弹窗提醒超过道口数
      if(openModal){
        this.isVisibleExceedAllDockNum = true;
      }else{
        this.handleSubmit();
      }
    },
    /** 提交保存time window数据 */
    async handleSubmit(){
      if(this.isVisibleExceedAllDockNum){
        this.confirmLoading = true;
      }
      try {
        await this.$store.dispatch('timeWindow/addList', this.timeWindowList)
        this.$emit('on-ok', true)
        this.$message.success("保存成功!")
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.loadData()
        this.confirmLoading = false;
        this.isVisibleExceedAllDockNum = false;
      }
    },
    //** 关闭弹窗 */
    close(){
      this.isVisibleExceedAllDockNum = false;
      this.$emit('on-ok', false)
    },
    /** 格式化日期(暂时没有用到) */
    getFormatDate(value) {
      const date = new Date(value)
      if (date == undefined) {
        return ''
      }
      return date.getFullYear() + '-' + (date.getMonth + 1) < 10
        ? '0' + (date.getMonth + 1)
        : date.getMonth + 1 + '-' + date.getDate() < 10
        ? '0' + date.getDate()
        : date.getDate()
    },
  },
  watch: {
    visible(val) {
      if (val == true && this.wareId != null) {
        this.loadData()
      }
    },
    isSave(val) {
      if (val == true) {
        this.handleSave()
      }
    },
  },
}
</script>

<style lang="less" scoped>
// .content{
//   background-image: linear-gradient(to bottom right,#2f508d,#34b8ab);
// }
.hour-div {
  // background-color: rgba(227, 225, 225, 0.5);
  background-image: var(--background-image);
  height: 18vh;
  line-height: 8vh;
  text-align: center;
  font-size: 20px;
  font-weight: 800;
}
.center {
  display: block;
  margin: 0 auto;
}
.center-input {
  display: block;
  margin: 0 auto;
  height: 3vh;
  width: 6vw;
  margin-bottom: 2vh;
  text-align: center;
  font-size: 12px;
}
.exceed-docknun-p{
  color: #f5222df0;
  font-size: 14px;
  font-weight: 900;
}
</style>
