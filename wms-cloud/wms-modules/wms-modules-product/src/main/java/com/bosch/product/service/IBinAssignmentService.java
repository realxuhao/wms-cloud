package com.bosch.product.service;

import com.bosch.binin.api.domain.dto.BinAllocationDTO;
import com.bosch.binin.api.domain.vo.BinAllocationVO;
import com.bosch.binin.api.domain.vo.FrameRemainVO;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.domain.vo.MaterialVO;

public interface IBinAssignmentService {

    public String getBinAllocationVO(String qrCode);

    public String validateBin(String frameCode);

}
