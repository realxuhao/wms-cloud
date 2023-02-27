<template>
  <a-drawer
    width="640px"
    :title="title"
    placement="right"
    :visible="visible"
    @close="onClose"
  >
    <a-form :form="form" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="仓库编码">
        <a-select
          show-search
          style="width: 100%"
          placeholder="仓库编码"
          v-decorator="[
            'wareId',
            { rules: [{ required: true, message: '请选择仓库编码!' }] }
          ]"
          @change="handleWareChange">
          <a-select-option v-for="item in wareOptionList" :key="item.id" :value="item.id">
            {{ item.code }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="道口">
        <a-select
          show-search
          style="width: 100%"
          placeholder="道口"
          v-decorator="[
            'dockCode',
            { rules: [{ required: true, message: '请选择道口!' }] }
          ]">
          <a-select-option v-for="item in dockList" :key="item" :value="item">
            {{ item }}</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>

    <div class="action">
      <a-button :style="{ marginRight: '8px' }" @click="onClose">
        取消
      </a-button>
      <a-button type="primary" @click="handleSubmit" :loading="submitLoading">
        提交
      </a-button>
    </div>
  </a-drawer>
</template>

<script>
import _ from 'lodash'

const labelCol = {
  span: 5
}
const wrapperCol = {
  span: 19
}

export default {
  props: {
    data: {
      type: String,
      default () {
        return ''
      }
    },
    visible: {
      type: Boolean,
      default () {
        return false
      }
    },
    updateType: {
      type: String,
      default () {
        return ''
      }
    }
  },
  data () {
    return {
      form: this.$form.createForm(this),
      submitLoading: false,
      dispatchId: null,
      wareOptionList: [],
      dockList: []
    }
  },
  model: {
    prop: 'visible',
    event: 'change'
  },
  computed: {
    title () {
      return this.updateType === 'edit' ? '分配道口' : '分配道口'
    },
    labelCol () {
      return labelCol
    },
    wrapperCol () {
      return wrapperCol
    }
  },
  methods: {
    /** 获取仓库List */
    async getWareOptionList () {
      this.wareOptionList = []
      try {
        const data = await this.$store.dispatch('ware/getOptionList')
        this.wareOptionList = data.data
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleWareChange (value) {
      this.dockList = []
      const ware = this.wareOptionList.find(x => x.id === Number(value))
      if (ware !== undefined) {
        const num = Number(ware.dockNum)
        for (let i = 0; i < num; i++) {
          this.dockList.push(i + 1)
        }
      }
    },
    onClose () {
      this.form.resetFields()

      this.$emit('change', false)
    },
    async getAndUpdateForm () {
      const signData = JSON.parse(this.data)
      this.dispatchId = signData.dispatchId
      this.$nextTick(() => {
        this.form.setFieldsValue(_.pick(signData, ['wareId', 'dockCode']))
        this.dockList = []
        const ware = this.wareOptionList.find(x => x.id === Number(signData.wareId))
        if (ware !== undefined) {
          const num = Number(ware.dockNum)
          for (let i = 0; i < num; i++) {
            this.dockList.push(i + 1)
          }
        }
      })
    },
    async loadData () {

    },
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFieldsAndScroll(async (err, values) => {
        if (err) {
          console.log('Received values of form: ', values)
          return
        }

        try {
          this.submitLoading = true

          if (this.updateType === 'edit') {
            const param = {
              ...values,
              ...{ dispatchId: this.dispatchId }
            }
            await this.$store.dispatch('driverDispatch/importDock', param)
          }

          this.$emit('on-ok')
          this.onClose()
        } catch (error) {
          this.$message.error(error.message)
        } finally {
          this.submitLoading = false
        }
      })
    }
  },
  mounted () {
    this.getWareOptionList()
  },
  watch: {
    visible (val) {
      if (val) {
        this.loadData()
        if (this.updateType === 'edit') {
          this.getAndUpdateForm()
        }
      }
    }
  }
}
</script>

<style lang="less" scoped>
.action{
  position: absolute;
  right: 0;
  bottom: 0;
  width: 100%;
  border-top: 1px solid #e9e9e9;
  padding: 10px 16px;
  background: #fff;
  text-align: right;
  z-index: 1;
}
</style>
