package com.bosch.binin.service;

import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.*;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.domain.vo.MaterialVO;

public interface IBinAssignmentService {

    public BinAllocationVO getBinAllocationVO(BinAllocationDTO binAllocationDTO);

    public FrameRemainVO validateBin(String frameCode, MaterialVO material, Pallet pallet);

}
