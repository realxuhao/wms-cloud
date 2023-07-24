package com.bosch.masterdata.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.Ecn;
import com.bosch.masterdata.api.domain.ProductPackaging;
import com.bosch.masterdata.api.domain.dto.EcnDTO;
import com.bosch.masterdata.api.domain.dto.MdProductPackagingDTO;
import com.bosch.masterdata.api.domain.vo.MdProductPackagingVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.masterdata.service.IMdProductPackagingService;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mdProductPackaging")
public class MdProductPackagingController extends BaseController {

    @Autowired
    private IMdProductPackagingService mdProductPackagingService;

    @Autowired
    private FileService fileService;

    /**
     * 查询列表
     */
    @ApiOperation("查询列表")
    @PostMapping("/list")
    public R<PageVO<MdProductPackagingVO>> list(@RequestBody MdProductPackagingDTO dTO) {
        List<MdProductPackagingVO> list =new ArrayList<>();
        if(dTO!=null&&dTO.getPageSize().equals(0)){
            list = mdProductPackagingService.selectList(dTO);
        }else {
            startPage();
             list = mdProductPackagingService.selectList(dTO);
        }

        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }



    @GetMapping("/getByCell/{cell}")
    public R<List<MdProductPackagingVO>> getByCell(@PathVariable("cell")String cell){
        MdProductPackagingDTO dto = new MdProductPackagingDTO();
        dto.setCell(cell);
        return R.ok(mdProductPackagingService.selectList(dto));

    }

    @GetMapping("/getByCode/{code}")
    public R<MdProductPackagingVO> getByCode(@PathVariable("code")String code){
        MdProductPackagingDTO dto = new MdProductPackagingDTO();
        dto.setProductNo(code);
        List<MdProductPackagingVO> productPackagingVOS = mdProductPackagingService.selectList(dto);
        if (CollectionUtils.isEmpty(productPackagingVOS)){
            throw new ServiceException("没有"+code+"对应的成品主数据");
        }

        return R.ok(productPackagingVOS.get(0));

    }

    @GetMapping("/getNotExistCodeList")
    public R<List<String>> getNotExistCodeList(@RequestParam("codeList")List<String> codeList){
        return R.ok(mdProductPackagingService.getNotExistCodeList(codeList));
    }


    /**
     * 获取详细信息
     */
    @ApiOperation("获取详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(mdProductPackagingService.selectMdProductPackagingById(id));
    }
    /**
     * 新增
     */
    @Log(title = "新增", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增")
    public R add(@RequestBody MdProductPackagingDTO dto) {
        List<MdProductPackagingDTO> list = new ArrayList<>();
        list.add(dto);
        boolean b=false;
        boolean valid = mdProductPackagingService.validMdProductPackagingList(list);
        if (valid) {
            return R.fail(400, "存在重复成品料号和cell的数据");
        } else {
            List<ProductPackaging> dos = BeanConverUtil.converList(list, ProductPackaging.class);
            b = mdProductPackagingService.saveBatch(dos);
        }
        return b?R.ok(dto): R.fail("保存失败");
    }

    /**
     * 修改
     */
    @Log(title = "修改", businessType = BusinessType.UPDATE)
    @ApiOperation("修改")
    @PutMapping
    public AjaxResult edit(@RequestBody MdProductPackagingDTO dto) {

        return toAjax(mdProductPackagingService.updateMdProductPackaging(dto));
    }

    /**
     * 删除
     */
    @Log(title = "删除成品", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    @DeleteMapping("/{ids}")
    public AjaxResult delete(@PathVariable Long[] ids) {
        return toAjax(mdProductPackagingService.deleteMdProductPackaging(ids));
    }

    /**
     * 批量上传
     */
    @ApiOperation("批量上传")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    public R importExcel(@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        //解析文件服务
        R result = fileService.masterDataImport(file, ClassType.MdProductPackagingDTO.getDesc());
        if (result.isSuccess()) {
            Object data = result.getData();
            List<MdProductPackagingDTO> list = JSON.parseArray(JSON.toJSONString(data), MdProductPackagingDTO.class);
            if (CollectionUtils.isNotEmpty(list)) {
                boolean valid = mdProductPackagingService.validMdProductPackagingList(list);
                if (valid) {
                    return R.fail(400, "存在重复成品料号和cell的数据");
                } else {

                    List<ProductPackaging> dos = BeanConverUtil.converList(list, ProductPackaging.class);
                    mdProductPackagingService.saveBatch(dos);
                }
            } else {
                return R.fail("excel中无数据");
            }
            return R.ok(list);
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
            R result = fileService.masterDataImport(file, ClassType.MdProductPackagingDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<MdProductPackagingDTO> list = JSON.parseArray(JSON.toJSONString(data), MdProductPackagingDTO.class);
                if (CollectionUtils.isNotEmpty(list)) {
                    //校验
                    boolean valid = mdProductPackagingService.validMdProductPackagingList(list);
                    //转换DO
                    List<ProductPackaging> dos = BeanConverUtil.converList(list, ProductPackaging.class);
                    dos.forEach(r -> {
                        LambdaUpdateWrapper<ProductPackaging> wrapper = new LambdaUpdateWrapper<ProductPackaging>();
                        wrapper.eq(ProductPackaging::getProductNo, r.getProductNo());
                        wrapper.eq(ProductPackaging::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
                        boolean update = mdProductPackagingService.update(r, wrapper);
                        if (!update) {
//                            r.setCreateBy(SecurityUtils.getUsername());
//                            r.setCreateTime(DateUtils.getNowDate());
                            mdProductPackagingService.save(r);
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
