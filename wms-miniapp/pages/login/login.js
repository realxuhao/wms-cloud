// pages/login/login.js
const app = getApp()
const networkAPI = require('../../utils/NetworkUtils')
var util = require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  start(){ 
    this.getOpenId()
  },

  getOpenId(){
    var that = this;
    // 获取用户的当前设置，判断是否点击了“总是保持以上，不在询问”
    wx.getSetting({
      withSubscriptions: true, // 是否获取用户订阅消息的订阅状态，默认false不返回
      success(res) {
        console.log('res.authSetting', res.authSetting)
        if (res.authSetting['scope.subscribeMessage']) {
          console.log('用户点击了“总是保持以上，不再询问”')
        } else {
          console.log('用户没有点击“总是保持以上，不再询问”则每次都会调起订阅消息')
          //因为没有选择总是保持，所以需要调起授权弹窗再次授权
          that.authorizationBtn();
        }
      }
    })
  },

  // 授权
  authorizationBtn() {
    wx.requestSubscribeMessage({
      tmplIds: ['w5_f3Ez3R3kEqHGJ-7C8-ZcNUY6aell5Qyg9cHW1fy4'],
      success(res) {
        console.log('授权成功')
        wx.login({
          success (res) {
            if (res.code) {
              var param = {code: res.code}
              networkAPI._get('auth/wx/getOpenId/'+res.code, null).then(res => {
                if(res.data){
                  wx.setStorageSync('token', res.data.access_token)
                  app.globalData.openid = res.data.open_id          
                  wx.switchTab({
                    url: '../main-page/main-page'
                  })        
                }                 
              })
            } else {
              console.log('登录失败！' + res.errMsg)
            }
          }
        })
      }
    })
  },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {


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