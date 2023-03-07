// pages/sign-in/deliver/list/list-deliver.js
const networkAPI = require('../../../../utils/NetworkUtils');
const app = getApp()

Page({

  /**
   * Page initial data
   */
  data: {
    hasData: false,
    list: []
  },

  getList(){
    this.data.list = [];
    const param = {
      wechatId : app.globalData.openid,
      status: 1,
      pageSize: 20
    }
    networkAPI._get('vehiclereservation/driverDeliver/list', param).then(res => {
        if(res.data){
          this.setData({
            list: res.data.rows,            
            hasData: res.data.rows.length > 0
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
  /**
   * Lifecycle function--Called when page load
   */
  onLoad(options) {
    this.getList();
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