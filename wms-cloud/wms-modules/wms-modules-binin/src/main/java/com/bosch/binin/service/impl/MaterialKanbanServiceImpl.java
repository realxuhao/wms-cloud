package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.api.enumeration.KanbanActionTypeEnum;
import com.bosch.binin.mapper.MaterialKanbanMapper;
import com.bosch.binin.mapper.StockMapper;
import com.bosch.binin.mapper.WareShiftMapper;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.binin.service.IStockService;
import com.bosch.binin.service.IWareShiftService;
import com.bosch.binin.utils.BeanConverUtil;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MaterialKanbanServiceImpl extends ServiceImpl<MaterialKanbanMapper, MaterialKanban> implements IMaterialKanbanService {

    @Autowired
    private IStockService stockService;
    @Resource
    MaterialKanbanMapper materialKanbanMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private WareShiftMapper wareShiftMapper;

    @Autowired
    private IWareShiftService wareShiftService;

    @Override
    public IPage<MaterialKanbanVO> pageList(MaterialKanbanDTO dto) {
        IPage<MaterialKanban> page = new Page<>();
        if (dto.getPageNum() != null && dto.getPageSize() != null) {
            page = new Page<>(dto.getPageNum(), dto.getPageSize());
        }
        //查询条件
        LambdaQueryWrapper<MaterialKanban> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getOrderNumber()), MaterialKanban::getOrderNumber,
                dto.getOrderNumber());
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getFactoryCode()), MaterialKanban::getFactoryCode,
                dto.getFactoryCode());
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getWareCode()), MaterialKanban::getWareCode,
                dto.getWareCode());
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getAreaCode()), MaterialKanban::getAreaCode,
                dto.getAreaCode());
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getMaterialCode()), MaterialKanban::getMaterialCode,
                dto.getMaterialCode());
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getSsccNumber()), MaterialKanban::getSsccNumber,
                dto.getSsccNumber());
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getBinCode()), MaterialKanban::getBinCode, dto.getBinCode());
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getCell()), MaterialKanban::getCell, dto.getCell());
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getCreateBy()), MaterialKanban::getCreateBy,
                dto.getCreateBy());
        lambdaQueryWrapper.ge(dto.getCreateTimeStart() != null, MaterialKanban::getCreateTime,
                dto.getCreateTimeStart());
        lambdaQueryWrapper.le(dto.getCreateTimeEnd() != null, MaterialKanban::getCreateTime, dto.getCreateTimeEnd());
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getUpdateBy()), MaterialKanban::getUpdateBy,
                dto.getUpdateBy());
        lambdaQueryWrapper.ge(dto.getUpdateTimeStart() != null, MaterialKanban::getUpdateTime,
                dto.getUpdateTimeStart());
        lambdaQueryWrapper.le(dto.getUpdateTimeEnd() != null, MaterialKanban::getUpdateTime, dto.getUpdateTimeEnd());
        lambdaQueryWrapper.like(dto.getType() != null, MaterialKanban::getType, dto.getType());
        lambdaQueryWrapper.like(dto.getStatus() != null, MaterialKanban::getStatus, dto.getStatus());

        IPage<MaterialKanban> materialKanbanIPage = materialKanbanMapper.selectPage(page, lambdaQueryWrapper);
        //mp提供了convert方法,将数据重新封装
        return materialKanbanIPage.convert(u -> {
            MaterialKanbanVO v = new MaterialKanbanVO();
            BeanUtils.copyProperties(u, v);//拷贝
            return v;
        });
    }

    @Override
    public List<StockVO> getStockInfo(String materialNb, String wareCode) {
        LambdaQueryWrapper<MaterialKanban> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MaterialKanban::getMaterialCode, materialNb);
        lambdaQueryWrapper.eq(MaterialKanban::getWareCode, wareCode);
        List<MaterialKanban> kanbanList = materialKanbanMapper.selectList(lambdaQueryWrapper);
        List<String> ssccList = kanbanList.stream().map(MaterialKanban::getSsccNumber).collect(Collectors.toList());

        List<StockVO> stockVOS = stockMapper.selectStockVOListBySSCCList(ssccList);
        return stockVOS;
    }

    /**
     * 批量删除
     *
     * @param ids 需要删除的主键
     * @return 结果
     */
    @Override
    public int deleteByIds(Long[] ids) {
        return materialKanbanMapper.deleteByIds(ids);
    }

    /**
     * 校验库存表sscc码对应可用库存
     *
     * @param dtos
     * @return
     */
    @Override
    public boolean checkStock(List<MaterialKanbanDTO> dtos) {

        Integer integer = stockMapper.selectCountByList(dtos);


        return integer == dtos.size();
    }

    @Override
    public List<Stock> selectStockList(List<MaterialKanbanDTO> dtos) {

        return stockMapper.selectStockList(dtos);
    }

    /**
     * 库存表数据转换看板数据
     *
     * @param stocks
     * @return
     */
    @Override
    public List<MaterialKanban> setValue(List<Stock> stocks, List<MaterialKanbanDTO> dtos) {
        List<MaterialKanban> materialKanbans = new ArrayList<>();
        stocks.forEach(r -> {
            MaterialKanban conver = BeanConverUtil.conver(r, MaterialKanban.class);
            //根据sscc获取前端传入的数量
            MaterialKanbanDTO dto =
                    dtos.stream().filter(x -> x.getSsccNumber().equals(r.getSsccNumber())).findFirst().orElse(new MaterialKanbanDTO());
            conver.setOrderNumber(dto.getOrderNumber());
            conver.setFactoryCode(r.getPlantNb());
            conver.setMaterialCode(r.getMaterialNb());
            conver.setQuantity(dto.getQuantity());
            conver.setMoveType(MoveTypeEnums.CALL.getCode());
            conver.setCell(dto.getCell());
            conver.setType(dto.getQuantity() == r.getAvailableStock() ?
                    KanbanActionTypeEnum.FULL_BIN_DOWN.value() : KanbanActionTypeEnum.PART_BIN_DOWN.value());
            conver.setStatus(KanbanStatusEnum.WAITING_ISSUE.value());
            conver.setUpdateBy(null);
            conver.setUpdateTime(null);
            conver.setCreateBy(null);
            conver.setCreateTime(null);
            materialKanbans.add(conver);
        });
        return materialKanbans;
    }

    @Override
    public int updateStockBySSCC(String sscc, Double quantity) {
        //根据sscc码修改stock  available_stock freeze_stock
        //取值
        LambdaQueryWrapper<Stock> qw = new LambdaQueryWrapper<>();
        qw.eq(Stock::getSsccNumber, sscc);
        qw.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        qw.last(" for update");
        Stock stock = stockMapper.selectOne(qw);
        if (stock == null) {
            throw new ServiceException("未查询到sscc码相关数据，请刷新页面");
        }
        Double availableStock = stock.getAvailableStock();
        Double freezeStock = stock.getFreezeStock();
        //计算
        availableStock = DoubleMathUtil.doubleMathCalculation(availableStock, quantity, "+");
        freezeStock = DoubleMathUtil.doubleMathCalculation(freezeStock, quantity, "-");
        //赋值
        stock.setAvailableStock(availableStock);
        stock.setFreezeStock(freezeStock);
        //更新
        LambdaUpdateWrapper<Stock> uw = new LambdaUpdateWrapper<>();
        uw.eq(Stock::getSsccNumber, sscc);


        return stockMapper.updateById(stock);
    }


    @Override
    public int updateKanban(Long id) {

        MaterialKanban materialKanban = new MaterialKanban();
        materialKanban.setStatus(KanbanStatusEnum.CANCEL.value());
        LambdaUpdateWrapper<MaterialKanban> uw = new LambdaUpdateWrapper<>();
        uw.eq(MaterialKanban::getId, id);
        uw.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());

        return materialKanbanMapper.update(materialKanban, uw);
    }

    @Override
    public void issueJob(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("任务为空，请选择任务后重试");
        }
        LambdaQueryWrapper<MaterialKanban> kanbanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        kanbanLambdaQueryWrapper.in(MaterialKanban::getId, idList);
        kanbanLambdaQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
