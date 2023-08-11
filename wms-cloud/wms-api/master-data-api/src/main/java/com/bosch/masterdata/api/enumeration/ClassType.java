package com.bosch.masterdata.api.enumeration;

import org.apache.ibatis.javassist.runtime.Desc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ClassType {
    MATERIALDTO("MaterialDTO", new ArrayList<String>(Arrays.asList("物料代码","物料名称","物料类型","托盘类型","来料总重量（每托）","托盘重量\n[只针对称重物料]"
            ,"IQC Plan\n[Y/N]",
            "物料防错方式\n[点数,称重,免检]","最小包装毛重\n[只针对称重物料]","最小包装数量","单位\n[KG,M,L,㎡]","最小包装净重\n[只针对称重物料]","最小包装量\n允许最小值",
            "最小包装量\n允许最大值","标准计数单位[L,Kg,m,㎡]对应的重量值[只针对称重物料]"))),
    MATERIALEXCELDTO("MaterialDTO", new ArrayList<String>(Arrays.asList("物料代码","物料名称","物料类型","托盘类型","来料总重量（每托）","托盘重量\n[只针对称重物料]"
            ,"IQC Plan\n[Y/N]",
            "物料防错方式\n[点数,称重,免检]","最小包装毛重\n[只针对称重物料]","最小包装数量","单位\n[KG,M,L,㎡]","最小包装净重\n[只针对称重物料]","最小包装量\n允许最小值",
            "最小包装量\n允许最大值","标准计数单位[L,Kg,m,㎡]对应的重量值[只针对称重物料]"))),
    MATERIALBINDTO("MaterialBinDTO", new ArrayList<String>(Arrays.asList("物料代码","优先级","可用跨编码","备注"))),
    SUPPLIERINFODTO("SupplierInfoDTO", new ArrayList<String>(Arrays.asList("供应商编码","供应商名称","供应商时间窗口"))),
    AREADTO("AreaDTO",new ArrayList<String>(Arrays.asList("工厂编码","工厂描述","仓库编码","仓库描述","存储区编码","存储区名称"))),
    FRAMEDTO("FrameDTO",new ArrayList<String>(Arrays.asList("存储区编码","跨编码","跨类型编码","跨名称","宽度[CM]","承重[KG]","高度[CM]","跨类型编码（暂定）"))),
    BINDTO("BinDTO",new ArrayList<String>(Arrays.asList("跨Code","库位编码","库位描述[选填]"))),
    MATERIALRECEIVE("MaterialReceive",new ArrayList<String>(Arrays.asList(
            "PlantNb","SSCCNumber","MaterialNb","BatchNb","SupplierNb","StorageLocation","Bin","ExpiryDate","Quantity","Unit","Operation","FromArea","FromProdOrder","FromPurchaseOrder","ToPlant","ToStorage","ToBin","ToProductionOrder","ToDelivery","ToPickingArea","InitialSAPStatus","FinalSAPStatus","Empty","CreationUser","CreationDate","CreationHour","TEKDANTransaction","SAPProcessMethod","SAPMovementType","ReasonCode","SAPDocNumber","ManualReasonCode","ActionType","BlockingNumber","Workcenter","newSSCC","Identification","CustomerNb","TruckDeliveryNb","GateDeliveryNb","InitialFlowStatus","FinalFlowStatus","ToArea","InitialQualityStatus","FinalQualityStatus","InitialPositiveReleaseStatus","FinalPositiveReleaseStatus","LockedByGardian","InitialQuarantineStockStatus","FinalQuarantineStockStatus","GuardianInstrCode","GuardianInstrNb","GuardianReasonCode","NbPlugDerog","Control1","Control2","Control3","Control4","Control5","TareWeight","PlugCount","ControlCode1","ControlCode2","ControlCode3","ControlCode4","ControlCode5","SupplierMeasure","SupplierContainerID","PlugInStation","InitialGuardianStockStatus","FinalGuardianStockStatus","Grade","AuthorizedForOnline","AuthorizedForShipment","InitialRemainingQty","FinalRemainingQty","Release","Description","PlantNb1","SSCCNumber1","SupplierSSCC","SSCCOrigin","CreationMode","SAPMaterialCode","SAPBatchNumber","DefaultQty","AdjustedQty","PartiallyPicked","RemainingQty","UnitOfMeasure","Dluo","Info1","Info2","PalletSupport","Width","Height","Weight","Equiv","PONumber","PONumberItem","ReceptionArea","SAPSupplierNumber","SupplierBatchNumber","VendorBatchReceived","BlockingNumber1","ProductionOrder","Operation1","QualityCommentCode","Sample","R3StockStatus","R3ChangeDate","ReasonCode1","Usable","Empty1","FlowStatus","LabelSticked","ReasonManualChange","ReasonManualEnd","SAPStorageLocation","Area","Bin1","CreationDate1","CreationHour1","ReceptionDate","ReceptionHour","LastPrintDate","LastPrintHour","LastPrintNbCopies","LastPrinterName","LastPrintUser","LastStockMovementDate","LastStockMovementHour","LastStockMovementUser","FirstUsageDate","LastUsageDate","LastTransactionCode","LastTransactionDate","LastTransactionHour","LastUser","LastOperationNumber","retentionSample","QualityFreeText","OriginWeighedSSCC","WeighingTerminal","WeighingArea","Container","GroupPO","ReservationNumber","ReservationItem","POOperation","FinalDeliveryNb","FinalCustomerNb","TruckDeliveryNb1","FinalDeliveryItem","EndingUser","EndingDate","EndingHour","QualityStatus","QuarantineStockStatus","WaitingPositiveRelease","GuardianStockStatus","LockedByGuardian","PositiveReleaseEndDate","PositiveReleaseEndHour","PositiveReleaseUser","ReasonManualCreationBis","CreationUser1","OriginCode","LastGuardianInstrCode","LastGuardianInstrNb","LastGuardianProcessDate","LastGuardianProcessHour","LastGuardianReasonCode","Grade1","AuthorizedForOnline1","AuthorizedForShipment1","EanNb","InfoProdOrder","ReceptionInfo1","InitialBatchNumber","OriginProdOrderNb","OriginWorkCenter","SpecialStock","SupplierContainerID1","OriginProdDate","ReceptionGrossWeight","ReceptionNetWeight","TareWeight1","PlugCount1","NbPlugDerog1","PlugInStatus","NumberControls","ReceptionControl1","ReceptionControl2","ReceptionControl3","ReceptionControl4","ReceptionControl5","Control11","Control21","Control31","Control41","Control51","ControlCode11","ControlCode21","ControlCode31","ControlCode41","ControlCode51","TruckReturnNb","LastControlDate","LastControlHour","LastControlUser","LastPlugInDate","LastPlugInHour","LastPlugOutDate","LastPlugOutHour","PlugInStation1","OriginPurchOrder","PlantNb2","ModuleCode","SAPSupplierNumber1","SupplierDescription","CorrespondingPlant","KeepBatchAsSAPBatch","NoReprintLabel"
    ))),
    MATERIALCALL("MaterialCallDTO", new ArrayList<String>(Arrays.asList("订单号","物料代码","物料名称","数量","单位","备注"))),
    NMDDTO("NmdDTO", new ArrayList<String>(Arrays.asList("料号","分类","检验水平级别(s-1,s-2,s-3,s-4,Ⅰ,Ⅱ,Ⅲ)","抽样方案（1：正常,2：加严,3：放宽）","AQL"))),
    ECNDTO("EcnDTO", new ArrayList<String>(Arrays.asList("料号","分类","TTS取样规则"))),
    FSMPDTO("FsmpDTO", new ArrayList<String>(Arrays.asList("料号","取样方式"))),
    IQCDTO("IQCDTO", new ArrayList<String>(Arrays.asList("PlantNb","SSCCNumber","FinalSAPStatus","SAPProcessStatus","Identification"))),
    MdProductPackagingDTO("MdProductPackagingDTO", new ArrayList<String>(Arrays.asList("成品料号","Cell","名称","运输单位(Tr)","箱 Tr 对应包装规格","标准 Tr/托","重量(Tr)","体积 (Tr)","类别"))),
    PRODUCTRECEIVEDTO("ProductReceiveDTO", new ArrayList<String>(Arrays.asList("PlantNb","SSCCNumber","MaterialNb","ExpiryDate","Quantity","Unit","FromProdOrder"))),
    SHIPPINGPLANDTO("ShippingPlanImpDTO", new ArrayList<String>(Arrays.asList("Shipping  Mark","ETO PO","ETO PLANT","stock movement    移库日期","Country","Prod-order","Qty","是否拆托","TR","SAP Code","Pallet Quantity","after packing"))),
    PRODUCTFRAMEDTO("ProductFrameDTO", new ArrayList<String>(Arrays.asList("成品代码","优先级","可用跨编码","备注"))),
    RMCOMPARISONEXCELDTO("RmComparisonExcelDTO", new ArrayList<String>(Arrays.asList("PlantNb","SSCCNumber","SAPMaterialCode","SAPBatchNumber","RemainingQty","UnitOfMeasure","R3StockStatus"))),
    PROCOMPARISONDTO("ProComparisonDTO", new ArrayList<String>(Arrays.asList("Plant","Material Description","Material","Unrestricted","Base Unit of Measure","Storage Location","Batch","In Quality Insp.","Restricted-Use Stock","Blocked","Returns","Stock in Transit","In transfer (plant)"))),
    INITSTOCKDTO("InitStockDTO",new ArrayList<String>(Arrays.asList("PlantNb","SSCCNumber","SAPMaterialCode","SAPBatchNumber","DefaultQty","RemainingQty","Dluo","SAPStorageLocation","Area","Bin","PONumber","R3StockStatus"))),
    SPDNDTO("SPDNDTO",new ArrayList<String>(Arrays.asList("External Ref","Plant","DeliveryNb","Ship_To","Ship_Date","Vendor Code","SSCCNumber","Delivery_Item","Material","Batch","Qty","UoM","StorageLocation","ProdBatch"))),
    SUDNDTO("SUDNDTO",new ArrayList<String>(Arrays.asList("Delivery","Plant","Ship-To Party","Name of the ship-to party","Deliv. date(From/to)","Item","Material","Batch","Delivery quantity","Sales Unit","Production batch")));

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
