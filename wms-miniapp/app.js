//app.js
App({
  onLaunch: function () {
    wx.setEnableDebug({
      enableDebug: true
    })

    var sysInfo = wx.getSystemInfoSync();
    this.globalData._windowWidth = sysInfo.screenWidth;
    this.globalData._windowHeight = sysInfo.screenHeight;
    this.globalData.benchmarkLevel = sysInfo.benchmarkLevel;

    if (sysInfo.system.indexOf("iOS") != -1) {
      this.globalData.isiOSDevice = true
    }

    this.calculateIQA(sysInfo)

    var menuBtnPosition = wx.getMenuButtonBoundingClientRect();
    this.globalData.menuBtnPosition = menuBtnPosition;

    //内存警告
    wx.onMemoryWarning((result) => {
      console.log('onMemoryWarningReceive内存不够啦！！！')
      wx.showToast({
        title: '内存不够',
      })
    })
  },//IQA阈值
  calculateIQA: function (sysInfo) {

    var IQA_Device = 0;
    var IQARnage_Base_Android = 90;
    var IQARnage_Base_iOS = 100;
    var IQARnage_Peek = 120;
    var DEV_Benchmark = sysInfo.benchmarkLevel

    if(DEV_Benchmark == undefined){
      DEV_Benchmark = 1
      this.globalData.benchmarkLevel
    }

    //Android
    if (this.globalData.isiOSDevice == false) {

      if (DEV_Benchmark >= 1 && DEV_Benchmark < 16) {
        //性能过低 不支持
        IQA_Device = '-1'
      } else {
        IQA_Device = IQARnage_Base_Android;
      }
    }

    //iOS
    if (this.globalData.isiOSDevice == true) {

      if (DEV_Benchmark >= 1 && DEV_Benchmark < 13) {
        //性能过低 不支持
        IQA_Device = '-1'
      } else if (DEV_Benchmark <= 30) {
        IQA_Device = IQARnage_Base_iOS;
      } else {
        IQA_Device = IQARnage_Peek;
      }
    }

    this.globalData.IQA_Device = IQA_Device;

  },
  globalData: {
    userInfo: null,
    host: 'https://check1.bosch-smartlife.com',
    frameData: null,
    _windowWidth: '',
    _windowHeight: '',
    menuBtnPosition: null,
    benchmarkLevel: -1,
    IQA_Device: '',
    isiOSDevice: false,
    //动态调整居中取景框的大小（屏幕宽度的百分比）
    _QRAreaRatioW: 0.701
  }
})