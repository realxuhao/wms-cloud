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
                <a-button
                  style="margin-left: 8px"
                  type="primary"
                  @click="handleSave"
                  :loading="saveLoading"
                ><a-icon type="save" />保存</a-button>
              </span>
            </a-col>

            <a-col span="3" v-show="queryForm.wareId != null">
              <a-tag class="docknum-tag" color="#1890ff">
                该仓库总道口数：{{ queryForm.wareId == null ? null : wareOptionList.find(x => x.id == queryForm.wareId).dockNum }}
              </a-tag>
            </a-col>
          </a-row>
        </a-form>

        <router-link :to="{name:'fullscreenTimeWIndow'}" v-show="!isFullscreen">
          <a-tooltip placement="bottom" title="切为全屏">
            <a-icon class="fullscreen" type="fullscreen" @click="handleFullscreen" />
          </a-tooltip>
        </router-link>
        <router-link v-show="isFullscreen" :to="{name:'timeWindow'}" >
          <a-icon class="fullscreen" @click="handleExitFullscreen" type="fullscreen-exit" />
        </router-link>
      </div>

      <TimeWindowTable
        :visible="searchLoading"
        :wareId="queryForm.wareId"
        :allDockNum="queryForm.wareId == null ? null : wareOptionList.find(x => x.id == queryForm.wareId).dockNum"
        :isSave="saveLoading"
        @on-ok="childernSave"
        centered
      ></TimeWindowTable>
    </div>
  </div>
</template>

<script>
import TimeWindowTable from './TimeWindowTable'
import { fullscreen, exitFullscreen } from '@/utils/util'

const queryFormAttr = () => {
  return {
    wareId: null
  }
}

export default {
  name: 'TimeWindow',
  props: {
    isFullscreen: {
      type: Boolean,
      default: () => false
    }
  },
  components: {
    TimeWindowTable
  },
  data () {
    return {
      visible: false,
      updateType: 'add', // edit、add
      currentUpdateId: 0,

      searchLoading: false,
      saveLoading: false,
      queryForm: {
        ...queryFormAttr(),
        pageSize: 20,
        pageNum: 1
      },
      paginationTotal: 0,

      tableLoading: false,
      list: [],
      wareOptionList: []
    }
  },
  methods: {
    handleFullscreen () {
      fullscreen()
    },
    handleExitFullscreen () {
      exitFullscreen()
    },
    handleResetQuery () {
      this.queryForm = { ...this.queryForm, ...queryFormAttr() }
      this.handleSearch()
    },
    onShowSizeChange () {
      this.queryForm.pageNum = 1
      this.loadTableList()
    },
    changePagination (page) {
      this.queryForm.pageNum = page
      this.loadTableList()
    },

    async handleSearch () {
      this.searchLoading = true
      await this.loadTableList()
      this.searchLoading = false
    },
    /** 通知子页面保存time window数据 */
    handleSave () {
      if (this.queryForm.wareId == null) {
        this.$message.error('请选择要保存的仓库！')
        return
      }
      this.saveLoading = true
    },
    /** 接收Time window数据保存返回值 */
    childernSave (val) {
      this.saveLoading = false
      if (val === true) {
        this.loadTableList()
      }
    },
    handleEdit (record) {
      this.updateType = 'edit'
      this.visible = true
      this.currentUpdateId = record.id
    },
    async handleDelete (record) {
      try {
        await this.$store.dispatch('timeWindow/destroy', record.id)
        this.$message.success('删除成功！')

        this.loadTableList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
    },

    handleAdd () {
      this.updateType = 'add'
      this.visible = true
      this.currentUpdateId = null
    },

    async loadTableList () {
      try {
        this.tableLoading = true
        this.saveLoading = false

        const { rows, total } = await this.$store.dispatch('timeWindow/getList', this.queryForm)
        this.list = rows
        this.paginationTotal = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.tableLoading = false
      }
    },
    /** 获取仓库List */
    async getWareOptionList () {
      try {
        const data = await this.$store.dispatch('ware/getOptionList')
        this.wareOptionList = data.data
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async loadData () {
      this.getWareOptionList()
      this.loadTableList()
    }
  },
  mounted () {
    this.loadData()
  }
}
</script>

<style lang="less" scoped>
.wrapper{
  position: relative;
}

.docknum-tag{
    background-color: #1890ff;
    height: 32px;
    font-size: 15px;
    display: flex;
    justify-content: center;
    align-items: center;
}

.fullscreen{
  position: absolute;
  right: 16px;
  top:16px;
  font-size: 24px;
  transition: transform ease 0.2s;
  cursor: pointer;
  &:hover{
    transform: scale(1.4);
    transition: transform ease 0.2s;
  }
}
</style>
