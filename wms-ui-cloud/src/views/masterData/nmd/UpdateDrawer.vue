<template>
  <a-drawer
    width="800px"
    :title="title"
    placement="right"
    :visible="visible"
    @close="onClose"
  >
    <a-form :form="form" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="料号">
        <a-input
          :disabled="updateType === 'edit'"
          placeholder="料号"
          v-decorator="[
            'materialCode',
            { rules: [{ required: true, message: '请输料号!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="分类">
        <a-select
          allowClear
          show-search
          :filter-option="filterOption"
          option-filter-prop="children"
          v-decorator="[
            'classification',
            { rules: [{ required: true, message: '请选择料号分类！' }] }
          ]"
          placeholder="料号分类">
          <a-select-option :value="item.value" v-for="item in category" :key="item.value">
            {{ item.text }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="检验水平级别">
        <a-select
          allowClear
          show-search
          :filter-option="filterOption"
          option-filter-prop="children"
          v-decorator="[
            'level',
            { rules: [{ required: true, message: '请选择检验水平级别！' }] }
          ]"
          placeholder="检验水平级别">
          <a-select-option :value="item.value" v-for="item in checkLevel" :key="item.value">
            {{ item.text }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="严格程度">
        <a-select
          allowClear
          show-search
          :filter-option="filterOption"
          v-decorator="[
            'plan',
            { rules: [{ required: true, message: '请选严格程度！' }] }
          ]"
          placeholder="严格程度">
          <a-select-option :value="item.value" v-for="item in strictLevel" :key="item.value">
            {{ item.text }}
          </a-select-option>
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
    },
    category: {
      type: Array,
      default: () => []
    },
    checkLevel: {
      type: Array,
      default: () => []
    },
    strictLevel: {
      type: Array,
      default: () => []
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
    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      )
    },
    async getAndUpdateForm () {
      const { data } = await this.$store.dispatch('nmdRule/getOne', this.id)
      const columns = ['materialCode', 'classification', 'level', 'plan']
      this.form.setFieldsValue(_.pick(data, columns))
    },
    async loadNMDRuleList () {
    },
    async loadData () {
      await this.loadNMDRuleList()
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
            await this.$store.dispatch('nmdRule/edit', { id: this.id, updateEntity: values })
          } else {
            await this.$store.dispatch('nmdRule/add', values)
          }

          this.$emit('on-ok')
          this.onClose()
        } catch (error) {
          console.log(error.code)
          console.log(error.message)

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
