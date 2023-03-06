const app = getApp()
var _windowWidth = app.globalData._windowWidth
var _windowHeight = app.globalData._windowHeight
var stepClear = 1;
var QRAreaRatioW = app.globalData._QRAreaRatioW

Component({
  lifetimes: {
    ready: function () {
      // 在组件实例进入页面节点树时执行
      this.__proto__.initCanvas(this)
    }
  },
  /**
   * 组件的属性列表
   */
  properties: {

  },

  /**
   * 组件的初始数据
   */
  data: {

  },

  /**
   * 组件的方法列表
   */
  methods: {
    initCanvas: function (context) {

      var ctx = wx.createCanvasContext('frameCanvas', context)

      //渐变
      var grd = ctx.createLinearGradient(0, 0, 0, _windowHeight);
      grd.addColorStop(0, 'rgba(0, 0, 0, 1)');
      grd.addColorStop(0.3, 'rgba(0, 0, 0, 0.8)');
      grd.addColorStop(1, 'rgba(0, 0, 0, 0.7)');
      ctx.setFillStyle(grd);
      ctx.fillRect(0, 0, _windowWidth, _windowHeight);

      //圆点蒙版
      ctx.globalAlpha = 0.1
      ctx.beginPath()
      ctx.drawImage('../../img/dot_texture.jpeg', 0, 0, _windowWidth, _windowHeight,0, 0, _windowWidth, _windowHeight)
      ctx.closePath()
      ctx.save()
      ctx.globalAlpha = 1

      var rectW = _windowWidth * QRAreaRatioW
      var rectH = rectW
      var rectR = 16
      var rectX = (_windowWidth - rectW) / 2
      var rectY = (_windowHeight - rectH) / 2

      //镂空中心区域
      //4个圆形
      this.clearArc(ctx, rectX + rectR, rectY + rectR, rectR)
      stepClear = 1;
      this.clearArc(ctx, rectX + rectW - rectR, rectY + rectR, rectR)
      stepClear = 1;
      this.clearArc(ctx, rectX + rectR, rectY + rectH - rectR, rectR)
      stepClear = 1;
      this.clearArc(ctx, rectX + rectW - rectR, rectY + rectH - rectR, rectR)
      stepClear = 1;

      ctx.clearRect(rectX, rectY + rectR, rectW, rectH - rectR * 2)
      ctx.clearRect(rectX + rectR, rectY, rectW - rectR * 2, rectH)
      ctx.draw()

      //画取景框
      let color = '#FFC92F'
      let QRColor = 'rgba(255,201,47,0.8)'
      this.roundRect(ctx, rectX, rectY, rectW, rectH, rectR, color)
      
      ctx.draw(true)
    },

    clearArc: function (context, x, y, radius) { //圆心(x,y)，半径radius
      var calcWidth = radius - stepClear;
      var calcHeight = Math.sqrt(radius * radius - calcWidth * calcWidth);

      var posX = x - calcWidth;
      var posY = y - calcHeight;

      var widthX = 2 * calcWidth;
      var heightY = 2 * calcHeight;

      if (stepClear <= radius) {
        context.clearRect(posX, posY, widthX, heightY);
        stepClear += 1;
        this.clearArc(context, x, y, radius);
      }
    },

    roundRect: function (ctx, x, y, w, h, r, color) { //绘制圆角矩形(无填充色))

      ctx.save();
      if (w < 2 * r) {
        r = w / 2;
      }
      if (h < 2 * r) {
        r = h / 2;
      }
      ctx.beginPath();
      ctx.setFillStyle("#ccc");
      ctx.setStrokeStyle(color);
      ctx.setFillStyle("#ccc");
      ctx.setLineWidth(2);
      ctx.setFillStyle("#ccc");
      ctx.moveTo(x + r, y);
      ctx.arcTo(x + w, y, x + w, y + h, r);
      ctx.arcTo(x + w, y + h, x, y + h, r);
      ctx.arcTo(x, y + h, x, y, r);
      ctx.arcTo(x, y, x + w, y, r);

      ctx.stroke();
      ctx.closePath();

      ctx.draw(true);
    },

    roundRectColor(context, x, y, w, h, r, color) { //绘制圆角矩形（纯色填充）
      context.save();
      // context.setFillStyle(color);
      context.setStrokeStyle(color)
      context.setLineJoin('round'); //交点设置成圆角
      context.setLineWidth(r);
      context.strokeRect(x + r / 2, y + r / 2, w - r, h - r);
      context.fillRect(x + r, y + r, w - r * 2, h - r * 2);
      context.stroke();
      context.closePath();
      context.draw(true);
    }
  }

})