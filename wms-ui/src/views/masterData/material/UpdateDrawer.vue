<template>
  <a-drawer
    width="1200px"
    :title="title"
    placement="right"
    :visible="visible"
    @close="onClose"
  >
    <a-form :form="form" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="物料名称">
        <a-input
          placeholder="物料名称"
          v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入物料名称!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="物料编码">
        <a-input
          placeholder="物料编码"
          v-decorator="[
            'code',
            { rules: [{ required: true, message: '请输入物料编码!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="物料类型">
        <a-select
          allowClear
          show-search
          :filter-option="filterOption"
          option-filter-prop="children"
          :loading="materialTypeListLoading"
          v-decorator="[
            'materialTypeId',
            { rules: [{ required: true, message: '请选择物料类型！' }] }
          ]"
          placeholder="物料类型">
          <a-select-option :value="item.id" v-for="item in materialTypeList" :key="item.id">
            {{ item.code }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="托盘类型">
        <a-select
          allowClear
          show-search
          :filter-option="filterOption"
          v-decorator="[
            'palletId',
            { rules: [{ required: true, message: '请选择托盘类型！' }] }
          ]"
          placeholder="托盘类型">
          <a-select-option :value="item.id" v-for="item in palletTypeList" :key="item.id">
            {{ item.type }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <!-- <a-form-item label="包装重量">
        <a-input-number
          placeholder="包装重量"
          v-decorator="[
            'packageWeight',
            { rules: [{ required: true, message: '请输入包装重量!' }] }
          ]" />
      </a-form-item> -->
      <a-form-item label="托盘重量">
        <a-input-number
          placeholder="托盘重量"
          v-decorator="[
            'palletWeight',
            { rules: [{ required: true, message: '请输入托盘重量!' }] }
          ]" />
      </a-form-item>
      <!-- <a-form-item label="是否绑定托盘">
        <a-radio-group
          v-decorator="[
            'bindPallet',
            { rules: [{ required: true, message: '请选择是否绑定托盘!' }],
              initialValue: 1
            }
          ]"
        >
          <a-radio :value="1">
            是
          </a-radio>
          <a-radio :value="0">
            否
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="是否带托盘">
        <a-radio-group
          v-decorator="[
            'hasPallet',
            { rules: [{ required: true, message: '请选择是否带托盘!' }] ,
              initialValue: 1
            }
          ]"
        >
          <a-radio :value="1">
            是
          </a-radio>
          <a-radio :value="0">
            否
          </a-radio>
        </a-radio-group>
      </a-form-item> -->
      <a-form-item label="来料总重量">
        <a-input-number
          placeholder="来料总重量"
          v-decorator="[
            'totalWeight',
            { rules: [{ required: true, message: '请输入来料总重量!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="物料防错方式">
        <a-select
          v-decorator="[
            'errorProofingMethod',
            { rules: [{ required: true, message: '请选择物料防错方式！' }] }
          ]"
          placeholder="物料防错方式">
          <a-select-option value="称重">
            称重
          </a-select-option>
          <a-select-option value="数量">
            数量
          </a-select-option>
          <a-select-option value="免检">
            免检
          </a-select-option>
        </a-select>

      </a-form-item>

      <a-form-item label="允许负偏差比例">
        <a-input-number
          placeholder="允许负偏差比例（绝对值）"
          v-decorator="[
            'lessDeviationRatio',
            { rules: [{ required: true, message: '请输入允许负偏差比例!' }] }
          ]" />
      </a-form-item>

      <a-form-item label="最小包装重量">
        <a-input-number
          placeholder="最小包装重量（净重）"
          v-decorator="[
            'minPackageNetWeight',
            { rules: [{ required: true, message: '请输入最小包装重量!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="最小包装毛重">
        <a-input-number
          placeholder="最小包装毛重"
          v-decorator="[
            'packageWeight',
            { rules: [{ required: true, message: '不能为空!' }] }
          ]" />
      </a-form-item>

      <a-form-item label="最小包装数量">
        <a-input-number
          placeholder="最小包装数量"
          v-decorator="[
            'minPackageNumber',
            { rules: [{ required: true, message: '请输入最小包装数量!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="单位">
        <a-select
          v-decorator="[
            'unit',
            { rules: [{ required: true, message: '请选择单位' }], initialValue: 'kg' }
          ]"
          placeholder="物料防错方式">
          <a-select-option value="KG">
            KG
          </a-select-option>
          <a-select-option value="M">
            M
          </a-select-option>
          <a-select-option value="L">
            L
          </a-select-option>
          <a-select-option value="㎡">
            ㎡
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="最小包装量允许最小值">
        <a-input-number
          placeholder="最小包装量允许最小值"
          v-decorator="[
            'lessDeviationRatio',
            { rules: [{ required: true, message: '不能为空!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="最小包装量允许最大值">
        <a-input-number
          placeholder="最小包装量允许最大值"
          v-decorator="[
            'moreDeviationRatio',
            { rules: [{ required: true, message: '不能为空!' }] }
          ]" />
      </a-form-item>
      <a-form-item label="标准计数单位[L,Kg,m,㎡]对应的重量值KG[只针对称重物料]">
        <a-input-number
          placeholder="标准计数单位[L,Kg,m,㎡]对应的重量值KG[只针对称重物料]"
          v-decorator="[
            'transferWeightRatio',
            { rules: [{ required: true, message: '不能为空!' }] }
          ]" />
      </a-form-item>

      <a-form-item label="备注">
        <a-input
          placeholder="备注"
          v-decorator="[
            'remark',
            { rules: [{ required: true, message: '请输入备注!' }] }
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
      submitLoading: false,

      materialTypeListLoading: false,
      materialTypeList: [],
      palletTypeList: []
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
      const { data } = await this.$store.dispatch('material/getOne', this.id)
      const columns = ['name', 'code', 'materialTypeId', 'packageWeight', 'palletWeight',
        'bindPallet', 'hasPallet', 'totalWeight', 'errorProofingMethod', 'lessDeviationRatio',
        'minPackageNetWeight', 'minPackageNumber', 'unit', 'remark', 'palletId', 'lessDeviationRatio',
        'moreDeviationRatio', 'transferWeightRatio', 'minPackageNetWeight']
      this.form.setFieldsValue(_.pick(data, columns))
    },
    async loadMaterialTypeList () {
      try {
        this.materialTypeListLoading = true

        const { data: { rows } } = await this.$store.dispatch('materialType/getList', { pageSize: 0 })
        this.materialTypeList = rows
      } catch (error) {
        this.$message.error('获取物料类型失败，请联系管理员！')
      } finally {
        this.materialTypeListLoading = false
      }
    },
    async loadPalletTypeList () {
      try {
        const rows = await this.$store.dispatch('pallet/getList')
        this.palletTypeList = rows
      } catch (error) {
        this.$message.error('获取托盘失败，请联系管理员！')
      }
    },
    async loadData () {
      await this.loadMaterialTypeList()
      await this.loadPalletTypeList()
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
            await this.$store.dispatch('material/edit', { id: this.id, updateEntity: values })
          } else {
            await this.$store.dispatch('material/add', values)
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
