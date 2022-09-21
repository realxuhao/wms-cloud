<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="跨编码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入跨编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="宽度" prop="width">
        <el-input
          v-model="queryParams.width"
          placeholder="请输入宽度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="承重" prop="bearWeight">
        <el-input
          v-model="queryParams.bearWeight"
          placeholder="请输入承重"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="货架id" prop="shelveId">
        <el-input
          v-model="queryParams.shelveId"
          placeholder="请输入货架id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="仓库代码" prop="wareCode">
        <el-input
          v-model="queryParams.wareCode"
          placeholder="请输入仓库代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="仓库名称" prop="wareName">
        <el-input
          v-model="queryParams.wareName"
          placeholder="请输入仓库名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="区域代码" prop="areaCode">
        <el-input
          v-model="queryParams.areaCode"
          placeholder="请输入区域代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="区域名称" prop="areaName">
        <el-input
          v-model="queryParams.areaName"
          placeholder="请输入区域名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="货架代码" prop="shelveCode">
        <el-input
          v-model="queryParams.shelveCode"
          placeholder="请输入货架代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="货架名称" prop="shelveName">
        <el-input
          v-model="queryParams.shelveName"
          placeholder="请输入货架名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:frame:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:frame:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:frame:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:frame:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="frameList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="跨编码" align="center" prop="code" />
      <el-table-column label="宽度" align="center" prop="width" />
      <el-table-column label="承重" align="center" prop="bearWeight" />
      <el-table-column label="货架id" align="center" prop="shelveId" />
      <el-table-column label="仓库代码" align="center" prop="wareCode" />
      <el-table-column label="仓库名称" align="center" prop="wareName" />
      <el-table-column label="区域代码" align="center" prop="areaCode" />
      <el-table-column label="区域名称" align="center" prop="areaName" />
      <el-table-column label="货架代码" align="center" prop="shelveCode" />
      <el-table-column label="货架名称" align="center" prop="shelveName" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:frame:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:frame:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改跨对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="跨编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入跨编码" />
        </el-form-item>
        <el-form-item label="宽度" prop="width">
          <el-input v-model="form.width" placeholder="请输入宽度" />
        </el-form-item>
        <el-form-item label="承重" prop="bearWeight">
          <el-input v-model="form.bearWeight" placeholder="请输入承重" />
        </el-form-item>
        <el-form-item label="货架id" prop="shelveId">
          <el-input v-model="form.shelveId" placeholder="请输入货架id" />
        </el-form-item>
        <el-form-item label="仓库代码" prop="wareCode">
          <el-input v-model="form.wareCode" placeholder="请输入仓库代码" />
        </el-form-item>
        <el-form-item label="仓库名称" prop="wareName">
          <el-input v-model="form.wareName" placeholder="请输入仓库名称" />
        </el-form-item>
        <el-form-item label="区域代码" prop="areaCode">
          <el-input v-model="form.areaCode" placeholder="请输入区域代码" />
        </el-form-item>
        <el-form-item label="区域名称" prop="areaName">
          <el-input v-model="form.areaName" placeholder="请输入区域名称" />
        </el-form-item>
        <el-form-item label="货架代码" prop="shelveCode">
          <el-input v-model="form.shelveCode" placeholder="请输入货架代码" />
        </el-form-item>
        <el-form-item label="货架名称" prop="shelveName">
          <el-input v-model="form.shelveName" placeholder="请输入货架名称" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFrame, getFrame, delFrame, addFrame, updateFrame } from "@/api/system/frame";

export default {
  name: "Frame",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 跨表格数据
      frameList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        width: null,
        bearWeight: null,
        shelveId: null,
        wareCode: null,
        wareName: null,
        areaCode: null,
        areaName: null,
        shelveCode: null,
        shelveName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [
          { required: true, message: "跨编码不能为空", trigger: "blur" }
        ],
        width: [
          { required: true, message: "宽度不能为空", trigger: "blur" }
        ],
        bearWeight: [
          { required: true, message: "承重不能为空", trigger: "blur" }
        ],
        shelveId: [
          { required: true, message: "货架id不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询跨列表 */
    getList() {
      this.loading = true;
      listFrame(this.queryParams).then(response => {
        this.frameList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        code: null,
        width: null,
        bearWeight: null,
        shelveId: null,
        wareCode: null,
        wareName: null,
        areaCode: null,
        areaName: null,
        shelveCode: null,
        shelveName: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加跨";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getFrame(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改跨";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFrame(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFrame(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除跨编号为"' + ids + '"的数据项？').then(function() {
        return delFrame(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/frame/export', {
        ...this.queryParams
      }, `frame_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
