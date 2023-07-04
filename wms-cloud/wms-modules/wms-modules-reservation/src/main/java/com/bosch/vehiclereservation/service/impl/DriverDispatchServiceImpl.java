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
import com.bosch.vehiclereservation.api.domain.DriverPickup;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;
import com.bosch.vehiclereservation.api.domain.dto.DispatchSendWxDTO;
import com.bosch.vehiclereservation.api.domain.dto.DriverDispatchDTO;
import com.bosch.vehiclereservation.api.domain.dto.DriverSortDTO;
import com.bosch.vehiclereservation.api.domain.dto.WxMsgDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDispatchVO;
import com.bosch.vehiclereservation.api.enumeration.DispatchStatusEnum;
import com.bosch.vehiclereservation.api.enumeration.DispatchTypeEnum;
import com.bosch.vehiclereservation.api.enumeration.ReserveStatusEnum;
import com.bosch.vehiclereservation.api.enumeration.ReserveTypeEnum;
import com.bosch.vehiclereservation.mapper.DriverDeliverMapper;
import com.bosch.vehiclereservation.mapper.DriverDispatchMapper;
import com.bosch.vehiclereservation.mapper.DriverPickupMapper;
import com.bosch.vehiclereservation.mapper.SupplierReserveMapper;
import com.bosch.vehiclereservation.service.IDriverDispatchService;
import com.bosch.vehiclereservation.utils.BeanConverUtil;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javax.xml.transform.OutputKeys.MEDIA_TYPE;

