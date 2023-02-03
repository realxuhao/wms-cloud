package com.bosch.system.controller;

import com.bosch.system.api.domain.SysMoveLog;
import com.bosch.system.api.domain.SysOperLog;
import com.bosch.system.service.ISysMoveLogService;
import com.bosch.system.service.ISysOperLogService;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.InnerAuth;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 操作日志记录
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/sysMoveLog")
public class SysMoveLogController extends BaseController
{
    @Autowired
    private ISysMoveLogService moveLogService;



    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysMoveLog sysMoveLog)
    {
        return toAjax(moveLogService.insertLog(sysMoveLog));
    }
}
