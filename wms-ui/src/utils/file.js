import path from 'path'

const mapIcon = new Map()
mapIcon.set('.pdf', 'file-pdf')
mapIcon.set('.doc', 'file-word')
mapIcon.set('.docx', 'file-word')
mapIcon.set('.xls', 'file-excel')
mapIcon.set('.xlsx', 'file-excel')
mapIcon.set('.jpg', 'picture')
mapIcon.set('.jpeg', 'picture')
mapIcon.set('.png', 'picture')
mapIcon.set('.txt', 'file-text')
mapIcon.set('.zip', 'copy')
mapIcon.set('.html', 'file-text')
mapIcon.set('.htm', 'file-text')
mapIcon.set('.json', 'file-text')

const mapType = new Map()
mapType.set('.pdf', 'pdf')
mapType.set('.png', 'img')
mapType.set('.jpg', 'img')
mapType.set('.jpeg', 'img')
mapType.set('.html', 'html')
mapType.set('.htm', 'html')
mapType.set('.json', 'json')
mapType.set('.xls', 'excel')
mapType.set('.xlsx', 'excel')
mapType.set('.doc', 'word')
mapType.set('.docx', 'word')
mapType.set('.zip', 'zip')

export const mapFileIcon = (filename) => {
  if (!filename) return 'file'
  const ext = path.extname(filename).toLocaleLowerCase()
  return mapIcon.get(ext) || 'file'
}

export const mapFileType = (filename) => {
  if (!filename) return ''
  const ext = path.extname(filename).toLocaleLowerCase()
  return mapType.get(ext) || ''
}

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
