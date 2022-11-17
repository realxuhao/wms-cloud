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
      <a-form-item label="Cell" required>
        <a-select
          v-decorator="[
            'cell',
            { rules: [{ required: true, message: '不能为空!' }] }
          ]" >
          <a-select-option v-for="item in departmentList" :key="item.id" :value="item.code">
            {{ item.code }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="库存检索规则" required>
        <a-radio-group
          v-decorator="[
            'sortType',
            { rules: [{ required: true, message: '不能为空!' }] }]">
          <a-radio value="0">
            基于有效期
          </a-radio>
          <a-radio value="1">
            基于先主库后外库
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="Dragger">
        <div class="dropbox">
          <a-upload-dragger
            :file-list="[]"
            name="file"
            :multiple="true"
            :before-upload="()=>false"
            @change="handleUpload"
            accept=".xlsx"
            v-decorator="[
              'file',
              { rules: [{ required: true, message: '不能为空!' }]}
            ]"
          >
            <p class="ant-upload-drag-icon">
              <a-icon type="inbox" />
            </p>
            <p>{{ fileName }}</p>
          </a-upload-dragger>
          <div class="download-template">
            <a >下载模板</a>
          </div>
        </div>
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

      departmentList: [],
      fileName: ''
    }
  },
  model: {
    prop: 'visible',
    event: 'change'
  },
  computed: {
    title () {
      return '上传'
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
    async handleDownloadTemplate () {
      try {
        this.$store.dispatch('file/downloadByFilename', 'WMS产线叫料需求导入模板.xlsx')
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFieldsAndScroll(async (err, values) => {
        if (err) {
          console.log('Received values of form: ', values)
          return
        }

        try {
          console.log(values.file)
          this.submitLoading = true
          const formData = new FormData()
          formData.append('file', values.file.file)
          formData.append('cell', values.code)
          formData.append('sortType', values.sortType)

          await this.$store.dispatch('materialFeeding/upload', formData)
          this.$emit('on-ok')
          this.onClose()
        } catch (error) {
          this.$message.error(error.message)
        } finally {
          this.submitLoading = false
        }
      })
    },
    async handleUpload (e) {
      const { file } = e

      this.fileName = file.name
      this.form.setFieldsValue({
        file
      })
    },
    async loadData () {
      const departmentList = await this.$store.dispatch('materialFeeding/getDepartmentList')
      this.departmentList = departmentList
    }
  },
  watch: {
    visible (val) {
      if (val) {
        this.loadData()
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
.download-template{
  text-align: right;
}
</style>
