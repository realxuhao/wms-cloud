<template>
  <a-drawer
    width="640px"
    :title="title"
    placement="right"
    :visible="visible"
    @close="onClose"
  >
    <a-form class="search-content m-b-8" :label-col="labelCol" :wrapper-col="wrapperCol" :form="form">
      <a-form-item label="物料">
        <a-select
          show-search
          style="width:100%"
          :filter-option="filterOption"
          v-decorator="[
            'materialId',
            { rules: [{ required: true, message: '请选择物料!' },] }
          ]">
          <a-select-option v-for="item in materialList" :key="item.id" :value="item.id">{{ item.productNo }}-{{ item.productName }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="跨类型">
        <a-select
          show-search
          :filter-option="filterOption"
          style="width:100%"
          v-decorator="[
            'frameTypeCode',
            { rules: [{ required: true, message: '请选择跨类型!' },] }
          ]">
          <a-select-option v-for="item in frameTypeList" :key="item" :value="item">{{ item }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="优先级">
        <a-input
          placeholder="优先级"
          v-decorator="[
            'priorityLevel',
            { rules: [{ required: true, message: '请输入优先级!' }] }
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
  span: 18
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
    frameTypeList: {
      type: Array,
      default: () => []
    },
    materialList: {
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
      const { data } = await this.$store.dispatch('product/getOne', this.id)
      const columns = ['priorityLevel', 'materialId', 'frameTypeCode']
      this.form.setFieldsValue(_.pick(data, columns))
    },

    async loadData () {
    },
    async handleSubmit (e) {
      e.preventDefault()
      this.form.validateFieldsAndScroll(async (err, values) => {
        if (err) {
          console.log('Received values of form: ', values)
          return
        }

        try {
          this.submitLoading = true

          if (this.updateType === 'edit') {
            await this.$store.dispatch('product/edit', { id: this.id, updateEntity: values })
          } else {
            await this.$store.dispatch('product/addDispatchBin', { ...values })
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
