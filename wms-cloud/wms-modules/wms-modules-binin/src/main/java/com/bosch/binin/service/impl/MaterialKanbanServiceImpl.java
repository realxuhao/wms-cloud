package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.api.enumeration.KanbanPerformTypeEnum;
import com.bosch.binin.api.enumeration.RequirementActionTypeEnum;
import com.bosch.binin.mapper.BinInMapper;
import com.bosch.binin.mapper.MaterialKanbanMapper;
import com.bosch.binin.mapper.StockMapper;
import com.bosch.binin.service.IBinInService;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.binin.service.IStockService;
import com.bosch.binin.utils.BeanConverUtil;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.page.PageDomain;
import lombok.Synchronized;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialKanbanServiceImpl extends ServiceImpl<MaterialKanbanMapper, MaterialKanban> implements IMaterialKanbanService {


    @Resource
    MaterialKanbanMapper materialKanbanMapper;

    @Autowired
    private StockMapper stockMapper;

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
                    RequirementActionTypeEnum.FULL_BIN_DOWN.value() : RequirementActionTypeEnum.PART_BIN_DOWN.value());
            conver.setStatus(KanbanPerformTypeEnum.WAIT_ISSUE.value());
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
        materialKanban.setStatus(KanbanPerformTypeEnum.CANCEL.value());
        LambdaUpdateWrapper<MaterialKanban> uw = new LambdaUpdateWrapper<>();
        uw.eq(MaterialKanban::getId, id);
        uw.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());

        return materialKanbanMapper.update(materialKanban,uw);
    }


}

