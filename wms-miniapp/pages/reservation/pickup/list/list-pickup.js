const networkAPI = require('../../../../utils/NetworkUtils');
const app = getApp()

// pages/reservation/pickup.js
Page({

  /**
   * Page initial data
   */
  data: {
    list: [],
    hasData: false
    
  },

  getList(){
    networkAPI._get('vehiclereservation/driverPickup/info/'+app.globalData.openid, null).then(res => {
      if(res.data){
        this.setData({
          list: res.data,
          hasData: res.data.length > 0
        })
      }                 
    })
  },
  
  onDetails(event){    
    let data = this.data.list.find(x => Number(x.pickupId) == Number(event.currentTarget.dataset.cid));
    wx.navigateTo({
      url: '../details/details-pickup?id=' + event.currentTarget.dataset.cid+'&reserveNo='+data.reserveNo
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
    this.getList();

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