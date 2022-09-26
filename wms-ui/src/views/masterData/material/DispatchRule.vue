<template>
  <a-drawer
    width="800px"
    title="设置分配规则"
    placement="right"
    :visible="visible"
    @close="onClose"
  >

    <a-button class="m-b-8" v-show="!formVisible" type="primary" icon="plus" @click="handleAdd">
      新建
    </a-button>

    <a-form v-show="formVisible" class="search-content m-b-8" layout="inline" :form="form">
      <a-form-item label="库位">
        <a-select
          class="width180"
          v-decorator="[
            'binId',
            { rules: [{ required: true, message: '请选择库位!' },] }
          ]">
          <a-select-option v-for="item in binList" :key="item.id" :value="item.id">{{ item.name }}</a-select-option>
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
      <a-form-item label="">
        <a-button class="m-r-8" type="primary" @click="handleSubmit" :loading="submitLoading">确认</a-button>
        <a-button @click="formVisible = false" :loading="submitLoading">取消</a-button>

      </a-form-item>
    </a-form>

    <a-table
      :columns="columns"
      :data-source="list"
      :loading="tableLoading"
      rowKey="id"
      :pagination="false"
      size="middle"
    >
      <template slot="action" slot-scope="text, record">
        <div class="action-con">
          <a-popconfirm title="确认要删除吗?" ok-text="确认" cancel-text="取消" @confirm="handleDelete(record)">
            <a class="danger-color"><a-icon class="m-r-4" type="delete" />删除</a>
          </a-popconfirm>
        </div>
      </template>
    </a-table>
  </a-drawer>
</template>

<script>
const labelCol = {
  span: 5
}
const wrapperCol = {
  span: 19
}

const columns = [
  {
    title: '物料名称',
    key: 'materialName',
    dataIndex: 'materialName',
    width: 200
  },
  {
    title: '库位名称',
    key: 'binName',
    dataIndex: 'binName',
    width: 200
  },
  {
    title: '优先级',
    key: 'priorityLevel',
    dataIndex: 'priorityLevel',
    width: 200
  },
  {
    title: '操作',
    key: 'action',
    width: 60,
    scopedSlots: { customRender: 'action' }
  }
]

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
    }
  },
  data () {
    return {
      form: this.$form.createForm(this),
      submitLoading: false,

      columns,
      list: [],
      tableLoading: false,

      binList: [],
      formVisible: false
    }
  },
  model: {
    prop: 'visible',
    event: 'change'
  },
  computed: {
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
      this.formVisible = false

      this.$emit('change', false)
    },
    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      )
    },
    async handleAdd () {
      this.formVisible = true
    },
    async handleDelete (record) {
      try {
        await this.$store.dispatch('material/destroyDispatchBin', record.id)
        this.$message.success('删除成功！')

        this.loadDispatchBinList()
      } catch (error) {
        console.log(error)
        this.$message.error('删除失败，请联系系统管理员！')
      }
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
          this.formVisible = true

          await this.$store.dispatch('material/addDiapatchBin', { ...values, materialId: this.id })

          this.formVisible = false

          this.$message.success('添加成功')
        } catch (error) {
          this.$message.error(error)
        } finally {
          this.submitLoading = false
        }
      })
    },
    async loadDispatchBinList () {
      try {
        this.tableLoading = true

        const { data: { rows } } = await this.$store.dispatch('material/getDispatchBinList', { id: this.id })
        this.list = rows
      } catch (error) {
        this.$message.error(error)
      } finally {
        this.tableLoading = false
      }
    },
    async getBinList () {
      try {
        const data = await this.$store.dispatch('bin/getList')
        this.binList = data
      } catch (error) {
        this.$message.error(error)
      }
    },
    async loadData () {
      await this.loadDispatchBinList()
      await this.getBinList()
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
</style>
