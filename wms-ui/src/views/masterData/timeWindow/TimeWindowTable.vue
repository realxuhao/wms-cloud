<template>
  <div class="content">
    <a-row :gutter="[16, 16]">
      <template v-for="(item, index) in timeWindowList">
        <a-col :key="index" :span="4">
          <div class="hour-div">
            {{ item.startTime + '-' + item.endTime }}
            <a-input
              oninput="value=value.replace(/[^\d]/g,'')"
              class="center-input"
              v-model="item.dockNum"
              :bordered="false"
              placeholder="道口数量"
              @change="onDockNumChange(item)"
            />
            <a-switch class="center" v-model="item.enable" checked-children="启用" un-checked-children="禁用" />
          </div>
        </a-col>
      </template>
    </a-row>
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
      if (this.wareId != null) {
        const { data } = await this.$store.dispatch('timeWindow/getDataByWareId', this.wareId)
        console.info(data)
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
          enable: true,
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
      if (num > this.allDockNum) {
        this.$message.error('您输入的道口数量大于仓库道口总数量，请重新输入!')
        item.dockNum = null
      }
    },
    /** 保存time window数据 */
    async handleSave() {
      if(this.wareId == null){
        this.$message.error('请选择仓库！')
        return;
      }
      this.timeWindowList.forEach((item) => {
        if (item.enable == true) {
          item.status = 1
        } else {
          item.status = 0
        }
        if (item.dockNum == '') {
          item.dockNum = null
        }
      })
      try {
        await this.$store.dispatch('timeWindow/addList', this.timeWindowList)
        this.$emit('on-ok', true)
        this.$message.success("保存成功!")
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.loadData()
      }
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
  background-image: linear-gradient(to bottom right, #2f508d40, #34b8abbd);
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
  width: 5vw;
  margin-bottom: 2vh;
  text-align: center;
}
</style>
