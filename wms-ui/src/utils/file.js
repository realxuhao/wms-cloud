import path from 'path'

export const format = (filename) => {
  const ext = path.extname(filename)
  return path.basename(filename, ext) + ext.toLocaleLowerCase()
}

export const download = (blob, filename) => {
  const a = document.createElement('a')
  var url = URL.createObjectURL(blob)
  a.href = url
  a.download = filename
  document.body.appendChild(a)
  a.click()
  setTimeout(function () {
    document.body.removeChild(a)
    window.URL.revokeObjectURL(url)
  }, 0)
}

export const getObjectURL = (file) => {
  var url = null
  if (window.createObjectURL !== undefined) { // basic
    url = window.createObjectURL(file)
  } else if (window.URL !== undefined) { // mozilla(firefox)
    url = window.URL.createObjectURL(file)
  } else if (window.webkitURL !== undefined) { // webkit or chrome
    url = window.webkitURL.createObjectURL(file)
  }
  return url
}
