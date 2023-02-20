package com.bosch.masterdata.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.Ecn;
import com.bosch.masterdata.api.domain.Nmd;
import com.bosch.masterdata.api.domain.dto.EcnDTO;
import com.bosch.masterdata.api.domain.dto.NmdDTO;
import com.bosch.masterdata.api.domain.vo.EcnVO;
import com.bosch.masterdata.api.domain.vo.NmdVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.masterdata.service.IEcnService;
import com.bosch.masterdata.service.INmdService;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 道口时间窗口Controller
 *
 * @author xuhao
 * @date 2022-09-22
 */
@RestController
@RequestMapping("/ecn")
@Api(tags = "ecn主数据")
public class EcnController extends BaseController {

    @Autowired
    private IEcnService ecnService;

    @Autowired
    private FileService fileService;

    /**
     * 查询列表
     */
    @ApiOperation("查询列表")
    @PostMapping("/list")
    public R<PageVO<EcnVO>> list(@RequestBody EcnDTO ecnDTO) {
        startPage();
        List<EcnVO> list = ecnService.selectList(ecnDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    /**
     * 获取详细信息
     */
    @ApiOperation("获取详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(ecnService.selectEcnById(id));
    }
    /**
     * 新增
     */
    @Log(title = "新增Ecn", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增")
    public AjaxResult add(@RequestBody EcnDTO ecnDTO) {
        return toAjax(ecnService.insertEcn(ecnDTO));
    }

    /**
     * 修改
     */
    @Log(title = "修改Ecn", businessType = BusinessType.UPDATE)
    @ApiOperation("修改")
    @PutMapping
    public AjaxResult edit(@RequestBody EcnDTO ecnDTO) {
        Ecn ecn = BeanConverUtil.conver(ecnDTO, Ecn.class);
        return toAjax(ecnService.updateById(ecn));
    }

    /**
     * 删除
     */
    @Log(title = "删除Ecn", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    @DeleteMapping("/{ids}")
    public AjaxResult delete(@PathVariable Long[] ids) {
        return toAjax(ecnService.deleteEcn(ids));
    }

    /**
     * 批量上传
     */
    @ApiOperation("批量上传")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    public R importExcel(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        //解析文件服务
        R result = fileService.masterDataImport(file, ClassType.ECNDTO.getDesc());
        if (result.isSuccess()) {
            Object data = result.getData();
            List<EcnDTO> ecnDTOList = JSON.parseArray(JSON.toJSONString(data), EcnDTO.class);
            if (CollectionUtils.isNotEmpty(ecnDTOList)) {
                boolean valid = ecnService.validEcnList(ecnDTOList);
                if (valid) {
                    return R.fail(400, "存在重复数据");
                } else {
                    //校验
                    ecnService.validData(ecnDTOList);
                    List<Ecn> ecns = BeanConverUtil.converList(ecnDTOList, Ecn.class);
                    ecnService.saveBatch(ecns);
                }
            } else {
                return R.fail("excel中无数据");
            }
            return R.ok(ecnDTOList);
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
                List<EcnDTO> ecnDTOList = JSON.parseArray(JSON.toJSONString(data), EcnDTO.class);
                if (CollectionUtils.isNotEmpty(ecnDTOList)) {
                    //校验
                    ecnService.validData(ecnDTOList);
                    //转换DO
                    List<Ecn> nmds = BeanConverUtil.converList(ecnDTOList, Ecn.class);
                    nmds.forEach(r -> {
                        LambdaUpdateWrapper<Ecn> wrapper = new LambdaUpdateWrapper<Ecn>();
                        wrapper.eq(Ecn::getMaterialCode, r.getMaterialCode());
                        wrapper.eq(Ecn::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
                        boolean update = ecnService.update(r, wrapper);
                        if (!update) {
//                            r.setCreateBy(SecurityUtils.getUsername());
//                            r.setCreateTime(DateUtils.getNowDate());
                            ecnService.save(r);
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
