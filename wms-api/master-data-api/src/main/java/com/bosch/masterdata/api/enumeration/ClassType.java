package com.bosch.masterdata.api.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ClassType {
    MATERIALDTO("MaterialDTO", new ArrayList<String>(Arrays.asList("物料名称","物料代码","物料类型","包装重量"
            ,"托盘重量","是否绑定托盘","是否带托盘","来料总重量","物料防错方式","允许负偏差比例","最小包装重量","最小包装数量","单位"))),
    MATERIALBINDTO("MaterialBinDTO", new ArrayList<String>(Arrays.asList("物料代码","跨编码"))),
    SUPPLIERINFODTO("SupplierInfoDTO", new ArrayList<String>(Arrays.asList("供应商编码","供应商名称","供应商时间窗口"))),
    AREADTO("AreaDTO",new ArrayList<String>(Arrays.asList("仓库Code","存储区编码","存储区名称"))),
    FRAMEDTO("FrameDTO",new ArrayList<String>(Arrays.asList("区域Code","跨编码","跨名称","宽度","承重"))),
    BINDTO("BinDTO",new ArrayList<String>(Arrays.asList("跨Code","库位编码","库位名称"))),
    MATERIALRECEIVE("MaterialReceive",new ArrayList<String>(Arrays.asList(
            "PlantNb","SSCCNumber","MaterialNb","BatchNb","SupplierNb","StorageLocation","Bin","ExpiryDate","Quantity","Unit","Operation","FromArea","FromProdOrder","FromPurchaseOrder","ToPlant","ToStorage","ToBin","ToProductionOrder","ToDelivery","ToPickingArea","InitialSAPStatus","FinalSAPStatus","Empty","CreationUser","CreationDate","CreationHour","TEKDANTransaction","SAPProcessMethod","SAPMovementType","ReasonCode","SAPDocNumber","ManualReasonCode","ActionType","BlockingNumber","Workcenter","newSSCC","Identification","CustomerNb","TruckDeliveryNb","GateDeliveryNb","InitialFlowStatus","FinalFlowStatus","ToArea","InitialQualityStatus","FinalQualityStatus","InitialPositiveReleaseStatus","FinalPositiveReleaseStatus","LockedByGardian","InitialQuarantineStockStatus","FinalQuarantineStockStatus","GuardianInstrCode","GuardianInstrNb","GuardianReasonCode","NbPlugDerog","Control1","Control2","Control3","Control4","Control5","TareWeight","PlugCount","ControlCode1","ControlCode2","ControlCode3","ControlCode4","ControlCode5","SupplierMeasure","SupplierContainerID","PlugInStation","InitialGuardianStockStatus","FinalGuardianStockStatus","Grade","AuthorizedForOnline","AuthorizedForShipment","InitialRemainingQty","FinalRemainingQty","Release","Description","PlantNb1","SSCCNumber1","SupplierSSCC","SSCCOrigin","CreationMode","SAPMaterialCode","SAPBatchNumber","DefaultQty","AdjustedQty","PartiallyPicked","RemainingQty","UnitOfMeasure","Dluo","Info1","Info2","PalletSupport","Width","Height","Weight","Equiv","PONumber","PONumberItem","ReceptionArea","SAPSupplierNumber","SupplierBatchNumber","VendorBatchReceived","BlockingNumber1","ProductionOrder","Operation1","QualityCommentCode","Sample","R3StockStatus","R3ChangeDate","ReasonCode1","Usable","Empty1","FlowStatus","LabelSticked","ReasonManualChange","ReasonManualEnd","SAPStorageLocation","Area","Bin1","CreationDate1","CreationHour1","ReceptionDate","ReceptionHour","LastPrintDate","LastPrintHour","LastPrintNbCopies","LastPrinterName","LastPrintUser","LastStockMovementDate","LastStockMovementHour","LastStockMovementUser","FirstUsageDate","LastUsageDate","LastTransactionCode","LastTransactionDate","LastTransactionHour","LastUser","LastOperationNumber","retentionSample","QualityFreeText","OriginWeighedSSCC","WeighingTerminal","WeighingArea","Container","GroupPO","ReservationNumber","ReservationItem","POOperation","FinalDeliveryNb","FinalCustomerNb","TruckDeliveryNb1","FinalDeliveryItem","EndingUser","EndingDate","EndingHour","QualityStatus","QuarantineStockStatus","WaitingPositiveRelease","GuardianStockStatus","LockedByGuardian","PositiveReleaseEndDate","PositiveReleaseEndHour","PositiveReleaseUser","ReasonManualCreationBis","CreationUser1","OriginCode","LastGuardianInstrCode","LastGuardianInstrNb","LastGuardianProcessDate","LastGuardianProcessHour","LastGuardianReasonCode","Grade1","AuthorizedForOnline1","AuthorizedForShipment1","EanNb","InfoProdOrder","ReceptionInfo1","InitialBatchNumber","OriginProdOrderNb","OriginWorkCenter","SpecialStock","SupplierContainerID1","OriginProdDate","ReceptionGrossWeight","ReceptionNetWeight","TareWeight1","PlugCount1","NbPlugDerog1","PlugInStatus","NumberControls","ReceptionControl1","ReceptionControl2","ReceptionControl3","ReceptionControl4","ReceptionControl5","Control11","Control21","Control31","Control41","Control51","ControlCode11","ControlCode21","ControlCode31","ControlCode41","ControlCode51","TruckReturnNb","LastControlDate","LastControlHour","LastControlUser","LastPlugInDate","LastPlugInHour","LastPlugOutDate","LastPlugOutHour","PlugInStation1","OriginPurchOrder","PlantNb2","ModuleCode","SAPSupplierNumber1","SupplierDescription","CorrespondingPlant","KeepBatchAsSAPBatch","NoReprintLabel"
    )));

    private String desc;
    private List<String> strings;

    public static List<String>  getValue(String key) {
        for (ClassType ele : values()) {
            if (ele.getDesc().equals(key)) {
                return ele.getCode();
            }
        }
        return null;
    }

    private ClassType(String desc, List<String> strings) {
        this.desc = desc;
        this.strings = strings;
    }

    public String getDesc() {
        return this.desc;
    }

    public List<String> getCode() {
        return this.strings;
    }
}
