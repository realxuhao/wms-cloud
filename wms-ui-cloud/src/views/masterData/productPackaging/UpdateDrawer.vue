<template>
  <a-drawer
    width="640px"
    :title="title"
    placement="right"
    :visible="visible"
    @close="onClose"
  >
    <a-form :form="form" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="成品料号">
        <a-input
          placeholder="成品料号"
          v-decorator="[
            'productNo',
            { rules: [{ required: true, message: '请输入成品料号!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="成品名称">
        <a-input
          placeholder="成品名称"
          v-decorator="[
            'productName',
            { rules: [{ required: true, message: '请输入成品名称!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="cell">
        <a-input
          placeholder="cell"
          v-decorator="[
            'cell',
            { rules: [{ required: true, message: '请输入cell!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="运输单位(Tr)">
        <a-input
          placeholder="运输单位(Tr)"
          v-decorator="[
            'transportUnit',
            { rules: [{ required: true, message: '请输入运输单位(Tr)!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="箱 Tr 对应包装规格">
        <a-input
          placeholder="箱 Tr 对应包装规格"
          v-decorator="[
            'boxSpecification',
            { rules: [{ required: true, message: '请输入箱 Tr 对应包装规格!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="标准 Tr/托">
        <a-input
          placeholder="标准 Tr/托"
          v-decorator="[
            'standardUnits',
            { rules: [{ required: true, message: '请输入标准 Tr/托!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="重量(Tr)">
        <a-input
          placeholder="重量(Tr)"
          v-decorator="[
            'weight',
            { rules: [{ required: true, message: '请输入重量(Tr)!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="体积 (Tr)">
        <a-input
          placeholder="体积 (Tr)"
          v-decorator="[
            'volume',
            { rules: [{ required: true, message: '请输入体积 (Tr)!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="类别">
        <a-input
          placeholder="类别"
          v-decorator="[
            'type',
            { rules: [{ required: true, message: '请输入类别!' }] }
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
      const { data } = await this.$store.dispatch('productPackaging/getOne', this.id)
      this.form.setFieldsValue(_.pick(data, ['productNo', 'productName', 'cell', 'transportUnit', 'boxSpecification', 'standardUnits', 'weight', 'volume','type']))
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
            await this.$store.dispatch('productPackaging/edit', { id: this.id, updateEntity: values })
          } else {
            await this.$store.dispatch('productPackaging/add', values)
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
