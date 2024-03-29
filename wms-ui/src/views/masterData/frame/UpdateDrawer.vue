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
      <a-form-item label="跨类型">
        <a-input
          placeholder="跨类型"
          v-decorator="[
            'typeCode',
            { rules: [{ required: true, message: '请输入跨类型!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="跨编码">
        <a-input
          placeholder="跨编码"
          v-decorator="[
            'code',
            { rules: [{ required: true, message: '请输入跨编码!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="跨名称">
        <a-input
          placeholder="跨名称"
          v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入跨名称!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="承重[kg]">
        <a-input-number
          placeholder="承重[kg]"
          v-decorator="[
            'bearWeight',
            { rules: [{ required: true, message: '请输入承重!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="宽度[cm]">
        <a-input-number
          placeholder="宽度[cm]"
          v-decorator="[
            'width',
            { rules: [{ required: true, message: '请输入宽度!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="高度[cm]">
        <a-input-number
          placeholder="高度[cm]"
          v-decorator="[
            'height',
            { rules: [{ required: true, message: '请输入高度!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="仓库">
        <a-select
          @change="handleGetAreaList"
          placeholder="仓库"
          v-decorator="[
            'wareId',
            { rules: [{ required: true, message: '请选择仓库!' },] }
          ]">
          <a-select-option v-for="item in wareList" :key="item.id" :value="item.id">{{ item.name }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="存储区">
        <a-select
          placeholder="存储区"
          :loading="areaLoading"
          v-decorator="[
            'areaId',
            { rules: [{ required: true, message: '请选择存储区!' },] }
          ]">
          <a-select-option v-for="item in areaList" :key="item.id" :value="item.id">{{ item.name }}</a-select-option>
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
    async handleGetAreaList (wareId) {
      try {
        this.form.setFieldsValue({ areaId: null })
        this.areaLoading = true
        const data = await this.$store.dispatch('area/getList', { wareId })
        this.areaList = data
      } catch (error) {
        this.$message.error('获取存储区数据失败！')
      } finally {
        this.areaLoading = false
      }
    },
    async getAndUpdateForm () {
      const { data } = await this.$store.dispatch('frame/getOne', this.id)
      this.form.setFieldsValue(_.pick(data, ['code', 'name', 'bearWeight', 'width', 'wareId', 'areaId']))
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
            await this.$store.dispatch('frame/edit', { id: this.id, updateEntity: values })
          } else {
            await this.$store.dispatch('frame/add', values)
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

          this.handleGetAreaList()
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
