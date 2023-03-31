// pages/reservation/deliver/add/add-deliver.js
import Notify from '@vant/weapp/notify/notify';
import Dialog from '@vant/weapp/dialog/dialog';
const networkAPI = require('../../../../utils/NetworkUtils');
const app = getApp()

Page({

  /**
   * Page initial data
   */
  data: {
    showDialog: true,
    showDate: false,
    showTimeWindow: false,
    timeWindowList: [],
    reserveNo: '',
    reserveId: null,
    /** 供应商&仓库信息 */
    supplierCode: '',
    reserveDate: null,
    timeWindow: null,
    wareId: '',
    wareName: '',
    wareLocation: '',
    wareUser: '',
    wareUserPhone: '',
    /** 司机信息 */
    driverName: '',
    driverPhone: '',
    carNum: '',    
    /** 表单验证提示 */
    driverNameError: '',
    driverPhoneError: '',
    carNumError: '',
    reserveDateError: '',
    timeWindowError: '',
    minDate: null,
    maxDate: null,
  },

  /** 根据供应商预约单号获取供应商预约信息 */
  getSupplierReserve(){
    if(this.data.reserveNo == undefined ||this.data.reserveNo == '' || this.data.reserveNo == null){
      wx.showModal({
        title: '提示',
        showCancel: false,
        content: '未输入供应商预约单号',
        success: function (res) {
          var pages = getCurrentPages()
          var num = pages.length
          wx.navigateBack({
            delta: num
          })
        }
      })      
    }
    const _this = this;
    networkAPI._get('vehiclereservation/supplierReserve/selectByReserveNo/'+this.data.reserveNo, null).then(res => {
      if(res.data){
        this.setData({
          reserveDate: res.data.reserveDate,
          reserveId : res.data.reserveId,
          supplierCode : res.data.supplierCode,
          timeWindow : res.data.timeWindow == "" ? null : res.data.timeWindow,
          wareLocation : res.data.wareLocation,
          wareId: res.data.wareId,
          wareName : res.data.wareName,
          wareUser : res.data.wareUser,
          wareUserPhone : res.data.wareUserPhone
        })
        _this.data.reserveDate = res.data.reserveDate;
        _this.getTimeWindowList();
        _this.getDriverData();
        _this.showDialog = false;
      }else{
        Dialog.alert({
          title: '提示',
          message: res.msg,
        }).then(() => {
          var pages = getCurrentPages()
          var num = pages.length
          wx.navigateBack({
            delta: num
          })
        });
      }           
    })
  },
  
  /** 获取司机信息 */
  async getDriverData(){
    await networkAPI._get('masterdata/driver/black/' + app.globalData.openid).then(res => {
      if(res.data){
        if(res.data.length){
          if(res.data[0].status == 1){
            wx.showModal({
              title: '提示',
              showCancel: false,
              content: '您已进入黑名单，请联系客户确认！',
              success: function (res) {
                var pages = getCurrentPages()
                var num = pages.length
                wx.navigateBack({
                  delta: num
                })
              }
            })
            return;
          }
          this.setData({
            driverName: res.data[0].driverName,
            driverPhone: res.data[0].driverPhone,
            carNum: res.data[0].carNum
          })
        }
      }                 
    })
  },

  submit(){ 
    if(this.data.reserveDate == ""){      
      wx.showModal({
        title: '提示',
        showCancel: false,
        content: '请选择送货日期',
        success: function (res) {
        }
      })
      return;
    }
    if(this.data.timeWindow == null){
      wx.showModal({
        title: '提示',
        showCancel: false,
        content: '请选择送货时间段',
        success: function (res) {
        }
      })
      return;
    }   
    if(this.data.driverName == ""){
      this.setData({
        driverNameError: "请输入姓名"
      })
      return;
    }
    if(this.data.driverPhone == ""){
      this.setData({
        driverPhoneError: "请输入手机号码"
      })
      return;
    }
    if(this.data.carNum == ""){
      this.setData({
        carNumError: "请输入车牌号"
      })
      return;
    }
    const param = {
      reserveNo: this.data.reserveNo,
      wechatId: app.globalData.openid,
      driverName: this.data.driverName,
      driverPhone: this.data.driverPhone,
      carNum: this.data.carNum.toUpperCase(),
      wareId: this.data.wareId,
      reserveDate: this.data.reserveDate,
      timeWindow: this.data.timeWindow
    }
    networkAPI._post('vehiclereservation/driverDeliver/add', param).then(res => {
      if(res.code === 200){
        wx.showModal({
          title: '成功',
          showCancel: false,
          content: '新增送货预约单成功',
          success: function (res) {
            var pages = getCurrentPages()
            var num = pages.length
            wx.navigateBack({
              delta: num
            })
          }
        })
      }else{        
        Dialog.alert({
          title: '提示',
          message: res.msg,
        }).then(() => {
          var pages = getCurrentPages()
          var num = pages.length
          wx.navigateBack({
            delta: num
          })
        });
      }
    })
  },  
  onTimeWindowDisplay() {
    this.setData({ showTimeWindow: true });
  },
  onTimeWindowClose() {
    this.setData({ showTimeWindow: false });
  },
  onClickTimeWindow(event){
    const id = event.currentTarget.dataset.cid;
    const data = this.data.timeWindowList.find(x => x.id == id)
    if(data != undefined){
      if(!data.enable){
        wx.showModal({
          title: '提示',
          showCancel: false,
          content: '该时间段已约满，请重新选择',
          success: function (res) {
          }
        })
        this.setData({
          timeWindow : null
        })
        return
      }
      this.setData({
        timeWindow : data.startTime + "-" + data.endTime,
        showTimeWindow: false
      })
      wx.showModal({
        title: '提示',
        showCancel: false,
        content: '选择时间段成功',
        success: function (res) {
        }
      })
    }else{
      this.setData({
        timeWindow : null
      })
    }
  },
  onDateDisplay() {
    this.setData({ showDate: true });
  },
  onDateClose() {
    this.setData({ showDate: false });
  },
  onDateConfirm(event) {    
    const date = this.formatDate(event.detail)
    this.setData({
      showDate: false,
      reserveDate: date,
      timeWindowList: []
    });
    this.getTimeWindowList();
  },
  async getTimeWindowList(){
    const param ={
      wareId: this.data.wareId,
      reserveDate: this.data.reserveDate
    }
    await networkAPI._get('vehiclereservation/supplierReserve/timewindow', param).then(res => {
      if(res.data){
        let list = res.data;
        list.forEach(item => {
          item['color'] = item.enable ? "#2196F3" : "#F44336"
        })
        this.setData({
          timeWindowList: res.data
        })
      }                 
    })
  },
  
  formatDate(date) {
    var date = new Date(date);
    var YY = date.getFullYear() + '-';
    var MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
    return YY + MM + DD;
  },
  //#region 表单验证提示
  driverNameChange(){
    if(this.data.driverName == ""){
      this.setData({
        driverNameError: "请输入姓名"
      })
    }else{
      this.setData({
        driverNameError: ""
      })
    }
  },
  driverPhoneChange(){
    if(this.data.driverPhone == ""){
      this.setData({
        driverPhoneError: "请输入手机号码"
      })
    }else{
      this.setData({
        driverPhoneError: ""
      })
    }
  },
  carNumChange(){
    if(this.data.carNum == ""){
      this.setData({
        carNumError: "请输入车牌号"
      })
    }else{
      this.setData({
        carNumError: ""
      })
    }
  },
  //#endregion

  onClose(){
    this.showDialog = false;
    var pages = getCurrentPages()
    var num = pages.length
    wx.navigateBack({
      delta: num
    })
  },

  /**
   * Lifecycle function--Called when page load
   */
  onLoad(options) { 
    this.setData({
      minDate: new Date().getTime(),
      maxDate: new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate()+14).getTime()
    })
  },

  /**
   * Lifecycle function--Called when page is initially rendered
   */
  onReady() {

  },

  /**
   * Lifecycle function--Called when page show
   */
  onShow() {

  },

  /**
   * Lifecycle function--Called when page hide
   */
  onHide() {

  },

  /**
   * Lifecycle function--Called when page unload
   */
  onUnload() {

  },

  /**
   * Page event handler function--Called when user drop down
   */
  onPullDownRefresh() {

  },

  /**
   * Called when page reach bottom
   */
  onReachBottom() {

  },

  /**
   * Called when user click on the top right corner to share
   */
  onShareAppMessage() {

  }
})