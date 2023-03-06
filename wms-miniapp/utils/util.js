const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}

const extraTokenFromContent = textContent =>{

  var uuidIndex = textContent.indexOf('uuid=')
  if(uuidIndex == -1){
    return
  }

  let preIndex = textContent.indexOf('uuid=') + 5
  let postIndex = textContent.lastIndexOf('@')

  if(postIndex == -1){
    return
  }

  if (postIndex < 0) {
    postIndex = textContent.length
  }

  let uuid = textContent.substring(preIndex, postIndex)

  return uuid
}

module.exports = {
  formatTime: formatTime,
  extraTokenFromContent:extraTokenFromContent
}
