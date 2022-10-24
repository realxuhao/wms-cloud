package com.bosch.masterdata.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.dto.BinDTO;
import com.bosch.masterdata.api.domain.vo.BinVO;
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
import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.service.IBinService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 库位Controller
 * 
 * @author xuhao
 * @date 2022-09-26
 */
@Api(tags = "库位接口")
@RestController
@RequestMapping("/bin")
public class BinController extends BaseController
{
    @Autowired
    private IBinService binService;

    @Autowired
    private FileService fileService;

    /**
     * 查询库位列表
     */
    @RequiresPermissions("masterdata:bin:list")
    @GetMapping("/list")
    public TableDataInfo list(Bin bin)
    {
        startPage();
        List<Bin> list = binService.selectBinList(bin);
        return getDataTable(list);
    }

    /**
     * 导出库位列表
     */
    @RequiresPermissions("masterdata:bin:export")
    @Log(title = "库位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Bin bin)
    {
        List<Bin> list = binService.selectBinList(bin);
        ExcelUtil<Bin> util = new ExcelUtil<Bin>(Bin.class);
        util.exportExcel(response, list, "库位数据");
    }

    /**
     * 获取库位详细信息
     */
    @RequiresPermissions("masterdata:bin:query")
    @GetMapping(value = "/{id}")
    public R<BinVO> getInfo(@PathVariable("id") Long id)
    {
        try {
            BinVO binVO = binService.selectBinVOById(id);
            return R.ok(binVO);

        }catch (Exception e){
            return R.fail(e.getMessage());
        }

    }


    /**
     * 获取库位详细信息
     */
    @GetMapping(value = "/getInfoByCode/{code}")
    public R<BinVO> getInfoByCode(@PathVariable("code") String code)
    {
        try {
            BinVO binVO = binService.selectBinVOByCode(code);
            return R.ok(binVO);

        }catch (Exception e){
            return R.fail(e.getMessage());
        }

    }
    /**
     * 根据跨id获取库位详细信息
     */
    @GetMapping(value = "/getInfoByFrameId/{frameId}")
    public R<List<Bin> > getInfoByFrameId(@PathVariable("frameId") Long frameId)
    {
        try {

            List<Bin> bins = binService.selectBinByFrameId(frameId);

            return R.ok(bins);

        }catch (Exception e){
            return R.fail(e.getMessage());
        }

    }
    /**
     * 删除库位
     */
    @RequiresPermissions("masterdata:bin:remove")
    @Log(title = "库位", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(binService.deleteBinByIds(ids));
    }

    /**
     * 查询库位列表
     */
    @ApiOperation("查询库位列表")
    //@RequiresPermissions("masterdata:bin:list")
    @GetMapping("/binVOlist")
    public R<PageVO<BinVO>> list(BinDTO binDTO)
    {
        startPage();
        List<BinVO> list = binService.selectBinList(binDTO);

        return R.ok(new PageVO<>(list,new PageInfo<>(list).getTotal()));
    }

    /**
     * 新增库位
     */
    @ApiOperation("新增库位")
    //@RequiresPermissions("masterdata:bin:add")
    @Log(title = "库位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BinDTO binDTO)
    {
        return toAjax(binService.insertBin(binDTO));
    }

    /**
     * 修改库位
     */
    @ApiOperation("修改库位")
    @Log(title = "库位", businessType = BusinessType.UPDATE)
    @PutMapping("{/id}")
    public AjaxResult edit(@PathVariable("id") Long id,@RequestBody BinDTO binDTO)
    {
        binDTO.setId(id);
        return toAjax(binService.updateBin(binDTO));
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
            R result = fileService.masterDataImport(file, ClassType.BINDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<BinDTO> dtos = JSON.parseArray(JSON.toJSONString(data), BinDTO.class);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    List<String> collect = dtos.stream().map(BinDTO::getCode).collect(Collectors.toList());

                    boolean valid = binService.validList(collect);
                    if (valid) {
                        return R.fail(400, "存在重复数据");
                    } else {
                        //dto赋值
                        List<BinDTO> binDTOS = binService.setValue(dtos);
                        //添加
                        List<Bin> dos = BeanConverUtil.converList(binDTOS, Bin.class);
                        binService.saveBatch(dos);
                    }
                }
                return R.ok("解析成功");
            }else {
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
            R result = fileService.masterDataImport(file, ClassType.BINDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<BinDTO> dtos = JSON.parseArray(JSON.toJSONString(data), BinDTO.class);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    //dto赋值
                    List<BinDTO> binDTOS = binService.setValue(dtos);
                    //转换DO
                    List<Bin> dos = BeanConverUtil.converList(binDTOS, Bin.class);
                    dos.forEach(r->{
                        LambdaUpdateWrapper<Bin> wrapper=new LambdaUpdateWrapper<Bin>();
                        wrapper.eq(Bin::getCode,r.getCode());
                        boolean update = binService.update(r, wrapper);
                        if (!update){
                            r.setCreateBy(SecurityUtils.getUsername());
                            r.setCreateTime(DateUtils.getNowDate());
                            binService.save(r);
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
