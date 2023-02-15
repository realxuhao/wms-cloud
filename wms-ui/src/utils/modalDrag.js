import Vue from 'vue'

/* 调用示例
<a-modal v-drag-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
  <img alt="example" style="width: 100%" :src="previewImage" />
</a-modal> */

// v-drag-modal: 弹窗拖拽
Vue.directive('drag-modal', (el, binding, vnode, oldVnode) => {
  Vue.nextTick(() => {
    // eslint-disable-next-line no-unused-vars
    const { visible, destroyOnClose } = vnode.componentInstance
    if (!visible) return
    const isThemeModal = el.classList.contains('grid-theme')
    const dialogHeaderEl = isThemeModal ? el.querySelector('.ant-tabs-bar') : el.querySelector('.ant-modal-header')
    const dragDom = isThemeModal ? el.querySelector('.ant-modal') : el.querySelector('.ant-modal')
    // dialogHeaderEl.style.cursor = 'move';
    dialogHeaderEl.style.cssText += ';cursor:move;'
    // dragDom.style.cssText += ';top:0px;'

    // 获取原有属性 ie dom元素.currentStyle 火狐谷歌 window.getComputedStyle(dom元素, null);
    const sty = (function () {
      if (window.document.currentStyle) {
        return (dom, attr) => dom.currentStyle[attr]
      } else {
        return (dom, attr) => getComputedStyle(dom, false)[attr]
      }
    })()
    dialogHeaderEl.onmousedown = (e) => {
      // 禁止选中文字，防止拖拽时弹框粘鼠标
      document.onselectstart = function () {
        return false
      }
      // 鼠标按下，计算当前元素距离可视区的距离
      const disX = e.clientX - dialogHeaderEl.offsetLeft
      const disY = e.clientY - dialogHeaderEl.offsetTop

      const screenWidth = document.body.clientWidth // body当前宽度
      const screenHeight = document.documentElement.clientHeight // 可见区域高度(应为body高度，可某些环境下无法获取)
      const dragDomWidth = dragDom.offsetWidth // 对话框宽度
      const dragDomheight = dragDom.offsetHeight // 对话框高度

      const minDragDomLeft = dragDom.offsetLeft
      const maxDragDomLeft = screenWidth - dragDom.offsetLeft - dragDomWidth - (isThemeModal ? 10 : 0)

      const minDragDomTop = dragDom.offsetTop
      const maxDragDomTop = screenHeight - dragDom.offsetTop - dragDomheight - (isThemeModal ? 10 : 0)
      // 获取到的值带px 正则匹配替换
      let styL = sty(dragDom, 'left')
      let styT = sty(dragDom, 'top')

      // 注意在ie中 第一次获取到的值为组件自带50% 移动之后赋值为px
      if (styL.includes('%')) {
        // eslint-disable-next-line no-useless-escape
        styL = +document.body.clientWidth * (+styL.replace(/\%/g, '') / 100)
        // eslint-disable-next-line no-useless-escape
        styT = +document.body.clientHeight * (+styT.replace(/\%/g, '') / 100)
      } else {
        styL = +styL.replace(/\px/g, '')
        styT = +styT.replace(/\px/g, '')
      };
      // dialogHeaderEl 此处直接使用el 防止鼠标移动太快脱离范围，导致无法拖动
      el.onmousemove = function (e) {
        // 通过事件委托，计算移动的距离
        let left = e.clientX - disX
        let top = e.clientY - disY
        // 边界处理
        if (-(left) > minDragDomLeft) {
          left = -(minDragDomLeft)
        } else if (left > maxDragDomLeft) {
          left = maxDragDomLeft
        }

        if (-(top) > minDragDomTop) {
          top = -(minDragDomTop)
        } else if (top > maxDragDomTop) {
          top = maxDragDomTop
        }
        // 移动当前元素
        dragDom.style.cssText += `;left:${left + styL}px;top:${top + styT}px;`
      }

      el.onmouseup = function (e) {
        el.onmousemove = null
        dialogHeaderEl.onmouseup = null
      }

      document.onmouseup = function (e) {
        el.onmousemove = null
        dialogHeaderEl.onmouseup = null
        // 在抬起鼠标之后，取消禁用选择文字
        document.onselectstart = function () {
          return true
        }
      }
    }
  })
})
