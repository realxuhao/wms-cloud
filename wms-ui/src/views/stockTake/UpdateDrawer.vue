<template>
  <a-drawer
    width="640px"
    :title="title"
    placement="right"
    :visible="visible"
    @close="onClose"
  >
    <a-form :form="form" :label-col="labelCol" :wrapper-col="wrapperCol">

      <a-form-item label="盘点cell">
        <a-select
          v-decorator="[
            'cell',
            { rules: [{ required: false, message: '请选择盘点cell!' },] }
          ]">
          <a-select-option v-for="(value, key) in cell" :key="key" :value="key">{{ value }}</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="盘点仓库">
        <a-select
          @change="onWareChange"
          v-decorator="[
            'wareCode',
            { rules: [{ required: false, message: '请选择盘点仓库!' },] }
          ]">
          <a-select-option v-for="item in wareOptionList" :key="item.id" :value="item.id">
            {{ item.code }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="盘点区域">
        <a-select
          @change="onWareChange"
          v-decorator="[
            'areaCode',
            { rules: [{ required: false, message: '请选择盘点区域!' },] }
          ]">
          <a-select-option v-for="item in areaList" :key="item.id" :value="item.id">
            {{ item.code }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="盘点类型">
        <a-select
          v-decorator="[
            'type',
            { rules: [{ required: true, message: '请选择盘点类型!' },] }
          ]">
          <a-select-option v-for="(value, key) in type" :key="key" :value="key">{{ value }}</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="盘点方式">
        <a-select
          v-decorator="[
            'method',
            { rules: [{ required: true, message: '请选择盘点方式!' },] }
          ]">
          <a-select-option v-for="(value, key) in method" :key="key" :value="key">{{ value }}</a-select-option>
        </a-select>
      </a-form-item>



      <a-form-item label="物料类型">
        <a-select
          v-decorator="[
            'takeMaterialType',
            { rules: [{ required: true, message: '请选择物料类型!' },] }
          ]">
          <a-select-option v-for="(value, key) in takeMaterialType" :key="key" :value="key">{{ value }}</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="循环盘点月份">
        <a-input
          placeholder="循环盘点月份"
          v-decorator="[      'circleTakeMonth',      { rules: [{ required: false, message: '请输入循环盘点月份!' }] }
    ]"
        />
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
const type =
  {
    0: '明盘',
    1: '盲盘'
  }

const method =
  {
    0: '普通盘点',
    1: '循环盘点'
  }
const takeMaterialType =
  {
    0: '原材料',
    1: '成品'
  }
const cell =
  {
    ECN: 'ECN',
    NMD: 'NMD',
    FSMP: 'FSMP',
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
      wareOptionList: [],
      areaList: [],
      wareList: []
    }
  },
  model: {
    prop: 'visible',
    event: 'change'
  },
  computed: {
    type: () => type,
    method: () => method,
    takeMaterialType: () => takeMaterialType,
    cell: () => cell,
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
    onWareChange (value) {
      console.log('value',value)
     this.form.setFieldsValue({
       areaCode:null
     })
      this.getAreaList(value)
    },
    async getAreaList(wareId){
      try {
        const data = await this.$store.dispatch('area/getList', { wareId })
        this.areaList = data
      } catch (error) {
        this.$message.error('获取存储区数据失败！')
      } finally {
        this.areaLoading = false
      }
    },
    /** 获取仓库List */
    async getWareOptionList () {
      try {
        const data = await this.$store.dispatch('ware/getOptionList')
        console.log('data',data)
        this.wareOptionList = data.data
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    onClose () {
      this.form.resetFields()

      this.$emit('change', false)
    },
    async getAndUpdateForm () {
      const { data } = await this.$store.dispatch('area/getOne', this.id)
      this.form.setFieldsValue(_.pick(data, ['code', 'name', 'wareId']))
    },
    async getWareList () {
      const list = await this.$store.dispatch('ware/getList')
      this.wareList = list
    },
    async loadData () {
      //await this.getWareList()
      await this.getWareOptionList()
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
            await this.$store.dispatch('stockTake/edit', { id: this.id, updateEntity: values })
          } else {
            await this.$store.dispatch('stockTake/add', values)
          }

          this.$emit('on-ok')
          this.onClose()
        } catch (error) {
          this.$message.error(error.message)
        } finally {
          this.submitLoading = false
          this.form.setFieldsValue({
            areaCode:null,
            wareCode:null
          })

        }
      })
    }
  },
  created(){
    console.log(2222)
    this.loadData()
  },
  watch: {
    visible (val) {
      if (val) {

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
