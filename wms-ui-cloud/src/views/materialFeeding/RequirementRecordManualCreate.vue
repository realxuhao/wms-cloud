<template>
  <a-drawer
    width="640px"
    title="手工创建需求"
    placement="right"
    :visible="visible"
    @close="onClose"
  >
    <a-form :form="form" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="生产需求号">
        <a-input
          placeholder="生产需求号"
          v-decorator="[
            'orderNb',
            { rules: [{ required: true, message: '请输入生产需求号!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="Cell">
        <a-select
          allow-clear
          placeholder="Cell"
          v-decorator="[
            'cell',
            { rules: [{ required: true, message: '请选择Cell!' }] }
          ]" >
          <a-select-option v-for="item in departmentList" :key="item.id" :value="item.code">
            {{ item.code }}
          </a-select-option>
        </a-select>
        <!-- <a-input
          placeholder="Cell"
          v-decorator="[
            'cell',
            { rules: [{ required: true, message: '请选择Cell!' }] }
          ]" /> -->
      </a-form-item>
      <a-form-item label="物料编码">
        <a-input
          placeholder="物料编码"
          v-decorator="[
            'materialNb',
            { rules: [{ required: true, message: '请输入物料编码!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="需求量">
        <a-input
          placeholder="需求量"
          v-decorator="[
            'quantity',
            { rules: [{ required: true, message: '请输入需求量!' }] }
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

const labelCol = {
  span: 5
}
const wrapperCol = {
  span: 19
}

export default {
  props: {

  },
  data () {
    return {
      visible: false,
      form: this.$form.createForm(this),
      submitLoading: false,

      departmentList: []
    }
  },
  computed: {
    labelCol () {
      return labelCol
    },
    wrapperCol () {
      return wrapperCol
    }

  },
  mounted () {
    this.loadData()
  },
  methods: {
    onOpen () {
      this.visible = true
    },
    onClose () {
      this.form.resetFields()
      this.visible = false
      // this.$emit('change', false)
    },
    async loadDepartmentList () {
      const departmentList = await this.$store.dispatch('materialFeeding/getDepartmentList')
      this.departmentList = departmentList
    },
    async loadData () {
      await this.loadDepartmentList()
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

          await this.$store.dispatch('materialFeeding/callAdd', values)

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
