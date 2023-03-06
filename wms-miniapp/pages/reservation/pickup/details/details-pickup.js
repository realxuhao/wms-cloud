const networkAPI = require('../../../../utils/NetworkUtils');
const app = getApp()
Page({

  /**
   * Page initial data
   */
  data: {
    id: '',
    reserveNo: '',
    pickupData: null,
  },

  async getDetails(){
    this.setData({
      pickupData: null,
    })
    const that = this;
    networkAPI._get('vehiclereservation/driverPickup/info/'+app.globalData.openid, null).then(res => {
      if(res.data){
        if(res.data.length > 0){
          this.setData({
            pickupData: res.data.find(x => x.pickupId == that.data.id)
          })
        }        
        this.setData({
          hasData: res.data.length > 0
        })
      }                 
    })
  },

  /**
   * 取消该预约记录
   */
  cancelPickup(){
    const that = this;
    wx.showModal({
      title: '提示',
      content: '确定要取消该预约信息吗？',
      success: function (res) {
        networkAPI._delete('vehiclereservation/driverPickup/' + that.data.id).then(res => {
          if(res.code === 200){
            wx.showModal({
              title: '成功',
              showCancel: false,
              content: '取消取货预约成功',
              success: function (res) {
                var pages = getCurrentPages()
                var num = pages.length[pages.length - 2]
                wx.navigateBack({
                  delta: num
                })
              }
            })
          }else{        
            wx.showModal({
              title: '提示',
              showCancel: false,
              content: res.msg,
            }).then(() => {
              var pages = getCurrentPages()
              var num = pages.length[pages.length - 2]
              wx.navigateBack({
                delta: num
              })
            });
          }
        })
      }
    })
  },
  

  /**
   * Lifecycle function--Called when page load
   */
  onLoad(options) {
    this.data.id = options.id;
    this.data.reserveNo = options.reserveNo;
    this.getDetails();
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