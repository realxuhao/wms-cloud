<template>
  <a-drawer
    width="640px"
    :title="title"
    placement="right"
    :closable="false"
    :visible="visible"
    @close="onClose"
  >
    <a-form :form="form" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="编码">
        <a-input
          placeholder="编码"
          v-decorator="[
            'code',
            { rules: [{ required: true, message: '请输入编码!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input
          placeholder="名称"
          v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入名称!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="仓库">
        <a-select
          @change="handleGetAreaList"
          v-decorator="[
            'wareId',
            { rules: [{ required: true, message: '请选择仓库!' },] }
          ]">
          <a-select-option v-for="item in wareList" :key="item.id" :value="item.id">{{ item.name }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="存储区">
        <a-select
          @change="handleGetFrameList"
          :loading="areaLoading"
          v-decorator="[
            'areaId',
            { rules: [{ required: true, message: '请选择存储区!' },] }
          ]">
          <a-select-option v-for="item in areaList" :key="item.id" :value="item.id">{{ item.name }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="跨">
        <a-select
          :loading="frameLoading"
          v-decorator="[
            'frameId',
            { rules: [{ required: true, message: '请选择跨!' },] }
          ]">
          <a-select-option v-for="item in frameList" :key="item.id" :value="item.id">{{ item.name }}</a-select-option>
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
    id: {
      type: Number,
      default () {
        return 0
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

      wareList: [],
      areaList: [],
      frameList: [],
      frameLoading: false,
      areaLoading: false
    }
  },
  model: {
    prop: 'visible',
    event: 'change'
  },
  computed: {
    title () {
      return this.updateType === 'edit' ? '编辑' : '新增'
    },
    labelCol () {
      return labelCol
    },
    wrapperCol () {
      return wrapperCol
    }
  },
  methods: {
    onClose () {
      this.form.resetFields()

      this.$emit('change', false)
    },
    async handleGetAreaList () {
      const { wareId } = this.form.getFieldsValue()

      try {
        this.form.setFieldsValue({ areaId: null })
        this.form.setFieldsValue({ frameId: null })
        this.areaLoading = true
        const data = await this.$store.dispatch('area/getList', { wareId })
        this.areaList = data
      } catch (error) {
        this.$message.error('获取存储区数据失败！')
      } finally {
        this.areaLoading = false
      }
    },
    async handleGetFrameList () {
      const { areaId } = this.form.getFieldsValue()

      if (!areaId) {
        return
      }

      try {
        this.form.setFieldsValue({ areaId: null })
        this.frameLoading = true
        const data = await this.$store.dispatch('frame/getList', { areaId })
        this.frameList = data
      } catch (error) {
        this.$message.error('获取跨数据失败！')
      } finally {
        this.frameLoading = false
      }
    },
    async getAndUpdateForm () {
      const { data } = await this.$store.dispatch('bin/getOne', this.id)
      this.form.setFieldsValue(_.pick(data, ['code', 'name', 'wareId', 'areaId', 'frameId']))
    },
    async getWareList () {
      const list = await this.$store.dispatch('ware/getList')
      this.wareList = list
    },
    async loadData () {
      await this.getWareList()
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
            await this.$store.dispatch('bin/edit', { id: this.id, updateEntity: values })
          } else {
            await this.$store.dispatch('bin/add', values)
          }

          this.$emit('on-ok')
          this.onClose()
        } catch (error) {
          this.$message.error(error)
        } finally {
          this.submitLoading = false
        }
      })
    }
  },
  watch: {
    visible (val) {
      if (val) {
        this.loadData()
        if (this.updateType === 'edit') {
          this.getAndUpdateForm()

          this.handleGetAreaList()
          this.handleGetFrameList()
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
/deep/.ant-drawer-body{
  // overflow-y: auto;
  padding-bottom: 60px;
}

/deep/.ant-input-number{
  width: 100%;
}
</style>
