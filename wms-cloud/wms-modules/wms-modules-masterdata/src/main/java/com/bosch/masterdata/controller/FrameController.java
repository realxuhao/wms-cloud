package com.bosch.masterdata.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.dto.FrameDTO;
import com.bosch.masterdata.api.domain.vo.FrameVO;
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
import com.bosch.masterdata.api.domain.Frame;
import com.bosch.masterdata.service.IFrameService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;

/**
 * 跨Controller
 *
 * @author xuhao
 * @date 2022-09-26
 */
@RestController
@Api(tags = "跨接口")
@RequestMapping("/frame")
public class FrameController extends BaseController {
    @Autowired
    private IFrameService frameService;


    @Autowired
    private FileService fileService;
    /**
     * 查询跨列表
     */
    //@RequiresPermissions("masterdata:frame:list")
    @GetMapping("/list")
    @ApiOperation("查询跨列表")
    public R<PageVO<FrameVO>> list(FrameDTO frameDTO) {
        startPage();
        List<FrameVO> list = frameService.selectFrameList(frameDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 导出跨列表
     */
    //@RequiresPermissions("masterdata:frame:export")
    @Log(title = "跨", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FrameDTO frameDTO) {
        List<FrameVO> list = frameService.selectFrameList(frameDTO);
        ExcelUtil<Frame> util = new ExcelUtil<Frame>(Frame.class);
        util.exportExcel(response, BeanConverUtil.converList(list, Frame.class), "跨数据");
    }

    /**
     * 获取跨详细信息
     */
    //@RequiresPermissions("masterdata:frame:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("根据id查询跨详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(frameService.selectFrameById(id));
    }

    @GetMapping(value = "getFrameInfo/{id}")
    @ApiOperation("根据id查询跨详细信息")
    public R<FrameVO> getFrameInfo(@PathVariable("id") Long id) {
        try {
            FrameVO frameVO = frameService.selectFrameById(id);
            return R.ok(frameVO);
        }catch (Exception e) {
            return R.fail(e.getMessage());
        }

    }
    @GetMapping(value = "getFrameInfoByCode/{code}")
    @ApiOperation("根据code查询跨详细信息")
    public R<FrameVO> getFrameInfoByCode(@PathVariable("code") String code) {
        try {
            FrameVO frameVO = frameService.selectFrameByCode(code);
            return R.ok(frameVO);
        }catch (Exception e) {
            return R.fail(e.getMessage());
        }

    }

    @GetMapping(value = "/getFrameType")
    @ApiOperation("查询跨类型")
    public R<List<String>> getFrameType() {
        try {
            List<Frame> frames= frameService.list();
            List<String> types=new ArrayList<>();
            if (CollectionUtils.isNotEmpty(frames)){
                List<String> collect = frames.stream().map(Frame::getTypeCode).distinct().collect(Collectors.toList());
                types.addAll(collect);
            }

            return R.ok(types);
        }catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @GetMapping(value = "getFrameInfoByType/{type}")
    @ApiOperation("根据type查询跨详细信息")
    public R<List<FrameVO>> getFrameInfoByType(@PathVariable("type") String type, @RequestParam("wareCode") String wareCode) {
        try {
            FrameDTO frameDTO=new FrameDTO();
            frameDTO.setTypeCode(type);
            frameDTO.setWareCode(wareCode);
            List<FrameVO> frameVOS = frameService.selectFrameList(frameDTO);
            return R.ok(frameVOS);
        }catch (Exception e) {
            return R.fail(e.getMessage());
        }

    }
    /**
     * 新增跨
     */
    //@RequiresPermissions("masterdata:frame:add")
    @Log(title = "跨", businessType = BusinessType.INSERT)
    @ApiOperation("新增跨")
    @PostMapping
    public AjaxResult add(@RequestBody FrameDTO frameDTO) {
        return toAjax(frameService.insertFrame(frameDTO));
    }

    /**
     * 修改跨
     */
    //@RequiresPermissions("masterdata:frame:edit")
    @Log(title = "跨", businessType = BusinessType.UPDATE)
    @ApiOperation("更新跨")
    @PutMapping
    public AjaxResult edit(@RequestBody FrameDTO frameDTO) {
        return toAjax(frameService.updateFrame(frameDTO));
    }

    /**
     * 删除跨
     */
    //@RequiresPermissions("masterdata:frame:remove")
    @Log(title = "跨", businessType = BusinessType.DELETE)
    @ApiOperation("删除跨")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(frameService.deleteFrameByIds(ids));
    }

    /**
     * 批量上传跨
     */
    @ApiOperation("批量上传跨")
    @PostMapping(value = "/import" , headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R importExcel(@RequestPart(value = "file" , required = true) MultipartFile file) throws IOException {
        try {
            //解析文件服务
            R result = fileService.masterDataImport(file, ClassType.FRAMEDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<FrameDTO> dtos = JSON.parseArray(JSON.toJSONString(data), FrameDTO.class);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    List<String> collect = dtos.stream().map(FrameDTO::getCode).collect(Collectors.toList());

                    boolean valid = frameService.validList(collect);
                    if (valid) {
                        return R.fail(400, "存在重复数据");
                    } else {
                        //dto赋值
                        List<FrameDTO> frameDTOS = frameService.setValue(dtos);
                        //添加
                        List<Frame> dos = BeanConverUtil.converList(frameDTOS, Frame.class);
                        frameService.saveBatch(dos);
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
     * 批量更新跨
     */
    @ApiOperation("批量更新跨")
    @PostMapping(value = "/saveBatch" , headers = "content-type=multipart/form-data")
    @Transactional(rollbackFor = Exception.class)
    public R saveBatch(@RequestPart(value = "file" , required = true) MultipartFile file) throws IOException {

        try {
            //解析文件服务
            R result = fileService.masterDataImport(file, ClassType.FRAMEDTO.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<FrameDTO> dtos = JSON.parseArray(JSON.toJSONString(data), FrameDTO.class);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    //dto赋值
                    List<FrameDTO> areaDTOS = frameService.setValue(dtos);
                    //转换DO
                    List<Frame> dos = BeanConverUtil.converList(areaDTOS, Frame.class);
                    List<Frame> doUpate=new ArrayList<>();
                    List<Frame> doNew=new ArrayList<>();
                    List<Frame> listAll = frameService.list();

                    for (Frame frame : dos) {
                        boolean flag = false;
                        Long id=null;
                        String code = frame.getCode();

                        for(Frame frame2 :listAll){
                            if (frame2.getCode().equals(code)){
                                flag=true;
                                id= frame2.getId();
                                break;

                            }else {

                            }
                        }
                        if (flag){
                            frame.setId(id);
                              doUpate.add(frame);
                        }else {
                            doNew.add(frame);
                        }
                    }
                    boolean b1 = frameService.saveBatch(doNew);
                    boolean b = frameService.updateBatchById(doUpate);


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