@Service
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
    private DriverPickupMapper driverPickupMapper;

    @Autowired
    private SupplierReserveMapper supplierReserveMapper;

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;

    @Override
    public List<DriverDispatchVO> selectTodaySignData(DriverDispatchDTO driverDispatchDTO) {
        //DriverDispatch driverDispatch = BeanConverUtil.conver(driverDispatchDTO, DriverDispatch.class);
        List<DriverDispatchVO> driverDispatchVOS = driverDispatchMapper.selectTodaySignData(driverDispatchDTO);
        Map<Long, String> wareMap = new HashMap<>();
        Map<String, String> supplierMap = new HashMap<>();
        for (DriverDispatchVO dispatchVO : driverDispatchVOS) {
            if (dispatchVO.getWareId() != null) {
                if (!wareMap.keySet().contains(dispatchVO.getWareId())) {
                    Ware ware = remoteMasterDataService.getWareInfo(dispatchVO.getWareId().toString()).getData();
                    if (ware != null) {
                        wareMap.put(dispatchVO.getWareId(), ware.getCode());
                        wareMap.put(dispatchVO.getWareId() + 1, ware.getName());
                    }
                }
                dispatchVO.setWareCode(wareMap.get(dispatchVO.getWareId()));
                dispatchVO.setWareName(wareMap.get(dispatchVO.getWareId() + 1));
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
    public List<DriverDispatchVO> selectTodayNotSignData(DriverDispatchDTO driverDispatchDTO) {
        return driverDispatchMapper.selectTodayNotSignData(driverDispatchDTO);
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
        QueryWrapper<DriverDispatch> wrapper = new QueryWrapper<>();
        wrapper.eq("ware_id", driverDispatch.getWareId());
        wrapper.eq("dock_code", driverDispatch.getDockCode());
        wrapper.eq("status", DispatchStatusEnum.ENTER.getCode());
        wrapper.ne("dispatch_id", driverDispatch.getDispatchId());
        wrapper.between("comein_date", DateUtils.parseDate(DateUtils.getDate() + " 00:00:00"), DateUtils.parseDate(DateUtils.getDate() + " 23:59:59"));
        int i = driverDispatchMapper.selectList(wrapper).size();
        if (i > 0) {
            throw new ServiceException("道口已占用！");
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
    public boolean dispatchCancel(Long dispatchId) {
        DriverDispatch driverDispatch = driverDispatchMapper.selectById(dispatchId);
        if (driverDispatch == null) {
            throw new ServiceException("调度信息不存在！");
        }
        if (driverDispatch.getStatus() == DispatchStatusEnum.COMPLETE.getCode() || driverDispatch.getStatus() == DispatchStatusEnum.ERROR.getCode()) {
            throw new ServiceException("订单已完成，不允许取消！");
        }
        boolean res = super.removeById(dispatchId);
        if (driverDispatch.getDriverType() == DispatchTypeEnum.DELIVER.getCode()) {
            QueryWrapper<DriverDeliver> wrapper = new QueryWrapper<>();
            wrapper.eq("deliver_id", driverDispatch.getDriverId());
            Optional<DriverDeliver> driverDeliver = driverDeliverMapper.selectList(wrapper).stream().findFirst();
            if (driverDeliver.isPresent()) {
                int i = driverDeliverMapper.deleteById(driverDeliver.get().getDeliverId());
                if (StringUtils.isNotEmpty(driverDeliver.get().getReserveNo())) {
                    QueryWrapper<SupplierReserve> wrapper1 = new QueryWrapper<>();
                    wrapper1.eq("reserve_no", driverDeliver.get().getReserveNo());
                    Optional<SupplierReserve> supplierReserve = supplierReserveMapper.selectList(wrapper1).stream().findFirst();
                    if (supplierReserve.isPresent()) {
                        supplierReserve.get().setStatus(ReserveStatusEnum.RESERVED.getCode());
                        supplierReserveMapper.updateById(supplierReserve.get());
                    }
                }
            }
        }
        if (driverDispatch.getDriverType() == DispatchTypeEnum.PICKUP.getCode()) {
            QueryWrapper<DriverPickup> wrapper = new QueryWrapper<>();
            wrapper.eq("pickup_id", driverDispatch.getDriverId());
            Optional<DriverPickup> driverPickup = driverPickupMapper.selectList(wrapper).stream().findFirst();
            if (driverPickup.isPresent()) {
                int i = driverPickupMapper.deleteById(driverPickup.get().getPickupId());
            }
        }
        return res;
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
    public boolean dispatchChange(Long dispatchId) {
        DriverDispatch driverDispatch = driverDispatchMapper.selectById(dispatchId);
        if (driverDispatch == null) {
            throw new ServiceException("调度信息不存在！");
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
    public String getWxToken() {
        String token = "";
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret=" + SECERT;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = okHttpClient.newCall(request);
        try {
            Response resp = call.execute();
            String body = resp.body().string();
            JSONObject jsonBody = JSON.parseObject(body);
            token = jsonBody.getString("access_token");
        } catch (IOException e) {
            throw new ServiceException("获取微信AccessToken失败！");
        }
        return token;
    }

    @Override
    public boolean sendMsgToWx(DispatchSendWxDTO dispatchSendWxDTO) {
        if (StringUtils.isEmpty(dispatchSendWxDTO.getWxToken())) {
            return false;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + dispatchSendWxDTO.getWxToken();
        Map<String, Map<String, String>> data = new HashMap<>();
        Map<String, String> value1Map = new HashMap<>();
        value1Map.put("value", "入场提醒");
        data.put("thing1", value1Map);
        Map<String, String> value2Map = new HashMap<>();
        value2Map.put("value", (dispatchSendWxDTO.getWareName().split("-").length > 0 ? dispatchSendWxDTO.getWareName().split("-")[0] : "") + ".请入厂!");
        data.put("thing2", value2Map);
        WxMsgDTO wxMsgDTO = new WxMsgDTO();
        wxMsgDTO.setTouser(dispatchSendWxDTO.getWechatId());
        wxMsgDTO.setTemplate_id(TEMPLATEID);
        wxMsgDTO.setPage("index");
        wxMsgDTO.setData(data);
        try {
            String body = JSON.toJSONString(wxMsgDTO);
            RequestBody reqBody = RequestBody.create(MediaType.parse(MEDIA_TYPE), body);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).post(reqBody).build();
            Call call = okHttpClient.newCall(request);
            Response resp = call.execute();
            String respBody = resp.body().string();
            JSONObject jsonBody = JSON.parseObject(respBody);
            Integer code = jsonBody.getInteger("errcode");
            return code == 0;
        } catch (IOException e) {
            throw new ServiceException("推送微信消息失败！");
        }
    }

    @Override
    public void syncErrorData() {
        // 查询所有创建时间超过当天24点的未完成的vr_driver_dispatch
        QueryWrapper<DriverDispatch> wrapper = new QueryWrapper<>();
        wrapper.and(q -> q.eq("status", DispatchStatusEnum.WAITE.getCode()).or().eq("status", DispatchStatusEnum.ENTER.getCode()))
                .le("create_time", DateUtils.parseDate(DateUtils.getDate() + " 00:00:00"));
        List<DriverDispatch> driverDispatches = driverDispatchMapper.selectList(wrapper);
        driverDispatches.forEach(c -> {
            c.setStatus(DispatchStatusEnum.ERROR.getCode());
        });
        if (driverDispatches.size() == 0) {
            return;
        }
        super.saveOrUpdateBatch(driverDispatches);
        // 司机预约单id
        List<Long> driverIdList = driverDispatches.stream().filter(c -> c.getDriverType().equals(DispatchTypeEnum.DELIVER.getCode()))
                .map(c -> c.getDriverId()).collect(Collectors.toList());

        if (driverIdList.size() == 0) {
            return;
        }
        QueryWrapper<DriverDeliver> deliverQueryWrapper = new QueryWrapper<>();
        deliverQueryWrapper.in("deliver_id", driverIdList);
        deliverQueryWrapper.eq("reserve_type", ReserveTypeEnum.RESERVED.getCode());
        List<String> reserveNoList = driverDeliverMapper.selectList(deliverQueryWrapper).stream().map(c -> c.getReserveNo()).collect(Collectors.toList());
        if (reserveNoList.size() == 0) {
            return;
        }
        QueryWrapper<SupplierReserve> supplierReserveQueryWrapper = new QueryWrapper<>();
        supplierReserveQueryWrapper.in("reserve_no", reserveNoList);
        supplierReserveQueryWrapper.ne("status", ReserveStatusEnum.ERROR.getCode());
        List<SupplierReserve> supplierReserves = supplierReserveMapper.selectList(supplierReserveQueryWrapper);
        supplierReserves.forEach(c -> {
            c.setStatus(ReserveStatusEnum.ERROR.getCode());
            supplierReserveMapper.updateById(c);
        });
    }

}
