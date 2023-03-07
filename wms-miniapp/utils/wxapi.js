/**
 * promise化接口
 */
function wxPromisify(functionName, params) {
  return new Promise((resolve, reject) => {
    wx[functionName]({
      ...params,
      success: res => resolve(res),
      fail: res => reject(res)
    });
  });
}

//https://www.jianshu.com/p/b8343eaa78fd
function uploadIMGtoVerify(IMGfilePath, URLPath, uuidToken) {

  var fileName = uuidToken + '_' + Math.floor(Math.random() * (10000) + 1) + '.jpg'

  let params = {
    url: URLPath,
    filePath: IMGfilePath,
    name: 'img',
    formData: {
      fileName: fileName,
    }
  }

  return wxPromisify('uploadFile', params)

}

function saveIMGToAlbum(filePath) {

  let params = {
    filePath: filePath
  }

  return wxPromisify('saveImageToPhotosAlbum', params)
}

function extrctToken(textContent) {
  let preIndex = textContent.indexOf('uuid=') + 5
  let postIndex = textContent.lastIndexOf('@')

  if (postIndex < 0) {
    postIndex = textContent.length
  }

  let uuid = textContent.substring(preIndex, postIndex)

  return uuid
}

function canvasPutImageData(canvasId,IMGData,width,height){

  let params = {
    canvasId: canvasId,
    x: 0,
    y: 0,
    width: width,
    height: height,
    data: new Uint8ClampedArray(IMGData)
  }

  return wxPromisify('canvasPutImageData', params)
}

function canvasToTempFilePath(canvasId,QRWH,frameW,QRAreaRatioW){

  var x = (frameW - QRWH) * QRAreaRatioW;

  let params = {
    x: x,
    y: 0,
    width: QRWH,
    height: QRWH,
    destWidth: QRWH,
    destHeight: QRWH,
    quality: 0.8,
    canvasId: canvasId,
    fileType: 'jpg'
  }

  return wxPromisify('canvasToTempFilePath', params)
}

function getImageInfo(src){
  let params = {
    src:src
  }
  return wxPromisify('getImageInfo', params)
}

module.exports = {
  uploadIMGtoVerify,
  extrctToken,
  saveIMGToAlbum,
  canvasPutImageData,
  canvasToTempFilePath,
  getImageInfo
}