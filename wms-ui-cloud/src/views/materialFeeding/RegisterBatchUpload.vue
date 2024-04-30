<template>
  <div>
    <a-drawer
      width="640px"
      :title="title"
      placement="right"

      :visible="visible"
      @close="onClose"
    >
      <a-form :form="form" :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-form-item label="生产需求号" required>
          <a-auto-complete
            name="orderNumber"           
            :data-source="orderList"
            v-decorator="[
              'orderNumber',
              { rules: [{ required: true, message: '不能为空!' }] }
            ]"
            placeholder="请输入生产需求号"         
            :filter-option="filterOrder">            
          </a-auto-complete>
        </a-form-item>    
        <a-form-item label="Excel文件">
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
              <a @click="handleDownloadTemplate">下载模板</a>

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

    <a-modal
      v-drag-modal
      v-model="isVisibleWarning"
      title="提示"
      :width="420"
      @cancel="closeWarning"
      @ok="handleSubmitWarning"
      :confirmLoading="submitLoading">
      <p class="exceed-docknun-p">生产需求号已导入过相关的BOM信息，重新导入会覆盖原有数据</p>
    </a-modal>
  </div>

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
      orderList: [],
      fileName: '',
      isVisibleWarning: false,
      formData: null
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
        this.$store.dispatch('file/downloadByFilename', 'FSMP注册批BOM导入模板.xlsx')
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
          this.submitLoading = true
          const formData = new FormData()
          formData.append('file', values.file.file)
          formData.append('orderNumber', values.orderNumber)
          this.formData = formData
          const res = await this.$store.dispatch('materialFeeding/validRegisterOrderNumber', values.orderNumber)
          if (res) {
            this.isVisibleWarning = true
          } else {
            await this.handleSubmitWarning()
          }
         // this.$emit('on-ok')
         // this.onClose()
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
      const orderList = await this.$store.dispatch('materialFeeding/getRegisterOrderNumber')
      this.orderList = orderList
    },
    filterOrder(input, option) {
      return (
        option.componentOptions.children[0].text.toUpperCase().indexOf(input.toUpperCase()) >= 0
      )
    },
    closeWarning() {
      this.isVisibleWarning = false
    },
    async handleSubmitWarning(){
      try {       
          this.submitLoading = true
          await this.$store.dispatch('materialFeeding/uploadRegisterBom', this.formData)
          this.$emit('on-ok')
          this.onClose()
        } catch (error) {
          this.$message.error(error.message)
        } finally {
          this.submitLoading = false
          this.isVisibleWarning = false
        }
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
