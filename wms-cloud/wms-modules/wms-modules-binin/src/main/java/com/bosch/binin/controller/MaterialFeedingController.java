package com.bosch.binin.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.dto.MaterialCallDTO;
import com.bosch.binin.api.domain.dto.MaterialCallQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.MaterialCallVO;

import com.bosch.binin.service.IMaterialCallService;
import com.bosch.file.api.FileFeignService;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.api.domain.Department;
import com.bosch.masterdata.api.domain.dto.BinDTO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bosch.masterdata.api.enumeration.ClassType.MATERIALCALL;

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
    @Transactional(rollbackFor = Exception.class)
    public R<List<MaterialCallDTO>> call(@RequestParam(value = "file") MultipartFile file,
                                @RequestParam("sortType") Integer sortType,
                                @RequestParam("cell") String cell) {
//        return R.fail(fileFeignService.reduct());
        try {
            //解析文件服务
            R result = fileService.materialCallImport(file, ClassType.MATERIALCALL.getDesc());
            if (result.isSuccess()) {
                Object data = result.getData();
                List<MaterialCallDTO> dtos = JSON.parseArray(JSON.toJSONString(data), MaterialCallDTO.class);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    boolean valid = materialCallService.validList(dtos);
                    if (valid) {
                        return R.fail(400, "已录入相同订单号的记录");
                    } else {
                        dtos.forEach(r->{
                            r.setSortType(sortType);
                            r.setCell(cell);
                        });
                        //添加
                        List<MaterialCall> dos = BeanConverUtil.converList(dtos, MaterialCall.class);
                        materialCallService.saveBatch(dos);
                    }
                }
                return R.ok(dtos);
            }else {
                return R.fail(result.getMsg());
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }
    }




    @GetMapping(value = "/call/list")
    @ApiOperation("叫料需求列表查询")
    public R<PageVO<MaterialCallVO>> list(MaterialCallQueryDTO queryDTO) {
        startPage();
        List<MaterialCallVO> list = materialCallService.getMaterialCallList(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));

    }
    /**
     * 导出叫料需求列表
     */
    @Log(title = "叫料需求", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MaterialCallQueryDTO queryDTO)
    {
        startPage();
        List<MaterialCallVO> list = materialCallService.getMaterialCallList(queryDTO);
        ExcelUtil<MaterialCallVO> util = new ExcelUtil<MaterialCallVO>(MaterialCallVO.class);
        util.exportExcel(response, list, "叫料需求");
    }
}
