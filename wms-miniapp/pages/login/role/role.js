// pages/login/role/role.js
const app = getApp()
const networkAPI = require('../../../utils/NetworkUtils')
var util = require('../../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    roleList: null,
    roleName: null,
    roleIndex: null,
    validTime: [
      {value: '1', name: '一个月'},
      {value: '6', name: '六个月'},
      {value: '12', name: '一年'},
      {value: '0', name: '永久有效'}
    ],
    isPermissionGranted: false,
    role: "",
    time: "",
    nowDate: null

  },
  
  submit(){
    if(this.data.role === ""){
      wx.showModal({
        title: '提示',
        showCancel: false,
        content: '请选择一个角色'
      })
      return;
    }
    if(this.data.time === ""){
      wx.showModal({
        title: '提示',
        showCancel: false,
        content: '请选择角色有效期'
      })
      return;
    }
    console.log("确定选择角色：", this.data.role)
    console.log("确定选择时间：", this.data.time)
    this.saveLogin()
  },

  

  saveLogin(){
    var that = this;
    
    var date = util.formatTime(new Date());
    date = date.replaceAll("/","-")
    if((this.data.time + " 23:59:59") < date){
      wx.showModal({
        title: '提示',
        showCancel: false,
        content: '请选择正确的日期'
      })
      return;
    }

    var param = {
      "openid": app.globalData.userInfo,
      "mpRoleId" :this.data.roleIndex,
      "name" : this.data.role,
      "dueToTime" : this.data.time + " 23:59:59"
    }
    networkAPI._post('mpuser', param).then(res => {        
      console.log(res)

      if (res.code == 200) {
        that.dispatchToiMainPage();
      } else {
        that.setData({
          error: res.msg
        })
      }

    }).catch(e => {
      console.log(e)
      that.setData({
        error: e.errMsg
      })
    });
  },
  
  dispatchToiMainPage: function () {
    wx.switchTab({
      url: '../../main-page/main-page'
    })
  },
  selectRole(e){
    this.data.role = this.data.roleName[e.detail.value]
    this.setData({
      roleIndex : e.detail.value
    })
  },
  selectTime(e){
    this.setData({
      time : e.detail.value
    })
  },
  permissionCallback: function (res) {
    this.checkPermission();
  },
  requestPermission: function () {
    var that = this;
    wx.authorize({
      scope: 'scope.camera',
      complete() {
        that.checkPermission();
      }
    })
    wx.authorize({
      scope: 'scope.userInfo',
      complete() {
        that.checkPermission();
      }
    })
    wx.authorize({
      scope: 'scope.userLocation',
      complete() {
        that.checkPermission();
      }
    })
    wx.authorize({
      scope: 'scope.userLocationBackground',
      complete() {
        that.checkPermission();
      }
    })
    wx.authorize({
      scope: 'scope.writePhotosAlbum',
      complete() {
        that.checkPermission();
      }
    })
  },
  
  checkPermission: function () {
    var that = this;

    wx.getSetting({
      success(res) {
        // console.log(res.authSetting)
        var isPermissionGrantedTmp = true;
        if (res.authSetting["scope.camera"] == false) {
          isPermissionGrantedTmp = false;
        }

        if (res.authSetting["scope.userLocationBackground"] == false) {
          isPermissionGrantedTmp = false;
        }

        if (res.authSetting["scope.userLocation"] == false) {
          isPermissionGrantedTmp = false;
        }

        if (res.authSetting["scope.writePhotosAlbum"] == false) {
          isPermissionGrantedTmp = false;
        }

        that.setData({
          isPermissionGranted: isPermissionGrantedTmp
        })
      }
    })

  },

  getRoleName(){
    var that = this
    networkAPI._get('mpuser/roleList', null).then(res => {        
      console.log(res)

      if (res.code == 200) {
        that.data.roleList = res.rows;
        var role = [];
        for(var i = 0; i < res.rows.length; i++){
          role.push(res.rows[i].dictLabel)
        }
        that.setData({
          roleName: role
        })
      } else {
        that.setData({
          error: res.msg
        })
      }}).catch(e => {
        console.log(e)
        that.setData({
          error: e.errMsg
        })
      });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getRoleName()
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
    this.checkPermission()
    this.requestPermission()

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