//        kanbanLambdaQueryWrapper.eq(MaterialKanban::getStatus, KanbanPerformTypeEnum.WAIT_ISSUE.value());

        List<MaterialKanban> kanbanList = materialKanbanMapper.selectList(kanbanLambdaQueryWrapper);

        if (CollectionUtils.isEmpty(kanbanList) || kanbanList.size() != idList.size()) {
            throw new ServiceException("数据已过期，请重新选择");
        }
        kanbanList.stream().forEach(item -> {
            if (KanbanStatusEnum.CANCEL.value().equals(item.getStatus())) {
                throw new ServiceException("包含已取消任务，请重新选择");
            }
            if (!KanbanStatusEnum.WAITING_ISSUE.value().equals(item.getStatus())){
                throw new ServiceException("包含已下发数据，请重新选择");
            }
        });

        List<WareShift> wareShiftList = new ArrayList<>();

        List<String> outWareSsccList = kanbanList.stream().filter(item -> "7752".equals(item.getFactoryCode())).map(MaterialKanban::getSsccNumber).collect(Collectors.toList());

        //查询外库的库存
        Map<String, List<Stock>> stockMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(outWareSsccList)) {
            LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
            stockQueryWrapper.in(Stock::getSsccNumber, outWareSsccList);
            stockQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
            List<Stock> stockList = stockMapper.selectList(stockQueryWrapper);
            stockMap = stockList.stream().collect(Collectors.groupingBy(Stock::getSsccNumber));
        }


        //状态变为下发状态
        Map<String, List<Stock>> finalStockMap = stockMap;
        kanbanList.stream().forEach(item -> {
            //修改任务状态
            item.setStatus(KanbanStatusEnum.WAITING_BIN_DOWN.value());
            //如果是7752的，需要生成一个移库任务
            if ("7752".equals(item.getFactoryCode())) {
                String ssccNumber = item.getSsccNumber();
                Stock stock = finalStockMap.get(ssccNumber).get(0);
                WareShift wareShift = WareShift.builder().sourcePlantNb(item.getFactoryCode()).sourceWareCode(item.getWareCode()).sourceAreaCode(item.getAreaCode())
                        .sourceBinCode(item.getBinCode()).materialNb(item.getMaterialCode()).expireDate(stock.getExpireDate()).batchNb(stock.getBatchNb())
                        .ssccNb(item.getSsccNumber()).deleteFlag(DeleteFlagStatus.FALSE.getCode()).moveType(MoveTypeEnums.WARE_SHIFT.getCode())
                        .build();

                wareShiftList.add(wareShift);
            }
        });

        //更新任务状态
        updateBatchById(kanbanList);
        //新增移库任务
        wareShiftService.saveBatch(wareShiftList);


    }

    @Override
    public boolean updateStocks(List<MaterialKanban> list) {
        if (CollectionUtils.isEmpty(list)){
            throw new  ServiceException("kanban导入数据为空");
        }
        List<Stock> stocks=new ArrayList<>();
        list.forEach(r->{
            LambdaQueryWrapper<Stock> qw=new LambdaQueryWrapper<>();
            qw.eq(Stock::getSsccNumber,r.getSsccNumber());
            qw.eq(Stock::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
            qw.last(" for update");
            Stock stock = stockMapper.selectOne(qw);
            if (stock==null){
                throw new  ServiceException("sscc码:"+r.getSsccNumber()+"未查询到库存");
            }
            Double availableStock = stock.getAvailableStock();
            Double freezeStock = stock.getFreezeStock();
            //计算
            availableStock = DoubleMathUtil.doubleMathCalculation(availableStock, r.getQuantity(), "-");
            freezeStock = DoubleMathUtil.doubleMathCalculation(freezeStock, r.getQuantity(), "+");
            //赋值
            stock.setAvailableStock(availableStock);
            stock.setFreezeStock(freezeStock);
            stocks.add(stock);
        });


        return  stockService.updateBatchById(stocks);
    }

    @Override
    public List<MaterialKanbanVO> getWaitingJob(String mesbarCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesbarCode);
        String materialNb = MesBarCodeUtil.getMaterialNb(mesbarCode);

        LambdaQueryWrapper<MaterialKanban> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaterialKanban::getSsccNumber, sscc);
        queryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<MaterialKanban> kanbanList = materialKanbanMapper.selectList(queryWrapper);
        List<MaterialKanban> kanbans = kanbanList.stream().filter(item -> "0".equals(item.getStatus())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(kanbans)) {
            throw new ServiceException("该托 " + "sscc" + " 不存在下架任务");
        }

        List<MaterialKanbanVO> materialKanbanVOS = BeanConverUtil.converList(kanbans, MaterialKanbanVO.class);
        return materialKanbanVOS;
    }




}

