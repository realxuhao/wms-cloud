const networkAPI = require('../../utils/NetworkUtils');
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    reservationList: []
  },

  async getTenReservationList(){
    this.reservationList = [];   
    let deliverList = [];
    let pickupList = [];
    await networkAPI._get('vehiclereservation/driverDeliver/info/'+app.globalData.openid, null).then(res => {
      if(res.data){
        deliverList = res.data;
      }                 
    })
    await networkAPI._get('vehiclereservation/driverPickup/info/'+app.globalData.openid, null).then(res => {
      if(res.data){
        pickupList = res.data;
      }                 
    })
  },
  
  dispatchCheckDetail: function (e) {
    var idx = e.currentTarget.dataset.index
    if(idx !== ""){
      wx.navigateTo({
        url: '../flee-goods/details/details?id=' + idx
      })
    }else{  
      wx.navigateTo({
        url: ''
      })
    }

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // this.getTenReservationList();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})