<template>
  <a-drawer
    width="640px"
    :title="title"
    placement="right"
    :visible="visible"
    @close="onClose"
  >
    <a-form :form="form" :label-col="labelCol" :wrapper-col="wrapperCol">
      <!-- <a-form-item label="司机姓名">
        <a-input
          placeholder="司机姓名"
          v-decorator="[
            'driverName',
            { rules: [{ required: true, message: '请输入司机姓名!' }] }
          ]" />
      </a-form-item> -->
      <a-form-item label="是否加入黑名单">
        <a-select
          style="width: 100%;"
          placeholder="是否加入黑名单"
          v-decorator="[
            'status',
            { rules: [{ required: true, message: '请选择是否加入黑名单!' }] }
          ]">
          <a-select-option v-for="item in statusList" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="备注">
        <a-textarea
          row="4"
          placeholder="备注"
          v-decorator="[
            'remark'
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
    driverId: {
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
      statusList: [
        { value: 0, label: '否' },
        { value: 1, label: '是' }
      ]
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
      const { data } = await this.$store.dispatch('blackDriver/getOne', this.driverId)
      this.form.setFieldsValue(_.pick(data, ['status', 'remark']))
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
            await this.$store.dispatch('blackDriver/edit', { id: this.driverId, updateEntity: values })
          } else {
            await this.$store.dispatch('blackDriver/add', values)
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
</style>
