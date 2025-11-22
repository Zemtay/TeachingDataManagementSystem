import request from '@/utils/request'

// 查询班级信息列表
export function listClassinfo(query) {
  return request({
    url: '/system/classinfo/list',
    method: 'get',
    params: query
  })
}

// 查询班级信息详细
export function getClassinfo(id) {
  return request({
    url: '/system/classinfo/' + id,
    method: 'get'
  })
}

// 新增班级信息
export function addClassinfo(data) {
  return request({
    url: '/system/classinfo',
    method: 'post',
    data: data
  })
}

// 修改班级信息
export function updateClassinfo(data) {
  return request({
    url: '/system/classinfo',
    method: 'put',
    data: data
  })
}

// 删除班级信息
export function delClassinfo(id) {
  return request({
    url: '/system/classinfo/' + id,
    method: 'delete'
  })
}
