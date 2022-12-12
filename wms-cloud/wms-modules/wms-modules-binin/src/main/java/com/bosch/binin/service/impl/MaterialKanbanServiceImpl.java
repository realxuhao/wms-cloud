package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectById;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.dto.SplitPalletDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.MaterialInfoVO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.api.enumeration.BinInStatusEnum;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.api.enumeration.KanbanActionTypeEnum;
import com.bosch.binin.mapper.MaterialKanbanMapper;
import com.bosch.binin.mapper.StockMapper;
import com.bosch.binin.mapper.WareShiftMapper;
import com.bosch.binin.service.*;
import com.bosch.binin.utils.BeanConverUtil;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.page.PageDomain;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.apache.coyote.http11.filters.VoidInputFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MaterialKanbanServiceImpl extends ServiceImpl<MaterialKanbanMapper, MaterialKanban> implements IMaterialKanbanService {

    @Autowired
    private IStockService stockService;
    @Autowired
    private MaterialKanbanMapper materialKanbanMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private WareShiftMapper wareShiftMapper;

    @Autowired
    @Lazy
    private IWareShiftService wareShiftService;

    @Autowired
    private IBinInService binInService;

    @Autowired
    @Lazy
    private IMaterialCallService callService;


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
        lambdaQueryWrapper.eq(dto.getType() != null, MaterialKanban::getType, dto.getType());
        lambdaQueryWrapper.eq(dto.getStatus() != null, MaterialKanban::getStatus, dto.getStatus());

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
        lambdaQueryWrapper.eq(MaterialKanban::getStatus, KanbanStatusEnum.WAITING_ISSUE.value());
        lambdaQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<MaterialKanban> kanbanList = materialKanbanMapper.selectList(lambdaQueryWrapper);
        List<String> ssccList = kanbanList.stream().map(MaterialKanban::getSsccNumber).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(ssccList)) {
            return Collections.emptyList();
        }
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
        //qw.orderByDesc(Stock::getUpdateTime);
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
    public void issueJob(String[] ssccNumbers) {
        List<String> ssccNumberList = Arrays.asList(ssccNumbers);
        if (CollectionUtils.isEmpty(ssccNumberList)) {
            throw new ServiceException("任务为空，请选择任务后重试");
        }
        LambdaQueryWrapper<MaterialKanban> kanbanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        kanbanLambdaQueryWrapper.in(MaterialKanban::getSsccNumber, ssccNumberList);
        kanbanLambdaQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanLambdaQueryWrapper.eq(MaterialKanban::getStatus, KanbanStatusEnum.WAITING_ISSUE.value());

        List<MaterialKanban> kanbanList = materialKanbanMapper.selectList(kanbanLambdaQueryWrapper);

        if (CollectionUtils.isEmpty(kanbanList) || kanbanList.size() != ssccNumberList.size()) {
            throw new ServiceException("数据已过期，请重新选择");
        }
        kanbanList.stream().forEach(item -> {
            if (KanbanStatusEnum.CANCEL.value().equals(item.getStatus())) {
                throw new ServiceException("包含已取消任务，请重新选择");
            }
            if (!KanbanStatusEnum.WAITING_ISSUE.value().equals(item.getStatus())) {
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
        List<Stock> stockList = new ArrayList<>();
        kanbanList.stream().forEach(item -> {
            //修改任务状态
            item.setStatus(KanbanStatusEnum.WAITING_BIN_DOWN.value());
            //如果是7752的，需要生成一个移库任务，移库任务是  生成
            if ("7752".equals(item.getFactoryCode())) {
                String ssccNumber = item.getSsccNumber();
                Stock stock = finalStockMap.get(ssccNumber).get(0);
                WareShift wareShift = WareShift.builder().sourcePlantNb(item.getFactoryCode()).sourceWareCode(item.getWareCode()).sourceAreaCode(item.getAreaCode())
                        .sourceBinCode(item.getBinCode()).materialNb(item.getMaterialCode()).expireDate(stock.getExpireDate()).batchNb(stock.getBatchNb())
                        .ssccNb(item.getSsccNumber()).deleteFlag(DeleteFlagStatus.FALSE.getCode()).moveType(MoveTypeEnums.WARE_SHIFT.getCode())
                        .build();

                wareShiftList.add(wareShift);
                stock.setFreezeStock(stock.getFreezeStock() + stock.getAvailableStock());
                stockList.add(stock);
            }
        });

        //更新库存状态
        stockService.updateBatchById(stockList);
        //更新任务状态
        updateBatchById(kanbanList);
        //新增移库任务
        wareShiftService.saveBatch(wareShiftList);


    }

    @Override
    public boolean updateStocks(List<MaterialKanban> list) {
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException("kanban导入数据为空");
        }
        List<Stock> stocks = new ArrayList<>();
        list.forEach(r -> {
            LambdaQueryWrapper<Stock> qw = new LambdaQueryWrapper<>();
            qw.eq(Stock::getSsccNumber, r.getSsccNumber());
            qw.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
            qw.last(" for update");
            Stock stock = stockMapper.selectOne(qw);
            if (stock == null) {
                throw new ServiceException("sscc码:" + r.getSsccNumber() + "未查询到库存");
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


        return stockService.updateBatchById(stocks);
    }

    @Override
    public MaterialKanbanVO getWaitingJob(String mesbarCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesbarCode);

        MaterialKanbanVO kanbanVO = materialKanbanMapper.getKanbanInfoBySsccNb(sscc);

        if (Objects.isNull(kanbanVO)) {
            throw new ServiceException("该托 " + "sscc" + " 任务不存在");
        }
        if (!KanbanStatusEnum.WAITING_BIN_DOWN.value().equals(kanbanVO.getStatus())) {
            throw new ServiceException("该托 " + "sscc" + " 不存在下架任务");
        }
        return kanbanVO;
    }

    @Override
    public List<MaterialKanbanVO> receivingMaterialList(MaterialKanbanDTO dto) {


        List<MaterialKanbanVO> materialKanbanVOS = materialKanbanMapper.receivingMaterialList(dto);
        return materialKanbanVOS;

    }

    @Override
    public void binDown(String ssccNb) {
        LambdaQueryWrapper<MaterialKanban> kanbanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        kanbanLambdaQueryWrapper.eq(MaterialKanban::getSsccNumber, ssccNb);
        kanbanLambdaQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanLambdaQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.CANCEL.value());
        kanbanLambdaQueryWrapper.last("for update");
        MaterialKanban kanban = materialKanbanMapper.selectOne(kanbanLambdaQueryWrapper);
        if (Objects.isNull(kanban) || !KanbanStatusEnum.WAITING_BIN_DOWN.value().equals(kanban.getStatus())) {
            throw new ServiceException("任务状态过期，请刷新后重试");
        }

        //在移库任务中查询
        LambdaQueryWrapper<WareShift> wareShiftLambdaQueryWrapper = new LambdaQueryWrapper<>();
        wareShiftLambdaQueryWrapper.eq(WareShift::getSsccNb, ssccNb);
        wareShiftLambdaQueryWrapper.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE);
        wareShiftLambdaQueryWrapper.eq(WareShift::getStatus, KanbanStatusEnum.WAITING_BIN_DOWN);
        WareShift wareShift = wareShiftMapper.selectOne(wareShiftLambdaQueryWrapper);
        //移库任务修改状态
        if (wareShift != null) {
            wareShift.setStatus(KanbanStatusEnum.INNER_RECEIVING.value());
            wareShiftMapper.updateById(wareShift);
        }

        //状态修改为外库已下架
        kanban.setStatus(KanbanStatusEnum.OUT_DOWN.value());
        materialKanbanMapper.updateById(kanban);

        //执行下架
        binInService.binDown(ssccNb);


    }

    @Override
    public List<MaterialKanbanVO> binDownList(PageDomain pageDomain, String wareCode) {
        return materialKanbanMapper.getBinDownList(wareCode);
    }

    @Override
    public List<MaterialKanbanVO> receivedMaterialList(MaterialKanbanDTO dto) {

        List<MaterialKanbanVO> materialKanbanVOS = materialKanbanMapper.receivedMaterialList(dto);
        return materialKanbanVOS;
    }

    @Override
    public List<MaterialInfoVO> materialInfoList(String sscc, String wareCode) {
        List<MaterialInfoVO> materialInfoVOS = materialKanbanMapper.materialInfoList(sscc, wareCode);
        return materialInfoVOS;
    }

    @Override
    public int updateKanbanByIds(List<Long> ids, Integer status) {
        MaterialKanban materialKanban = new MaterialKanban();
        materialKanban.setStatus(status);
        LambdaUpdateWrapper<MaterialKanban> uw = new LambdaUpdateWrapper<>();
        uw.in(MaterialKanban::getId, ids);
        uw.ne(MaterialKanban::getStatus, KanbanStatusEnum.CANCEL);
        uw.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());

        return materialKanbanMapper.update(materialKanban, uw);
    }

    @Override
    public int updateKanbanByStatus(List<String> ssccs, Integer queryStatus, Integer status) {
        MaterialKanban materialKanban = new MaterialKanban();
        materialKanban.setStatus(status);
        LambdaUpdateWrapper<MaterialKanban> uw = new LambdaUpdateWrapper<>();
        uw.in(MaterialKanban::getSsccNumber, ssccs);
        uw.eq(MaterialKanban::getStatus, queryStatus);
        uw.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());

        return materialKanbanMapper.update(materialKanban, uw);
    }

    @Override
    public int updateKanbanBySSCC(List<String> ssccs, Integer status) {
        MaterialKanban materialKanban = new MaterialKanban();
        materialKanban.setStatus(status);
        LambdaUpdateWrapper<MaterialKanban> uw = new LambdaUpdateWrapper<>();
        uw.in(MaterialKanban::getSsccNumber, ssccs);
        uw.ne(MaterialKanban::getStatus, KanbanStatusEnum.CANCEL);
        uw.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());

        return materialKanbanMapper.update(materialKanban, uw);
    }

    @Override
    public List<MaterialInfoVO> materialInfoBySSCC(List<String> sscc) {
        return materialKanbanMapper.materialInfoBySSCC(sscc);
    }

    @Override
    public List<MaterialKanban> getListBySCAndStatus(List<String> sscc, Integer status) {
        LambdaQueryWrapper<MaterialKanban> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MaterialKanban::getSsccNumber, sscc);
        queryWrapper.eq(MaterialKanban::getStatus, status);
        queryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<MaterialKanban> materialKanbans = materialKanbanMapper.selectList(queryWrapper);
        return materialKanbans;
    }

    @Override
    public MaterialKanban getOneBySCAndStatus(String sscc, Integer status) {
        LambdaQueryWrapper<MaterialKanban> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaterialKanban::getSsccNumber, sscc);
        queryWrapper.eq(MaterialKanban::getStatus, status);
        queryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        MaterialKanban materialKanban = materialKanbanMapper.selectOne(queryWrapper);
        return materialKanban;
    }

    @Override
    public MaterialKanbanVO getKanbanBySSCC(String sscc) {
        LambdaQueryWrapper<MaterialKanban> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaterialKanban::getSsccNumber, sscc);
        queryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.CANCEL.value());
        queryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        MaterialKanban materialKanban = materialKanbanMapper.selectOne(queryWrapper);
        MaterialKanbanVO conver = BeanConverUtil.conver(materialKanban, MaterialKanbanVO.class);
        return conver;
    }

    @Override
    public void splitPallet(SplitPalletDTO splitPallet) {

        //校验quantity
        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(Stock::getSsccNumber, splitPallet.getSourceSsccNb()).eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode()).last("limit 1").last("for update");
        Stock stock = stockMapper.selectOne(stockQueryWrapper);
        StockVO stockVO = BeanConverUtil.conver(stock, StockVO.class);
        if (Objects.isNull(stock)) {
            stockVO = stockService.getOneBySSCC(splitPallet.getSourceSsccNb());
        }
        if (stock.getTotalStock() < Double.valueOf(MesBarCodeUtil.getQuantity(splitPallet.getNewMesBarCode()))) {
            throw new ServiceException("拆托数量不能超过源库存量");
        }


        //老任务变为结束
        LambdaQueryWrapper<MaterialKanban> kanbanQueryWrapper = new LambdaQueryWrapper<>();
        kanbanQueryWrapper.eq(MaterialKanban::getSsccNumber, splitPallet.getSourceSsccNb());
        kanbanQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.FINISH.value());
        kanbanQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.CANCEL.value());
        kanbanQueryWrapper.last("limit 1");
        kanbanQueryWrapper.last("for update");
        MaterialKanban materialKanban = materialKanbanMapper.selectOne(kanbanQueryWrapper);
        if (materialKanban == null) {
            throw new ServiceException("任务不存在");
        }
        if (!materialKanban.getStatus().equals(KanbanStatusEnum.INNER_BIN_IN) || !materialKanban.getStatus().equals(KanbanStatusEnum.WAITING_BIN_DOWN)) {
            throw new ServiceException("当前任务状态不正确");
        }

        materialKanban.setStatus(KanbanStatusEnum.FINISH.value());
        materialKanban.setUpdateBy(SecurityUtils.getUsername());
        materialKanban.setUpdateTime(new Date());
        materialKanbanMapper.updateById(materialKanban);

        //生成一个新任务
        MaterialKanban newKanban = new MaterialKanban();
        newKanban.setOrderNumber(materialKanban.getOrderNumber());
        newKanban.setFactoryCode(materialKanban.getFactoryCode());
        newKanban.setWareCode(materialKanban.getWareCode());
        newKanban.setAreaCode(materialKanban.getAreaCode());
        newKanban.setBinCode(materialKanban.getBinCode());
        newKanban.setMaterialCode(materialKanban.getMaterialCode());
        newKanban.setSsccNumber(MesBarCodeUtil.getSSCC(splitPallet.getNewMesBarCode()));
        newKanban.setCell(materialKanban.getCell());
        newKanban.setType(KanbanActionTypeEnum.FULL_BIN_DOWN.value());
        newKanban.setQuantity(Double.valueOf(MesBarCodeUtil.getQuantity(splitPallet.getNewMesBarCode())));
        newKanban.setStatus(KanbanStatusEnum.INNER_DOWN.value());
        newKanban.setCreateBy(SecurityUtils.getUsername());
        newKanban.setCreateTime(new Date());
        newKanban.setMoveType(MoveTypeEnums.CALL.getCode());
        newKanban.setParentId(materialKanban.getId());
        materialKanbanMapper.insert(newKanban);

        //如果老sscc没有上架，需要生成一个新的上架任务
        LambdaQueryWrapper<BinIn> bininQueryWrapper = new LambdaQueryWrapper<>();
        bininQueryWrapper.eq(BinIn::getSsccNumber, splitPallet.getSourceSsccNb());
        bininQueryWrapper.eq(BinIn::getStatus, BinInStatusEnum.FINISH.value());
        bininQueryWrapper.last("limmit 1");
        BinIn binIn = binInService.getOne(bininQueryWrapper);
        if (binIn == null) {
            //生成上架任务
            BinInVO binInVO = binInService.generateInTaskByOldStock(splitPallet.getSourceSsccNb(), Double.valueOf(MesBarCodeUtil.getQuantity(splitPallet.getNewMesBarCode())), SecurityUtils.getWareCode());
        }


    }

    @Override
    public void cancelKanban(Long id) {
        //查询取消任务详情
        LambdaQueryWrapper<MaterialKanban> qw = new LambdaQueryWrapper<>();
        qw.eq(MaterialKanban::getId, id);
        qw.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        qw.last("for update ");
        MaterialKanban materialKanban = getOne(qw);
        if (KanbanStatusEnum.FINISH.value().equals(materialKanban.getStatus()) ||
                KanbanStatusEnum.LINE_RECEIVED.value().equals(materialKanban.getStatus()) ||
                KanbanStatusEnum.CANCEL.value().equals(materialKanban.getStatus())) {
            throw new ServiceException("当前状态为: " + KanbanStatusEnum.getDesc(String.valueOf(materialKanban.getStatus())) + " 不可取消");
        }
        //kanban 修改取消状态
        updateKanban(id);
        //叫料需求的下发量修改
        callService.updateCallQuantity(materialKanban);
        //sscc库存可用 冻结修改
        if (materialKanban.getStatus().equals(KanbanStatusEnum.WAITING_ISSUE.value()) || materialKanban.getStatus().equals(KanbanStatusEnum.WAITING_BIN_DOWN.value())) {

            updateStockBySSCC(materialKanban.getSsccNumber(),
                    materialKanban.getQuantity());
        }

        WareShift wareShift = wareShiftService.getWareShiftBySsccAndStatus(materialKanban.getSsccNumber());
        if (wareShift != null) {
            wareShiftService.cancelWareShift(wareShift.getId());
        }

        if (materialKanban.getStatus().equals(KanbanStatusEnum.LINE_RECEIVED.value())) {
            if (materialKanban.getParentId() == null) {//说明不是子任务,拆托而来的子任务
                binInService.generateInTaskByOldStock(materialKanban.getSsccNumber(), Double.valueOf(0), materialKanban.getWareCode());
            } else {
                //是子任务，需要先获取之前的看板
                dealCancelSubJob(materialKanban);
            }
        }


    }

    private void dealCancelSubJob(MaterialKanban subKanban) {
        MaterialKanban parentKanban = materialKanbanMapper.selectById(subKanban.getParentId());
        //获取一个库存
        Stock parentStock = stockService.getOneStock(parentKanban.getSsccNumber());
        Date expireDate = parentStock.getExpireDate();
        String batchNb = parentStock.getBatchNb();
        String mesBarCode = MesBarCodeUtil.generateMesBarCode(expireDate, subKanban.getSsccNumber(), subKanban.getMaterialCode(), batchNb, subKanban.getQuantity());
        binInService.generateInTaskByMesBarCode(mesBarCode);

    }
}

