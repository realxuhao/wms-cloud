package com.bosch.masterdata.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.Area;
import com.bosch.masterdata.api.domain.vo.FactoryVO;
import com.bosch.masterdata.mapper.AreaMapper;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.WareMapper;
import com.bosch.masterdata.api.domain.Ware;
import com.bosch.masterdata.service.IWareService;

/**
 * 仓库Service业务层处理
 *
 * @author xuhao
 * @date 2022-09-26
 */
@Service
public class WareServiceImpl extends ServiceImpl<WareMapper, Ware> implements IWareService {
    @Autowired
    private WareMapper wareMapper;

    /**
     * 查询仓库
     *
     * @param id 仓库主键
     * @return 仓库
     */
    @Override
    public Ware selectWareById(String id) {
        return wareMapper.selectWareById(id);
    }

    /**
     * 查询仓库列表
     *
     * @param ware 仓库
     * @return 仓库
     */
    @Override
    public List<Ware> selectWareList(Ware ware) {
        if (ware.getStatus() == null) {
            ware.setStatus(1L);
        }
        return wareMapper.selectWareList(ware);
    }

    /**
     * 新增仓库
     *
     * @param ware 仓库
     * @return 结果
     */
    @Override
    public int insertWare(Ware ware) {
        ware.setCreateTime(DateUtils.getNowDate());
        ware.setCreateBy(SecurityUtils.getUsername());
        return wareMapper.insertWare(ware);
    }

    /**
     * 修改仓库
     *
     * @param ware 仓库
     * @return 结果
     */
    @Override
    public int updateWare(Ware ware) {
        ware.setUpdateTime(DateUtils.getNowDate());
        return wareMapper.updateWare(ware);
    }

    /**
     * 批量删除仓库
     *
     * @param ids 需要删除的仓库主键
     * @return 结果
     */
    @Override
    public int deleteWareByIds(String[] ids) {
        return wareMapper.deleteWareByIds(ids);
    }

    /**
     * 删除仓库信息
     *
     * @param id 仓库主键
     * @return 结果
     */
    @Override
    public int deleteWareById(String id) {
        return wareMapper.deleteWareById(id);
    }

    @Override
    public List<FactoryVO> selectFactoryList() {
        return wareMapper.selectFactoryList();
    }
}
