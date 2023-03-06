// pages/step/index.js
Page({

  /**
   * Page initial data
   */
  data: {
    steps: [
      {
        text: '【送货预约】',
        desc: '输入供应商提供的预约单号，获取预约信息，填写相关信息进行送货预约',
      },
      {
        text: '【送货签到】',
        desc: '到厂后在现场签到，签到完成后等待仓库安排，收到微信提醒后方可入厂',
      },
      {
        text: '【送货预约列表】',
        desc: '查看已预约未签到的送货预约信息',
      },
      {
        text: '【送货签到列表】',
        desc: '查询最近20条已签到信息',
      }
    ],
    steps2: [
      {
        text: '【取货预约】',
        desc: '填写相关取货预约信息(其中预约时间可以多选)，进行取货预约',
      },
      {
        text: '【取货签到】',
        desc: '到厂后在现场签到，签到完成后等待仓库安排，收到微信提醒后方可入厂',
      },
      {
        text: '【取货预约列表】',
        desc: '查看已预约未签到的取货预约信息',
      },
      {
        text: '【取货签到列表】',
        desc: '查询最近20条已签到信息',
      }
    ],
    active:4

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