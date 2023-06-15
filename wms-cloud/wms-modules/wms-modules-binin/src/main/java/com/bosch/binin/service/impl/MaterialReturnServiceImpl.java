package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.*;
import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.*;
import com.bosch.binin.api.enumeration.*;
import com.bosch.binin.service.IBinInService;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.binin.service.IMaterialReturnService;
import com.bosch.binin.mapper.MaterialReturnMapper;
import com.bosch.binin.service.IStockService;
import com.bosch.binin.utils.BeanConverUtil;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.api.enumeration.AreaTypeEnum;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author GUZ1CGD4
 * @description 针对表【material_return(退库表)】的数据库操作Service实现
 * @createDate 2022-12-12 11:09:13
 */
@Service
public class MaterialReturnServiceImpl extends ServiceImpl<MaterialReturnMapper, MaterialReturn>
        implements IMaterialReturnService {

    @Autowired
    private MaterialReturnMapper materialReturnMapper;

    @Autowired
    private IStockService stockService;

    @Autowired
    private IBinInService binInService;

    @Autowired
    private RemoteMasterDataService masterDataService;

    @Autowired
    private IMaterialKanbanService kanbanService;

    @Override
    public List<MaterialReturnVO> list(MaterialReturnQueryDTO queryDTO) {
        List<MaterialReturnVO> list = materialReturnMapper.list(queryDTO);
        return list;
    }

    @Override
    public void issueJob(MaterialReturnConfirmDTO confirmDTO) {
        List<String> ssccList = confirmDTO.getSsccNumbers();
        LambdaQueryWrapper<MaterialReturn> materialReturnQueryWrapper = new LambdaQueryWrapper<>();
        materialReturnQueryWrapper.in(MaterialReturn::getSsccNumber, ssccList);
        materialReturnQueryWrapper.eq(MaterialReturn::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        materialReturnQueryWrapper.eq(MaterialReturn::getStatus, MaterialReturnStatusEnum.WAITING_CONFIRM.value());
        List<MaterialReturn> materialReturnList = list(materialReturnQueryWrapper);
        if (CollectionUtils.isEmpty(materialReturnList) || materialReturnList.size() != ssccList.size()) {
            throw new ServiceException("退库数据过期，请刷新后重试");
        }
        R<AreaVO> areaVOR = masterDataService.getByCode(confirmDTO.getAreaCode());
        if (areaVOR == null || !areaVOR.isSuccess()) {
            throw new ServiceException("调用主数据服务查询区域失败");
        }
        if (Objects.isNull(areaVOR.getData())) {
            throw new ServiceException("没有" + confirmDTO.getAreaCode() + "区域，请维护主数据");
        }
        AreaVO areaVO = areaVOR.getData();
        boolean unquanlified = false;
        if (AreaTypeEnum.UNQUALIFIED.getCode().equals(areaVO.getAreaType())) {
            unquanlified = true;
        }
        boolean finalUnquanlified = unquanlified;
        materialReturnList.stream().forEach(item -> {
            item.setType(finalUnquanlified ? MaterialTransTypeEnum.AB_NORMAL.code() : MaterialTransTypeEnum.NORMAL.code());
            item.setStatus(MaterialReturnStatusEnum.WAITING_BIN_IN.value());
            item.setWareCode(confirmDTO.getWareCode());
            item.setAreaCode(confirmDTO.getAreaCode());
        });
        updateBatchById(materialReturnList);
    }

    @Override
    public boolean addMaterialReturn(MaterialReturnDTO materialReturnDTO) {
        String mesBarCode = materialReturnDTO.getMesBarCode();
//        String plantNb = materialReturnDTO.getPlantNb();
//        String wareCode = materialReturnDTO.getWareCode();
//        String areaCode = materialReturnDTO.getAreaCode();
//        Integer type = materialReturnDTO.getType();
        Double quantity = materialReturnDTO.getQuantity();

        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(Stock::getSsccNumber, MesBarCodeUtil.getSSCC(mesBarCode));
        stockQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockQueryWrapper.last("limit 1");
        stockQueryWrapper.last("for update");
        Stock stock = stockService.getOne(stockQueryWrapper);
        if (!Objects.isNull(stock)) {
            throw new ServiceException("该托已存在于库存中，暂时不能退料");
        }

        LambdaQueryWrapper<MaterialReturn> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaterialReturn::getSsccNumber, MesBarCodeUtil.getSSCC(mesBarCode));
        queryWrapper.eq(MaterialReturn::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.ne(MaterialReturn::getStatus, MaterialReturnStatusEnum.CANCEL.value());
        queryWrapper.ne(MaterialReturn::getStatus, MaterialReturnStatusEnum.FINISH.value());
        queryWrapper.last("limit 1");
        MaterialReturn materialReturnDO = materialReturnMapper.selectOne(queryWrapper);
        if (materialReturnDO != null) {
            throw new ServiceException("该mesbarcode" + mesBarCode + "已有退料任务，不可重复添加");
        }

        MaterialReturn materialReturn = new MaterialReturn();
        materialReturn.setMaterialNb(MesBarCodeUtil.getMaterialNb(mesBarCode));
        materialReturn.setStatus(MaterialReturnStatusEnum.WAITING_CONFIRM.value());
        materialReturn.setBatchNb(MesBarCodeUtil.getBatchNb(mesBarCode));
        materialReturn.setExpireDate(MesBarCodeUtil.getExpireDate(mesBarCode));
        materialReturn.setMoveType(MoveTypeEnums.MATERIAL_RETURN.getCode());
        materialReturn.setSsccNumber(MesBarCodeUtil.getSSCC(mesBarCode));
//        materialReturn.setAreaCode(areaCode);
//        materialReturn.setWareCode(wareCode);
//        materialReturn.setType(type);
        materialReturn.setQuantity(quantity);
//        materialReturn.setCell(materialReturnDTO.getCell());
        return save(materialReturn);
    }

    @Override
    public BinInVO allocateBin(String mesBarCode, String wareCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<MaterialReturn> qw = new LambdaQueryWrapper<>();
        qw.eq(MaterialReturn::getSsccNumber, sscc);
        qw.eq(MaterialReturn::getStatus, MaterialReturnStatusEnum.WAITING_BIN_IN.value());
        qw.eq(MaterialReturn::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        qw.last("limit 1");
        qw.last("for update");
        MaterialReturn materialReturn = materialReturnMapper.selectOne(qw);

        if (Objects.isNull(materialReturn)) {
            throw new ServiceException("该SSCC码 " + sscc + " 不存在退库任务");
        }
        if (!MaterialReturnStatusEnum.WAITING_BIN_IN.value().equals(materialReturn.getStatus())) {
            throw new ServiceException("该SSCC码 " + sscc + "对应任务状态为: " + KanbanStatusEnum.getDesc(String.valueOf(materialReturn.getStatus())) + " 不可分配库位 ");
        }
        BinInVO binInVO = null;
        String barCode = MesBarCodeUtil.generateMesBarCode(MesBarCodeUtil.getExpireDate(mesBarCode), sscc, materialReturn.getMaterialNb(), materialReturn.getBatchNb(), materialReturn.getQuantity());

        if (materialReturn.getType() == MaterialTransTypeEnum.NORMAL.code()) {//正常退料分配到库位
            binInVO = binInService.allocateToBin(barCode, materialReturn.getQuantity());
        } else if (materialReturn.getType() == MaterialTransTypeEnum.AB_NORMAL.code()) {//异常退料分配到存储区
            binInVO = binInService.allocateToBinOrArea(barCode, materialReturn.getQuantity(), null, materialReturn.getAreaCode());
        }

        return binInVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void performBinIn(ManualBinInDTO binInDTO) {
        String mesBarCode = binInDTO.getMesBarCode();
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);

        LambdaQueryWrapper<BinIn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BinIn::getSsccNumber, sscc).eq(BinIn::getDeleteFlag, 0);
        BinIn binIn = binInService.getOne(lambdaQueryWrapper);


        if (binIn == null) {
            throw new ServiceException("物料号" + materialNb + "暂无上架任务");
        }

        if (BinInStatusEnum.FINISH.value() == binIn.getStatus()) {
            throw new ServiceException("物料号" + materialNb + "已经上架");
        }

        if (StringUtils.isEmpty(binIn.getPalletCode()) && StringUtils.isEmpty(binInDTO.getPalletCode())) {
            throw new ServiceException("实物托盘码不能为空");
        }

        String ssccNb = MesBarCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<MaterialReturn> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaterialReturn::getSsccNumber, ssccNb);
        queryWrapper.eq(MaterialReturn::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.ne(MaterialReturn::getStatus, MaterialReturnStatusEnum.CANCEL.value());
        queryWrapper.ne(MaterialReturn::getStatus, MaterialReturnStatusEnum.FINISH.value());
        queryWrapper.last("limit 1");
        queryWrapper.last("for update");
        MaterialReturn materialReturn = materialReturnMapper.selectOne(queryWrapper);
        //插入库存

        Stock lastOneBySSCC = new Stock();

        StockVO oneBySSCC = stockService.getLastOneBySSCC(sscc);
        lastOneBySSCC = BeanConverUtil.conver(oneBySSCC, Stock.class);

        if (lastOneBySSCC == null) {
            LambdaQueryWrapper<MaterialKanban> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MaterialKanban::getSsccNumber, sscc);
            wrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
            wrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.CANCEL.value());
            wrapper.eq(MaterialKanban::getStatus, KanbanStatusEnum.LINE_RECEIVED.value());
            MaterialKanban kanban = kanbanService.getOne(wrapper);
            if (kanban.getParentId() != null) {
                MaterialKanban materialKanban = kanbanService.getById(kanban.getParentId());
                lastOneBySSCC = stockService.getRecentOneBySSCC(materialKanban.getSsccNumber());
            }


        }

        if (materialReturn.getType() == MaterialTransTypeEnum.AB_NORMAL.code()) {
            //异常入库
            binIn.setUpdateBy(SecurityUtils.getUsername());
            binIn.setStatus(BinInStatusEnum.FINISH.value());
            binIn.setUpdateTime(new Date());
            binIn.setAreaCode(binInDTO.getActualCode());
            if (StringUtils.isEmpty(binIn.getPalletCode())) {
                binIn.setPalletCode(binInDTO.getPalletCode());
            }
            binInService.updateById(binIn);


            Stock stock = new Stock();
            stock.setPlantNb(binIn.getPlantNb());
            stock.setWareCode(binIn.getWareCode());
            stock.setSsccNumber(binIn.getSsccNumber());
            stock.setWareCode(binIn.getWareCode());
            stock.setBinCode(binIn.getActualBinCode());
            stock.setFrameCode(binIn.getActualFrameCode());
            stock.setMaterialNb(binIn.getMaterialNb());
            stock.setBatchNb(binIn.getBatchNb());
            stock.setExpireDate(binIn.getExpireDate());
            stock.setTotalStock(binIn.getQuantity());
            stock.setFreezeStock(Double.valueOf(0));
            stock.setAvailableStock(stock.getTotalStock() - stock.getFreezeStock());
            stock.setBinInId(binIn.getId());
            stock.setCreateBy(SecurityUtils.getUsername());
            stock.setCreateTime(new Date());
            stock.setQualityStatus(StringUtils.isEmpty(lastOneBySSCC.getQualityStatus()) ? QualityStatusEnums.WAITING_QUALITY.getCode() : QualityStatusEnums.USE.getCode());
            stock.setFromPurchaseOrder(binIn.getFromPurchaseOrder());
            stock.setAreaCode(binIn.getAreaCode());
            stock.setPalletCode(binIn.getPalletCode());
            stockService.save(stock);


        } else {
            BinInDTO dto = new BinInDTO();
            dto.setActualBinCode(binInDTO.getActualCode());
            dto.setPalletCode(binInDTO.getPalletCode());
            dto.setMesBarCode(binInDTO.getMesBarCode());
            BinInVO binInVO = binInService.performBinIn(dto, StringUtils.isEmpty(lastOneBySSCC.getQualityStatus()) ? QualityStatusEnums.WAITING_QUALITY.getCode() : QualityStatusEnums.USE.getCode());
        }
        materialReturn.setStatus(MaterialReturnStatusEnum.FINISH.value());
        this.updateById(materialReturn);
    }
}




