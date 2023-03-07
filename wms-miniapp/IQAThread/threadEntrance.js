const jsfeat = require('./utils/jsfeat')

//因为不能再worker线程中读取全局变量，所以在此处重复定义比例
var QRAreaRatioW = 0.701
const threshold_brightness_dim = 100
const threshold_brightness_overexposure = 200
let threshold_iqavalue = 300

const msgs = [
  '验证未成功',
  '',
  '无法识别二维码',
  '未注册/未激活',
  '未注册/未激活',
  '未对焦或手抖',
  '二维码过小',
  '倾斜角度过大',
  '光照不均',
  '二维码区域过暗',
  '二维码区域过亮',
  '二维码反光或破损'
]

worker.onMessage(function (res) {
  if(res["IQA"] == null || res["IQA"] == undefined){
    validateQRIMG(res)
  }else{
    threshold_iqavalue = res["IQA"]
  }

})

function calculateMiddleQRArea(qrAreaXY, imgMatrix_U8C4, frameW, frameH) {

  var qrWH = qrAreaXY;

  if (qrWH > frameW) {
    qrWH = frameW;
  }

  var TLY = (frameH - qrWH) / 2;
  var BLY = (frameH + qrWH) / 2;

  var sliceIndex1 = frameW * TLY;
  var sliceIndex4 = frameW * BLY;

  sliceIndex1 = sliceIndex1 * 4;
  sliceIndex4 = sliceIndex4 * 4;

  var newMatrix = imgMatrix_U8C4.slice(sliceIndex1, sliceIndex4);

  //返回去掉头和尾的图像，此图像为长方形，还没有剔除两边非二维码区域，这亚航做只是为了使用一次silce提高性能
  return new Uint8ClampedArray(newMatrix);
}

/**
 * 检测Frame的入口
 * @param {*} frame 
 */
function validateQRIMG(frame) {

  console.log('👌开始处理')

  var timestamp1 = new Date().getTime();

  let frameWidth = frame.width;
  let frameHeight = frame.height;

  var qrAreaXY = Math.floor(QRAreaRatioW * frameWidth);
  
  var u8CA = calculateMiddleQRArea(qrAreaXY,frame.data,frameWidth,frameHeight);
  frameHeight = qrAreaXY;
  frame = null;

  //灰度图
  var img_u8C1_gray = new jsfeat.matrix_t(frameWidth, frameHeight, jsfeat.U8_t | jsfeat.C1_t);
  jsfeat.imgproc.grayscale(u8CA, frameWidth, frameHeight, img_u8C1_gray, jsfeat.COLOR_RGBA2GRAY);

  //QR中心区域
  let centralQRQrea = calculateCentralQRArea(qrAreaXY, img_u8C1_gray, frameWidth, frameHeight);

  //亮度
  var brightness = brightnessCheck(centralQRQrea)

  if (brightness < threshold_brightness_dim) {
    saySomethingBacktoUIThread(-1,"IQA:-1"+'亮度:'+brightness, 9);
    return
  }

  if (brightness > threshold_brightness_overexposure) {
    saySomethingBacktoUIThread(-1,"IQA:-1"+'亮度:'+brightness, 10);
    return
  }

  //Sobel检验
  var img_gxgy = sobel(qrAreaXY, centralQRQrea)

  let IQARst = IQAResult(img_gxgy);

  // let IQARst = 300

  var timestamp7 = new Date().getTime();

  saySomethingBacktoUIThread(IQARst,"IQA:" + IQARst + "用时:" + (timestamp7 - timestamp1)+'亮度:'+brightness, IQAStateCode(brightness,IQARst));

  //测试 释放资源
  img_u8C1_gray = null
  centralQRQrea = null
  img_gxgy = null
}

function brightnessCheck(imageArr) {
  var total = 0;
  for (var index = 0; index < imageArr.length; index++){
    total += imageArr[index]
  }
  var avg = total / imageArr.length
  return Math.floor(avg)
}

function sobel(qrAreaXY, centralQRQrea) {
  var roi_U8C1 = new jsfeat.matrix_t(qrAreaXY, qrAreaXY, jsfeat.U8C1_t);
  roi_U8C1.data = Uint8Array.from(centralQRQrea);
  var img_gxgy = new jsfeat.matrix_t(qrAreaXY, qrAreaXY, jsfeat.S32C2_t);
  jsfeat.imgproc.sobel_derivatives(roi_U8C1, img_gxgy);
  return img_gxgy;
}

