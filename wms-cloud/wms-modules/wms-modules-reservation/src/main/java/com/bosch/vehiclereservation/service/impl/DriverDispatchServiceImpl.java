package com.bosch.vehiclereservation.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.domain.Ware;
import com.bosch.masterdata.api.domain.vo.SupplierInfoVO;
import com.bosch.vehiclereservation.api.domain.DriverDeliver;
import com.bosch.vehiclereservation.api.domain.DriverDispatch;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;
import com.bosch.vehiclereservation.api.domain.dto.DispatchSendWxDTO;
import com.bosch.vehiclereservation.api.domain.dto.DriverDispatchDTO;
import com.bosch.vehiclereservation.api.domain.dto.DriverSortDTO;
import com.bosch.vehiclereservation.api.domain.dto.WxMsgDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDispatchVO;
import com.bosch.vehiclereservation.api.enumeration.DispatchStatusEnum;
import com.bosch.vehiclereservation.api.enumeration.DispatchTypeEnum;
import com.bosch.vehiclereservation.api.enumeration.ReserveStatusEnum;
import com.bosch.vehiclereservation.mapper.DriverDeliverMapper;
import com.bosch.vehiclereservation.mapper.DriverDispatchMapper;
import com.bosch.vehiclereservation.mapper.SupplierReserveMapper;
import com.bosch.vehiclereservation.service.IDriverDispatchService;
import com.bosch.vehiclereservation.utils.BeanConverUtil;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import io.swagger.models.auth.In;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Component
public class DriverDispatchServiceImpl extends ServiceImpl<DriverDispatchMapper, DriverDispatch> implements IDriverDispatchService {

    @Value("${wx.miniappid}")
    private String APPID;

    @Value("${wx.secret}")
    private String SECERT;

    @Value("${wx.templateid}")
    private String TEMPLATEID;

    @Autowired
    private DriverDispatchMapper driverDispatchMapper;

    @Autowired
    private DriverDeliverMapper driverDeliverMapper;

    @Autowired
    private SupplierReserveMapper supplierReserveMapper;

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;

    @Override
    public List<DriverDispatchVO> selectTodaySignData(DriverDispatchDTO driverDispatchDTO) {
        List<DriverDispatchVO> driverDispatchVOS = driverDispatchMapper.selectTodaySignData(driverDispatchDTO.getWareId(), driverDispatchDTO.getStatusList(), driverDispatchDTO.isToday(), driverDispatchDTO.getDriverType());
        Map<Long, String> wareMap = new HashMap<>();
        Map<String, String> supplierMap = new HashMap<>();
        for (DriverDispatchVO dispatchVO : driverDispatchVOS) {
            if (dispatchVO.getWareId() != null) {
                if (!wareMap.keySet().contains(dispatchVO.getWareId())) {
                    Ware ware = remoteMasterDataService.getWareInfo(dispatchVO.getWareId().toString()).getData();
                    if (ware != null) {
                        wareMap.put(dispatchVO.getWareId(), ware.getCode());
                    }
                }
                dispatchVO.setWareCode(wareMap.get(dispatchVO.getWareId()));
            }
            if (StringUtils.isNotEmpty(dispatchVO.getSupplierCode())) {
                if (!supplierMap.keySet().contains(dispatchVO.getSupplierCode())) {
                    SupplierInfoVO supplierInfoVO = remoteMasterDataService.getSupplierInfoByCode(dispatchVO.getSupplierCode()).getData();
                    if (supplierInfoVO != null) {
                        supplierMap.put(dispatchVO.getSupplierCode(), supplierInfoVO.getName());
                    }
                }
                dispatchVO.setSupplierName(supplierMap.get(dispatchVO.getSupplierCode()));
            }
        }
        return driverDispatchVOS;
    }

    @Override
    public List<DriverDispatchVO> selectTodayNotSignData() {
        return driverDispatchMapper.selectTodayNotSignData();
    }

    @Override
    public boolean dispatchDock(DriverDispatchDTO driverDispatchDTO) {
        DriverDispatch driverDispatch = driverDispatchMapper.selectById(driverDispatchDTO.getDispatchId());
        if (driverDispatch == null) {
            throw new ServiceException("调度信息不存在！");
        }
        driverDispatch.setWareId(driverDispatchDTO.getWareId());
        driverDispatch.setDockCode(driverDispatchDTO.getDockCode());
        return driverDispatchMapper.updateById(driverDispatch) > 0;
    }

    @Override
    public boolean dispatchEnter(Long dispatchId) {
        DriverDispatch driverDispatch = driverDispatchMapper.selectById(dispatchId);
        if (driverDispatch == null) {
            throw new ServiceException("调度信息不存在！");
        }
        if (driverDispatch.getWareId() == null || StringUtils.isEmpty(driverDispatch.getDockCode())) {
            throw new ServiceException("请先分配仓库和道口！");
        }
        driverDispatch.setStatus(DispatchStatusEnum.ENTER.getCode());
        driverDispatch.setComeinDate(DateUtils.getNowDate());
        return driverDispatchMapper.updateById(driverDispatch) > 0;
    }

