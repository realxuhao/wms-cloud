package com.bosch.masterdata.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.Ecn;
import com.bosch.masterdata.api.domain.Fsmp;
import com.bosch.masterdata.api.domain.dto.EcnDTO;
import com.bosch.masterdata.api.domain.dto.FsmpDTO;
import com.bosch.masterdata.api.domain.vo.EcnVO;
import com.bosch.masterdata.api.domain.vo.FsmpVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.masterdata.service.IFsmpService;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/fsmp")
@Api(tags = "fsmp主数据")
public class FsmpController extends BaseController {

    @Autowired
    private IFsmpService fsmpService;

    @Autowired
    private FileService fileService;

    /**
     * 查询列表
     */
    @ApiOperation("查询列表")
    @PostMapping("/list")
    public R<PageVO<FsmpVO>> list(@RequestBody FsmpDTO dto) {
        startPage();
        List<FsmpVO> list = fsmpService.selectList(dto);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    /**
     * 根据物料号查询
     */
    @ApiOperation("根据物料号查询")
    @GetMapping("/getByMaterialNb/{materialNb}")
    public R<Fsmp> getByMaterialNb(@PathVariable("materialNb") String materialNb) {
        return R.ok(fsmpService.getByMaterialNb(materialNb));
    }


    /**
     * 获取详细信息
     */
    @ApiOperation("获取详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(fsmpService.selectFsmpById(id));
    }
    /**
     * 新增
     */
    @Log(title = "新增Ecn", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增")
    public AjaxResult add(@RequestBody FsmpDTO dto) {
        return toAjax(fsmpService.insertFsmp(dto));
    }

    /**
     * 修改
     */
    @Log(title = "修改Ecn", businessType = BusinessType.UPDATE)
    @ApiOperation("修改")
    @PutMapping
    public AjaxResult edit(@RequestBody FsmpDTO dto) {
        Fsmp fsmp = BeanConverUtil.conver(dto, Fsmp.class);
        return toAjax(fsmpService.updateById(fsmp));
    }

    /**
     * 删除
     */
    @Log(title = "删除Ecn", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    @DeleteMapping("/{ids}")
    public AjaxResult delete(@PathVariable Long[] ids) {
        return toAjax(fsmpService.deleteFsmp(ids));
    }

    /**
     * 批量上传
     */
    @ApiOperation("批量上传")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    public R importExcel(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        //解析文件服务
        R result = fileService.masterDataImport(file, ClassType.FSMPDTO.getDesc());
        if (result.isSuccess()) {
            Object data = result.getData();
            List<FsmpDTO> dtoList = JSON.parseArray(JSON.toJSONString(data), FsmpDTO.class);
            if (CollectionUtils.isNotEmpty(dtoList)) {
                boolean valid = fsmpService.validFsmpList(dtoList);
                if (valid) {
                    return R.fail(400, "存在重复数据");
                } else {
                    //校验
                    fsmpService.validData(dtoList);
                    List<Fsmp> fsmps = BeanConverUtil.converList(dtoList, Fsmp.class);
                    fsmpService.saveBatch(fsmps);
                }
            } else {
                return R.fail("excel中无数据");
            }
            return R.ok(dtoList);
        } else {
            return R.fail(result.getMsg());
        }
    }

    /**
     * 批量更新
     */
    @ApiOperation("批量更新")
    @PostMapping(value = "/saveBatch", headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R saveBatch(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        try {
            //解析文件服务
            R result = fileService.masterDataImport(file, ClassType.ECNDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<FsmpDTO> dtoList = JSON.parseArray(JSON.toJSONString(data), FsmpDTO.class);
                if (CollectionUtils.isNotEmpty(dtoList)) {
                    //校验
                    fsmpService.validData(dtoList);
                    //转换DO
                    List<Fsmp> fsmps = BeanConverUtil.converList(dtoList, Fsmp.class);
                    fsmps.forEach(r -> {
                        LambdaUpdateWrapper<Fsmp> wrapper = new LambdaUpdateWrapper<Fsmp>();
                        wrapper.eq(Fsmp::getMaterialCode, r.getMaterialCode());
                        wrapper.eq(Fsmp::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
                        boolean update = fsmpService.update(r, wrapper);
                        if (!update) {
                            fsmpService.save(r);
                        }
                    });
                }
                return R.ok("导入成功");
            } else {
                return R.fail(result.getMsg());
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }

    }
}
