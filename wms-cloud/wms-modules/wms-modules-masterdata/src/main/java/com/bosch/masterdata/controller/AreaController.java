package com.bosch.masterdata.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.Area;
import com.bosch.masterdata.api.domain.dto.AreaDTO;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.bosch.masterdata.service.IAreaService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;

/**
 * 区域Controller
 * 
 * @author xuhao
 * @date 2022-09-26
 */
@Api(tags = "存储区接口")
@RestController
@RequestMapping("/area")
public class AreaController extends BaseController
{
    @Autowired
    private IAreaService areaService;

    @Autowired
    private FileService fileService;

    /**
     * 导出区域列表
     */
    @RequiresPermissions("masterdata:area:export")
    @Log(title = "区域", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Area area)
    {
        List<Area> list = areaService.selectAreaList(area);
        ExcelUtil<Area> util = new ExcelUtil<Area>(Area.class);
        util.exportExcel(response, list, "区域数据");
    }

    /**
     * 获取区域详细信息
     */
    //@RequiresPermissions("masterdata:area:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(areaService.selectAreaById(id));
    }

    /**
     * 删除区域
     */
    @RequiresPermissions("masterdata:area:remove")
    @Log(title = "区域", businessType = BusinessType.DELETE)
    @ApiOperation("删除区域")
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(areaService.deleteAreaByIds(ids));
    }

    /**
     * 查询区域信息
     */
    @ApiOperation("查询区域信息")
    @GetMapping("/areaVOList")
    public R<PageVO<AreaVO>> list(AreaDTO areaDTO)
    {
        startPage();
        List<AreaVO> list = areaService.selectAreaVOList(areaDTO);

        return R.ok(new PageVO<>(list,new PageInfo<>(list).getTotal()));
    }

    /**
     * 新增区域
     */
    //@RequiresPermissions("masterdata:area:add")
    @ApiOperation("新增区域")
    @Log(title = "区域", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AreaDTO areaDTO)
    {
        return toAjax(areaService.insertArea(areaDTO));
    }

    /**
     * 修改区域
     */
    //@RequiresPermissions("masterdata:area:edit")
    @ApiOperation("修改区域")
    @Log(title = "区域", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable("id") Integer id,@RequestBody AreaDTO areaDTO)
    {
        areaDTO.setId(id);
        return toAjax(areaService.updateArea(areaDTO));
    }
    /**
     * 批量上传存储区
     */
    @ApiOperation("批量上传存储区")
    @PostMapping(value = "/import" , headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R importExcel(@RequestPart(value = "file" , required = true) MultipartFile file) throws IOException {
        try {
            //解析文件服务
            R result = fileService.masterDataImport(file, ClassType.AREADTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<AreaDTO> dtos = JSON.parseArray(JSON.toJSONString(data), AreaDTO.class);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    List<String> collect = dtos.stream().map(AreaDTO::getCode).collect(Collectors.toList());

                    boolean valid = areaService.validList(collect);
                    if (valid) {
                        return R.fail(400, "存在重复数据");
                    } else {
                        //dto赋值
                        List<AreaDTO> areaDTOS = areaService.setValue(dtos);
                        //添加
                        List<Area> dos = BeanConverUtil.converList(areaDTOS, Area.class);
                        areaService.saveBatch(dos);
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
     * 批量更新存储区
     */
    @ApiOperation("批量更新存储区")
    @PostMapping(value = "/saveBatch" , headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R saveBatch(@RequestPart(value = "file" , required = true) MultipartFile file) throws IOException {

        try {
            //解析文件服务
            R result = fileService.masterDataImport(file, ClassType.AREADTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<AreaDTO> dtos = JSON.parseArray(JSON.toJSONString(data), AreaDTO.class);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    //dto赋值
                    List<AreaDTO> areaDTOS = areaService.setValue(dtos);
                    //转换DO
                    List<Area> dos = BeanConverUtil.converList(areaDTOS, Area.class);
                    dos.forEach(r->{
                        LambdaUpdateWrapper<Area> wrapper=new LambdaUpdateWrapper<Area>();
                        wrapper.eq(Area::getCode,r.getCode());
                        boolean update = areaService.update(r, wrapper);
                        if (!update){
                            r.setCreateBy(SecurityUtils.getUsername());
                            r.setCreateTime(DateUtils.getNowDate());
                            areaService.save(r);
                        }
                    });
                }
                return R.ok("导入成功");
            }else {
                return R.fail(result.getMsg());
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }

    }
}
