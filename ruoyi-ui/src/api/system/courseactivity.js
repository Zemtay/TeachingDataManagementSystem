import request from '@/utils/request'

// 查询课程活动列表
export function listCourseactivity(query) {
  return request({
    url: '/system/courseactivity/list',
    method: 'get',
    params: query
  })
}

// 查询课程活动详细
export function getCourseactivity(id) {
  return request({
    url: '/system/courseactivity/' + id,
    method: 'get'
  })
}

// 新增课程活动
export function addCourseactivity(data) {
  return request({
    url: '/system/courseactivity',
    method: 'post',
    data: data
  })
}

// 修改课程活动
export function updateCourseactivity(data) {
  return request({
    url: '/system/courseactivity',
    method: 'put',
    data: data
  })
}

// 删除课程活动
export function delCourseactivity(id) {
  return request({
    url: '/system/courseactivity/' + id,
    method: 'delete'
  })
}
