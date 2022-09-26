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
      <a-form-item label="时间窗口余量">
        <!--  {pattern:/d/,required:true,message:'只能输入数字和小数点!'} -->
        <a-input
          placeholder="时间窗口余量"
          v-decorator="[
            'timeWindow',
            { rules: [{ required: true, message: '请输入时间窗口余量!' },] }
          ]">
          <span slot="addonAfter" style="width: 80px">
            min
          </span>
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
      const { data } = await this.$store.dispatch('supplier/getOne', this.id)
      this.form.setFieldsValue(_.pick(data, ['code', 'name', 'timeWindow']))
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
            await this.$store.dispatch('supplier/edit', { id: this.id, updateEntity: values })
          } else {
            await this.$store.dispatch('supplier/add', values)
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