function IQAStateCode(brightness,iqavalue) {
  /*
  CODE
MESSAGE
0
验证未成功（假）
1
验证成功（真）
2
无法识别二维码
3
未注册/未激活
4
未注册/未激活
5
未对焦或手抖
6
二维码过小
7
倾斜角度过大
8
光照不均
9
二维码区域过暗
10
二维码区域过亮
11
二维码反光或破损
    */
  var stateCode = 1

  if (brightness < threshold_brightness_dim) {
    stateCode = 9
    return stateCode
  }

  if (brightness > threshold_brightness_overexposure) {
    stateCode = 10
    return stateCode
  }

  if (iqavalue < threshold_iqavalue) {
    stateCode = 5
    return stateCode
  }

  return stateCode
}

/**
 * 计算灰度图中的二维码，返回GRAY一维数组
 * @param {*} qrAreaXY 二维码矩形框的边长
 * @param {*} grayImgMatrix_U8C1 
 * @param {*} frameW 
 * @param {*} frameH 
 */
function calculateCentralQRArea(qrAreaXY, grayImgMatrix_U8C1, frameW, frameH) {

  var qrWH = qrAreaXY;

  if (qrWH > frameW) {
    qrWH = frameW;
  }

  var TLX = (frameW - qrWH) / 2;
  var TLY = (frameH - qrWH) / 2;
  var TRX = (frameW + qrWH) / 2;
  var TRY = TLY
  var BLX = TLX;
  var BLY = (frameH + qrWH) / 2;
  var BRX = TRX;
  var BRY = BLY;

  var sliceIndex1 = frameW * (TLY - 1) + TLX;
  var sliceIndex2 = sliceIndex1 + qrWH;
  var sliceIndex3 = frameW * (BLY - 1) + BLX;
  var sliceIndex4 = sliceIndex3 + qrWH;

  //灰度之后的图进行切割 C1
  var rowStartIndex = sliceIndex1;
  var tmpROIArr = [];

  for (var i = 0; i < grayImgMatrix_U8C1.data.length; i++) {

    if (i < sliceIndex1 || i > sliceIndex4) {
      continue;
    }

    if (Math.floor(i / frameW) > Math.floor(rowStartIndex / frameW)) {
      //下一行
      rowStartIndex = rowStartIndex + frameW;
    }

    if (i > rowStartIndex && i < rowStartIndex + qrWH) {
      tmpROIArr.push(grayImgMatrix_U8C1.data[i]);
    }
  }

  return tmpROIArr;
}

/**
 * 返回UI主线程
 * @param {*} msg 
 */
function saySomethingBacktoUIThread(iqaValue,msg, iqaStateCode) {
  worker.postMessage({
    iqaValue:iqaValue,
    statecode: iqaStateCode,
    msg: msgs[iqaStateCode],
    testmsg: msg
  })
}

/**
 * XY轴的梯度向量合并
 * @param {*} img_gxgy 
 */
function IQAResult(img_gxgy) {

  var gxgyArr = img_gxgy.data;
  var arrLength = gxgyArr.length;
  var IQARst = 0;
  for (var i = 0; i < arrLength / 2; i++) {
    var gxIndex = 2 * i;
    var gyIdnex = 2 * i + 1;

    var gx = gxgyArr[gxIndex];
    var gy = gxgyArr[gyIdnex];

    IQARst = IQARst + Math.sqrt(gx * gx + gy * gy)
  }

  IQARst = IQARst / (1.0 * img_gxgy.cols * img_gxgy.rows)

  return Math.floor(IQARst);
}

/**
 * 使用JS计算途中二维码的坐标(性能问题，暂时弃用)
 */
// function QRPosition(u8CA, frameWidth, frameHeight) {
//   var timestamp1 = new Date().getTime();
//   var code = jsQR(u8CA, frameWidth, frameHeight, "dontInvert");

//   console.log('JSQR用时:' + (timestamp2 - timestamp1))

//   if (code == undefined || code == null) {
//     console.log('无二维码')
//     saySomethingBacktoUIThread('无二维码', 2)
//     return
//   }

//   const BLC = code.location.bottomLeftCorner;
//   const BRC = code.location.bottomRightCorner;
//   const TLC = code.location.topLeftCorner;
//   const TRC = code.location.topRightCorner;

//   //计算最小外接矩阵
//   let W = Math.max(TRC.x, BRC.x) - Math.min(TLC.x, BLC.x);
//   let H = Math.max(BLC.y, BRC.y) - Math.min(TLC.y, TRC.y);

//   let recArea = W * H;

//   //最大二维码面积
//   let maxQRRect = Math.max(W, H) * Math.max(W, H);

//   //倾斜阈值
//   let percentage = 0.95;

//   let ratio = 1.0 * recArea / maxQRRect;

//   code = null;
//   var timestamp2 = new Date().getTime();

//   if (ratio <= percentage) {
//     console.log("倾斜过大")
//     saySomethingBacktoUIThread("倾斜过大", 3)
//   } else {
//     saySomethingBacktoUIThread('JSQR用时:' + (timestamp2 - timestamp1), 0)
//   }
// }