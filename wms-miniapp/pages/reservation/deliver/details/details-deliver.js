const networkAPI = require('../../../../utils/NetworkUtils');
const app = getApp()
Page({

  /**
   * Page initial data
   */
  data: {
    id: '',
    reserveNo: '',
    deliverData: null,
    supplierData: null,
  },

  async getDetails(){
    this.setData({
      deliverData: null,
      supplierData: null
    })
    const that = this;
    await networkAPI._get('vehiclereservation/driverDeliver/info/'+app.globalData.openid, null).then(res => {
      if(res.data){
        if(res.data.length > 0){
          this.setData({
            deliverData: res.data.find(x => x.deliverId == that.data.id)
          })
        }        
        this.setData({
          hasData: res.data.length > 0
        })
      }                 
    })
    await networkAPI._get('vehiclereservation/supplierReserve/selectByReserveNo/wx/'+this.data.reserveNo, null).then(res => {
      if(res.data){
        this.setData({
          supplierData: res.data
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