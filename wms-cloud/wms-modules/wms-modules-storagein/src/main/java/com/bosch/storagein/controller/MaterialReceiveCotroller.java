package com.bosch.storagein.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.storagein.api.domain.MaterialReceive;
import com.bosch.storagein.api.domain.dto.MaterialQueryDTO;
import com.bosch.storagein.api.domain.dto.MaterialReceiveDTO;
import com.bosch.storagein.api.domain.dto.request.EditBean;
import com.bosch.storagein.api.domain.vo.MaterialReceiveVO;
import com.bosch.storagein.api.enumeration.ClassType;
import com.bosch.storagein.service.IMaterialReceiveService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "原材料收货接口")
@RequestMapping("/material-receive")
public class MaterialReceiveCotroller extends BaseController {

    @Autowired
    private FileService fileService;
    @Autowired
    private IMaterialReceiveService materialReceiveService;



    @GetMapping("/list")
    @ApiOperation("查询收货列表")
    public R<PageVO<MaterialReceiveVO>> list(MaterialReceiveDTO materialReceiveDTO) {
        startPage();
        List<MaterialReceiveVO> list = materialReceiveService.selectMaterialReceiveList(materialReceiveDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @Log(title = "原材料收货", businessType = BusinessType.DELETE)
    @ApiOperation("逻辑删除原材料收货")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(materialReceiveService.deleteMaterialReceiveById(id));
    }


    @ApiOperation("批量逻辑删除原材料收货")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(materialReceiveService.deleteMaterialReceiveByIds(ids));
    }

    @GetMapping(value = "/{id}")
    @ApiOperation("获取收货信息详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(materialReceiveService.selectById(id));
    }


    @PatchMapping("/edit")
    @ApiOperation("批量是否可编辑")
    public AjaxResult edit(@RequestBody EditBean editBean) {
        Asserts.notNull(editBean, "参数不能为空");
        Asserts.check(!CollectionUtils.isEmpty(editBean.getIds()), "id不能为空");
        Asserts.notNull(editBean.getEditFlag(), "editFlag不能为空");
        return AjaxResult.success((materialReceiveService.updateEditFlag(editBean.getIds(), editBean.getEditFlag())));
    }

    /**
     * 批量上传
     */
    @ApiOperation("批量上传")
    @PostMapping(value = "/import" , headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R importExcel(@RequestPart(value = "file" , required = true) MultipartFile file) throws IOException {
        try {
            //解析文件服务
            R result = fileService.materialReceiveImport(file, ClassType.MATERIALRECEIVE.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<MaterialReceive> dtos = JSON.parseArray(JSON.toJSONString(data), MaterialReceive.class);
                if (!CollectionUtils.isEmpty(dtos)) {
                    List<String> ssccList = dtos.stream().map(MaterialReceive::getSsccNumber).collect(Collectors.toList());

                    //查询是否有已上架状态的sscc码
                    boolean validReceive = materialReceiveService.validReceive(ssccList);
                    if (validReceive){
                        return R.fail("excel中存在已入库状态的SSCCNumber,请修改后上传");
                    }
                    boolean validList = materialReceiveService.validList(ssccList);
                    if (validList) {
                        return R.fail(400, "存在重复数据");
                    } else {
                        //添加
                        dtos.forEach(item->item.setMoveType(MoveTypeEnums.RECEIVE.getCode()));
                        materialReceiveService.saveBatch(dtos);
                    }
                }
                return R.ok("解析成功");
            } else {
                return R.fail(result.getMsg());
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }

    }

    /**
     * 批量更新
     */
    @ApiOperation("批量更新")
    @PostMapping(value = "/saveBatch" , headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R saveBatch(@RequestPart(value = "file" , required = true) MultipartFile file) throws IOException {

        try {
            //解析文件服务
            R result = fileService.materialReceiveImport(file, ClassType.MATERIALRECEIVE.getDesc());
            if (result.isSuccess()) {

                Object data = result.getData();
                List<MaterialReceive> dtos = JSON.parseArray(JSON.toJSONString(data), MaterialReceive.class);
                if (!CollectionUtils.isEmpty(dtos)) {

                    dtos.forEach(r->{
                        r.setMoveType(MoveTypeEnums.RECEIVE.getCode());
                        boolean update =materialReceiveService.updateBatchReceive(r);
                        if (!update){
                            r.setUpdateTime(null);
                            r.setUpdateUser(null);
                            materialReceiveService.save(r);
                        }
                    });
                }
                return R.ok("导入成功");
            }
            else {
                return R.fail(result.getMsg());
            }

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }

    }


}
