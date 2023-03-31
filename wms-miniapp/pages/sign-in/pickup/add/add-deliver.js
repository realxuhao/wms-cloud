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
    radio: '1',
    pickupList: [],
    hasData: false,
    selectId: null,
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

  /** 根据供应商预约单号获取供应商预约信息 */
  getPickupList(){
    //查询已预约送货列表
    if(Number(this.data.radio) == 1){
      this.getList();
    }
    //未预约
    if(Number(this.data.radio) == 0){
      this.getDriverData()
    }
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

  getList(){
    this.data.pickupList = [];
    networkAPI._get('vehiclereservation/driverPickup/info/'+app.globalData.openid, null).then(res => {
        if(res.data){
          this.setData({
            pickupList: res.data,
            hasData: res.data.length > 0
          })
          this.showDialog = false;
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
    const param = {
      wechatId: app.globalData.openid,
      driverName: this.data.driverName,
      driverPhone: this.data.driverPhone,
      carNum: this.data.carNum,
      cell: this.data.cellList[this.data.cellIndex]
    }
    networkAPI._post('vehiclereservation/driverPickup/signin', param).then(res => {
      if(res.code === 200){
        wx.showModal({
          title: '成功',
          showCancel: false,
          content: '签到成功，请等待仓库人员分配入厂时间',
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
  cellChange: function (e) {
    this.setData({
      cellIndex: e.detail.value
    })
  },  
  //#endregion

  onSignIn(event){
    const that = this;
    this.setData({
      selectId: event.currentTarget.dataset.cid
    })
    let data = this.data.pickupList.find(x => Number(x.pickupId) == Number(this.data.selectId));
    if(data != undefined){
      wx.showModal({
        title: '提示',
        content: '确认要签到吗？请确认已到厂，提前签到可以会进入黑名单，影响后续预约',
        showCancel: true,//是否显示取消按钮
        cancelColor:'red',//取消文字的颜色
        confirmColor: 'blue',//确定文字的颜色
        success: function (res) {
           if (res.cancel) {
              //点击取消,默认隐藏弹框
           } else {
              //点击确定
              networkAPI._get('vehiclereservation/driverPickup/signin/'+event.currentTarget.dataset.cid, null).then(res => {
                if(res.code === 200){
                  wx.showModal({
                    title: '成功',
                    showCancel: false,
                    content: '签到成功，请等待仓库人员分配入厂时间',
                    success: function (res) {
                      that.getList();
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
              
           }
        },
        fail: function (res) { },//接口调用失败的回调函数
        complete: function (res) { },//接口调用结束的回调函数（调用成功、失败都会执行）
      })
    }
  },

  onRadioChange(event) {
    this.setData({
      radio: event.detail,
    });
  },
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