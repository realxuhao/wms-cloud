package com.bosch.system.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bosch.binin.api.RemoteBinInService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.SSCCLogVO;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.InnerAuth;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.bosch.system.api.domain.SysOperLog;
import com.bosch.system.service.ISysOperLogService;

/**
 * 操作日志记录
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/operlog")
public class SysOperlogController extends BaseController {
    @Autowired
    private ISysOperLogService operLogService;


    //@RequiresPermissions("system:operlog:list")
    @GetMapping("/list")
    public TableDataInfo list(SysOperLog operLog) {
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        list.stream().forEach(log -> {
            String jsonResult = log.getJsonResult();
            if (StringUtils.isNotEmpty(jsonResult)) {
                try {
                    R r = JSON.parseObject(jsonResult, R.class);
                    if (r != null) {
                        Object data = r.getData();
                        if (Objects.nonNull(data)) {

                            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(data));

                            log.setSsccNumber((String) jsonObject.get("ssccNumber"));
                            log.setQuantity((BigDecimal) jsonObject.get("quantity"));
                            log.setNewSSCCNumber((String) jsonObject.get("newSSCCNumber"));

                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        });
        return getDataTable(list);
    }

    @GetMapping("/ssccOperLogList")
    public TableDataInfo ssccOperLogList(SysOperLog operLog) {
        startPage();
        List<SysOperLog> list = operLogService.selectSsccOperLogList(operLog);
        return getDataTable(list);
    }

    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    //@RequiresPermissions("system:operlog:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOperLog operLog) {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        util.exportExcel(response, list, "操作日志");
    }

    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    //@RequiresPermissions("system:operlog:remove")
    @DeleteMapping("/{operIds}")
    public AjaxResult remove(@PathVariable Long[] operIds) {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    //@RequiresPermissions("system:operlog:remove")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        operLogService.cleanOperLog();
        return AjaxResult.success();
    }

    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysOperLog operLog) {
        return toAjax(operLogService.insertOperlog(operLog));
    }
}