    @Override
    public boolean dispatchComplete(Long dispatchId) {
        DriverDispatch driverDispatch = driverDispatchMapper.selectById(dispatchId);
        if (driverDispatch == null) {
            throw new ServiceException("调度信息不存在！");
        }
        if (driverDispatch.getStatus() != DispatchStatusEnum.ENTER.getCode()) {
            throw new ServiceException("车辆状态异常！");
        }
        driverDispatch.setCompleteDate(DateUtils.getNowDate());
        driverDispatch.setStatus(DispatchStatusEnum.COMPLETE.getCode());
        int i = driverDispatchMapper.updateById(driverDispatch);
        if (i > 0 && driverDispatch.getDriverType() == DispatchTypeEnum.DELIVER.getCode()) {
            Long driverId = driverDispatch.getDriverId();
            DriverDeliver driverDeliver = driverDeliverMapper.selectById(driverId);
            if (StringUtils.isNotEmpty(driverDeliver.getReserveNo())) {
                QueryWrapper<SupplierReserve> wrapper = new QueryWrapper<>();
                wrapper.eq("reserve_no", driverDeliver.getReserveNo());
                Optional<SupplierReserve> first = supplierReserveMapper.selectList(wrapper).stream().findFirst();
                if (first.isPresent()) {
                    SupplierReserve supplierReserve = first.get();
                    supplierReserve.setStatus(ReserveStatusEnum.COMPLETE.getCode());
                    supplierReserveMapper.updateById(supplierReserve);
                }
            }
        }
        return i > 0;
    }

    @Override
    public boolean dispatchSort(DriverSortDTO driverDispatchDTO) {
        if (driverDispatchDTO.getDispatchId() == null) {
            return false;
        }
        DriverDispatch driverDispatch = driverDispatchMapper.selectById(driverDispatchDTO.getDispatchId());
        if (driverDispatch == null) {
            return false;
        }
        if (driverDispatch.getSortNo() == driverDispatchDTO.getNewSortNo()) {
            return false;
        }
        Integer sortNO = driverDispatch.getSortNo();
        driverDispatch.setSortNo(driverDispatchDTO.getNewSortNo());
        int i = driverDispatchMapper.updateById(driverDispatch);
        if (i > 0) {
            if (sortNO > driverDispatchDTO.getNewSortNo()) {
                i = driverDispatchMapper.updateSortNo(driverDispatchDTO.getDispatchId(), driverDispatchDTO.getNewSortNo(), sortNO, "add");
                return i > 0;
            }
            if (sortNO < driverDispatchDTO.getNewSortNo()) {
                i = driverDispatchMapper.updateSortNo(driverDispatchDTO.getDispatchId(), sortNO, driverDispatchDTO.getNewSortNo(), "sub");
                return i > 0;
            }
        }
        return i > 0;
    }

    @Override
    public String getWxToken() {
        String token = "";
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret=" + SECERT;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = null;
        byte[] res = null;
        try {
            response = httpclient.execute(httpget);
            res = IOUtils.toByteArray(response.getEntity().getContent());
            JSONObject jo = JSON.parseObject(new String(res, "utf-8"));
            token = jo.getString("access_token");
        } catch (Exception e) {
            throw new ServiceException("获取微信Token失败！");
        } finally {
            if (httpget != null) {
                httpget.abort();
            }
            httpclient.getConnectionManager().shutdown();
        }
        return token;
    }

    @Override
    public boolean sendMsgToWx(DispatchSendWxDTO dispatchSendWxDTO) {
        if (StringUtils.isEmpty(dispatchSendWxDTO.getWxToken())) {
            return false;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + dispatchSendWxDTO.getWxToken();
        HttpPost httpPost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        byte[] res = null;
        try {
            Map<String, Map<String, String>> data = new HashMap<>();
            Map<String, String> value1Map = new HashMap<>();
            value1Map.put("value", "入场提醒");
            data.put("thing1", value1Map);
            Map<String, String> value2Map = new HashMap<>();
            value2Map.put("value", "仓库:" + dispatchSendWxDTO.getWareCode() + ", 道口:" + dispatchSendWxDTO.getDockCode() + "。请及时入厂!");
            data.put("thing2", value2Map);

            httpPost.addHeader("content-type", "application/json; charset=UTF-8");
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("Accept-Encoding", "UTF-8");

            WxMsgDTO wxMsgDTO = new WxMsgDTO();
            wxMsgDTO.setTouser(dispatchSendWxDTO.getWechatId());
            wxMsgDTO.setTemplate_id(TEMPLATEID);
            wxMsgDTO.setPage("index");
            wxMsgDTO.setData(data);

            StringEntity stringEntity = new StringEntity(JSONObject.parseObject(JSON.toJSONString(wxMsgDTO)).toString(), "UTF-8");
            stringEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);

            HttpResponse response = httpclient.execute(httpPost);
            res = IOUtils.toByteArray(response.getEntity().getContent());
            JSONObject jo = JSON.parseObject(new String(res, "utf-8"));
            return jo.size() == 3;
        } catch (Exception e) {
            throw new ServiceException("推送微信消息失败！");
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
            httpclient.getConnectionManager().shutdown();
        }

    }

}
