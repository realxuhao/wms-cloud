<template>
  <a-drawer
    width="640px"
    :title="title"
    placement="right"
    :visible="visible"
    @close="onClose"
  >
    <a-form :form="form" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="工厂编码">
        <a-input
          placeholder="工厂编码"
          v-decorator="[
            'factoryCode',
            { rules: [{ required: true, message: '请输入工厂编码!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="工厂名称">
        <a-input
          placeholder="工厂名称"
          v-decorator="[
            'factoryName',
            { rules: [{ required: true, message: '请输入工厂名称!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="仓库编码">
        <a-input
          placeholder="仓库编码"
          v-decorator="[
            'code',
            { rules: [{ required: true, message: '请输入仓库编码!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="仓库名称">
        <a-input
          placeholder="仓库名称"
          v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入仓库名称!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="地点">
        <a-input
          placeholder="地点"
          v-decorator="[
            'location',
            { rules: [{ required: true, message: '请输入地点!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="联系人">
        <a-input
          placeholder="联系人"
          v-decorator="[
            'wareUser',
            { rules: [{ required: true, message: '请输入联系人!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="联系电话">
        <a-input
          oninput="value=value.replace(/[^\d]/g,'')"
          placeholder="联系电话"
          v-decorator="[
            'wareUserPhone',
            { rules: [{ required: true, message: '请输入联系电话!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="道口数量">
        <a-input
          oninput="value=value.replace(/[^\d]/g,'')"
          placeholder="道口数量"
          v-decorator="[
            'dockNum',
            { rules: [{ required: true, message: '请输入道口数量!' }] }
          ]" />
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
      submitLoading: false
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
    async getAndUpdateForm () {
      const { data } = await this.$store.dispatch('ware/getOne', this.id)
      this.form.setFieldsValue(_.pick(data, ['factoryName', 'factoryCode', 'code', 'name', 'location', 'wareUser', 'wareUserPhone', 'dockNum']))
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
            await this.$store.dispatch('ware/edit', { id: this.id, updateEntity: values })
          } else {
            await this.$store.dispatch('ware/add', values)
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
/deep/.ant-drawer-body{
  // overflow-y: auto;
  padding-bottom: 60px;
}

/deep/.ant-input-number{
  width: 100%;
}
</style>
