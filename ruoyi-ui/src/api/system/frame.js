import request from '@/utils/request'

// 查询跨列表
export function listFrame(query) {
  return request({
    url: '/system/frame/list',
    method: 'get',
    params: query
  })
}

// 查询跨详细
export function getFrame(id) {
  return request({
    url: '/system/frame/' + id,
    method: 'get'
  })
}

// 新增跨
export function addFrame(data) {
  return request({
    url: '/system/frame',
    method: 'post',
    data: data
  })
}

// 修改跨
export function updateFrame(data) {
  return request({
    url: '/system/frame',
    method: 'put',
    data: data
  })
}

// 删除跨
export function delFrame(id) {
  return request({
    url: '/system/frame/' + id,
    method: 'delete'
  })
}
