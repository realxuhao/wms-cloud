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
      <a-form-item label="时间窗口起">
        <a-time-picker
          class="width180"
          placeholder="时间窗口起"
          v-decorator="[
            'startTime',
            { rules: [{ required: true, message: '请选择时间!' }] }
          ]"
          format="HH:mm" />
      </a-form-item>
      <a-form-item label="时间窗口止">
        <a-time-picker
          class="width180"
          placeholder="时间窗口起"
          v-decorator="[
            'endTime',
            { rules: [{ required: true, message: '请选择时间!' }] }
          ]"
          format="HH:mm" />
      </a-form-item>
      <a-form-item label="道口">
        <!--  {pattern:/d/,required:true,message:'只能输入数字和小数点!'} -->
        <a-input
          placeholder="道口"
          v-decorator="[
            'windowCode',
            { rules: [{ required: true, message: '请输入道口!' },] }
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
import moment from 'moment'

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
      const { data } = await this.$store.dispatch('timeWindow/getOne', this.id)
      console.log(data)
      data.startTime = moment(data.startTime, 'HH:mm')
      data.endTime = moment(data.endTime, 'HH:mm')
      this.form.setFieldsValue(_.pick(data, ['startTime', 'endTime', 'windowCode']))
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
            await this.$store.dispatch('timeWindow/edit', { id: this.id, updateEntity: values })
          } else {
            await this.$store.dispatch('timeWindow/add', values)
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
