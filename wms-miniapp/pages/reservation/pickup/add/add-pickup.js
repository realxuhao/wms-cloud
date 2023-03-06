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
    showCalendar: false,
    selectDateText: '',
    pickupDateList: [],
    minDate: null,
    maxDate: null,
    /** cell list */
    cellList: ["ECN", "NMD", "FSMP"],
    cellIndex: null,
    /** 表单 */
    driverName: '',
    driverPhone: '',
    carNum: '',
    /** 表单验证提示 */
    driverNameError: '',
    driverPhoneError: '',
    carNumError: ''
  },

  cellChange: function (e) {
    this.setData({
      cellIndex: e.detail.value
    })
  },  
  onDisplay() {
    this.setData({ showCalendar: true });
  },
  onClose() {
    this.setData({ showCalendar: false });
  },
  onConfirm(event) {    
    let dateList = event.detail.filter(x=>new Date(x).getFullYear() > 2000)
    this.setData({
      pickupDateList: dateList,
      showCalendar: false,
      selectDateText: `选择了 ${dateList.length} 个日期`,
    });
  },


  submit(){    
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
    if(this.data.cellIndex == null){
      wx.showModal({
        title: '提示',
        content: '请选择车间',
        success: function (res) {
        }
      })
      return;
    }
    if(this.data.pickupDateList.length <= 0){      
      wx.showModal({
        title: '提示',
        content: '请选择取货日期',
        success: function (res) {
        }
      })
      return;
    }
    let allList = [];
    for (let i = 0; i < this.data.pickupDateList.length; i++) {      
      const row = {
        wechatId: app.globalData.openid,
        driverName: this.data.driverName,
        driverPhone: this.data.driverPhone,
        carNum: this.data.carNum,
        cell: this.data.cellList[this.data.cellIndex],
        pickupDate: new Date(this.data.pickupDateList[i].getFullYear(), this.data.pickupDateList[i].getMonth(),this.data.pickupDateList[i].getDate()+1)
      }
      allList = [...allList, row];
    }
    networkAPI._post('vehiclereservation/driverPickup/add', allList).then(res => {
      if(res.code === 200){
        wx.showModal({
          title: '成功',
          showCancel: false,
          content: '新增取货预约单成功',
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


  /**
   * Lifecycle function--Called when page load
   */
  onLoad(options) {
    this.setData({
      minDate: new Date().getTime(),
      maxDate: new Date(new Date().getFullYear(), new Date().getMonth()+2, new Date().getDate()).getTime()
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