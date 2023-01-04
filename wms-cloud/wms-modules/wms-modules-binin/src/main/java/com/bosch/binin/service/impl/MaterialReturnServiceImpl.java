package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.*;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.ManualBinInDTO;
import com.bosch.binin.api.domain.dto.MaterialReturnDTO;
import com.bosch.binin.api.domain.dto.MaterialReturnQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.MaterialReturnVO;
import com.bosch.binin.api.domain.vo.WareShiftVO;
import com.bosch.binin.api.enumeration.*;
import com.bosch.binin.service.IBinInService;
import com.bosch.binin.service.IMaterialReturnService;
import com.bosch.binin.mapper.MaterialReturnMapper;
import com.bosch.binin.service.IStockService;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Override
    public List<MaterialReturnVO> list(MaterialReturnQueryDTO queryDTO) {
        List<MaterialReturnVO> list = materialReturnMapper.list(queryDTO);
        return list;
    }

    @Override
    public void issueJob(String[] ssccNumbers) {
        List<String> ssccList = Arrays.asList(ssccNumbers);
        LambdaQueryWrapper<MaterialReturn> materialReturnQueryWrapper = new LambdaQueryWrapper<>();
        materialReturnQueryWrapper.in(MaterialReturn::getSsccNumber, ssccList);
        materialReturnQueryWrapper.eq(MaterialReturn::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        materialReturnQueryWrapper.eq(MaterialReturn::getStatus, MaterialReturnStatusEnum.WAITING_ISSUE.value());
        List<MaterialReturn> materialReturnList = list(materialReturnQueryWrapper);
        if (CollectionUtils.isEmpty(materialReturnList) || materialReturnList.size() != ssccList.size()) {
            throw new ServiceException("退库数据过期，请刷新后重试");
        }
        materialReturnList.stream().forEach(item -> item.setStatus(MaterialReturnStatusEnum.WAITING_BIN_IN.value()));
        updateBatchById(materialReturnList);

    }

    @Override
    public boolean addMaterialReturn(MaterialReturnDTO materialReturnDTO) {
        String mesBarCode = materialReturnDTO.getMesBarCode();
        String plantNb = materialReturnDTO.getPlantNb();
        String wareCode = materialReturnDTO.getWareCode();
        String areaCode = materialReturnDTO.getAreaCode();
        Integer type = materialReturnDTO.getType();
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
        MaterialReturn materialReturn = new MaterialReturn();
        materialReturn.setMaterialNb(MesBarCodeUtil.getMaterialNb(mesBarCode));
        materialReturn.setStatus(MaterialReturnStatusEnum.WAITING_ISSUE.value());
        materialReturn.setBatchNb(MesBarCodeUtil.getBatchNb(mesBarCode));
        materialReturn.setExpireDate(MesBarCodeUtil.getExpireDate(mesBarCode));
        materialReturn.setMoveType(MoveTypeEnums.MATERIAL_RETURN.getCode());
        materialReturn.setSsccNumber(MesBarCodeUtil.getSSCC(mesBarCode));
        materialReturn.setAreaCode(areaCode);
        materialReturn.setWareCode(wareCode);
        materialReturn.setType(type);
        materialReturn.setQuantity(quantity);
        materialReturn.setCell(materialReturnDTO.getCell());
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
        if (materialReturn.getType() == 0) {//正常退料分配到库位
            String barCode = MesBarCodeUtil.generateMesBarCode(MesBarCodeUtil.getExpireDate(mesBarCode), sscc, materialReturn.getMaterialNb(), materialReturn.getBatchNb(), materialReturn.getQuantity());
            binInVO = binInService.generateInTaskByMesBarCode(mesBarCode);
        } else if (materialReturn.getType() == 1) {//异常退料分配到存储区
            binInVO = binInService.allocateToBinOrArea(sscc, materialReturn.getMaterialNb(), null, materialReturn.getAreaCode(), materialReturn.getQuantity());
        }

        return binInVO;
    }

    @Override
    public BinInVO performBinIn(ManualBinInDTO binInDTO) {
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
        queryWrapper.ne(MaterialReturn::getStatus, ManuTransStatusEnum.CANCEL.code());
        queryWrapper.last("limit 1");
        queryWrapper.last("for update");
        MaterialReturn materialReturn = materialReturnMapper.selectOne(queryWrapper);


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
        } else {
            BinInDTO dto = new BinInDTO();
            dto.setActualBinCode(binInDTO.getActualCode());
            dto.setPalletCode(binInDTO.getPalletCode());
            dto.setMesBarCode(dto.getMesBarCode());
            BinInVO binInVO = binInService.performBinIn(dto);
        }
        return binInService.getByMesBarCode(binInDTO.getMesBarCode());
    }
}




