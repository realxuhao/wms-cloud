package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.RegisterBom;
import com.bosch.binin.api.domain.dto.RegisterBomDTO;

import java.util.List;

public interface IRegisterBomService extends IService<RegisterBom> {

    public boolean validList(String orderNb);

    public Integer deleteList(String orderNb);
}
