package com.bosch.binin.controller;

import com.alibaba.fastjson2.JSON;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.dto.MaterialCalJobRequestDTO;
import com.bosch.binin.api.domain.dto.MaterialCallDTO;
import com.bosch.binin.api.domain.dto.MaterialCallQueryDTO;
import com.bosch.binin.api.domain.vo.MaterialCallCheckResultVO;
import com.bosch.binin.api.domain.vo.MaterialCallVO;

import com.bosch.binin.api.domain.vo.RequirementResultVO;
import com.bosch.binin.service.IMaterialCallService;
import com.bosch.file.api.FileFeignService;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-09 13:05
 **/

@RestController
@Api(tags = "叫料接口")
@RequestMapping("/material-feeding")
public class MaterialFeedingController extends BaseController {


    @Autowired
    private FileService fileService;

    @Autowired
    private IMaterialCallService materialCallService;

    @Autowired
    private FileFeignService fileFeignService;

    @PostMapping(value = "/call")
    @ApiOperation("叫料")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件", name = "file", dataType = "File"),
            @ApiImplicitParam(value = "排序类型,0基于有效期，1、基于先主库后外库", name = "sortType", dataType = "Integer"),
            @ApiImplicitParam(value = "cell", name = "cell", dataType = "String")
    })
    @Transactional(rollbackFor = Exception.class)
    public R call(@RequestParam(value = "file") MultipartFile file,
                  @RequestParam("sortType") Integer sortType,
                  @RequestParam("cell") String cell) {
        try {
            //解析文件服务
            R result = fileService.materialCallImport(file, ClassType.MATERIALCALL.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<MaterialCallDTO> dtos = JSON.parseArray(JSON.toJSONString(data), MaterialCallDTO.class);
                if (!CollectionUtils.isEmpty(dtos)) {
                    boolean valid = materialCallService.validList(dtos);
                    if (valid) {
                        return R.fail(400, "已录入相同订单号的记录");
                    } else {
                        dtos.forEach(r -> {
                            if (r.getQuantity() == null || r.getQuantity() < 0) {
                                throw new ServiceException("数量输入有误", 400);
                            }
                            r.setSortType(sortType);
                            r.setCell(cell);
                        });

//                        List<Student> studentList = new ArrayList<>();
//                        list.parallelStream().collect(Collectors.groupingBy(o -> (o.getUid() + o.getUname()), Collectors.toList())).forEach((id, transfer) -> {
//                            transfer.stream().reduce((a, b) -> new Student(a.getUid(), a.getUname(), a.getScore() + b.getScore())).ifPresent(studentList::add);
//                        });

                        List<MaterialCallDTO> dtoList = new ArrayList<>();
                        dtos.parallelStream().collect(Collectors.groupingBy(o -> (o.getOrderNb() + o.getMaterialNb()), Collectors.toList())).forEach((num, dto) -> {
                            dto.stream().reduce((a, b) -> {
                                a.setQuantity(a.getQuantity() + b.getQuantity());

                                return a;
                            }).ifPresent(dtoList::add);
                        });
                        //添加
                        List<MaterialCall> dos = BeanConverUtil.converList(dtoList, MaterialCall.class);

                        return R.ok(materialCallService.saveBatch(dos));
                    }
                }
                return R.ok(null);
            } else {
                return R.fail(result.getMsg());
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }
    }

    @PostMapping(value = "/call/checkStock")
    @ApiOperation("系统生成叫料任务需要先进行check库存校验")
    public R<MaterialCallCheckResultVO> systemGenerateJobCheck(@RequestBody MaterialCalJobRequestDTO.SystemGenerateJob systemGenerateJob) {

        return R.ok(materialCallService.systemGenerateJobCheck(systemGenerateJob));
    }

    @PostMapping(value = "/call/system")
    @ApiOperation("系统生成叫料任务接口")
    public R<RequirementResultVO> systemGenerateJob(@RequestBody MaterialCalJobRequestDTO.SystemGenerateJob systemGenerateJob) {
        RequirementResultVO requirementResultVO = materialCallService.systemGenerateJob(systemGenerateJob);
        return R.ok(requirementResultVO);
    }


    @PutMapping(value = "/call/{id}")
    @ApiOperation("更新需求量")
    public R updateCallQuantity(@PathVariable Long id, @RequestBody MaterialCallDTO callDTO) {
        if (Objects.isNull(id)) {
            throw new ServiceException("id不能为空");
        }
        return R.ok(materialCallService.updateCallQuantity(callDTO));
    }

    @ApiOperation("删除区域")
    @DeleteMapping("/call/{ids}")
    public R cancelRequirement(@PathVariable Long[] ids) {
        if (ids == null || ids.length == 0) {
            throw new ServiceException("ids不能为空");
        }
        materialCallService.deleteRequirement(Arrays.asList(ids));
        return R.ok("删除成功");

    }

    @GetMapping(value = "/call/list")
    @ApiOperation("叫料需求列表查询")
    public R<PageVO<MaterialCallVO>> list(MaterialCallQueryDTO queryDTO) {
        startPage();
        List<MaterialCall> list = materialCallService.getMaterialCallList(queryDTO);
        PageInfo pageInfo = new PageInfo<>(list);

        List<MaterialCallVO> materialCallVOS = BeanConverUtil.converList(list, MaterialCallVO.class);
        pageInfo.setList(materialCallVOS);
        return R.ok(new PageVO<>(materialCallVOS, pageInfo.getTotal()));

    }

    /**
     * 导出叫料需求列表
     */
    @Log(title = "叫料需求", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MaterialCallQueryDTO queryDTO) {
        startPage();
        List<MaterialCall> list = materialCallService.getMaterialCallList(queryDTO);
        List<MaterialCallVO> materialCallVOS = BeanConverUtil.converList(list, MaterialCallVO.class);

        ExcelUtil<MaterialCallVO> util = new ExcelUtil<MaterialCallVO>(MaterialCallVO.class);
        util.exportExcel(response, materialCallVOS, "叫料需求");
    }


}
