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
      <a-form-item label="托盘长度">
        <a-input-number
          placeholder="托盘长度"
          v-decorator="[
            'length',
            { rules: [{ required: true, message: '请输入托盘长度!' },] }
          ]">
        </a-input-number>
      </a-form-item>
      <a-form-item label="托盘宽度">
        <a-input-number
          placeholder="托盘宽度"
          v-decorator="[
            'width',
            { rules: [{ required: true, message: '请选择托盘宽度!' }] }
          ]">
        </a-input-number>
      </a-form-item>
      <a-form-item label="托盘高度">
        <a-input
          placeholder="托盘高度"
          v-decorator="[
            'height',
            { rules: [{ required: true, message: '请输入托盘高度!' },] }
          ]">
        </a-input>
      </a-form-item>
      <a-form-item label="托盘类型">
        <a-input
          placeholder="托盘类型"
          v-decorator="[
            'type',
            { rules: [{ required: true, message: '请输入托盘类型!' },] }
          ]">
        </a-input>
      </a-form-item>
      <a-form-item label="是否有托盘码">
        <a-radio-group
          v-decorator="[
            'isVirtual',
            { rules: [{ required: true, message: '不能为空!' }],
              initialValue: 1
            }
          ]"
        >
          <a-radio :value="1">
            虚拟
          </a-radio>
          <a-radio :value="0">
            实物
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="托盘前缀编码">
        <a-input
          placeholder="托盘前缀编码"
          v-decorator="[
            'virtualPrefixCode',
            { rules: [{ required: true, message: '请输入托盘前缀编码!' },] }
          ]">
        </a-input>
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
      const { data } = await this.$store.dispatch('pallet/getOne', this.id)
      const columns = ['length', 'width', 'height', 'type', 'virtualPrefixCode', 'isVirtual']
      this.form.setFieldsValue(_.pick(data, columns))
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
            await this.$store.dispatch('pallet/edit', { id: this.id, updateEntity: values })
          } else {
            await this.$store.dispatch('pallet/add', values)
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
