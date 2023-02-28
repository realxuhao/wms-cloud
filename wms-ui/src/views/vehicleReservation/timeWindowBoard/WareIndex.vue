<template>
  <div class="wrapper">
    <div class="table-content">
      <div class="action-content">
        <a-form layout="inline" class="search-content">
          <a-row :gutter="16">
            <a-col :span="4">
              <a-form-model-item label="仓库编码">
                <a-select show-search v-model="queryForm.wareId" style="width: 100%" placeholder="仓库编码">
                  <a-select-option v-for="item in wareOptionList" :key="item.id" :value="item.id">
                    {{ item.code }}</a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col span="4">
              <span class="table-page-search-submitButtons">
                <a-button
                  type="primary"
                  @click="handleSearch"
                  :loading="searchLoading"
                ><a-icon type="search" />查询</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <div class="content">
        <a-row :gutter="[16,16]">
          <a-col :key="index" v-for="(item, index) in dockList" :span="6">
            <a-card
              class="dock-card"
              :title="item.dockName"
              :headStyle="{ 'text-align': 'center','font-size': '20px',
                            'font-weight': '800' }">
              <div v-if="item.enable" class="stopPoint  dock-div">
                {{ item.carNum }}
              </div>
              <div v-if="!item.enable" class="not-dock-div" :style="{'--background-color': item.enable ? '#ffe046c4' : '#8dd4b8b5'}">
                {{ item.carNum }}
              </div>
            </a-card>
          </a-col>
        </a-row>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'VrWareTimeWindowBoardIndex',
  props: {
  },
  data () {
    return {
      searchLoading: false,
      queryForm: {
        wareId: null,
        statusList: [1]
      },
      wareOptionList: [],
      dockList: [],
      dockNum: 0,
      list: [],
      timer: null

    }
  },
  beforeDestroy () {
    clearInterval(this.timer)
    this.timer = null
  },
  methods: {
    /** 获取仓库List */
    async getWareOptionList () {
      this.wareOptionList = []
      try {
        const data = await this.$store.dispatch('ware/getOptionList')
        this.wareOptionList = data.data
        this.timer = setInterval(() => {
          this.handleSearch()
        }, 10 * 1000 * 1000)
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async handleSearch () {
      const that = this
      if (that.queryForm.wareId != null) {
        this.dockNum = 0
        this.dockList = []
        const ware = this.wareOptionList.find(x => x.id === Number(this.queryForm.wareId))
        if (ware !== undefined) {
          await this.loadTableList()
          this.dockNum = ware.dockNum
          for (let i = 0; i < this.dockNum; i++) {
            const data = this.list.find(x => x.dockCode === (i + 1).toString())
            const row = {
              dockNum: i + 1,
              dockName: 'Dock ' + (i + 1),
              carNum: data === undefined ? null : data.carNum,
              enable: data !== undefined
            }
            this.dockList = [...this.dockList, row]
          }
        }
      }
    },
    async loadTableList () {
      try {
        this.list = []
        const { data } = await this.$store.dispatch('driverDispatch/getTodaySignlist', this.queryForm)
        this.list = data
      } catch (error) {
        this.$message.error(error.message)
      } finally {
      }
    },
    loadData () {
      this.getWareOptionList()
    }
  },
  mounted () {
    this.loadData()
  },
  watch: {
  }
}
</script>

<style lang="less" scoped>
.dock-div {
  background-color: var(--background-color);
  height: 22vh;
  line-height: 22vh;
  text-align: center;
  font-size: 60px;
  font-weight: 800;
  animation: myfirst 5s;
  animation-iteration-count: infinite;

}
.not-dock-div {
  background-color: var(--background-color);
  height: 22vh;
  line-height: 22vh;
  text-align: center;
  font-size: 60px;
  font-weight: 800;

}

@keyframes myfirst {
  0% {
    background: rgba(255, 187, 0, 0.99);
  }
  25% {
    background: rgba(255, 251, 0, 0.714);
  }
  50% {
    background: rgba(255, 187, 0, 0.99);
  }
  100% {
    background: rgba(255, 230, 0, 0.985);
  }
}
.dock-card{
  box-shadow: 4px 4px 7px 4PX #3808784d;
}
</style>
